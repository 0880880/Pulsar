package com.pulsar.api;

public class Button {

    public interface ButtonListener {
        void click();
    }

    public String text;
    public ButtonListener listener;

    public Button(String text, ButtonListener listener) {
        this.text = text;
        this.listener = listener;
    }

}

