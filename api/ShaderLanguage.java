package com.pulsar.api;

import java.util.HashMap;


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

 static HashMap<String, VariableType> variableTypeMap = new HashMap<>() {};

 public static class Variable {}

 public static class ShaderData {}

 public static ShaderData getShaderData(String source) {return null;}

 
static class Section {}


}
