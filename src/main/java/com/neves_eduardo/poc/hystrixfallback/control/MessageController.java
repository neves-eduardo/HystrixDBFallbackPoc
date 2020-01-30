package com.neves_eduardo.poc.hystrixfallback.control;

import com.neves_eduardo.poc.hystrixfallback.command.MessageAddCommand;
import com.neves_eduardo.poc.hystrixfallback.command.MessageGetByIdCommand;
import com.neves_eduardo.poc.hystrixfallback.dto.Message;
import com.neves_eduardo.poc.hystrixfallback.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/message")
public class MessageController {
    private MessageRepository messageRepository;
    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @PostMapping (produces = "application/json")
    public @ResponseBody ResponseEntity addMessage (@RequestBody Message message) {
        Optional<Message> messageToInsert = new MessageAddCommand(message,messageRepository).observe().toBlocking().first();
        if(!messageToInsert.isPresent()) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\"Error\": \"Error Inserting message\"");}
        return ResponseEntity.ok(messageToInsert.get());
    }

    @GetMapping ("/{id}")
    public @ResponseBody ResponseEntity getMessage (@PathVariable int id) {
        Optional<Message> message = new MessageGetByIdCommand(id, messageRepository).observe().toBlocking().first();
        if(!message.isPresent()) {return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(message.get());
    }
}
