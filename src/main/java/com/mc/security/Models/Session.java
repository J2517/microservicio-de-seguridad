package com.mc.security.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document

public class Session {
    @Id
    private String _id;
    private String token;
    private String token2FA;
    private String expiration;
    private int intentosFallidos;
    private String recaptchaToken;

    @DBRef
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Session(String token, String expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public String get_id() {
        return _id;
    }

    public String getToken() {
        return token;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getToken2FA() {
        return token2FA;
    }

    public void setToken2FA(String token2FA) {
        this.token2FA = token2FA;
    }

    public Session() {
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }
    public String getRecaptchaToken() {
        return recaptchaToken;
    }

    public void setRecaptchaToken(String recaptchaToken) {
        this.recaptchaToken = recaptchaToken;
    }
}
