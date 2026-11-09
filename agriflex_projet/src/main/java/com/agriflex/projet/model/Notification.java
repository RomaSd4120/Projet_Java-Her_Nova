package com.agriflex.projet.model;

import java.time.LocalDateTime;

public class Notification {
    private int idNotification;
    private int idCandidat;
    private Integer idMission; // Integer autorise la valeur null (pour ON DELETE SET NULL)
    private String canal;
    private String message;
    private LocalDateTime dateEnvoi;
    private String statutEnvoi;

    public Notification() {}

    public Notification(int idNotification, int idCandidat, Integer idMission, String canal, String message, LocalDateTime dateEnvoi, String statutEnvoi) {
        this.idNotification = idNotification;
        this.idCandidat = idCandidat;
        this.idMission = idMission;
        this.canal = canal;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.statutEnvoi = statutEnvoi;
    }

    public Notification(int idCandidat, Integer idMission, String canal, String message, String statutEnvoi) {
        this.idCandidat = idCandidat;
        this.idMission = idMission;
        this.canal = canal;
        this.message = message;
        this.statutEnvoi = statutEnvoi;
    }

    public int getIdNotification() { return idNotification; }
    public void setIdNotification(int idNotification) { this.idNotification = idNotification; }

    public int getIdCandidat() { return idCandidat; }
    public void setIdCandidat(int idCandidat) { this.idCandidat = idCandidat; }

    public Integer getIdMission() { return idMission; }
    public void setIdMission(Integer idMission) { this.idMission = idMission; }

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public String getStatutEnvoi() { return statutEnvoi; }
    public void setStatutEnvoi(String statutEnvoi) { this.statutEnvoi = statutEnvoi; }
}