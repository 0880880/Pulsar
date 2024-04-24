package com.pulsar.api.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pulsar.api.ShaderLanguage;
import com.pulsar.utils.Utils;

public class Shader
    implements Json.Serializable
{

    public String shaderFile;
    public String shaderSource;

    // Ignore
    com.badlogic.gdx.graphics.glutils.ShaderProgram shaderProgram;

    // Ignore
    public ShaderLanguage.ShaderData shaderData;

    public Shader() { }

    public Shader(FileHandle shaderFile, String shaderSource) {

        this.shaderFile = shaderFile.path();
        this.shaderSource = shaderSource;

        shaderData = ShaderLanguage.getShaderData(shaderSource);

        this.shaderProgram = new ShaderProgram(shaderData.vertexSource, shaderData.fragmentSource);

    }

    public void update(FileHandle shaderFile, String shaderSource) {

        if (shaderProgram != null) this.shaderProgram.dispose();

        this.shaderFile = shaderFile.path();
        this.shaderSource = shaderSource;

        shaderData = ShaderLanguage.getShaderData(shaderSource);

        this.shaderProgram = new ShaderProgram(shaderData.vertexSource, shaderData.fragmentSource);

    }

    public void bind() {
        shaderProgram.bind();
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override public void write(Json json) {
        json.writeValue("shader_file", shaderFile);
        json.writeValue("shader_source", shaderSource);
    }

    @Override public void read(Json json, JsonValue jsonData) {
        update(Utils.getFile(json.readValue("shader_file", String.class, jsonData)), json.readValue("shader_source", String.class, jsonData));
    }
}
