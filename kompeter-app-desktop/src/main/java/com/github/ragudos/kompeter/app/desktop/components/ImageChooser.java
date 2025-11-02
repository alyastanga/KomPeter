package com.github.ragudos.kompeter.app.desktop.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

public class ImageChooser implements DropTargetListener {
    private JLabel imageLabel;
    private JFileChooser imageChooser;
    private ProductImageChooserMouseListener productImageChooserMouseListener;
    private ImagePanel chosenImage;
    private File chosenImageFile;

    public final static String[] ALLOWED_EXTENSIONS = { ".png", ".jpg", ".jpeg" };

    public File chosenImageFile() {
        return chosenImageFile;
    }

    public JLabel imageLabel() {
        return imageLabel;
    }

    public ImagePanel chosenImage() {
        return chosenImage;
    }

    private class ProductImageChooserMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int result = imageChooser.showOpenDialog(KompeterDesktopApp.getRootFrame());

            if (result == JFileChooser.APPROVE_OPTION) {
                File sFile = imageChooser.getSelectedFile();

                try {
                    chosenImageFile = sFile;
                    chosenImage.setImage(ImageIO.read(sFile));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                System.out.println("Selected PNG: " + sFile.getAbsolutePath());
            }
        }
    }

    public ImageChooser() {
        productImageChooserMouseListener = new ProductImageChooserMouseListener();
        chosenImage = new ImagePanel(null);
        FileNameExtensionFilter imgFilter = new FileNameExtensionFilter(
                "PNG Images", "png", "jpg", "JPG Images", "jpeg", "JPEG Images");
        imageLabel = new JLabel(HtmlUtils.wrapInHtml("<p align='center'>Drag & Drop product image"));
        imageChooser = new JFileChooser(String.format("%s%sPictures", SystemInfo.USER_HOME, File.separator));

        chosenImage.setMinimumSize(new Dimension(100, 100));
        chosenImage.setScaleMode(ImagePanel.ScaleMode.CONTAIN);
        imageLabel.setPreferredSize(new Dimension(200, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        float[] dash = { 5f, 5f };
        Border dashedBorder = BorderFactory.createStrokeBorder(
                new BasicStroke(
                        2f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10f,
                        dash,
                        0f),
                UIManager.getColor("foreground.background"));

        imageLabel.setBorder(BorderFactory.createCompoundBorder(
                dashedBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.putClientProperty(FlatClientProperties.STYLE, "font: 11;");

        imageChooser.setFileFilter(imgFilter);
        imageChooser.setAcceptAllFileFilterUsed(false);

        imageLabel.addMouseListener(productImageChooserMouseListener);

        new DropTarget(imageLabel, this);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
                dtde.rejectDrop();

                return;
            }

            dtde.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable transferable = dtde.getTransferable();

            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                @SuppressWarnings("unchecked")
                List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                if (droppedFiles.isEmpty()) {
                    dtde.dropComplete(false);
                    return;
                }

                File file = droppedFiles.get(0);

                if (Arrays.stream(ALLOWED_EXTENSIONS).anyMatch((s) -> file.getName().toLowerCase().endsWith(s))) {
                    chosenImageFile = file;
                    chosenImage.setImage(ImageIO.read(file));
                } else {
                    JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                            "Unsupported images. Only png and jpegs are allowed", "Invalid Extensions",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

    }
}
