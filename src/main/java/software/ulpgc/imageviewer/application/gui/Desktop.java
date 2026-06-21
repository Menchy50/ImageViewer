package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Command;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.BorderLayout.SOUTH;
import static java.awt.FlowLayout.CENTER;

public class Desktop extends JFrame {
    private final Map<String, Command> commands;

    public static Desktop create(SwingImageDisplay imageDisplay) throws IOException {
        return new Desktop(imageDisplay);
    }

    private Desktop(SwingImageDisplay imageDisplay) throws HeadlessException {
        this.commands = new HashMap<>();
        this.setTitle("Image Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());
        this.createToolbar();
        this.setLocationRelativeTo(null);
        this.getContentPane().add(imageDisplay);

    }

    private void createToolbar() {
        this.getContentPane().add(toolbar(), SOUTH);
    }

    private JPanel toolbar() {
        JPanel panel = new JPanel(new FlowLayout(CENTER));
        panel.add(button("prev"));
        panel.add(button("next"));
        return panel;
    }

    private JButton button(String name) {
        JButton button = new JButton(name);
        button.addActionListener(e -> commands.get(name).execute());
        return button;
    }

    public Desktop put(String name, Command command) {
        commands.put(name, command);
        return this;
    }
}
