package com.example.mostafa.talents.Models;

public class scoutmodel { String email,talent;
public scoutmodel(){

}

    public scoutmodel(String email, String talent) {
        this.email = email;
        this.talent = talent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTalent() {
        return talent;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }
}
