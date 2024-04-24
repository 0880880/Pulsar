package com.game;

import com.pulsar.api.Renderer;
import com.pulsar.api.graphics.TextureAsset;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;

public class TextureManager {

    private static HashMap<String, TextureAsset> textures = new HashMap<>();

    public static TextureAsset get(String path, String name) {
        if ("internal".equals(path) && "NONAME".equals(name))
            textures.put(path, Renderer.getDefaultTexture().textureAsset);
        else if (!textures.containsKey(path))
            textures.put(path, new TextureAsset(path, name, new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(name))));
        return textures.get(path);
    }

}
