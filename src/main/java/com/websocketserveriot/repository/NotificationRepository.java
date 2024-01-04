package com.websocketserveriot.repository;

import com.websocketserveriot.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("select n from NotificationEntity n where n.receiver = ?1 and n.status = ?2")
    List<NotificationEntity> findAllByReceiverAndStatus(String receiver, int status);
}
