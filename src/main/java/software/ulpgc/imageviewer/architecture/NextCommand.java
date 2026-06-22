package software.ulpgc.imageviewer.architecture;

public class NextCommand implements Command {
    private final ImageDisplay imageDisplay;

    public NextCommand(ImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    @Override
    public void execute() {
        imageDisplay.show(imageDisplay.image().next());
    }


}
