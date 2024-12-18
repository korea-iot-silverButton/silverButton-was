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
    public ResponseDto<BoardResponseDto> createBoard(Long authorId, BoardRequestDto dto) {

        // 유효성 검사
        if (authorId == null || authorId <= 0) {
            return ResponseDto.setFailed("유효하지 않은 사용자 ID입니다.");
        }

        BoardResponseDto data = null;
        String title = dto.getTitle();
        String content = dto.getContent();



        try {
            User user = userRepository.findById(authorId)
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.NOT_EXIST_USER));

            Board board = Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .likes(0)
                    .views(0)
                    .created_at(LocalDateTime.now())
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
    public ResponseDto<List<BoardResponseDto>> getAllBoards(int page, int size) {
        List<BoardResponseDto> data = null;

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardRepository.findAll(pageable);

            data = boardPage
                    .getContent()
                    .stream()
                    .map(board -> new BoardResponseDto(board))
                    .collect(Collectors.toList());
            PagedResponseDto<BoardResponseDto> pagedResponseDto = new PagedResponseDto<>(
                    data,
                    boardPage.getNumber(),
                    boardPage.getTotalPages(),
                    boardPage.getTotalElements()
            );

            if(data.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

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
            List<Board> boards = boardRepository.findByTitleContaining(keyword);

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
    public ResponseDto<List<BoardResponseDto>> getBoardByUser(Long user) {
        try {
            List<Board> boards = boardRepository.findByUser_Id(user);
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
    public ResponseDto<BoardResponseDto> updateBoard(Long authorId, Long id,  BoardRequestDto dto) {
        BoardResponseDto data = null;
        String title = dto.getTitle();
        String content = dto.getContent();


        try {

            Optional<Board> boardOptional = boardRepository.findByUserIdAndId(authorId, id);
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
    public ResponseDto<Void> deleteBoard(Long authorId, Long id) {


        try {
            Optional<Board> optionalBoard =
                    boardRepository.findByUserIdAndId(authorId,id);
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