package software.ulpgc.imageviewer.architecture;

import software.ulpgc.imageviewer.architecture.ImageDisplay.Paint;

public class ImagePresenter {
    private final ImageDisplay display;
    private Image image;

    public ImagePresenter(ImageDisplay display) {
        this.display = display;
        this.display.on((ImageDisplay.Shift) offset -> {
            display.paint(
                    new Paint(image.bitmap(), offset),
                    new Paint(
                            offset < 0 ? image.next().bitmap() : image.previous().bitmap(),
                            offset < 0 ? display.width() + offset : offset - display.width())
            );
        });
        this.display.on((ImageDisplay.Released) offset -> {
            if (Math.abs(offset) * 2 > display.width()) image = offset < 0 ? image.next() : image.previous();
            System.out.println(image.id());
            display.paint(new Paint(image.bitmap(), 0));
        });
    }

    private void released(int offset) {

    }

    public void show(Image image) {
        this.image = image;
        this.display.paint(new Paint(image.bitmap(), 0));
    }

    public Image image() {
        return image;
    }

}
