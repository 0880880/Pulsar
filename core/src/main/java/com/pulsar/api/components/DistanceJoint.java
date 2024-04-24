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

    private com.badlogic.gdx.physics.box2d.joints.DistanceJoint joint;

    private boolean initialize = true;

    public void fixedUpdate() {

        if (initialize) {
            initialize = false;
            if (gameObject.hasComponent(RigidBody.class)) {
                com.badlogic.gdx.physics.box2d.joints.DistanceJointDef jointDef = new com.badlogic.gdx.physics.box2d.joints.DistanceJointDef();
                jointDef.length = length;
                jointDef.dampingRatio = dampingRatio;
                jointDef.frequencyHz = frequency;
                RigidBody bodyA = gameObject.getComponent(RigidBody.class);
                jointDef.initialize(bodyA.getBody(), otherBody.getBody(), new com.badlogic.gdx.math.Vector2(anchorA.x, anchorA.y), new com.badlogic.gdx.math.Vector2(anchorB.x, anchorB.y));

                joint = (com.badlogic.gdx.physics.box2d.joints.DistanceJoint) Physics.createJoint(jointDef);
            }
        }

        joint.setLength(length);
        joint.setDampingRatio(dampingRatio);
        joint.setFrequency(frequency);

    }

}
