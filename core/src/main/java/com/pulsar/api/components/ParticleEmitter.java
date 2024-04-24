package com.pulsar.api.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pulsar.api.*;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.graphics.Texture;
import com.pulsar.api.math.MathUtils;
import com.pulsar.api.math.Vector2;

public class ParticleEmitter extends Component {

    private float[] getTimeline(Curve curve) {
        float[] scaling = new float[curve.size()];
        for (int i = 0; i < scaling.length; i++) {
            scaling[i] = curve.getPoint(i).x;
        }
        return scaling;
    }

    private float[] getTimeline(Curve curve, float[] timeline) {
        if (curve.size() != timeline.length) {
            timeline = new float[curve.size()];
        }
        for (int i = 0; i < timeline.length; i++) {
            timeline[i] = curve.getPoint(i).x;
        }
        return timeline;
    }

    private float[] getScaling(Curve curve) {
        float[] scaling = new float[curve.size()];
        for (int i = 0; i < scaling.length; i++) {
            scaling[i] = curve.getPoint(i).y;
        }
        return scaling;
    }

    private float[] getScaling(Curve curve, float[] scaling) {
        if (curve.size() != scaling.length) {
            scaling = new float[curve.size()];
        }
        for (int i = 0; i < scaling.length; i++) {
            scaling[i] = curve.getPoint(i).x;
        }
        return scaling;
    }

