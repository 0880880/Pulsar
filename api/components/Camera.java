package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.Debug;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.math.Vector2;
import com.pulsar.api.math.Vector3;

public class Camera extends Component {

 public Vector2 size = new Vector2(19.2f, 10.8f);

 public Color backgroundColor = new Color(.4f, .4f, .4f, 1);


 public void start() {}

 public void update() {}

 public void debugUpdate() {}



 public Vector2 project(Vector2 worldCoords) {return null;}

 
public Vector3 project(Vector3 worldCoords) {return null;}

 
public Vector2 unproject(Vector2 screenCoords) {return null;}

 
public Vector3 unproject(Vector3 screenCoords) {return null;}



}
