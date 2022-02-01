package com.example.smsbomber;

public class Contacts {
    private String name;
    private String phonenumber;
    private int nb_messages;

    public Contacts(String name, String phonenumber, int nb_messages){
        this.name = name;
        this.phonenumber = phonenumber;
        this.nb_messages = nb_messages;
    }

    public int getNb_messages() {
        return nb_messages;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void incrementeNbMessages(){
        this.nb_messages++;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", nb_messages=" + nb_messages +
                '}';
    }
}
