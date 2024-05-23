package com.pulsar.utils;

import java.util.ArrayList;
import java.util.List;

public class AssetPackInfo {

    public String name;
    public String pkg;
    public String version;
    public String[] dependencies;

    public AssetPackInfo() { }

    public AssetPackInfo(String name, String pkg, String version, String... dependencies) {
        this.name = name;
        this.pkg = pkg;
        this.version = version;
        this.dependencies = dependencies;
    }

}
