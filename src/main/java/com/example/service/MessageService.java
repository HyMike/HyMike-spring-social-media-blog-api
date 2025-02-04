package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import java.util.Optional;

import javax.websocket.server.PathParam;

import java.util.List;

@Service
public class MessageService {

    MessageRepository messageRepository; 

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository; 
    }

    public Message createMessage(Message message) {

            if (message.getMessageText() == null || message.getMessageText().isBlank()) {
                return null; 
            } 
            if (message.getMessageText().length() > 255) {
                return null; 
            }
            //check if user is real
            Optional<Message> postedByOptional = messageRepository.findByPostedBy(message.getPostedBy()); 
            if (postedByOptional.isPresent()) {
                return messageRepository.save(message);

            } else {
                return null; 
            }

    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll(); 
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> messageOptional = messageRepository.findByMessageId(messageId);
        Message message = messageOptional.orElse(new Message());
        return message;  

    }

    @Transactional 
    public int deleteMessageById(Integer messageId) {
        return messageRepository.deleteByMessageId(messageId);
    }
    
    public Integer updateMessage(Message messageObj, Integer messageId) {
        //check if message exist 
        Message message = getMessageById(messageId);
        String messageText = messageObj.getMessageText();

        if (message == null || messageText.isEmpty() || messageText.length() > 255) {
            return 0;
        }

        Integer rowsUpdated =  messageRepository.updateMessageById(messageText, messageId);
        return rowsUpdated;

    }


    public Optional<List<Message>> getAllMessagesByAccount(int accountId) {
        Optional<List<Message>> allMessages = messageRepository.findAllMessagesByAccountId(accountId);
        return allMessages; 
    }




}
