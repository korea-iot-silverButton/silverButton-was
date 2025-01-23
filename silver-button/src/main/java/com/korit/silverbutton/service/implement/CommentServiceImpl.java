package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.comment.Request.CommentRequestDto;
import com.korit.silverbutton.dto.comment.Response.CommentResponseDto;
import com.korit.silverbutton.entity.Board;
import com.korit.silverbutton.entity.Comment;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.repository.BoardRepository;
import com.korit.silverbutton.repository.CommentRepository;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseDto<CommentResponseDto> createComment(
            PrincipalUser name,
            PrincipalUser phone,
            CommentRequestDto dto
    ) {
        CommentResponseDto data = null;
        String content = dto.getContent();
        Long boardId = dto.getBoardId();

        User user = userRepository.findByNameAndPhone(name.getName(), phone.getPhone())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

        if (boardId == null) {
            return ResponseDto.setFailed(ResponseMessage.INVALID_POST_ID);
        }
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.INVALID_POST_ID);
        }
        Board board = boardOptional.get();
        try {
            Comment comment = Comment.builder()
                    .content(content)
                    .board(board)
                    .writer(user)
                    .build();
            commentRepository.save(comment);
            data = new CommentResponseDto(comment);
            return ResponseDto.setSuccess(ResponseMessage.POST_COMMENT_CREATION_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.POST_COMMENT_CREATION_FAILED);
        }
    }


    @Override
    public ResponseDto<List<CommentResponseDto>> getAllComments(Long boardId) {
        List<CommentResponseDto> data = null;

        try {
            List<Comment> comments = commentRepository.findByBoardId(boardId);
            data = comments.stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_COMMENT);
        }
        return ResponseDto.setSuccess(ResponseMessage.COMMENT_EXISTS, data);
    }

    @Override
    public ResponseDto<Void> deleteComment(Long userId, Long id) {
        try {
            Optional<Comment> optionalComment = commentRepository.findByWriter_IdAndId(userId, id);
            if (optionalComment.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }
            Comment comment = optionalComment.get();
            commentRepository.delete(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.COMMENT_DELETE_FAILED);
        }
        return ResponseDto.setSuccess(ResponseMessage.COMMENT_DELETE_SUCCESS, null);
    }
}
