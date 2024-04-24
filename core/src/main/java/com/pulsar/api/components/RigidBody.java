package com.pulsar.api.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.pulsar.api.Component;
import com.pulsar.api.physics.Physics;
import com.pulsar.api.math.Vector2;

public class RigidBody extends Component {

    public boolean isStatic = false;
    public boolean fixedRotation = false;
    public boolean bullet = false;
    public String userData;

    private Body body;

    public void start() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(gameObject.transform.position.x, gameObject.transform.position.y);
        bodyDef.angle = MathUtils.degRad * gameObject.transform.rotation;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = bullet;

        body = Physics.createBody(bodyDef);
        body.setUserData(gameObject.ID);

        for (int i = 0; i < gameObject.components.size(); i++) {
            Component component = gameObject.components.get(i);

            if (ClassReflection.isAssignableFrom(Collider.class, component.getClass())) {
                Collider collider = (Collider) component;
                collider.createFixture(body);
            }
        }

    }

    public void fixedUpdate() {

        gameObject.transform.position.set(body.getPosition().x, body.getPosition().y);
        gameObject.transform.rotation = MathUtils.radDeg * body.getAngle();

        body.setFixedRotation(fixedRotation);
        body.setBullet(bullet);

    }

    public void setAwake(boolean awake) {
        body.setAwake(awake);
    }

    public void setPosition(float x, float y) {
        setTransform(x, y, body.getAngle());
    }

    public void setAngle(float angle) {
        setTransform(body.getPosition().x, body.getPosition().y, angle);
    }

    public void setTransform(float x, float y, float angle) {
        body.setTransform(x, y, angle);
    }

    public void applyForce(float forceX, float forceY, float pointX, float pointY, boolean wake) {
        body.applyForce(forceX, forceY, pointX, pointY, wake);
    }

    public void applyForce(Vector2 force, Vector2 point, boolean wake) {
        applyForce(force.x, force.y, point.x, point.y, wake);
    }

    public void applyForceToCenter(float forceX, float forceY, boolean wake) {
        body.applyForceToCenter(forceX, forceY, wake);
    }

    public void applyForceToCenter(Vector2 force, boolean wake) {
        applyForceToCenter(force.x, force.y, wake);
    }

    public void applyLinearImpulse(float impulseX, float impulseY, float pointX, float pointY, boolean wake) {
        body.applyLinearImpulse(impulseX, impulseY, pointX, pointY, wake);
    }

    public void applyLinearImpulse(Vector2 impulse, Vector2 point, boolean wake) {
        applyLinearImpulse(impulse.x, impulse.y, point.x, point.y, wake);
    }

    public void applyAngularImpulse(float impulse, boolean wake) {
        body.applyAngularImpulse(impulse, wake);
    }

    public void applyTorque(float torque, boolean wake) {
        body.applyTorque(torque, wake);
    }

    public void setLinearVelocity(float velocityX, float velocityY) {
        body.setLinearVelocity(velocityX, velocityY);
    }

    public void setLinearVelocity(Vector2 velocity) {
        setLinearVelocity(velocity.x, velocity.y);
    }

    public void setAngularVelocity(float omega) {
        body.setAngularVelocity(omega);
    }

    private Vector2 tmp = new Vector2();

    public Vector2 getLinearVelocity() {
        return tmp.set(body.getLinearVelocity().x, body.getLinearVelocity().y);
    }

    public float getLinearVelocityX() {
        return body.getLinearVelocity().x;
    }

    public float getLinearVelocityY() {
        return body.getLinearVelocity().y;
    }

    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    Body getBody() {
        return body;
    }

}
