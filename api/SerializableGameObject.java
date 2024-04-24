package com.pulsar.api;

import com.pulsar.api.components.*;

import java.util.HashMap;


public class SerializableGameObject {

 public String name;
 public int ID;


 public HashMap<String, HashMap<String, Object>> componentFields = new HashMap<>();


 public SerializableGameObject() {}

 public SerializableGameObject(GameObject gameObject) {}

 public GameObject createGameObject(GameObject parent) {return null;}


}
