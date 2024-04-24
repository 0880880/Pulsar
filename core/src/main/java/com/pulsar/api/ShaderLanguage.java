package com.pulsar.api;

import java.util.ArrayList;
import java.util.HashMap;

import regexodus.Matcher;
import regexodus.Pattern;

public class ShaderLanguage {

    public enum VariableType {
        INT,
        FLOAT,
        VECTOR2,
        VECTOR3,
        VECTOR4,
        MATRIX3,
        MATRIX4,
        SAMPLER2D
    }

    static HashMap<String, VariableType> variableTypeMap = new HashMap<>() {{
        put("int", VariableType.INT);
        put("float", VariableType.FLOAT);
        put("Vector2", VariableType.VECTOR2);
        put("Vector3", VariableType.VECTOR3);
        put("Vector4", VariableType.VECTOR4);
        put("Matrix3", VariableType.MATRIX3);
        put("Matrix4", VariableType.MATRIX4);
        put("Sampler2D", VariableType.SAMPLER2D);
    }};

    public static class Variable {

        public VariableType type;
        public String name;
        public boolean array;
        public boolean hidden;

        public Variable(String type, String name, boolean array, boolean hidden) {
            if (!ShaderLanguage.variableTypeMap.containsKey(type)) throw new RuntimeException("Invalid variable type \"" + type + "\".");
            this.type = ShaderLanguage.variableTypeMap.get(type);
            this.name = name;
            this.array = array;
            this.hidden = hidden;
        }

    }

    public static class ShaderData {

        public ArrayList<Variable> variables = new ArrayList<>();
        public String vertexSource;
        public String fragmentSource;

    }

    public static ShaderData getShaderData(String source) {

        ShaderData shaderData = new ShaderData();

        ArrayList<Section> sections = parseSections(source);

        Pattern pattern = Pattern.compile("((hidden)(?<=hidden)\\s+)?(int|float|Vector2|Vector3|Vector4|Matrix3|Matrix4|Sampler2D)\\s*(\\[\\])?\\s+([a-zA-Z][a-zA-Z0-9_]*)");
        Pattern newline = Pattern.compile("\n");

        for (Section section : sections) {
            if (section.name.equals("VERTEX")) {
                shaderData.vertexSource = section.content;
            } else if (section.name.equals("FRAGMENT")) {
                shaderData.fragmentSource = section.content;
            } else if (section.name.equals("VARIABLES")) {
                String[] lines = newline.split(section.content);
                for (String l : lines) {
                    String line = l.trim();
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        shaderData.variables.add(new Variable(
                            matcher.group(3),
                            matcher.group(5),
                            "[]".equals(matcher.group(4)),
                            matcher.group(2) != null
                        ));
                    }
                }
            }

        }

        return shaderData;

    }

    static class Section {
        public final String name;
        public final String content;

        public Section(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }

    static ArrayList<Section> parseSections(String input) {
        ArrayList<Section> sections = new ArrayList<>();
        Pattern pattern = Pattern.compile("([a-zA-Z_]+)\\s*\\{\\s*((?:[^{}]+|\\{(?:[^{}]+|\\{[^{}]*\\})*\\})*)\\s*\\}");

        Matcher matcher = pattern.matcher(input);

        boolean hasVertex = false;
        boolean hasFragment = false;

        while (matcher.find()) {
            String name = matcher.group(1).trim();
            String content = matcher.group(2).trim();
            if (!(name.equals("VARIABLES") || name.equals("VERTEX") || name.equals("FRAGMENT")))
                throw new RuntimeException("Invalid section name: \"" + name + "\"");
            if (name.equals("VERTEX"))
                if (!hasVertex)
                    hasVertex = true;
                else
                    throw new RuntimeException("Duplicate VERTEX section.");
            if (name.equals("FRAGMENT"))
                if (!hasFragment)
                    hasFragment = true;
                else
                    throw new RuntimeException("Duplicate FRAGMENT section.");
            sections.add(new Section(name, content));
        }

        if (!hasVertex || !hasFragment) {
            if (!hasVertex && !hasFragment)
                throw new RuntimeException("Missing required VERTEX & FRAGMENT sections.");
            else if (!hasVertex)
                throw new RuntimeException("Missing required VERTEX section.");
            else throw new RuntimeException("Missing required FRAGMENT section.");
        }

        return sections;
    }

}
