package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

public class PrismaticJoint extends Component {

 public RigidBody otherBody;
 public Vector2 anchor = new Vector2();
 public Vector2 axis = new Vector2(Vector2.Y);
 public boolean enableLimit = false;
 public float lowerTranslation = 0;
 public float upperTranslation = 1;
 public boolean enableMotor = false;
 public float maxMotorForce = 0;
 public float motorSpeed = 0;


 private boolean initialize = true;

 public void fixedUpdate() {}

}
