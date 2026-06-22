package software.ulpgc.imageviewer.application;

import software.ulpgc.imageviewer.architecture.ImageStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class FileImageStore implements ImageStore {
    private final File folder;

    public FileImageStore(File folder) {
        this.folder = folder;
    }

    @Override
    public Stream<String> images() {
        if (!folder.exists() || !folder.isDirectory()) return Stream.empty();
        File[] files = folder.listFiles(imageFilter());
        return files == null ? Stream.empty() : Arrays.stream(files).map(File::getName);
    }

    private FilenameFilter imageFilter() {
        Set<String> extensions = Set.of(".jpg", ".jpeg", ".png", ".bmp", ".gif");
        return (dir, name) -> extensions.stream().anyMatch(name.toLowerCase()::endsWith);
    }
}
