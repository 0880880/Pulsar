package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

;

public class WeldJoint extends Component {

 public RigidBody otherBody;
 public Vector2 anchor = new Vector2();
 public float dampingRatio = 0;
 public float frequency = 0;


 private boolean initialize = true;

 public void fixedUpdate() {}

}
