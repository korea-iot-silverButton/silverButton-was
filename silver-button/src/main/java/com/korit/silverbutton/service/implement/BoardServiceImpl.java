package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.board.Request.BoardRequestDto;
import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import com.korit.silverbutton.dto.board.Response.BoardUpdateResponseDto;
import com.korit.silverbutton.dto.paged.Response.PagedResponseDto;
import com.korit.silverbutton.entity.Board;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.BoardRepository;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.BoardService;
import com.korit.silverbutton.service.ProfileImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ProfileImgService profileImgService;

    @Override
    public ResponseDto<BoardResponseDto> createBoard(Long userId, BoardRequestDto dto) {

        if (userId == null || userId <= 0) {
            return ResponseDto.setFailed("유효하지 않은 사용자 ID입니다.");
        }

        BoardResponseDto data = null;
        String title = dto.getTitle();
        String content = dto.getContent();
        String imageUrl = null;

        // 이미지 파일 처리 (Content에서 이미지 URL 추출)
        MultipartFile imageFile = dto.getImage(); // 요청에서 이미지를 받을 때 MultipartFile로 받기
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = profileImgService.uploadFile(imageFile); // 이미지 업로드 후 URL 반환
        }

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.NOT_EXIST_USER));

            Board board = Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .imageUrl(imageUrl)
                    .likes(0)
                    .views(0)
                    .createdAt(LocalDateTime.now())
                    .build();

            boardRepository.save(board);
            data = new BoardResponseDto(board);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.POST_CREATION_FAIL);
        }
        return ResponseDto.setSuccess(ResponseMessage.POST_CREATION_SUCCESS, data);
    }

    @Override
    public ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getAllBoards(int page, int size) {
        List<BoardResponseDto> data = null;

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardRepository.findAllByOrderByCreatedAtDesc(pageable);

            data = boardPage
                    .getContent()
                    .stream()
                    .map(BoardResponseDto::new)
                    .collect(Collectors.toList());

            if (data.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            PagedResponseDto<List<BoardResponseDto>> paged = new PagedResponseDto<>(
                    data,
                    boardPage.getNumber(),
                    boardPage.getTotalPages(),
                    boardPage.getTotalElements()
            );

            return ResponseDto.setSuccess(ResponseMessage.POST_CREATION_SUCCESS, paged);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.POST_CREATION_FAIL);
        }
    }

    @Override
    public ResponseDto<BoardResponseDto> getBoardAndIncreaseViews(Long id) {
        BoardResponseDto data = null;
        Long boardId = id;

        try {
            Optional<Board> boardOptional = boardRepository.findWithCommentsById(boardId);

            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();

                board.setViews(board.getViews() + 1);
                boardRepository.save(board);

                // 게시글 작성자 정보 조회
                User boardAuthor = userRepository.findById(board.getUser().getId())
                        .orElseThrow(() -> new RuntimeException(ResponseMessage.NOT_EXIST_USER));

                data = new BoardResponseDto(board);

            } else {
                return ResponseDto.setFailed(ResponseMessage.INVALID_POST_ID);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.POST_DETAIL_NOT_FOUND);
        }
        return ResponseDto.setSuccess(ResponseMessage.POST_DETAIL_FOUND, data);
    }

    @Override
    public ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getBoardByTitle(String keyword, int page, int size) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD);
        }

        try {
            keyword = keyword.trim();
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);

            if (boardPage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            List<BoardResponseDto> data = boardPage.getContent().stream()
                    .map(BoardResponseDto::new)
                    .collect(Collectors.toList());

            PagedResponseDto<List<BoardResponseDto>> pagedResponse = new PagedResponseDto<>(data,
                    boardPage.getNumber(), boardPage.getTotalPages(), boardPage.getTotalElements());

            return ResponseDto.setSuccess(ResponseMessage.SEARCH_RESULTS_FOUND, pagedResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDto.setFailed(ResponseMessage.NO_SEARCH_RESULTS);
    }

    @Override
    public ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getBoardByUserName(String name, int page, int size) {
        if (name == null) {
            return ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD);
        }

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardRepository.findByUserName(name, pageable);

            if (boardPage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            List<BoardResponseDto> data = boardPage.getContent().stream()
                    .map(BoardResponseDto::new)
                    .collect(Collectors.toList());

            PagedResponseDto<List<BoardResponseDto>> pagedResponse = new PagedResponseDto<>(data,
                    boardPage.getNumber(), boardPage.getTotalPages(), boardPage.getTotalElements());

            return ResponseDto.setSuccess(ResponseMessage.POST_FOUND_FOR_USER, pagedResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.POST_NOT_FOUND_FOR_USER);
        }
    }

    @Override
    public ResponseDto<BoardUpdateResponseDto> updateBoard(Long userId, Long id, BoardRequestDto dto) {
        BoardUpdateResponseDto data = null;
        String title = dto.getTitle();
        String content = dto.getContent();

        try {
            Optional<Board> boardOptional = boardRepository.findByUserIdAndId(userId, id);
            if (boardOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }
            Board board = boardOptional.get();
            board = board.toBuilder()
                    .title(title)
                    .content(content)
                    .build();

            boardRepository.save(board);
            data = new BoardUpdateResponseDto(board);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.POST_UPDATE_FAIL);
        }
        return ResponseDto.setSuccess(ResponseMessage.POST_UPDATE_SUCCESS, data);

    }

    @Override
    public ResponseDto<Void> deleteBoard(Long userId, Long id) {
        try {
            Optional<Board> optionalBoard;

            if (userId != null) {
                optionalBoard = boardRepository.findByUserIdAndId(userId, id);
            } else {
                optionalBoard = boardRepository.findById(id);
            }

            Board board = optionalBoard.orElseThrow(() ->
                    new IllegalArgumentException(ResponseMessage.NOT_EXIST_POST)
            );

            boardRepository.delete(board);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.POST_DELETION_FAIL);
        }
        return ResponseDto.setSuccess(ResponseMessage.POST_DELETION_SUCCESS, null);
    }
}

