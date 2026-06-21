package software.ulpgc.imageviewer.architecture;

public interface Image {
    String id();
    byte[] bitmap();
    Image next();
    Image previous();
}
