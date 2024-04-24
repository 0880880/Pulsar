package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.Debug;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.math.Vector2;
import com.pulsar.api.physics.Physics;

public class BoxCollider extends Component implements Collider {

 public Vector2 size = new Vector2(1,1);
 public Vector2 center = new Vector2(0,0);
 public float angle = 0;
 public float density = 1;
 public float friction = .5f;
 public float restitution = 0;
 public boolean isSensor = false;
 public PhysicsFilter filter = new PhysicsFilter();


 public int ID;

 public void start() {}



 public void debugUpdate() {}

 @Override
 public int hashCode() {return 0;}

}
