package com.example.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Optional<Message> findByPostedBy(Integer postedBy); 
    Optional<Message> findByMessageId(Integer messageId);
    @Modifying
    @Query("DELETE FROM Message m WHERE m.messageId = :messageId")
    int deleteByMessageId(@Param("messageId") Integer messageId);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.messageId = :messageId")
    int updateMessageById(@Param("messageText") String messageText, @Param("messageId") Integer messageId);

    @Query("Select m FROM Message m WHERE m.postedBy = :accountId") 
    Optional<List<Message>> findAllMessagesByAccountId(@Param("accountId") int accountId);



}
