package com.korit.silverbutton.controller;


import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.board.Request.BoardRequestDto;
import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.paged.Response.PagedResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.BoardService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;


//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(ApiMappingPattern.BOARD)
@RequiredArgsConstructor
public class BoardController {

    private  final BoardService boardService;

    private static final String BOARD = "/create";
    private static final String BOARD_ALL = "/all";
    private static final String BOARD_ID = "/{id}";
    private static final String BOARD_SEARCH_TITLE = "/search/title";
    private static final String BOARD_SEARCH_NAME = "/search/name";
    private static final String BOARD_PUT = "/{id}";
    private static final String BOARD_DELETE = "/delete/{id}";


    @PostMapping(BOARD)
    public ResponseEntity<ResponseDto<BoardResponseDto>> createBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @Valid @RequestBody BoardRequestDto dto
    ) {
        ResponseDto<BoardResponseDto> response = boardService.createBoard(principalUser.getId(), dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(BOARD_ALL)
    @PermitAll
    public ResponseEntity<ResponseDto<PagedResponseDto<List<BoardResponseDto>>>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ResponseDto<PagedResponseDto<List<BoardResponseDto>>> response = boardService.getAllBoards(page, size);
        System.out.println("ResponseDto 내용: " + response);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 제목 검색
    @GetMapping(BOARD_SEARCH_TITLE)
    @PermitAll
    public ResponseEntity<ResponseDto<PagedResponseDto<List<BoardResponseDto>>>> getBoardByTitle(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ){
        System.out.println("Received keyword: " + keyword); // keyword 로그 확인
        if(keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD));

        }

        ResponseDto<PagedResponseDto<List<BoardResponseDto>>> response = boardService.getBoardByTitle(keyword, page, size);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(BOARD_SEARCH_NAME)
    @PermitAll
    public ResponseEntity<ResponseDto<PagedResponseDto<List<BoardResponseDto>>>> getBoardByUserName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ){
        System.out.println("Received name: " + name);
        if(name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD));
        }
        ResponseDto<PagedResponseDto<List<BoardResponseDto>>> response = boardService.getBoardByUserName(name, page, size);
        System.out.println("User name: " + name);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }


    @GetMapping(BOARD_ID)
    @PermitAll
    public ResponseEntity<ResponseDto<BoardResponseDto>> getBoardById(
            @PathVariable Long id
    ) {
        ResponseDto<BoardResponseDto> response = boardService.getBoardAndIncreaseViews(id);

        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(BOARD_PUT)
    public ResponseEntity<ResponseDto<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id,
            @Valid @RequestBody BoardRequestDto dto
    ){
        ResponseDto<BoardResponseDto> response = boardService.updateBoard(principalUser.getId(), id, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(BOARD_DELETE)
    public ResponseEntity<ResponseDto<Void>> deleteBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id
    ) {
       ResponseDto<Void> response = boardService.deleteBoard(principalUser.getId(), id);
       HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
       return ResponseEntity.status(status).body(response);


    }

}
