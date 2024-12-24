package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.boardlike.Request.BoardLikeRequestDto;
import com.korit.silverbutton.dto.boardlike.Response.BoardLikeResponseDto;
import com.korit.silverbutton.entity.Board;
import com.korit.silverbutton.entity.User;
import jakarta.validation.Valid;

public interface BoardLikeService  {


    ResponseDto<BoardLikeResponseDto> insertLike(Long userId, @Valid BoardLikeRequestDto dto);

    ResponseDto<Void> deleteLike(Long userId, Long id);


    User findUserById(Long likerId);

    Board findBoardById(Long boardId);
}