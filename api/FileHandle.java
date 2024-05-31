package com.pulsar.api;


public class FileHandle
{


 private String path;
 private String type;
 private String[] types;

 private String fileType = null;

 public FileHandle(String type) {}

 public String readString() {return null;}

 
public byte[] readBytes() {return null;}

 
public void writeString(String string, boolean append) {}

 public void writeBytes(byte[] bytes, boolean append) {}

 public FileHandle setAbsolute(String path) {return null;}

 
public FileHandle setInternal(String path) {return null;}

 
public FileHandle setExternal(String path) {return null;}

 
public FileHandle setLocal(String path) {return null;}

 
public FileHandle child(String path) {return null;}

 
public FileHandle parent() {return null;}

 
public String path() {return null;}

 
public String pathWithoutExtension() {return null;}

 
public String name() {return null;}

 
public String nameWithoutExtension() {return null;}

 
public String extension() {return null;}

 
public boolean exists() {return false;}

 
public boolean isDirectory() {return false;}

 
public boolean delete() {return false;}

 
public boolean deleteDirectory() {return false;}

 
public void emptyDirectory() {}

 public void copyTo(FileHandle dest) {}

 public void moveTo(FileHandle dest) {}

 public long lastModified() {return 0L;}

 
public long length() {return 0L;}

 
public void mkdirs() {}

 public FileHandle[] list() {return null;}

 
public FileHandle[] list(String suffix) {return null;}

 
public boolean equals (Object obj) {return false;}

 
public int hashCode () {return 0;}

 
public String toString () {return null;}

 
public String fileType() {return null;}

 
public void setType(String type) {}

 public String[] getTypes() {return null;}

 
public String getType() {return null;}

 
@Override

 @Override
}
