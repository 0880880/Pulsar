package com.pulsar.api;

import com.pulsar.api.components.Transform;



public class GameObject implements Cloneable
{

 public String name;
 public int ID;



 public GameObject parent;
 public int parentID;

 public Transform transform = new Transform();

 public transient boolean renameMode = false;

 public GameObject() {}

 public GameObject(int ID) {}

 public GameObject(String name) {}

 void start() {}

 void update() {}

 void fixedUpdate() {}

 void lateUpdate() {}

 public void addGameObject(GameObject child) {}

 public void removeGameObject(GameObject child) {}

 public void addComponent(Component component) {}

 public void addComponent(Class<? extends Component> componentClass) {}

 public GameObject clone() {return null;}

 
public boolean hasComponent(Class<? extends Component> componentClass) {return false;}

 
public <T> T getComponent(Class<T> componentClass) {return null;}

 
public void removeComponent(Component component) {}



}
