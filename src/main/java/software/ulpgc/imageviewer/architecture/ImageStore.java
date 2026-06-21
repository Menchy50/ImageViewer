package software.ulpgc.imageviewer.architecture;

import java.util.stream.Stream;

public interface ImageStore {
    Stream<String> images();
}
