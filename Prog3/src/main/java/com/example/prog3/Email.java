package com.example.prog3;

import java.util.ArrayList;
import java.util.List;

public class Email {
    private String sender;
    private String receivers;
    private String subject;
    private String text;

    private Email() {}

    /**
     * Costruttore della classe.
     *
     * @param sender     email del mittente
     * @param receivers  emails dei destinatari
     * @param subject    oggetto della com.example.prog3.lorenzo.dipalma@unito.it.txt
     * @param text       testo della com.example.prog3.lorenzo.dipalma@unito.it.txt
     */


    public Email(String sender, String receivers, String subject, String text) {
        this.sender = sender;
        this.subject = subject;
        this.text = text;
        this.receivers = receivers;
    }

    public String getSender() {
        return sender;
    }

    public String getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    /**
     * @return      stringa composta dagli indirizzi e-com.example.prog3.lorenzo.dipalma@unito.it.txt del mittente più destinatari
     */
    @Override
    public String toString() {
        return String.join(" - ", List.of(this.sender,this.subject));
    }
}
