package com.mc.security.Models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document

public class Profile {
    private String _id;

    private String phone;

    private String photo;

    public Profile(String phone, String photo) {
        this.phone = phone;
        this.photo = photo;
    }

    public String get_id() {
        return _id;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
