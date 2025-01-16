package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Message;
import com.korit.silverbutton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    List<Message> findMessageById(@Param("userId") Long id);

    List<Message> findAllBySender(User sender); // 발신자 ID로 메시지 조회
    List<Message> findAllByReceiver(User receiver);

}
