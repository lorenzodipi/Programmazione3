package com.example.prog3;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Email implements Serializable {
    private String sender;
    private String receiver;
    private String subject;
    private String text;


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

    @Override
    public String toString() {
        return String.join(" - ", List.of(this.sender,this.subject));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //if (!(o instanceof Email email)) return false;
        return false;
        //return Objects.equals(getSender(), email.getSender()) && Objects.equals(getReceiver(), email.getReceiver()) && Objects.equals(getSubject(), email.getSubject()) && Objects.equals(getText(), email.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getReceiver(), getSubject(), getText());
    }
}
