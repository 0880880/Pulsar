package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.Debug;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.math.Vector2;
import com.pulsar.api.physics.Physics;

public class BoxCollider extends Component implements Collider {

    public Vector2 size = new Vector2(1,1);
    public Vector2 center = new Vector2(0,0);
    public float angle = 0;
    public float density = 1;
    public float friction = .5f;
    public float restitution = 0;
    public boolean isSensor = false;
    public PhysicsFilter filter = new PhysicsFilter();

    private com.badlogic.gdx.physics.box2d.Fixture fixture;

    public int ID;

    public void start() {
        ID = Physics.colliderCounter++;
    }

    public com.badlogic.gdx.physics.box2d.Fixture createFixture(com.badlogic.gdx.physics.box2d.Body body) {

        com.badlogic.gdx.physics.box2d.PolygonShape shape = new com.badlogic.gdx.physics.box2d.PolygonShape();
        shape.setAsBox(size.x / 2f, size.y / 2f, new com.badlogic.gdx.math.Vector2(center.x, center.y), angle);

        com.badlogic.gdx.physics.box2d.FixtureDef fixtureDef = new com.badlogic.gdx.physics.box2d.FixtureDef();
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

    public com.badlogic.gdx.physics.box2d.Fixture getFixture() {
        return fixture;
    }

    public void debugUpdate() {
        Vector2 position = gameObject.transform.position;
        Debug.rectangle(position.x + center.x, position.y + center.y, size.x, size.y, gameObject.transform.rotation + angle, Color.GREEN, 1);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }
}