    private com.badlogic.gdx.graphics.g2d.ParticleEmitter setEmitter(com.badlogic.gdx.graphics.g2d.ParticleEmitter emitter) {

        emitter.setName(particleEffect.name);
        if (particleEffect.delayActive) {
            emitter.getDelay().setActive(particleEffect.delayActive);
            emitter.getDelay().setLow(particleEffect.delayLowMin, particleEffect.delayLowMax);
        }
        emitter.getDuration().setActive(true);
        emitter.getDuration().setLow(particleEffect.durationLowMin, particleEffect.durationLowMax);
        emitter.setMinParticleCount(particleEffect.countMin);
        emitter.setMaxParticleCount(particleEffect.countMax);
        if (particleEffect.emissionActive) {
            emitter.getEmission().setActive(true);
            emitter.getEmission().setLow(particleEffect.emissionLowMin, particleEffect.emissionLowMax);
            emitter.getEmission().setHigh(particleEffect.emissionHighMin, particleEffect.emissionHighMax);
            emitter.getEmission().setTimeline(getTimeline(particleEffect.emission));
            emitter.getEmission().setScaling(getScaling(particleEffect.emission));
        }
        emitter.getLife().setActive(true);
        emitter.getLife().setLow(particleEffect.lifeLowMin, particleEffect.lifeLowMax);
        emitter.getLife().setHigh(particleEffect.lifeHighMin, particleEffect.lifeHighMax);
        emitter.getLife().setTimeline(getTimeline(particleEffect.life));
        emitter.getLife().setScaling(getScaling(particleEffect.life));
        if (particleEffect.lifeOffsetActive) {
            emitter.getLifeOffset().setActive(particleEffect.lifeOffsetActive);
            emitter.getLifeOffset().setLow(particleEffect.lifeOffsetLowMin, particleEffect.lifeOffsetLowMax);
            emitter.getLifeOffset().setHigh(particleEffect.lifeOffsetHighMin, particleEffect.lifeOffsetHighMax);
            emitter.getLifeOffset().setTimeline(getTimeline(particleEffect.lifeOffset));
            emitter.getLifeOffset().setScaling(getScaling(particleEffect.lifeOffset));
        }
        if (particleEffect.xOffsetActive) {
            emitter.getXOffsetValue().setActive(particleEffect.xOffsetActive);
            emitter.getXOffsetValue().setLow(particleEffect.xOffsetLowMin, particleEffect.xOffsetLowMax);
        }
        if (particleEffect.yOffsetActive) {
            emitter.getYOffsetValue().setActive(particleEffect.yOffsetActive);
            emitter.getYOffsetValue().setLow(particleEffect.yOffsetLowMin, particleEffect.yOffsetLowMax);
        }
        emitter.getSpawnShape().setShape(ParticleEffect.getGdxSpawnShape(particleEffect.spawnShape));
        if (particleEffect.spawnShape != ParticleEffect.SpawnShape.POINT) {

            emitter.getSpawnWidth().setLow(particleEffect.spawnWidthLowMin, particleEffect.spawnWidthLowMax);
            emitter.getSpawnWidth().setHigh(particleEffect.spawnWidthHighMin, particleEffect.spawnWidthHighMax);
            emitter.getSpawnWidth().setTimeline(getTimeline(particleEffect.spawnWidth));
            emitter.getSpawnWidth().setScaling(getScaling(particleEffect.spawnWidth));

            emitter.getSpawnHeight().setLow(particleEffect.spawnHeightLowMin, particleEffect.spawnHeightLowMax);
            emitter.getSpawnHeight().setHigh(particleEffect.spawnHeightHighMin, particleEffect.spawnHeightHighMax);
            emitter.getSpawnHeight().setTimeline(getTimeline(particleEffect.spawnHeight));
            emitter.getSpawnHeight().setScaling(getScaling(particleEffect.spawnHeight));

            if (particleEffect.spawnShape == ParticleEffect.SpawnShape.ELLIPSE) {
                emitter.getSpawnShape().setEdges(particleEffect.spawnEdges);
                emitter.getSpawnShape().setSide(ParticleEffect.getGdxSpawnEllipseSide(particleEffect.spawnEllipseSide));
            }

        }
        emitter.getXScale().setLow(particleEffect.xScaleLowMin, particleEffect.xScaleLowMax);
        emitter.getXScale().setHigh(particleEffect.xScaleHighMin, particleEffect.xScaleHighMax);
        emitter.getXScale().setTimeline(getTimeline(particleEffect.xScale));
        emitter.getXScale().setScaling(getScaling(particleEffect.xScale));
        emitter.getYScale().setLow(particleEffect.yScaleLowMin, particleEffect.yScaleLowMax);
        emitter.getYScale().setHigh(particleEffect.yScaleHighMin, particleEffect.yScaleHighMax);
        emitter.getYScale().setTimeline(getTimeline(particleEffect.yScale));
        emitter.getYScale().setScaling(getScaling(particleEffect.yScale));
        if (particleEffect.velocityActive) {
            emitter.getVelocity().setActive(particleEffect.velocityActive);
            emitter.getVelocity().setLow(particleEffect.velocityLowMin, particleEffect.velocityLowMax);
            emitter.getVelocity().setHigh(particleEffect.velocityHighMin, particleEffect.velocityHighMax);
            emitter.getVelocity().setTimeline(getTimeline(particleEffect.velocity));
            emitter.getVelocity().setScaling(getScaling(particleEffect.velocity));
        }
        if (particleEffect.angleActive) {
            emitter.getAngle().setActive(particleEffect.angleActive);
            emitter.getAngle().setLow(particleEffect.angleLowMin, particleEffect.angleLowMax);
            emitter.getAngle().setHigh(particleEffect.angleHighMin, particleEffect.angleHighMax);
            emitter.getAngle().setTimeline(getTimeline(particleEffect.angle));
            emitter.getAngle().setScaling(getScaling(particleEffect.angle));
        }
        if (particleEffect.rotationActive) {
            emitter.getRotation().setActive(particleEffect.rotationActive);
            emitter.getRotation().setLow(particleEffect.rotationLowMin, particleEffect.rotationLowMax);
            emitter.getRotation().setHigh(particleEffect.rotationHighMin, particleEffect.rotationHighMax);
            emitter.getRotation().setTimeline(getTimeline(particleEffect.rotation));
            emitter.getRotation().setScaling(getScaling(particleEffect.rotation));
        }
        if (particleEffect.windActive) {
            emitter.getWind().setActive(particleEffect.windActive);
            emitter.getWind().setLow(particleEffect.windLowMin, particleEffect.windLowMax);
            emitter.getWind().setHigh(particleEffect.windHighMin, particleEffect.windHighMax);
            emitter.getWind().setTimeline(getTimeline(particleEffect.wind));
            emitter.getWind().setScaling(getScaling(particleEffect.wind));
        }
        if (particleEffect.gravityActive) {
            emitter.getGravity().setActive(particleEffect.gravityActive);
            emitter.getGravity().setLow(particleEffect.gravityLowMin, particleEffect.gravityLowMax);
            emitter.getGravity().setHigh(particleEffect.gravityHighMin, particleEffect.gravityHighMax);
            emitter.getGravity().setTimeline(getTimeline(particleEffect.gravity));
            emitter.getGravity().setScaling(getScaling(particleEffect.gravity));
        }
        {
            emitter.getTint().setActive(true);
            float[] scaling = emitter.getTint().getColors();
            float[] timeline = emitter.getTint().getTimeline();
            if (particleEffect.tintColors.size() != timeline.length) {
                timeline = new float[particleEffect.tintColors.size()];
                scaling = new float[particleEffect.tintColors.size() * 3];
            }
            for (int i = 0; i < particleEffect.tintColors.size(); i++) {
                Color c = particleEffect.tintColors.getColor(i);
                scaling[i * 3] = c.r;
                scaling[i * 3 + 1] = c.g;
                scaling[i * 3 + 2] = c.b;
                timeline[i] = particleEffect.tintColors.getTime(i);
            }
            emitter.getTint().setColors(scaling);
            emitter.getTint().setTimeline(timeline);
        }
        emitter.getTransparency().setActive(true);
        emitter.getTransparency().setLow(0, 0);
        emitter.getTransparency().setHigh(1, 1);
        emitter.getTransparency().setTimeline(getTimeline(particleEffect.transparency));
        emitter.getTransparency().setScaling(getScaling(particleEffect.transparency));
        emitter.setAttached(particleEffect.optionsAttached);
        emitter.setContinuous(particleEffect.optionsContinuous);
        emitter.setAligned(particleEffect.optionsAligned);
        emitter.setAdditive(particleEffect.optionsAdditive);
        emitter.setBehind(particleEffect.optionsBehind);
        emitter.setPremultipliedAlpha(particleEffect.optionsPremultipliedAlpha);
        emitter.setSpriteMode(ParticleEffect.getGdxSpriteMode(particleEffect.optionsSpriteMode));

        return emitter;

    }

