package com.pulsar.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.pulsar.Statics.currentProject;
import static com.pulsar.Statics.json;

public class AssetPackUtils {

    private static void compressFile(FileHandle file, ZipArchiveOutputStream zos, String path) {
        try {
            ZipArchiveEntry entry = new ZipArchiveEntry(path + file.name());
            zos.putArchiveEntry(entry);
            IOUtils.copy(file.read(), zos);
            zos.closeArchiveEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void compressDirectory(FileHandle dir, ZipArchiveOutputStream zos, String path) {
        for (FileHandle file : dir.list()) {
            String filePath = path + dir.name() + "/";
            if (file.isDirectory()) {
                compressDirectory(file, zos, filePath);
            } else {
                compressFile(file, zos, filePath);
            }
        }
    }

    public static void createAssetPack(FileHandle sourceFolder, FileHandle zipFile, AssetPackInfo assetPackInfo) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFile.file());
        ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos);

        FileHandle temp = Gdx.files.absolute(Files.createTempDirectory("pulsar_asset_a").toFile().getAbsolutePath());

        sourceFolder.copyTo(temp);
        FileHandle metadataFile = sourceFolder.child("info.json");
        metadataFile.writeString(json.toJson(assetPackInfo), false);
        compressFile(metadataFile, zos, "");

        for (FileHandle file : temp.list()) {
            if (file.isDirectory()) {
                compressDirectory(file, zos, "");
            } else {
                compressFile(file, zos, "");
            }
        }

        temp.file().deleteOnExit();

        zos.close();
    }

    public static AssetPackInfo expandAssetPack(FileHandle zipFile, FileHandle output) {
        try {
            FileHandle temp = Gdx.files.absolute(Files.createTempDirectory("pulsar_asset_e").toFile().getAbsolutePath());
            new Expander().expand(zipFile.file(), temp.file());
            AssetPackInfo assetPackInfo = null;
            if (temp.isDirectory()) {
                FileHandle info = temp.child("info.json");
                if (info.exists() && !info.isDirectory()) {
                    String content = info.readString();
                    assetPackInfo = json.fromJson(AssetPackInfo.class, content);
                }
            }
            if (assetPackInfo != null) {
                int idx = assetPackInfo.pkg.indexOf('.');
                String basePkg = idx == -1 ? assetPackInfo.pkg : assetPackInfo.pkg.substring(0, idx);
                temp.child(basePkg).copyTo(output);
                currentProject.dependencies.addAll(List.of(assetPackInfo.dependencies));
            }
            return assetPackInfo;
        } catch (IOException | ArchiveException e) {
            throw new RuntimeException(e);
        }
    }

}
