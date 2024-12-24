package com.korit.silverbutton.service;



import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.board.Request.BoardRequestDto;
import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface BoardService {


    ResponseDto<BoardResponseDto> createBoard(Long userId, BoardRequestDto dto);

    ResponseDto<List<BoardResponseDto>> getAllBoards(int page, int size);



    ResponseDto<List<BoardResponseDto>> getBoardByTitle(String keyword);

    ResponseDto<List<BoardResponseDto>> getBoardByUser(Long user);

    ResponseDto<BoardResponseDto> updateBoard(Long userId, Long id,  @Valid BoardRequestDto dto);

    ResponseDto<Void> deleteBoard(Long userId, Long id);

    ResponseDto<BoardResponseDto> getBoardAndIncreaseViews(Long id);


}