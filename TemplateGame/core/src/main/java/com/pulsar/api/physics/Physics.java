package com.pulsar.api.physics;

import com.game.Statics;
import com.pulsar.api.Component;
import com.pulsar.api.GameObject;
import com.pulsar.api.components.Collider;
import com.pulsar.api.math.Vector2;
import com.game.Utils;

import java.util.HashMap;

public class Physics {

    private static com.badlogic.gdx.physics.box2d.World world;

    public static boolean enabled = true;
    public static  Vector2 gravity = new Vector2(0, -9.81f);
    public static int velocityIterations = 6;
    public static int positionIterations = 2;

    public static int colliderCounter = 0;

    private static  HashMap<Integer, Collider> colliders = new HashMap<>();

    public static void start() {
        colliderCounter = 0;
        enabled = Statics.currentProject.physicsEnabled;
        gravity.set(Statics.currentProject.physicsGravity);
        velocityIterations = Statics.currentProject.physicsVelocityIterations;
        positionIterations = Statics.currentProject.physicsPositionIterations;

        world = new com.badlogic.gdx.physics.box2d.World(new com.badlogic.gdx.math.Vector2(gravity.x, gravity.y), true);
        colliders.clear();
    }

    private interface OnCollisionFunction {
        void onCollision(Collision collision);
    }

    public interface RayCastCallback {
        float reportRayCollider(Collider collider, Vector2 position, Vector2 normal, float fraction);
    }

    public interface QueryCallback {
        boolean reportCollider(Collider collider);
    }

    private static void enter(com.badlogic.gdx.physics.box2d.Fixture fixture) {
        int ID = (int) fixture.getBody().getUserData();
        GameObject gameObject = Utils.getGameObject(ID);
        Collider collider = null;
        for (Component component : gameObject.components) {
            if (component instanceof Collider) {
                Collider c = (Collider) component;
                if (c.getFixture().equals(fixture)) {
                    collider = c;
                    break;
                }
            }
        }
        for (Component component : gameObject.components) {
            component.onEnterCollision(new Collision(collider));
        }
    }

    private static void leave(com.badlogic.gdx.physics.box2d.Fixture fixture) {
        int ID = (int) fixture.getBody().getUserData();
        GameObject gameObject = Utils.getGameObject(ID);
        Collider collider = null;
        for (Component component : gameObject.components) {
            if (component instanceof Collider) {
                Collider c = (Collider) component;
                if (c.getFixture().equals(fixture)) {
                    collider = c;
                    break;
                }
            }
        }
        for (Component component : gameObject.components) {
            component.onLeaveCollision(new Collision(collider));
        }
    }

    public static void stop() {
        com.badlogic.gdx.utils.Array<com.badlogic.gdx.physics.box2d.Body> bodies = new com.badlogic.gdx.utils.Array<>();
        world.getBodies(bodies);
        for (com.badlogic.gdx.physics.box2d.Body body : bodies) {
            world.destroyBody(body);
        }
        world.setContactListener(new com.badlogic.gdx.physics.box2d.ContactListener() {
            @Override
            public void beginContact(com.badlogic.gdx.physics.box2d.Contact contact) {
                enter(contact.getFixtureA());
                enter(contact.getFixtureB());
            }

            @Override
            public void endContact(com.badlogic.gdx.physics.box2d.Contact contact) {
                leave(contact.getFixtureA());
                leave(contact.getFixtureB());
            }

            @Override
            public void preSolve(com.badlogic.gdx.physics.box2d.Contact contact, com.badlogic.gdx.physics.box2d.Manifold oldManifold) { }

            @Override
            public void postSolve(com.badlogic.gdx.physics.box2d.Contact contact, com.badlogic.gdx.physics.box2d.ContactImpulse impulse) { }
        });
        bodies.clear();
        world.dispose();
    }

    private static  com.badlogic.gdx.math.Vector2 tmpVec = new com.badlogic.gdx.math.Vector2();

    public static void step(float timeStep) {
        world.setGravity(tmpVec.set(gravity.x, gravity.y));
        world.step(timeStep, velocityIterations, positionIterations);
    }

    public static com.badlogic.gdx.physics.box2d.Body createBody(com.badlogic.gdx.physics.box2d.BodyDef bodyDef) {
        return world.createBody(bodyDef);
    }

    public static com.badlogic.gdx.physics.box2d.Joint createJoint(com.badlogic.gdx.physics.box2d.JointDef jointDef) {
        return world.createJoint(jointDef);
    }

    public static void addCollider(Collider collider, com.badlogic.gdx.physics.box2d.Fixture fixture) {
        colliders.put((int) fixture.getUserData(), collider);
    }

    public static void rayCast(RayCastCallback callback, float x0, float y0, float x1, float y1) {
        world.rayCast((fixture, point, normal, fraction) -> callback.reportRayCollider(colliders.get((int) fixture.getUserData()), new Vector2(point.x, point.y), new Vector2(normal.x, normal.y), fraction), x0, y0, x1, y1);
    }

    public static void queryAABB(QueryCallback callback, float x0, float y0, float x1, float y1) {
        world.QueryAABB(fixture -> callback.reportCollider(colliders.get((int) fixture.getUserData())), x0, y0, x1, y1);
    }

}

