package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

public class WheelJoint extends Component {

 public RigidBody otherBody;
 public Vector2 anchor = new Vector2();
 public Vector2 axis = new Vector2();
 public float dampingRatio = 0;
 public float frequency = 0;
 public boolean enableMotor = false;
 public float maxMotorTorque = 0;
 public float motorSpeed = 0;


 private boolean initialize = true;

 public void fixedUpdate() {}

}
