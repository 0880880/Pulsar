package com.pulsar.api;

public class Button {

 public interface ButtonListener {}

 public String text;
 public ButtonListener listener;

 public Button(String text, ButtonListener listener) {}

}
