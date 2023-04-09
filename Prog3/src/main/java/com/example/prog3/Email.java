package com.example.prog3;

import java.io.Serializable;
import java.util.List;

public class Email implements Serializable {
    private String sender;
    private String receiver;
    private String subject;
    private String text;

    Email() {}

    /**
     * Costruttore della classe.
     *
     * @param sender     email del mittente
     * @param receiver  emails dei destinatari
     * @param subject    oggetto della com.example.prog3.lorenzo.dipalma@unito.it.txt
     * @param text       testo della com.example.prog3.lorenzo.dipalma@unito.it.txt
     */


    public Email(String sender, String receiver, String subject, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;

    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
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
