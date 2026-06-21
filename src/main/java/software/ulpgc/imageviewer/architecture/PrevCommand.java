package software.ulpgc.imageviewer.architecture;

public class PrevCommand implements Command {
    private final ImagePresenter imagePresenter;

    public PrevCommand(ImagePresenter imagePresenter) {
        this.imagePresenter = imagePresenter;
    }

    @Override
    public void execute() {
        imagePresenter.show(imagePresenter.image().previous());
    }


}
