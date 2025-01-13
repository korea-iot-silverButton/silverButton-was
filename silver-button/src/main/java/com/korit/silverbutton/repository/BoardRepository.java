package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = "comments")
    Page<Board> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "comments")
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Board>findByUserName(String name, Pageable pageable);
    Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Optional<Board> findByUserIdAndId(Long userId, Long Id);

    @EntityGraph(attributePaths = "comments")
    Optional<Board> findWithCommentsById(Long id);
}
