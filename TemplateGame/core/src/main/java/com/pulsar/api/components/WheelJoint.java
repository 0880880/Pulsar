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

    private com.badlogic.gdx.physics.box2d.joints.WheelJoint joint;

    private boolean initialize = true;

    public void fixedUpdate() {

        if (initialize) {
            initialize = false;
            if (gameObject.hasComponent(RigidBody.class)) {
                com.badlogic.gdx.physics.box2d.joints.WheelJointDef jointDef = new com.badlogic.gdx.physics.box2d.joints.WheelJointDef();
                RigidBody bodyA = gameObject.getComponent(RigidBody.class);
                jointDef.initialize(bodyA.getBody(), otherBody.getBody(), new com.badlogic.gdx.math.Vector2(anchor.x, anchor.y), new com.badlogic.gdx.math.Vector2(axis.x, axis.y));

                jointDef.enableMotor = enableMotor;
                jointDef.dampingRatio = dampingRatio;
                jointDef.frequencyHz = frequency;

                jointDef.maxMotorTorque = maxMotorTorque;
                jointDef.motorSpeed = motorSpeed;

                joint = (com.badlogic.gdx.physics.box2d.joints.WheelJoint) Physics.createJoint(jointDef);
            }
        }

        joint.enableMotor(enableMotor);
        joint.setMaxMotorTorque(maxMotorTorque);
        joint.setMotorSpeed(motorSpeed);
        joint.setSpringDampingRatio(dampingRatio);
        joint.setSpringFrequencyHz(frequency);

    }

}

