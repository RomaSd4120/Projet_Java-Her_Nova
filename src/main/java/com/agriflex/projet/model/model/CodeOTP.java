
package com.agriflex.projet.model;

import java.time.LocalDateTime;

public class CodeOTP {

    private int idOtp;
    private String telephone;
    private String code;
    private LocalDateTime dateExpiration;
    private boolean estUtilise;

    public CodeOTP() {
    }

    public CodeOTP(int idOtp, String telephone, String code,
                   LocalDateTime dateExpiration, boolean estUtilise) {
        this.idOtp = idOtp;
        this.telephone = telephone;
        this.code = code;
        this.dateExpiration = dateExpiration;
        this.estUtilise = estUtilise;
    }

    public CodeOTP(String telephone, String code,
                   LocalDateTime dateExpiration, boolean estUtilise) {
        this.telephone = telephone;
        this.code = code;
        this.dateExpiration = dateExpiration;
        this.estUtilise = estUtilise;
    }

    public int getIdOtp() {
        return idOtp;
    }

    public void setIdOtp(int idOtp) {
        this.idOtp = idOtp;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public boolean isEstUtilise() {
        return estUtilise;
    }

    public void setEstUtilise(boolean estUtilise) {
        this.estUtilise = estUtilise;
    }
}
