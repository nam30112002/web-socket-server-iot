package com.websocketserveriot.controller;

import com.websocketserveriot.model.NotificationDTO;
import com.websocketserveriot.model.TextMessageDTO;
import com.websocketserveriot.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebSocketTextController {

    private final SimpMessagingTemplate template;
    private final NotificationService notificationService;

    public WebSocketTextController(SimpMessagingTemplate template, NotificationService notificationService) {
        this.template = template;
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody TextMessageDTO textMessageDTO) {
        template.convertAndSend("/topic/message", textMessageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload TextMessageDTO textMessageDTO) {
        // receive message from client
    }


    @SendTo("/topic/message")
    public TextMessageDTO broadcastMessage(@Payload TextMessageDTO textMessageDTO) {
        return textMessageDTO;
    }

    @Operation(summary = "Send notification to admins")
    @PostMapping("/send-notification")
    public ResponseEntity<Void> sendNotificationToAdmins(@RequestBody NotificationDTO notificationDTO) {
        notificationService.saveNotification(notificationDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get all unread notification")
    @GetMapping("/get-all-unread-notification/{receiver}")
    public ResponseEntity<Object> getAllUnreadNotification(@PathVariable("receiver") String receiver) {
        return new ResponseEntity<>(notificationService.getAllUnreadNotification(receiver), HttpStatus.OK);
    }
    @Operation(summary = "Get all unread notification")
    @PutMapping("/mark-all-read-notification/{receiver}")
    public ResponseEntity<Object> markAllReadNotification(@PathVariable("receiver") String receiver) {
        notificationService.markAllReadNotification(receiver);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Mark a read notification")
    @PutMapping("/mark-read-notification/{id}")
    public ResponseEntity<Object> markReadNotification(@PathVariable("id") Long id) {
        notificationService.markReadNotification(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
