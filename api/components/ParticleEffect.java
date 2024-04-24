package com.pulsar.api.components;

import com.pulsar.api.ColorRange;
import com.pulsar.api.Curve;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.graphics.Texture;
import com.pulsar.api.math.Vector2;

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




 public String name = "Flame";

 
 public boolean delayActive = false;
 public float delayLowMin = 0;
 public float delayLowMax = 0;

 
 public float durationLowMin = 3000;
 public float durationLowMax = 3000;

 
 public int countMin = 0;
 public int countMax = 200;

 
 public boolean emissionActive = true;
 public int emissionLowMin = 0;
 public int emissionLowMax = 0;
 public int emissionHighMin = 250;
 public int emissionHighMax = 250;
 public Curve emission = new Curve(new Vector2(0,1));

 
 public int lifeLowMin = 0;
 public int lifeLowMax = 0;
 public int lifeHighMin = 1000;
 public int lifeHighMax = 500;
 public Curve life = new Curve(new Vector2(0,1), new Vector2(.66f,1), new Vector2(1,.3f));

 
 public boolean lifeOffsetActive = false;
 public int lifeOffsetLowMin = 0;
 public int lifeOffsetLowMax = 0;
 public int lifeOffsetHighMin = 0;
 public int lifeOffsetHighMax = 0;
 public Curve lifeOffset = new Curve();

 
 public boolean xOffsetActive = false;
 public float xOffsetLowMin = 0;
 public float xOffsetLowMax = 0;

 
 public boolean yOffsetActive = false;
 public float yOffsetLowMin = 3000;
 public float yOffsetLowMax = 3000;

 
 public SpawnShape spawnShape = SpawnShape.POINT;
 public boolean spawnEdges;
 public SpawnEllipseSide spawnEllipseSide = SpawnEllipseSide.BOTH;

 
 public int spawnWidthLowMin = 0;
 public int spawnWidthLowMax = 0;
 public int spawnWidthHighMin = 0;
 public int spawnWidthHighMax = 0;
 public Curve spawnWidth = new Curve(new Vector2(0,1));

 
 public int spawnHeightLowMin = 0;
 public int spawnHeightLowMax = 0;
 public int spawnHeightHighMin = 0;
 public int spawnHeightHighMax = 0;
 public Curve spawnHeight = new Curve(new Vector2(0,1));

 
 public float xScaleLowMin = 0;
 public float xScaleLowMax = 0;
 public float xScaleHighMin = 1;
 public float xScaleHighMax = 1;
 public Curve xScale = new Curve(new Vector2(0,1));

 
 public float yScaleLowMin = 0;
 public float yScaleLowMax = 0;
 public float yScaleHighMin = 0;
 public float yScaleHighMax = 0;
 public Curve yScale = new Curve(new Vector2(0,1));

 
 public boolean velocityActive = true;
 public float velocityLowMin = 0;
 public float velocityLowMax = 0;
 public float velocityHighMin = .3f;
 public float velocityHighMax = 3;
 public Curve velocity = new Curve(new Vector2(0,1));

 
 public boolean angleActive = true;
 public float angleLowMin = 90f;
 public float angleLowMax = 90f;
 public float angleHighMin = 45f;
 public float angleHighMax = 135f;
 public Curve angle = new Curve(new Vector2(0,1), new Vector2(.5f,0), new Vector2(1,0));

 
 public boolean rotationActive = false;
 public float rotationLowMin = 0;
 public float rotationLowMax = 0;
 public float rotationHighMin = .3f;
 public float rotationHighMax = 3;
 public Curve rotation = new Curve(new Vector2(0,1));

 
 public boolean windActive = false;
 public float windLowMin = 0;
 public float windLowMax = 0;
 public float windHighMin = .3f;
 public float windHighMax = 3;
 public Curve wind = new Curve(new Vector2(0,1));

 
 public boolean gravityActive = false;
 public float gravityLowMin = 0;
 public float gravityLowMax = 0;
 public float gravityHighMin = .3f;
 public float gravityHighMax = 3;
 public Curve gravity = new Curve(new Vector2(0,1));

 
 public ColorRange tintColors = new ColorRange(new float[] {}, new Color(1, 0.12156863f, 0.047058824f, 1));

 
 public Curve transparency = new Curve(new Vector2(0,0), new Vector2(.2f,1), new Vector2(.8f,.75f), new Vector2(1, 0));

 
 public boolean optionsAttached = false;
 public boolean optionsContinuous = true;
 public boolean optionsAligned = false;
 public boolean optionsAdditive = true;
 public boolean optionsBehind = false;
 public boolean optionsPremultipliedAlpha = false;
 public SpriteMode optionsSpriteMode = SpriteMode.SINGLE;


}
