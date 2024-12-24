package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.board.Request.BoardRequestDto;
import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import com.korit.silverbutton.dto.paged.Response.PagedResponseDto;
import com.korit.silverbutton.entity.Board;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.BoardRepository;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    @Override
    public ResponseDto<BoardResponseDto> createBoard(Long userId, BoardRequestDto dto) {

        // 유효성 검사
        if (userId == null || userId <= 0) {
            return ResponseDto.setFailed("유효하지 않은 사용자 ID입니다.");
        }

        BoardResponseDto data = null;
        String title = dto.getTitle();
        String content = dto.getContent();



        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.NOT_EXIST_USER));

            Board board = Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .likes(0)
                    .views(0)
                    .createdAt(LocalDateTime.now())
                    .build();

            boardRepository.save(board);
            data = new BoardResponseDto(board);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

    }

    @Override
    public ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getAllBoards(int page, int size) {
        List<BoardResponseDto> data = null;

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardRepository.findAllByOrderByCreatedAtDesc(pageable); // 최신순 정렬된 데이터 가져오기

            System.out.println("boardPage 내용: " + boardPage.getContent());
            System.out.println("현재 페이지: " + boardPage.getNumber());
            System.out.println("총 페이지 수: " + boardPage.getTotalPages());
            System.out.println("전체 요소 수: " + boardPage.getTotalElements());

            data = boardPage
                    .getContent()
                    .stream()
                    .map(board -> new BoardResponseDto(board))
                    .collect(Collectors.toList());

            if(data.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

           PagedResponseDto<List<BoardResponseDto>> paged = new PagedResponseDto<>(
                    data,
                    boardPage.getNumber(),
                    boardPage.getTotalPages(),
                    boardPage.getTotalElements()
            );

            System.out.println("PagedResponseDto 생성된 데이터: " + paged);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, paged);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

    }

    @Override
    public ResponseDto<BoardResponseDto> getBoardAndIncreaseViews(Long id) {
        BoardResponseDto data = null;
        Long boardId = id;

        try {
            Optional<Board> boardOptional = boardRepository.findWithCommentsById(boardId);

            if(boardOptional.isPresent()) {
                Board board = boardOptional.get();

                board.setViews(board.getViews() + 1);
                boardRepository.save(board);

                data = new BoardResponseDto(board);

            } else {
                return ResponseDto.setFailed(ResponseMessage.INVALID_POST_ID);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

    }

    @Override
    public ResponseDto<List<BoardResponseDto>> getBoardByTitle(String keyword) {

        if(keyword == null || keyword.trim().isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD);
        }

        try {
            keyword = keyword.trim();
            List<Board> boards = boardRepository.findByTitleContainingIgnoreCase(keyword);

            System.out.println("검색 키워드: " + keyword);
            System.out.println("검색 결과 개수: " + boards.size());
            boards.forEach(board -> System.out.println("게시글 제목: " + board.getTitle()));

            if(boards.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            List<BoardResponseDto> data = boards.stream()
                    .map(BoardResponseDto::new)
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
        }
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
    }

    @Override
    public ResponseDto<List<BoardResponseDto>> getBoardByUserName(String name) {
           if(name == null){
               return ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD);
           }

            try {
            List<Board> boards = boardRepository.findByUserName(name);
            if(boards.isEmpty()) {
               return ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD);
            }

            List<BoardResponseDto> data = boards.stream()
                    .map(BoardResponseDto::new)
                    .collect(Collectors.toList());

                System.out.println("Boards: " + boards);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<BoardResponseDto> updateBoard(Long userId, Long id,  BoardRequestDto dto) {
        BoardResponseDto data = null;
        String title = dto.getTitle();
        String content = dto.getContent();


        try {

            Optional<Board> boardOptional = boardRepository.findByUserIdAndId(userId, id);
            if(boardOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }
                Board board = boardOptional.get();
                board = board.toBuilder()
                        .title(title)
                        .content(content)
                        .build();

                boardRepository.save(board);
                data = new BoardResponseDto(board);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

    }

    @Override
    public ResponseDto<Void> deleteBoard(Long userId, Long id) {


        try {
            Optional<Board> optionalBoard =
                    boardRepository.findByUserIdAndId(userId,id);
            if(optionalBoard.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            Board board = optionalBoard.get();
            boardRepository.delete(board);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return  ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }



}

