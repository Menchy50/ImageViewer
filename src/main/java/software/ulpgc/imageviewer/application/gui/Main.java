package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.application.FileImageStore;
import software.ulpgc.imageviewer.architecture.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    private static File root;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        root = new File("images");
        ImageStore store = new FileImageStore(root);
        ImageProvider provider = ImageProvider.with(store.images());
        SwingImageDisplay display = new SwingImageDisplay();
        Image image = provider.first(Main::readImage);
        if (image != null) display.show(image);

        Desktop.create(display)
                .put("next", new NextCommand(display))
                .put("prev", new PrevCommand(display))
                .setVisible(true);
    }

    private static byte[] readImage(String id) {
        try {
            return Files.readAllBytes(new File(root, id).toPath());
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
