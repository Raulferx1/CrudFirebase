package com.example.crudfirebase;

public class MainModel {
    String Nombre, Apellido, email, imgURL;
    public MainModel(){

    }

    public MainModel(String nombre, String apellido, String email, String imgURL){
        Nombre = nombre;
        Apellido = apellido;
        this.email = email;
        this.imgURL = imgURL;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
