package com.pulsar.api.graphics;

import com.pulsar.api.math.Vector2;

public class Texture {

    public TextureAsset textureAsset;
    public Vector2 uv0 = new Vector2(0,0);
    public Vector2 uv1 = new Vector2(1,1);

    public Texture() {}

    public Texture(TextureAsset textureAsset) {
        this.textureAsset = textureAsset;
    }

}

