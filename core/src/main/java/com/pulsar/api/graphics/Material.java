package com.pulsar.api.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pulsar.Statics;
import com.pulsar.api.Renderer;
import com.pulsar.api.ShaderLanguage;
import com.pulsar.api.math.Matrix3;
import com.pulsar.api.math.Matrix4;
import com.pulsar.api.math.Vector2;
import com.pulsar.api.math.Vector3;
import com.pulsar.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;

import static com.pulsar.Statics.json;

public class Material
    implements Json.Serializable
{

    public String materialFile;
    public String materialSource;

    // Ignore
    public Shader shader;
    public String shaderFile;

    public HashMap<String, Object> variableValueMap = new HashMap<>();
    public HashSet<ShaderLanguage.Variable> variableSet = new HashSet<>();

    public Material() {}

    public Material(FileHandle materialFile, String material) {

        this.materialFile = materialFile.path();
        this.materialSource = material;

        Material mat = json.fromJson(Material.class, material);

        shaderFile = mat.shaderFile;
        if (shaderFile != null) {
            if (!Statics.engine.loadedShaders.containsKey(shaderFile)) {
                Statics.engine.loadShader(shaderFile, shaderFile);
            }
            shader = Statics.engine.loadedShaders.get(shaderFile);
            update();
            variableValueMap.putAll(mat.variableValueMap);
        }

    }

    public void set(String name, int i) {
        variableValueMap.put(name, i);
    }

    public void set(String name, float i) {
        variableValueMap.put(name, i);
    }

    public void set(String name, Vector2 vec) {
        variableValueMap.put(name, vec);
    }

    public void set(String name, Vector3 vec) {
        variableValueMap.put(name, vec);
    }

    public void set(String name, Color col) {
        variableValueMap.put(name, col);
    }

    public void set(String name, Matrix3 mat) {
        variableValueMap.put(name, mat);
    }

    public void set(String name, Matrix4 mat) {
        variableValueMap.put(name, mat);
    }

    public void set(String name, Texture tex) {
        variableValueMap.put(name, tex);
    }

    public String save() {
        return json.toJson(this);
    }

    public void update() {
        if (shader != null) {
            shaderFile = shader.shaderFile;

            variableSet.clear();
            variableValueMap.clear();

            for (ShaderLanguage.Variable variable : shader.shaderData.variables) {
                variableSet.add(variable);
                Object val = null;

                if (variable.type == ShaderLanguage.VariableType.INT) {
                    val = 0;
                } else if (variable.type == ShaderLanguage.VariableType.FLOAT) {
                    val = 0f;
                    if (variable.array) val = new float[0];
                } else if (variable.type == ShaderLanguage.VariableType.VECTOR2) {
                    val = new Vector2();
                    if (variable.array) val = new float[0];
                } else if (variable.type == ShaderLanguage.VariableType.VECTOR3) {
                    val = new Vector3();
                    if (variable.array) val = new float[0];
                }/* else if (variable.type == ShaderLanguage.VariableType.VECTOR4) {
                    val = new Vector4();
                }*/ else if (variable.type == ShaderLanguage.VariableType.MATRIX3) {
                    val = new Matrix3();
                } else if (variable.type == ShaderLanguage.VariableType.MATRIX4) {
                    val = new Matrix4();
                }

                variableValueMap.put(variable.name, val);
            }

        }
    }

    com.badlogic.gdx.math.Vector2 tmpVec2 = new com.badlogic.gdx.math.Vector2();
    com.badlogic.gdx.math.Vector3 tmpVec3 = new com.badlogic.gdx.math.Vector3();
    com.badlogic.gdx.math.Vector4 tmpVec4 = new com.badlogic.gdx.math.Vector4();
    com.badlogic.gdx.math.Matrix3 tmpMat3 = new com.badlogic.gdx.math.Matrix3();
    com.badlogic.gdx.math.Matrix4 tmpMat4 = new com.badlogic.gdx.math.Matrix4();

    public void bind() {

        int textureUnit = 0;

        for (ShaderLanguage.Variable variable : variableSet) {
            Object value = variableValueMap.get(variable.name);

            if (variable.type == ShaderLanguage.VariableType.SAMPLER2D) {
                Texture texture = ((Texture) value);
                if (texture != null) {
                    texture.textureAsset.getGdxTexture().bind(textureUnit);
                } else {
                    Renderer.getDefaultTexture().textureAsset.getGdxTexture().bind(textureUnit);
                }
                textureUnit++;
            } else if (variable.type == ShaderLanguage.VariableType.INT) {
                shader.shaderProgram.setUniformi(variable.name, (int) value);
            } else if (variable.type == ShaderLanguage.VariableType.FLOAT) {
                if (variable.array) {
                    float[] arr = (float[]) value;
                    shader.shaderProgram.setUniform1fv(variable.name, arr, 0, arr.length);
                } else {
                    shader.shaderProgram.setUniformf(variable.name, (float) value);
                }
            } else if (variable.type == ShaderLanguage.VariableType.VECTOR2) {
                if (variable.array) {
                    float[] arr = (float[]) value;
                    shader.shaderProgram.setUniform2fv(variable.name, arr, 0, arr.length);
                } else {
                    Vector2 vec = (Vector2) value;
                    shader.shaderProgram.setUniformf(variable.name, tmpVec2.set(vec.x, vec.y));
                }
            } else if (variable.type == ShaderLanguage.VariableType.VECTOR3) {
                if (variable.array) {
                    float[] arr = (float[]) value;
                    shader.shaderProgram.setUniform3fv(variable.name, arr, 0, arr.length);
                } else {
                    Vector3 vec = (Vector3) value;
                    shader.shaderProgram.setUniformf(variable.name, tmpVec3.set(vec.x, vec.y, vec.z));
                }
            } else if (variable.type == ShaderLanguage.VariableType.VECTOR4) {
                if (variable.array) {
                    float[] arr = (float[]) value;
                    shader.shaderProgram.setUniform4fv(variable.name, arr, 0, arr.length);
                } else {
                    Color col = (Color) value;
                    shader.shaderProgram.setUniformf(variable.name, tmpVec4.set(col.r, col.g, col.b, col.a));
                }
            } else if (variable.type == ShaderLanguage.VariableType.MATRIX3) {
                Matrix3 mat = (Matrix3) value;
                tmpMat3.set(mat.getValues());
                shader.shaderProgram.setUniformMatrix(variable.name, tmpMat3);
            } else if (variable.type == ShaderLanguage.VariableType.MATRIX4) {
                Matrix4 mat = (Matrix4) value;
                tmpMat4.set(mat.getValues());
                shader.shaderProgram.setUniformMatrix(variable.name, tmpMat4);
            }

        }

    }

    @Override public void write(Json json) {
        json.writeValue("material_file", materialFile);
        json.writeValue("shader_file", shaderFile);
        json.writeValue("variable_value_map", variableValueMap);
    }

    @Override public void read(Json json, JsonValue jsonData) {
        materialFile = json.readValue("material_file", String.class, jsonData);
        shaderFile = json.readValue("shader_file", String.class, jsonData);
        materialSource = Utils.getFile(materialFile).readString();
        if (shaderFile != null) {
            if (!Statics.engine.loadedShaders.containsKey(shaderFile)) {
                Statics.engine.loadShader(shaderFile, shaderFile);
            }
            shader = Statics.engine.loadedShaders.get(shaderFile);
        }
        variableValueMap = json.readValue("variable_value_map", HashMap.class, jsonData);
    }

}
