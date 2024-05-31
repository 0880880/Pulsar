package com.pulsar.api;

import com.pulsar.api.components.Text;
import com.pulsar.api.graphics.Material;
import com.pulsar.api.graphics.Shader;
import com.pulsar.api.graphics.Texture;
import com.pulsar.api.graphics.TextureAsset;
import com.pulsar.api.math.MathUtils;

public class Renderer {

 static Texture defaultTexture;
 static CameraHolder cameraHolder;
 static Shader lastShader;
 static Material defaultMaterial;


 static void update() {}

 public static void drawTexture(float x, float y, float width, float height, float rotation, Texture texture, com.pulsar.api.graphics.Color tint, Material material) {}



 public static Texture getDefaultTexture() {return null;}

 
public static void drawFilledRect(float x, float y, float width, float height, float rotation, com.pulsar.api.graphics.Color color) {}

 public static void drawRect(float x, float y, float width, float height, float rotation, com.pulsar.api.graphics.Color color, float strokeWidth) {}

 public static void drawCircle(float x, float y, float radius, com.pulsar.api.graphics.Color color, float strokeWidth) {}

 public static void drawFilledCircle(float x, float y, float radius, com.pulsar.api.graphics.Color color) {}

 public static void drawPolygon(float[] vertices, com.pulsar.api.graphics.Color color, float strokeWidth) {}

}
