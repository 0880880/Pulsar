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

    private com.badlogic.gdx.physics.box2d.joints.PrismaticJoint joint;

    private boolean initialize = true;

    public void fixedUpdate() {

        if (initialize) {
            initialize = false;
            if (gameObject.hasComponent(RigidBody.class)) {
                com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef jointDef = new com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef();
                RigidBody bodyA = gameObject.getComponent(RigidBody.class);
                jointDef.initialize(bodyA.getBody(), otherBody.getBody(), new com.badlogic.gdx.math.Vector2(anchor.x, anchor.y), new com.badlogic.gdx.math.Vector2(axis.x, axis.y));

                jointDef.enableLimit = enableLimit;
                jointDef.enableMotor = enableMotor;

                jointDef.lowerTranslation = lowerTranslation;
                jointDef.upperTranslation = upperTranslation;

                jointDef.maxMotorForce = maxMotorForce;
                jointDef.motorSpeed = motorSpeed;

                joint = (com.badlogic.gdx.physics.box2d.joints.PrismaticJoint) Physics.createJoint(jointDef);
            }
        }

        joint.enableMotor(enableMotor);
        joint.enableLimit(enableLimit);
        joint.setMaxMotorForce(maxMotorForce);
        joint.setMotorSpeed(motorSpeed);
        joint.setLimits(lowerTranslation, upperTranslation);

    }

}

