package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = "comments")
    Page<Board> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "comments")
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Board>findByUserName(String name);
    List<Board> findByTitleContainingIgnoreCase(String title);

    Optional<Board> findByUserIdAndId(Long userId, Long Id);

    @EntityGraph(attributePaths = "comments") // 댓글까지 함께 로드
    Optional<Board> findWithCommentsById(Long id);




}
