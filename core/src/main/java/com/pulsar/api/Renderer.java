package com.pulsar.api;

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pulsar.api.components.Text;
import com.pulsar.api.graphics.Material;
import com.pulsar.api.graphics.Shader;
import com.pulsar.api.graphics.Texture;
import com.pulsar.api.graphics.TextureAsset;
import com.pulsar.api.math.MathUtils;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Renderer {

    static CpuSpriteBatch batch;
    static Sprite sprite;
    static ShapeDrawer drawer;
    static Texture defaultTexture;
    static CameraHolder cameraHolder;
    static Shader lastShader;
    static Material defaultMaterial;

    static void init(CpuSpriteBatch b, ShapeDrawer d, Sprite s, TextureAsset t, CameraHolder c, Material m) {
        batch = b;
        sprite = s;
        drawer = d;
        defaultTexture = new Texture(t);
        cameraHolder = c;
        defaultMaterial = m;
    }

    static void update() {
        if (cameraHolder.camera != null)
            Input.update(cameraHolder.camera);
        Text.batch = batch;
        lastShader = null;
    }

    public static void drawTexture(float x, float y, float width, float height, float rotation, Texture texture, com.pulsar.api.graphics.Color tint, Material material) {
        Texture tex = texture.textureAsset != null ? texture : defaultTexture;

        sprite.setSize(width, height);
        sprite.setRotation(rotation);
        sprite.setColor(tint.r, tint.g, tint.b, tint.a);
        sprite.setPosition(x - width / 2f, y - height / 2f);
        sprite.setTexture(tex.textureAsset.getGdxTexture());
        if (sprite.getTexture() != null) sprite.setRegion(tex.uv0.x, tex.uv0.y, tex.uv1.x, tex.uv1.y);

        if (material != null && material.shader != null) {
            material.bind();
            try {
                batch.setShader(material.shader.getShaderProgram());
            } catch (IllegalArgumentException | IllegalStateException e) {
                Debug.error(e.getMessage());
            }
            lastShader = material.shader;
        } else {
            defaultMaterial.bind();
            try {
                batch.setShader(defaultMaterial.shader.getShaderProgram());
            } catch (IllegalArgumentException | IllegalStateException e) {
                Debug.error(e.getMessage());
            }
            lastShader = defaultMaterial.shader;
        }

        sprite.setOriginCenter();
        if (sprite.getTexture() != null) sprite.draw(batch);
    }

    private static final com.badlogic.gdx.math.Matrix4 transform = new com.badlogic.gdx.math.Matrix4();

    public static void drawGdxEmitter(com.badlogic.gdx.graphics.g2d.ParticleEmitter particleEmitter, float rotation) {
        batch.setTransformMatrix(transform.idt().rotate(0,0, 1,rotation));
        particleEmitter.draw(batch);
        batch.setTransformMatrix(transform.idt());
    }

    public static Texture getDefaultTexture() {
        return defaultTexture;
    }

    public static void drawFilledRect(float x, float y, float width, float height, float rotation, com.pulsar.api.graphics.Color color) {
        drawer.setColor(color.r, color.g, color.b, color.a);
        drawer.filledRectangle(x - width / 2f, y - height / 2f, width, height, rotation * MathUtils.degToRad);
    }

    public static void drawRect(float x, float y, float width, float height, float rotation, com.pulsar.api.graphics.Color color, float strokeWidth) {
        drawer.setColor(color.r, color.g, color.b, color.a);
        drawer.rectangle(x - width / 2f, y - height / 2f, width, height, strokeWidth, rotation * MathUtils.degToRad);
        drawer.setColor(1, 1, 1, 1);
    }

    public static void drawCircle(float x, float y, float radius, com.pulsar.api.graphics.Color color, float strokeWidth) {
        drawer.setColor(color.r, color.g, color.b, color.a);
        drawer.circle(x, y, radius, strokeWidth);
        drawer.setColor(1, 1, 1, 1);
    }

    public static void drawFilledCircle(float x, float y, float radius, com.pulsar.api.graphics.Color color) {
        drawer.setColor(color.r, color.g, color.b, color.a);
        drawer.filledCircle(x, y, radius);
        drawer.setColor(1, 1, 1, 1);
    }

    public static void drawPolygon(float[] vertices, com.pulsar.api.graphics.Color color, float strokeWidth) {
        drawer.setColor(color.r, color.g, color.b, color.a);
        drawer.polygon(vertices, strokeWidth, JoinType.SMOOTH);
        drawer.setColor(1, 1, 1, 1);
    }

}
