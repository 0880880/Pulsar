package com.pulsar.api.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pulsar.api.Component;
import com.pulsar.api.Debug;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.math.Vector2;
import com.pulsar.api.physics.Physics;

public class CircleCollider extends Component implements Collider {

    public float radius = 1;
    public float density = 1;
    public float friction = .5f;
    public float restitution = 0;
    public boolean isSensor = false;
    public PhysicsFilter filter = new PhysicsFilter();

    private Fixture fixture;

    public int ID;

    public void start() {
        ID = Physics.colliderCounter++;
    }

    public Fixture createFixture(Body body) {

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = isSensor;
        fixtureDef.filter.categoryBits = filter.categoryBits;
        fixtureDef.filter.maskBits = filter.maskBits;
        fixtureDef.filter.groupIndex = filter.groupIndex;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(Physics.colliderCounter);

        Physics.addCollider(this, fixture);

        shape.dispose();

        return fixture;

    }

    public Fixture getFixture() {
        return fixture;
    }

    public void debugUpdate() {
        Vector2 position = gameObject.transform.position;
        Debug.circle(position.x, position.y, radius, Color.GREEN, 1);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }

}
