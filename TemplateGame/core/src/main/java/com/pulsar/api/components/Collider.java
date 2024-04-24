package com.pulsar.api.components;

public interface Collider {

    com.badlogic.gdx.physics.box2d.Fixture createFixture(com.badlogic.gdx.physics.box2d.Body body);

    com.badlogic.gdx.physics.box2d.Fixture getFixture();

    class PhysicsFilter {

        public short categoryBits = 1;
        public short maskBits = -1;
        public short groupIndex;

    }
}

