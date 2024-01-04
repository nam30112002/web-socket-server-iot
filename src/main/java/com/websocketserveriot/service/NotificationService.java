package com.websocketserveriot.service;

import com.websocketserveriot.model.NotificationDTO;
import com.websocketserveriot.model.NotificationEntity;
import com.websocketserveriot.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate template;

    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate template) {
        this.notificationRepository = notificationRepository;
        this.template = template;
    }
    public void saveNotification(NotificationDTO notificationDTO) {
        // save notification to database and send to admin
        List<String> receiverList = notificationDTO.getReceiver();
        for (String receiver : receiverList) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setContent(notificationDTO.getContent());
            notificationEntity.setSender(notificationDTO.getSender());
            notificationEntity.setReceiver(receiver);
            notificationEntity.setStatus(0);
            notificationRepository.save(notificationEntity);
            template.convertAndSend("/topic/notification", notificationDTO.getContent());
        }
    }
    public List<NotificationEntity> getAllUnreadNotification(String receiver) {
        // get all unread notification from database
        return notificationRepository.findAllByReceiverAndStatus(receiver, 0);
    }

    public void markAllReadNotification(String receiver) {
        // mark all unread notification to read
        List<NotificationEntity> notificationEntityList = notificationRepository.findAllByReceiverAndStatus(receiver, 0);
        for (NotificationEntity notificationEntity : notificationEntityList) {
            notificationEntity.setStatus(1);
            notificationRepository.save(notificationEntity);
        }
    }

    public void markReadNotification(Long id) {
        // mark a read notification
        NotificationEntity notificationEntity = notificationRepository.findById(id).orElse(null);
        if (notificationEntity != null) {
            notificationEntity.setStatus(1);
            notificationRepository.save(notificationEntity);
        }
    }
}
