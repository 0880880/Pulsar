package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

;

public class RopeJoint extends Component {

    public RigidBody otherBody;
    public Vector2 anchorA = new Vector2();
    public Vector2 anchorB = new Vector2();
    public float maxLength = 1;

    private boolean initialize = true;

    public void fixedUpdate() {

        if (initialize) {
            initialize = false;
            if (gameObject.hasComponent(RigidBody.class)) {

                com.badlogic.gdx.physics.box2d.joints.RopeJointDef jointDef = new com.badlogic.gdx.physics.box2d.joints.RopeJointDef();
                jointDef.maxLength = maxLength;
                jointDef.bodyA = gameObject.getComponent(RigidBody.class).getBody();
                jointDef.bodyB = otherBody.getBody();
                jointDef.localAnchorA.set(anchorA.x, anchorA.y);
                jointDef.localAnchorB.set(anchorB.x, anchorB.y);

                Physics.createJoint(jointDef);
            }
        }

    }

}

