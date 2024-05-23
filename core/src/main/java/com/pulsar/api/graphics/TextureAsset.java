package com.pulsar.api.graphics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pulsar.Statics;

public class TextureAsset
    implements Json.Serializable
{

    String path;
    String filename;
    transient com.badlogic.gdx.graphics.Texture gdxTexture;

    public TextureAsset() { }

    public TextureAsset(String path, String filename, com.badlogic.gdx.graphics.Texture gdxTexture) {
        this.path = path;
        this.filename = filename;
        this.gdxTexture = gdxTexture;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public com.badlogic.gdx.graphics.Texture getGdxTexture() {
        return gdxTexture;
    }

    public int getWidth() {
        return gdxTexture.getWidth();
    }

    public int getHeight() {
        return gdxTexture.getHeight();
    }

    @Override public void write(Json json) {
        json.writeValue("path", path.substring(Statics.currentProjectPath.path().length()));
        json.writeValue("filename", filename);
    }

    @Override public void read(Json json, JsonValue jsonData) {
        path = Statics.currentProjectPath.path() + json.readValue("path", String.class, jsonData);
        filename = json.readValue("filename", String.class, jsonData);
    }
}
