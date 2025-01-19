package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.board.Request.BoardRequestDto;
import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.board.Response.BoardUpdateResponseDto;
import com.korit.silverbutton.dto.paged.Response.PagedResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequestMapping(ApiMappingPattern.BOARD)
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private static final String POST_BOARD = "/create";
    private static final String GET_BOARD_ALL = "/all";
    private static final String GET_BOARD_BY_ID = "/view/{id}";
    private static final String GET_BOARD_SEARCH_TITLE = "/search/title";
    private static final String GET_BOARD_SEARCH_NAME = "/search/name";
    private static final String PUT_BOARD = "/edit/{id}";
    private static final String DELETE_BOARD = "/delete/{id}";

    @PostMapping(POST_BOARD)
    public ResponseEntity<ResponseDto<BoardResponseDto>> createBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {

        BoardRequestDto dto = new BoardRequestDto();
        dto.setTitle(title);
        dto.setContent(content);

        if (images != null) {
            images.forEach(image -> System.out.println(ResponseMessage.IMAGE_NAME + image.getOriginalFilename()));
        } else {
            System.out.println(ResponseMessage.NO_IMAGES_UPLOADED);
        }

        ResponseDto<BoardResponseDto> response = boardService.createBoard(principalUser.getId(), dto, images);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(GET_BOARD_ALL)
    public ResponseEntity<ResponseDto<PagedResponseDto<List<BoardResponseDto>>>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ResponseDto<PagedResponseDto<List<BoardResponseDto>>> response = boardService.getAllBoards(page, size);
        if (response != null && response.getData() != null) {
            List<BoardResponseDto> boardList = response.getData().getContent();
            for (BoardResponseDto board : boardList) {
                System.out.println("Board ID: " + board.getId() + ", Image URL: " + board.getImageUrl());
            }
        }

        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(GET_BOARD_SEARCH_TITLE)
    public ResponseEntity<ResponseDto<PagedResponseDto<List<BoardResponseDto>>>> getBoardByTitle(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD));
        }
        ResponseDto<PagedResponseDto<List<BoardResponseDto>>> response = boardService.getBoardByTitle(keyword, page, size);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(GET_BOARD_SEARCH_NAME)
    public ResponseEntity<ResponseDto<PagedResponseDto<List<BoardResponseDto>>>> getBoardByUserName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.setFailed(ResponseMessage.INVALID_KEYWORD));
        }
        ResponseDto<PagedResponseDto<List<BoardResponseDto>>> response = boardService.getBoardByUserName(name, page, size);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(GET_BOARD_BY_ID)
    public ResponseEntity<ResponseDto<BoardResponseDto>> getBoardById(
            @PathVariable Long id
    ) {
        ResponseDto<BoardResponseDto> response = boardService.getBoardAndIncreaseViews(id);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(PUT_BOARD)
    public ResponseEntity<ResponseDto<BoardUpdateResponseDto>> updateBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {

        BoardRequestDto dto = new BoardRequestDto();
        dto.setTitle(title);
        dto.setContent(content);

        if (images != null) {
            images.forEach(image -> System.out.println(ResponseMessage.IMAGE_NAME + image.getOriginalFilename()));
        } else {
            System.out.println(ResponseMessage.NO_IMAGES_UPLOADED);
        }

        ResponseDto<BoardUpdateResponseDto> response = boardService.updateBoard(principalUser.getId(), id, dto, images);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(DELETE_BOARD)
    public ResponseEntity<ResponseDto<Void>> deleteBoard(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id
    ) {
        ResponseDto<Void> response = boardService.deleteBoard(principalUser.getId(), id);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}