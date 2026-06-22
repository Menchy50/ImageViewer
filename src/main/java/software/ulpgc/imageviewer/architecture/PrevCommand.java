package software.ulpgc.imageviewer.architecture;

public class PrevCommand implements Command {
    private final ImageDisplay imageDisplay;

    public PrevCommand(ImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    @Override
    public void execute() {
        imageDisplay.show(imageDisplay.image().previous());
    }


}
