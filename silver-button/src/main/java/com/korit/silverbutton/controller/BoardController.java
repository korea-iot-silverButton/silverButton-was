package com.korit.silverbutton.controller;


import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.board.Request.BoardRequestDto;
import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
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

@RestController
@RequestMapping(ApiMappingPattern.BOARD)
@RequiredArgsConstructor
public class BoardController {

    private  final BoardService boardService;

    private static final String BOARD = "/";
    private static final String BOARD_ALL = "/all";
    private static final String BOARD_ID = "/{id}";
    private static final String BOARD_SEARCH = "/search";
    private static final String BOARD_USERID = "/search/user";
    private static final String BOARD_PUT = "/{id}";
    private static final String BOARD_DELETE = "/{id}";

    @PostMapping(BOARD)
    public ResponseEntity<ResponseDto<BoardResponseDto>> createBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @Valid @RequestBody BoardRequestDto dto
    ) {


        Long authorId = principalUser.getId();
        System.out.println("Author ID: " + authorId);
        ResponseDto<BoardResponseDto> response = boardService.createBoard(authorId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(BOARD_ALL)
    @PermitAll
    public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ResponseDto<List<BoardResponseDto>> response = boardService.getAllBoards(page, size);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(BOARD_SEARCH)
    @PermitAll
    public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getBoardByTitle(
            @RequestParam String keyword

    ){
        if(keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD));

        }

        ResponseDto<List<BoardResponseDto>> response = boardService.getBoardByTitle(keyword);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(BOARD_USERID)
    @PermitAll
    public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getBoardByUser(
            @RequestParam(name ="user") Long user

    ){
        if(user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD));
        }
        ResponseDto<List<BoardResponseDto>> response = boardService.getBoardByUser(user);
        System.out.println("User ID: " + user);
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
        if(principalUser == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        System.out.println("Authenticated User: " + principalUser);
        Long authorId = principalUser.getId();
        ResponseDto<BoardResponseDto> response = boardService.updateBoard(authorId, id, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(BOARD_DELETE)
    public ResponseEntity<ResponseDto<Void>> deleteBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id
    ) {
        Long authorId = principalUser.getId();
        ResponseDto<Void> response = boardService.deleteBoard(authorId, id);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);


    }

}
