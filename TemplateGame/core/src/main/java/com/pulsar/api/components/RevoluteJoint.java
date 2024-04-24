package com.pulsar.api.components;

import com.badlogic.gdx.math.MathUtils;
import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

public class RevoluteJoint extends Component {

    public RigidBody otherBody;
    public Vector2 anchor = new Vector2();
    public boolean enableLimit = false;
    public float lowerAngle = 0;
    public float upperAngle = 180;
    public boolean enableMotor = false;
    public float maxMotorTorque = 0;
    public float motorSpeed = 0;

    private com.badlogic.gdx.physics.box2d.joints.RevoluteJoint joint;

    private boolean initialize = true;

    public void fixedUpdate() {

        if (initialize) {
            initialize = false;
            if (gameObject.hasComponent(RigidBody.class)) {
                com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef jointDef = new com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef();
                RigidBody bodyA = gameObject.getComponent(RigidBody.class);
                jointDef.initialize(bodyA.getBody(), otherBody.getBody(), new com.badlogic.gdx.math.Vector2(anchor.x, anchor.y));

                jointDef.lowerAngle = MathUtils.degRad * lowerAngle;
                jointDef.upperAngle = MathUtils.degRad * upperAngle;

                jointDef.enableLimit = enableLimit;
                jointDef.enableMotor = enableMotor;

                jointDef.maxMotorTorque = maxMotorTorque;
                jointDef.motorSpeed = motorSpeed;

                joint = (com.badlogic.gdx.physics.box2d.joints.RevoluteJoint) Physics.createJoint(jointDef);
            }
        }

        joint.enableMotor(enableMotor);
        joint.enableLimit(enableLimit);
        joint.setMaxMotorTorque(maxMotorTorque);
        joint.setMotorSpeed(motorSpeed);
        joint.setLimits(lowerAngle, upperAngle);

    }

}

