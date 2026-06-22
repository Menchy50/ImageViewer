package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Image;
import software.ulpgc.imageviewer.architecture.ImageDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private Image image;
    private BufferedImage bitmap;
    private double zoomFactor = 1.0;
    private double prevZoomFactor = 1.0;
    private Point dragStartScreen;
    private final AffineTransform coordTransform = new AffineTransform();
    private Shift shiftListener = Shift.Null;

    public interface Shift {
        Shift Null = offset -> {};
        void offset(int offset);
    }

    public SwingImageDisplay() {
        this.setBackground(Color.BLACK);
        initInteraction();
    }

    public void on(Shift shiftListener) {
        this.shiftListener = shiftListener;
    }

    private void initInteraction() {
        addMouseWheelListener(e -> {
            if (bitmap == null) return;
            if (e.getWheelRotation() < 0) {
                zoomFactor *= 1.1;
            } else {
                zoomFactor /= 1.1;
            }
            if (zoomFactor < 1.0) zoomFactor = 1.0;
            if (zoomFactor > 10.0) zoomFactor = 10.0;

            if (zoomFactor == 1.0) coordTransform.setToIdentity();

            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartScreen = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (zoomFactor <= 1.05) {
                    handleSwipe();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                pan(e);
            }
        });
    }

    private void handleSwipe() {
        double translateX = coordTransform.getTranslateX();
        int threshold = getWidth() / 3;

        if (translateX > threshold) {
            shiftListener.offset(-1);
        } else if (translateX < -threshold) {
            shiftListener.offset(1);
        } else {
            coordTransform.setToIdentity();
            repaint();
        }
    }

    private void pan(MouseEvent e) {
        if (bitmap == null) return;
        Point dragEndScreen = e.getPoint();
        Point2D.Double dragStart = transformPoint(dragStartScreen);
        Point2D.Double dragEnd = transformPoint(dragEndScreen);
        double dx = dragEnd.getX() - dragStart.getX();
        double dy = dragEnd.getY() - dragStart.getY();
        coordTransform.translate(dx, dy);
        dragStartScreen = dragEndScreen;
        repaint();
    }

    @Override
    public Image image() {
        return image;
    }

    @Override
    public void show(Image image) {
        this.image = image;
        this.bitmap = readBitmap();
        this.zoomFactor = 1.0;
        this.prevZoomFactor = 1.0;
        this.coordTransform.setToIdentity();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bitmap == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        double widthRatio = (double) getWidth() / bitmap.getWidth();
        double heightRatio = (double) getHeight() / bitmap.getHeight();
        double baseScale = Math.min(widthRatio, heightRatio);

        int centerX = (getWidth() - (int) (bitmap.getWidth() * baseScale)) / 2;
        int centerY = (getHeight() - (int) (bitmap.getHeight() * baseScale)) / 2;

        AffineTransform at = new AffineTransform();
        at.translate(centerX, centerY);
        at.scale(baseScale, baseScale);

        if (zoomFactor != prevZoomFactor) {
            double relScale = zoomFactor / prevZoomFactor;
            Point2D p = transformPoint(new Point(getWidth()/2, getHeight()/2));
            coordTransform.translate(p.getX(), p.getY());
            coordTransform.scale(relScale, relScale);
            coordTransform.translate(-p.getX(), -p.getY());
            prevZoomFactor = zoomFactor;
        }

        at.concatenate(coordTransform);
        g2.drawImage(bitmap, at, null);
    }

    private Point2D.Double transformPoint(Point p1) {
        try {
            AffineTransform inverse = coordTransform.createInverse();
            Point2D.Double p2 = new Point2D.Double();
            inverse.transform(new Point2D.Double(p1.x, p1.y), p2);
            return p2;
        } catch (Exception e) {
            return new Point2D.Double(0,0);
        }
    }

    private BufferedImage readBitmap() {
        try {
            return ImageIO.read(new ByteArrayInputStream(image.bitmap()));
        } catch (IOException e) {
            return null;
        }
    }
}
