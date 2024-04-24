package com.pulsar.utils;

import java.io.IOException;
import java.nio.file.*;

public class DirectoryWatcher {

    public WatchService getService(String folder) {
        WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path path = Paths.get(folder);

        try {
            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return watchService;
    }

}
