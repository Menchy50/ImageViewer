package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class Desktop extends JFrame {
    private final Map<String, Command> commands;
    private final SwingImageDisplay imageDisplay;
    private final JLayeredPane layeredPane;
    private JButton prevBtn;
    private JButton nextBtn;

    public static Desktop create(SwingImageDisplay imageDisplay) {
        return new Desktop(imageDisplay);
    }

    private Desktop(SwingImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;
        this.commands = new HashMap<>();
        this.setTitle("Image Viewer Pro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 800);
        this.setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);

        imageDisplay.setBounds(0, 0, getWidth(), getHeight());

        imageDisplay.on(offset -> {
            if (offset > 0) commands.get("next").execute();
            if (offset < 0) commands.get("prev").execute();
        });

        layeredPane.add(imageDisplay, JLayeredPane.DEFAULT_LAYER);

        createOverlayControls();
        setupKeyBindings();
        setupResizeListener();
    }

    private void createOverlayControls() {
        prevBtn = createOverlayButton("❮", "prev");
        nextBtn = createOverlayButton("❯", "next");

        layeredPane.add(prevBtn, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(nextBtn, JLayeredPane.PALETTE_LAYER);

        updateButtonPositions();
    }

    private JButton createOverlayButton(String text, String commandKey) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 40));
        btn.setForeground(new Color(255, 255, 255, 150));
        btn.setBackground(new Color(0, 0, 0, 50));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setSize(80, 100);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setForeground(new Color(255, 255, 255, 150));
            }
        });

        btn.addActionListener(e -> {
            commands.get(commandKey).execute();
            this.requestFocus();
        });
        return btn;
    }

    private void setupResizeListener() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                imageDisplay.setBounds(0, 0, getWidth(), getHeight());
                updateButtonPositions();
            }
        });
    }

    private void updateButtonPositions() {
        int centerY = getHeight() / 2 - prevBtn.getHeight() / 2;
        prevBtn.setLocation(20, centerY);
        nextBtn.setLocation(getWidth() - nextBtn.getWidth() - 20, centerY);
    }

    private void setupKeyBindings() {
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
                    commands.get("prev").execute();
                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
                    commands.get("next").execute();
            }
        });
    }

    public Desktop put(String name, Command command) {
        commands.put(name, command);
        return this;
    }
}
