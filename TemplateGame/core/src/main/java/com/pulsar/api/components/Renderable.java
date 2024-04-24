package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.Renderer;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.graphics.Material;
import com.pulsar.api.graphics.Texture;

public class Renderable extends Component {

    public Texture texture = new Texture();
    public Color tint = new Color(Color.WHITE);
    public Material material;

    public void update() {
        Transform transform = gameObject.transform;
        if (transform != null) {
            Renderer.drawTexture(transform.position.x, transform.position.y, transform.scale.x, transform.scale.y, transform.rotation, texture, tint, material);
        }
    }

    public void debugUpdate() {
        update();
    }

}

