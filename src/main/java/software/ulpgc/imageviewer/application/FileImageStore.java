package software.ulpgc.imageviewer.application;
 
import software.ulpgc.imageviewer.architecture.ImageStore;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileImageStore implements ImageStore {
    private final File folder;

    public FileImageStore(File folder) {
        this.folder = folder;
    }

    @Override
    public Stream<String> images() {
        return imagesIn(folder.list());
    }

    private Stream<String> imagesIn(String[] id) {
        return Arrays.stream(id);
    }
}
