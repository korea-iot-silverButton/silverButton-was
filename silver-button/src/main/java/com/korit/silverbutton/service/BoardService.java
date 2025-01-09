package com.korit.silverbutton.service;



import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.board.Request.BoardRequestDto;
import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import com.korit.silverbutton.dto.paged.Response.PagedResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {


    ResponseDto<BoardResponseDto> createBoard(Long userId, BoardRequestDto dto);

    ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getAllBoards(int page, int size);

    ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getBoardByTitle(String keyword, int page, int size);

    ResponseDto<PagedResponseDto<List<BoardResponseDto>>> getBoardByUserName(String name, int page, int size);

    ResponseDto<BoardResponseDto> updateBoard(Long userId, Long id,  @Valid BoardRequestDto dto);

    ResponseDto<Void> deleteBoard(Long userId, Long id);

    ResponseDto<BoardResponseDto> getBoardAndIncreaseViews(Long id, Long userId);


}
