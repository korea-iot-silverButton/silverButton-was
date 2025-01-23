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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public ResponseDto<BoardLikeResponseDto> toggleLike(Long userId, BoardLikeRequestDto dto) {
        BoardLikeResponseDto data;
        try {
            User liker = findUserById(userId);
            Board board = findBoardById(dto.getBoardId());

            Optional<BoardLike> existingLike = boardLikeRepository.findByBoardIdAndLikerId(board.getId(), liker.getId());

            if (existingLike.isPresent()) {
                removeLike(existingLike.get(), board);
                data = new BoardLikeResponseDto(board.getId(), board.getLikes());
                return ResponseDto.setSuccess(ResponseMessage.POST_UNLIKE_SUCCESS, data);
            } else {
                addLike(board, liker);
                data = new BoardLikeResponseDto(board.getId(), board.getLikes());
                return ResponseDto.setSuccess(ResponseMessage.POST_LIKE_SUCCESS, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    private void addLike(Board board, User liker) {
        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .liker(liker)
                .build();
        boardLikeRepository.save(boardLike);
        incrementLikeCount(board);
    }

    private void removeLike(BoardLike boardLike, Board board) {
        boardLikeRepository.delete(boardLike);
        decrementLikeCount(board);
    }

    private void incrementLikeCount(Board board) {
        board.setLikes(board.getLikes() + 1);
        boardRepository.save(board);
    }

    private void decrementLikeCount(Board board) {
        if (board.getLikes() > 0) {
            board.setLikes(board.getLikes() - 1);
            boardRepository.save(board);
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));
    }

    @Override
    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_POST));
    }
}
