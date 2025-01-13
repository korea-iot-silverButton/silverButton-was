package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Message;
import com.korit.silverbutton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    //    @Query("""
//SELECT m
//FROM Message m
//WHERE m.senderId = :id
//    OR m.receiverId = :id
//ORDER BY m.createdAt DESC
//""")
    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    List<Message> findMessageById(Long id);

    List<Message> findAllBySender(User sender); // 발신자 ID로 메시지 조회
    List<Message> findAllByReceiverId(Long receiver);
//    List<Message> findById(User user);

//    List<Object[]> findAllMessages(); - 쿼리쓸때
}
