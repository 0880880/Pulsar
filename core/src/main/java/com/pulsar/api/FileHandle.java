package com.pulsar.api;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.pulsar.Statics;

public class FileHandle
    implements Json.Serializable
{

    public com.badlogic.gdx.files.FileHandle gdxFile; // TODO Change public to private

    private String path;
    private String type;
    private String[] types;

    private String fileType = null;

    public FileHandle(String type) {
        setType(type);
    }

    public String readString() {
        if (gdxFile == null) return null;
        return gdxFile.readString();
    }

    public byte[] readBytes() {
        if (gdxFile == null) return null;
        return gdxFile.readBytes();
    }

    public void writeString(String string, boolean append) {
        if (gdxFile == null) return;
        if (fileType.equals("Internal")) return;
        gdxFile.writeString(string, append);
    }

    public void writeBytes(byte[] bytes, boolean append) {
        if (gdxFile == null) return;
        if (fileType.equals("Internal")) return;
        gdxFile.writeBytes(bytes, append);
    }

    public FileHandle setAbsolute(String path) {
        fileType = "Absolute";
        this.path = path;
        gdxFile = Gdx.files.absolute(path);
        return this;
    }

    public FileHandle setInternal(String path) {
        fileType = "Internal";
        this.path = path;
        gdxFile = Statics.currentProjectPath.child("Assets").child(path);
        return this;
    }

    public FileHandle setExternal(String path) {
        fileType = "External";
        this.path = path;
        gdxFile = Gdx.files.external(path);
        return this;
    }

    public FileHandle setLocal(String path) {
        fileType = "Local";
        this.path = path;
        gdxFile = Gdx.files.local(path);
        return this;
    }

    public FileHandle child(String path) {
        if (gdxFile == null) return this;
        gdxFile = gdxFile.child(path);
        this.path = gdxFile.path();
        return this;
    }

    public FileHandle parent() {
        if (gdxFile == null) return this;
        gdxFile = gdxFile.parent();
        this.path = gdxFile.path();
        return this;
    }

    public String path() {
        if (gdxFile == null) return null;
        return gdxFile.path();
    }

    public String pathWithoutExtension() {
        if (gdxFile == null) return null;
        return gdxFile.pathWithoutExtension();
    }

    public String name() {
        if (gdxFile == null) return null;
        return gdxFile.name();
    }

    public String nameWithoutExtension() {
        if (gdxFile == null) return null;
        return gdxFile.nameWithoutExtension();
    }

    public String extension() {
        if (gdxFile == null) return null;
        return gdxFile.extension();
    }

    public boolean exists() {
        if (gdxFile == null) return false;
        return gdxFile.exists();
    }

    public boolean isDirectory() {
        if (gdxFile == null) return false;
        return gdxFile.isDirectory();
    }

    public boolean delete() {
        if (gdxFile == null) return false;
        return gdxFile.delete();
    }

    public boolean deleteDirectory() {
        if (gdxFile == null) return false;
        return gdxFile.deleteDirectory();
    }

    public void emptyDirectory() {
        if (gdxFile == null) return;
        gdxFile.emptyDirectory();
    }

    public void copyTo(FileHandle dest) {
        if (gdxFile == null) return;
        gdxFile.copyTo(dest.gdxFile);
    }

    public void moveTo(FileHandle dest) {
        if (gdxFile == null) return;
        gdxFile.moveTo(dest.gdxFile);
    }

    public long lastModified() {
        if (gdxFile == null) return 0;
        return gdxFile.lastModified();
    }

    public long length() {
        if (gdxFile == null) return 0;
        return gdxFile.length();
    }

    public void mkdirs() {
        if (gdxFile == null) return;
        gdxFile.mkdirs();
    }

    public FileHandle[] list() {
        if (gdxFile == null) return null;
        com.badlogic.gdx.files.FileHandle[] gdxFiles = gdxFile.list();
        FileHandle[] files = new FileHandle[gdxFiles.length];
        for (int i = 0; i < gdxFiles.length; i++) {
            FileHandle f = new FileHandle(gdxFiles[i].extension());
            if (gdxFiles[i].type() == Files.FileType.Absolute) files[i] = f.setAbsolute(gdxFiles[i].path());
            else if (gdxFiles[i].type() == Files.FileType.Internal) files[i] = f.setInternal(gdxFiles[i].path());
            else if (gdxFiles[i].type() == Files.FileType.External) files[i] = f.setExternal(gdxFiles[i].path());
            else if (gdxFiles[i].type() == Files.FileType.Local) files[i] = f.setLocal(gdxFiles[i].path());
        }
        return files;
    }

    public FileHandle[] list(String suffix) {
        if (gdxFile == null) return null;
        com.badlogic.gdx.files.FileHandle[] gdxFiles = gdxFile.list(suffix);
        FileHandle[] files = new FileHandle[gdxFiles.length];
        for (int i = 0; i < gdxFiles.length; i++) {
            FileHandle f = new FileHandle(gdxFiles[i].extension());
            if (gdxFiles[i].type() == Files.FileType.Absolute) files[i] = f.setAbsolute(gdxFiles[i].path());
            else if (gdxFiles[i].type() == Files.FileType.Internal) files[i] = f.setInternal(gdxFiles[i].path());
            else if (gdxFiles[i].type() == Files.FileType.External) files[i] = f.setExternal(gdxFiles[i].path());
            else if (gdxFiles[i].type() == Files.FileType.Local) files[i] = f.setLocal(gdxFiles[i].path());
        }
        return files;
    }

    public boolean equals (Object obj) {
        if (!(obj instanceof FileHandle)) return false;
        FileHandle other = (FileHandle)obj;
        return fileType.equals(other.fileType) && path().equals(other.path());
    }

    public int hashCode () {
        int hash = 1;
        hash = hash * 37 + type.hashCode();
        hash = hash * 67 + path().hashCode();
        return hash;
    }

    public String toString () {
        return path().replace('\\', '/');
    }

    public String fileType() {
        if (gdxFile == null) return null;
        return fileType;
    }

    public void setType(String type) {
        this.type = type;
        this.types = type.split(",");
    }

    public String[] getTypes() {
        return types;
    }

    public String getType() {
        return type;
    }

    @Override
    public void write(Json json) {
        json.writeValue("path", path);
        json.writeValue("type", type);
        json.writeValue("fileType", fileType);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        path = jsonData.getString("path");
        type = jsonData.getString("type");
        types = type.split(",");
        fileType = jsonData.getString("fileType");
        if (path != null) {
            switch (fileType) {
                case "Internal":
                    gdxFile = Gdx.files.internal(path);
                    break;
                case "External":
                    gdxFile = Gdx.files.external(path);
                    break;
                case "Local":
                    gdxFile = Gdx.files.local(path);
                    break;
                default:
                    gdxFile = Gdx.files.absolute(path);
                    break;
            }
        }
    }
}
