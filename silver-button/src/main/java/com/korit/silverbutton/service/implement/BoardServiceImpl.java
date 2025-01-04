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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";


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
            if (user == null) {
                user = new User(); // 임시 User 객체 생성 (또는 임시 작성자 이름 설정)
                user.setName("임시작성자"); // 임시 이름 설정
            }

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
    public String uploadImage(MultipartFile file) throws IOException {
        // 파일 이름을 가져오고 저장할 경로를 설정
        String fileName = file.getOriginalFilename();
        if (fileName == null || file.isEmpty()) {
            throw new IOException("파일이 비어 있습니다.");
        }

        // 저장할 경로 정의
        Path path = Paths.get(UPLOAD_DIR + fileName);

        // 파일이 저장될 디렉토리 없으면 생성
        Files.createDirectories(path.getParent());

        try {
            // 파일을 저장
            file.transferTo(path);
        } catch (IOException e) {
            // IOException 예외 처리
            throw new IOException("파일 저장에 실패했습니다: " + e.getMessage());
        }

        // 파일이 저장된 URL 반환
        return "/images/" + fileName;  // 저장된 파일에 접근할 수 있는 URL 반환
    }

    @Override
    public ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getBoardByTitle(String keyword, int page, int size) {

        if(keyword == null || keyword.trim().isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD);
        }

        try {
            keyword = keyword.trim();
            Pageable pageable = PageRequest.of(page, size); // 페이징 처리
            Page<Board> boardPage = boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);

            if(boardPage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }



            System.out.println("검색 키워드: " + keyword);
            System.out.println("검색 결과 개수: " + boardPage.getNumber());
            boardPage.forEach(board -> System.out.println("게시글 제목: " + board.getTitle()));

            if(boardPage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            List<BoardResponseDto> data = boardPage.getContent().stream()
                    .map(BoardResponseDto::new)
                    .collect(Collectors.toList());

            PagedResponseDto<List<BoardResponseDto>> pagedResponse = new PagedResponseDto<>(data,
                    boardPage.getNumber(), boardPage.getTotalPages(), boardPage.getTotalElements());

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, pagedResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
    }

    @Override
    public ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getBoardByUserName(String name, int page, int size) {
           if(name == null){
               return ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD);
           }

            try {
                Pageable pageable = PageRequest.of(page, size); // 페이징 처리
                Page<Board> boardPage = boardRepository.findByUserName(name, pageable);

                if(boardPage.isEmpty()) {
                    return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
                }


                List<BoardResponseDto> data = boardPage.getContent().stream()
                        .map(BoardResponseDto::new)
                        .collect(Collectors.toList());

                PagedResponseDto<List<BoardResponseDto>> pagedResponse = new PagedResponseDto<>(data,
                        boardPage.getNumber(), boardPage.getTotalPages(), boardPage.getTotalElements());

                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, pagedResponse);

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
            Optional<Board> optionalBoard ;
            // userId가 null일 경우 userId 체크를 건너뜁니다.
            if (userId != null) {
                optionalBoard = boardRepository.findByUserIdAndId(userId, id);
            } else {
                optionalBoard = boardRepository.findById(id);  // userId 없이 id로만 조회
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

