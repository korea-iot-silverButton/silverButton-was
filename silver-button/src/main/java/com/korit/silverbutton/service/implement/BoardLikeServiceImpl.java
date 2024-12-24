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
    public ResponseDto<BoardLikeResponseDto> insertLike(Long likerId, BoardLikeRequestDto dto)  {
        BoardLikeResponseDto data = null;
        Long id = dto.getBoardId();
        try {
            User liker = findUserById(likerId);
            Board board = boardRepository.findByUserIdAndId(liker.getId(), dto.getBoardId())
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_POST));

            Optional<BoardLike> existingLike = boardLikeRepository.findByBoardIdAndLikerId(dto.getBoardId(), likerId);
            if (existingLike.isPresent()) {
                return ResponseDto.setFailed(ResponseMessage.POST_ALREADY_LIKED);
            }
            BoardLike boardLike = BoardLike.builder()
                    .id(id)
                    .board(board)
                    .liker(liker)
                    .build();

            boardLikeRepository.save(boardLike);

            board.setLikes(board.getLikes() + 1);
            boardRepository.save(board);

            data = new BoardLikeResponseDto(boardLike);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS,data);
    }

    @Override
    public ResponseDto<Void> deleteLike(Long likerId, Long id) {

        try {
            User liker = findUserById(likerId);
            Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByLikerIdAndId(liker, id);
            if(optionalBoardLike.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            BoardLike boardLike = optionalBoardLike.get();
            boardLikeRepository.delete(boardLike);

            Board board = boardLike.getBoard();
            board.setLikes(board.getLikes() - 1);
            boardRepository.save(board);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
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