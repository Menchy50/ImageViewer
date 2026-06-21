package software.ulpgc.imageviewer.architecture;

public record Canvas(int width, int height) {
    public static Canvas ofSize(int width, int height) {
        return new Canvas(width, height);
    }

    public Canvas fit(int width, int height) {
        if (width <= this.width && height <= this.height) return new Canvas(width, height);
        return ratio(width, height) > ratio(this.width, this.height) ?
                Canvas.ofSize(this.width, height * this.width / width) :
                Canvas.ofSize(width * this.height / height, this.height);
    }

    private double ratio(int width, int height) {
        return width / (double) height;
    }
}
