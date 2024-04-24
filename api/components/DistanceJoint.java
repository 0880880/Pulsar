package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

public class DistanceJoint extends Component {

 public RigidBody otherBody;
 public Vector2 anchorA = new Vector2();
 public Vector2 anchorB = new Vector2();
 public float length = 1;
 public float dampingRatio = 0;
 public float frequency = 0;


 private boolean initialize = true;

 public void fixedUpdate() {}

}
