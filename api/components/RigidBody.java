package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

public class RigidBody extends Component {

 public boolean isStatic = false;
 public boolean fixedRotation = false;
 public boolean bullet = false;
 public String userData;


 public void start() {}

 public void fixedUpdate() {}

 public void setAwake(boolean awake) {}

 public void setPosition(float x, float y) {}

 public void setAngle(float angle) {}

 public void setTransform(float x, float y, float angle) {}

 public void applyForce(float forceX, float forceY, float pointX, float pointY, boolean wake) {}

 public void applyForce(Vector2 force, Vector2 point, boolean wake) {}

 public void applyForceToCenter(float forceX, float forceY, boolean wake) {}

 public void applyForceToCenter(Vector2 force, boolean wake) {}

 public void applyLinearImpulse(float impulseX, float impulseY, float pointX, float pointY, boolean wake) {}

 public void applyLinearImpulse(Vector2 impulse, Vector2 point, boolean wake) {}

 public void applyAngularImpulse(float impulse, boolean wake) {}

 public void applyTorque(float torque, boolean wake) {}

 public void setLinearVelocity(float velocityX, float velocityY) {}

 public void setLinearVelocity(Vector2 velocity) {}

 public void setAngularVelocity(float omega) {}

 private Vector2 tmp = new Vector2();

 public Vector2 getLinearVelocity() {return null;}

 
public float getLinearVelocityX() {return 0.0f;}

 
public float getLinearVelocityY() {return 0.0f;}

 
public float getAngularVelocity() {return 0.0f;}



}
