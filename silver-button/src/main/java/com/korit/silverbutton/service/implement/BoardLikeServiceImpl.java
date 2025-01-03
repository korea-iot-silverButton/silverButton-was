package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.boardlike.Request.BoardLikeRequestDto;
import com.korit.silverbutton.dto.boardlike.Response.BoardLikeResponseDto;

import com.korit.silverbutton.entity.Board;
import com.korit.silverbutton.entity.BoardLike;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.BoardLikeRepository;
import com.korit.silverbutton.repository.BoardRepository;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.BoardLikeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public ResponseDto<BoardLikeResponseDto> toggleLike(Long userId, BoardLikeRequestDto dto) {
        BoardLikeResponseDto data = null;

        try {
            User liker = findUserById(userId);
            Board board = findBoardById(dto.getBoardId());

            Optional<BoardLike> existingLike = boardLikeRepository.findByBoardIdAndLikerId(board.getId(), liker.getId());

            if (existingLike.isPresent()) {
                // 이미 좋아요가 있으면 삭제하고, 좋아요 수 감소
                removeLike(existingLike.get(), board);
                data = new BoardLikeResponseDto(board.getId(), board.getLikes());
            } else {
                // 좋아요가 없으면 추가하고, 좋아요 수 증가
                addLike(board, liker);
                data = new BoardLikeResponseDto(board.getId(), board.getLikes());
            }

            // 게시글 좋아요 수 업데이트
            boardRepository.save(board);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 좋아요 추가
    private void addLike(Board board, User liker) {
        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .liker(liker)
                .build();
        boardLikeRepository.save(boardLike);
        board.setLikes(board.getLikes() + 1);  // 좋아요 수 증가
    }

    // 좋아요 삭제
    private void removeLike(BoardLike boardLike, Board board) {
        boardLikeRepository.delete(boardLike);
        if (board.getLikes() > 0) {
            board.setLikes(board.getLikes() - 1);  // 좋아요 수 감소
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    @Override
    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
    }
}