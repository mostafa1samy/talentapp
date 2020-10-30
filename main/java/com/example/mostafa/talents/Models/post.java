package com.example.mostafa.talents.Models;

public class post {
    String name,image,discrp,video;
    public post(){

    }

    public post(String name, String image, String discrp,String v) {
        this.name = name;
        this.image = image;
        this.discrp = discrp;
        video=v;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscrp() {
        return discrp;
    }

    public void setDiscrp(String discrp) {
        this.discrp = discrp;
    }
}
