package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByWriter_IdAndId(Long writerId, Long id);

    List<Comment> findByBoardId(Long boardId);
}
