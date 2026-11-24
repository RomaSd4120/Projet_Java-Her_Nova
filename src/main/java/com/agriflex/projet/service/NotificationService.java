package com.agriflex.projet.service;

import java.time.LocalDateTime;

import com.agriflex.projet.dao.NotificationDAO;
import com.agriflex.projet.model.Notification;

public class NotificationService {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public boolean envoyerNotificationMission(int idCandidat, Integer idMission, String message) {
        if (message == null || message.isBlank()) {
            return false;
        }
        Notification notif = new Notification();
        notif.setIdCandidat(idCandidat);
        notif.setIdMission(idMission);
        notif.setCanal("SMS");
        notif.setMessage(message.trim());
        notif.setDateEnvoi(LocalDateTime.now());
        notif.setStatutEnvoi("ENVOYE");

        return notificationDAO.create(notif);
    }
}