    private com.badlogic.gdx.graphics.g2d.ParticleEmitter gdxEmitter;

    public ParticleEffect particleEffect = new ParticleEffect();
    public Vector2 position = new Vector2();
    public float rotation = 0;
    public boolean startOnAwake;

    public void start() {
        gdxEmitter = setEmitter(new com.badlogic.gdx.graphics.g2d.ParticleEmitter());
        for (int i = 0; i < particleEffect.sprites.size(); i++) {
            Sprite s;
            Texture tex = particleEffect.sprites.get(i);
            s = new com.badlogic.gdx.graphics.g2d.Sprite(Renderer.getDefaultTexture().textureAsset.getGdxTexture());
            s.setRegion(tex.uv0.x, tex.uv0.y, tex.uv1.x, tex.uv1.y);
            if (tex.textureAsset != null)
                s.setTexture(tex.textureAsset.getGdxTexture());
            gdxEmitter.getSprites().add(s);
        }
        if (startOnAwake) gdxEmitter.start();
    }

    public void update() {

        gdxEmitter.setPosition(
            gameObject.transform.position.x + (MathUtils.cosDeg(gameObject.transform.rotation - rotation) * position.x -
                MathUtils.sinDeg(gameObject.transform.rotation - rotation) * position.y),
            gameObject.transform.position.y + (MathUtils.sinDeg(gameObject.transform.rotation - rotation) * position.x +
                MathUtils.cosDeg(gameObject.transform.rotation - rotation) * position.y)
        );
        gdxEmitter.update(Time.getDeltaTime());
        Renderer.drawGdxEmitter(gdxEmitter, rotation);

    }

    public void startEmitter() {
        setEmitter(gdxEmitter);
        gdxEmitter.start();
    }

    public void resetEmitter() {
        setEmitter(gdxEmitter);
        gdxEmitter.reset();
    }

}
