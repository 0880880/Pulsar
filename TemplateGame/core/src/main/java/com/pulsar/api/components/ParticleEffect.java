package com.pulsar.api.components;

import com.pulsar.api.ColorRange;
import com.pulsar.api.Curve;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.graphics.Texture;
import com.pulsar.api.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class ParticleEffect {

    public enum SpawnShape {
        POINT,
        LINE,
        ELLIPSE,
        SQUARE
    }

    public enum SpawnEllipseSide {
        BOTH,
        TOP,
        BOTTOM
    }

    public enum SpriteMode {
        SINGLE,
        RANDOM,
        ANIMATED
    }

    public static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape getGdxSpawnShape(SpawnShape spawnShape) {
        com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.point;
        if (spawnShape == SpawnShape.LINE) e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.line;
        if (spawnShape == SpawnShape.ELLIPSE) e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.ellipse;
        if (spawnShape == SpawnShape.SQUARE) e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.square;
        return e;
    }

    public static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnEllipseSide getGdxSpawnEllipseSide(SpawnEllipseSide spawnEllipseSide) {
        com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnEllipseSide e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnEllipseSide.both;
        if (spawnEllipseSide == SpawnEllipseSide.TOP) e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnEllipseSide.top;
        if (spawnEllipseSide == SpawnEllipseSide.BOTTOM) e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnEllipseSide.bottom;
        return e;
    }

    public static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode getGdxSpriteMode(SpriteMode spriteMode) {
        com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode.single;
        if (spriteMode == SpriteMode.RANDOM) e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode.random;
        if (spriteMode == SpriteMode.ANIMATED) e = com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode.animated;
        return e;
    }

    public String name = "Flame";

    // Delay
    public boolean delayActive = false;
    public float delayLowMin = 0;
    public float delayLowMax = 0;

    // Duration
    public float durationLowMin = 3000;
    public float durationLowMax = 3000;

    // Count
    public int countMin = 0;
    public int countMax = 200;

    // Emission
    public boolean emissionActive = true;
    public int emissionLowMin = 0;
    public int emissionLowMax = 0;
    public int emissionHighMin = 250;
    public int emissionHighMax = 250;
    public Curve emission = new Curve(new Vector2(0,1));

    // Life
    public int lifeLowMin = 0;
    public int lifeLowMax = 0;
    public int lifeHighMin = 1000;
    public int lifeHighMax = 500;
    public Curve life = new Curve(new Vector2(0,1), new Vector2(.66f,1), new Vector2(1,.3f));

    // Life Offset
    public boolean lifeOffsetActive = false;
    public int lifeOffsetLowMin = 0;
    public int lifeOffsetLowMax = 0;
    public int lifeOffsetHighMin = 0;
    public int lifeOffsetHighMax = 0;
    public Curve lifeOffset = new Curve();

    // X Offset
    public boolean xOffsetActive = false;
    public float xOffsetLowMin = 0;
    public float xOffsetLowMax = 0;

    // Y Offset
    public boolean yOffsetActive = false;
    public float yOffsetLowMin = 3000;
    public float yOffsetLowMax = 3000;

    // Spawn Shape
    public SpawnShape spawnShape = SpawnShape.POINT;
    public boolean spawnEdges;
    public SpawnEllipseSide spawnEllipseSide = SpawnEllipseSide.BOTH;

    // Spawn Width
    public int spawnWidthLowMin = 0;
    public int spawnWidthLowMax = 0;
    public int spawnWidthHighMin = 0;
    public int spawnWidthHighMax = 0;
    public Curve spawnWidth = new Curve(new Vector2(0,1));

    // Spawn Height
    public int spawnHeightLowMin = 0;
    public int spawnHeightLowMax = 0;
    public int spawnHeightHighMin = 0;
    public int spawnHeightHighMax = 0;
    public Curve spawnHeight = new Curve(new Vector2(0,1));

    // X Scale
    public float xScaleLowMin = 0;
    public float xScaleLowMax = 0;
    public float xScaleHighMin = 1;
    public float xScaleHighMax = 1;
    public Curve xScale = new Curve(new Vector2(0,1));

    // Y Scale
    public float yScaleLowMin = 0;
    public float yScaleLowMax = 0;
    public float yScaleHighMin = 0;
    public float yScaleHighMax = 0;
    public Curve yScale = new Curve(new Vector2(0,1));

    // Velocity
    public boolean velocityActive = true;
    public float velocityLowMin = 0;
    public float velocityLowMax = 0;
    public float velocityHighMin = .3f;
    public float velocityHighMax = 3;
    public Curve velocity = new Curve(new Vector2(0,1));

    // Angle
    public boolean angleActive = true;
    public float angleLowMin = 90f;
    public float angleLowMax = 90f;
    public float angleHighMin = 45f;
    public float angleHighMax = 135f;
    public Curve angle = new Curve(new Vector2(0,1), new Vector2(.5f,0), new Vector2(1,0));

    // Rotation
    public boolean rotationActive = false;
    public float rotationLowMin = 0;
    public float rotationLowMax = 0;
    public float rotationHighMin = .3f;
    public float rotationHighMax = 3;
    public Curve rotation = new Curve(new Vector2(0,1));

    // Wind
    public boolean windActive = false;
    public float windLowMin = 0;
    public float windLowMax = 0;
    public float windHighMin = .3f;
    public float windHighMax = 3;
    public Curve wind = new Curve(new Vector2(0,1));

    // Gravity
    public boolean gravityActive = false;
    public float gravityLowMin = 0;
    public float gravityLowMax = 0;
    public float gravityHighMin = .3f;
    public float gravityHighMax = 3;
    public Curve gravity = new Curve(new Vector2(0,1));

    // Tint
    public ColorRange tintColors = new ColorRange(new float[] {0}, new Color(1, 0.12156863f, 0.047058824f, 1));

    // Transparency
    public Curve transparency = new Curve(new Vector2(0,0), new Vector2(.2f,1), new Vector2(.8f,.75f), new Vector2(1, 0));

    // Options
    public boolean optionsAttached = false;
    public boolean optionsContinuous = true;
    public boolean optionsAligned = false;
    public boolean optionsAdditive = true;
    public boolean optionsBehind = false;
    public boolean optionsPremultipliedAlpha = false;
    public SpriteMode optionsSpriteMode = SpriteMode.SINGLE;

    // Sprites
    public ArrayList<Texture> sprites = new ArrayList<>(List.of(new Texture()));

}

