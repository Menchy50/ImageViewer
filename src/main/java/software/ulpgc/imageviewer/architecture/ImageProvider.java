package software.ulpgc.imageviewer.architecture;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ImageProvider {
    private final List<String> images;

    private ImageProvider(Stream<String> images) {
        this.images = images.toList();
    }

    public static ImageProvider with(Stream<String> images) {
        return new ImageProvider(images);
    }

    public Image first(Function<String, byte[]> loader) {
        return load(0, loader);
    }

    private Image load(int index, Function<String, byte[]> loader) {
        return new Image() {
            private int size = images.size();

            @Override
            public String id() {
                return images.get(index);
            }

            @Override
            public byte[] bitmap() {
                return loader.apply(id());
            }

            @Override
            public Image next() {
                return index == size - 1 ? load(0, loader) : load(index + 1, loader);
            }

            @Override
            public Image previous() {
                return index == 0 ? load(size - 1, loader) : load(index - 1, loader);
            }
        };
    }
}
