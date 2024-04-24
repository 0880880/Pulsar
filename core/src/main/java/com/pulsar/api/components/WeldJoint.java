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

    private com.badlogic.gdx.physics.box2d.joints.WeldJoint joint;

    private boolean initialize = true;

    public void fixedUpdate() {

        if (initialize) {
            initialize = false;
            if (gameObject.hasComponent(RigidBody.class)) {
                com.badlogic.gdx.physics.box2d.joints.WeldJointDef jointDef = new com.badlogic.gdx.physics.box2d.joints.WeldJointDef();
                RigidBody bodyA = gameObject.getComponent(RigidBody.class);
                jointDef.dampingRatio = dampingRatio;
                jointDef.frequencyHz = frequency;
                jointDef.initialize(bodyA.getBody(), otherBody.getBody(), new com.badlogic.gdx.math.Vector2(anchor.x, anchor.y));

                joint = (com.badlogic.gdx.physics.box2d.joints.WeldJoint) Physics.createJoint(jointDef);
            }
        }

        joint.setDampingRatio(dampingRatio);
        joint.setFrequency(frequency);

    }

}
