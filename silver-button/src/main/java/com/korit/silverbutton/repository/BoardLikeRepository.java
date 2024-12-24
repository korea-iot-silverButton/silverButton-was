package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.BoardLike;
import com.korit.silverbutton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    Optional<BoardLike> findByBoardIdAndLikerId(Long boardId, Long likerId);
    Optional<BoardLike> findByLikerIdAndId(Long likerId, Long id);

}
