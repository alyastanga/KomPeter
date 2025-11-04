/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.components.border.KompeterBorderFactory;
import com.github.ragudos.kompeter.configurations.ApplicationConfig;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import com.github.ragudos.kompeter.utilities.platform.SystemInfo;

public class ImageChooser extends JFileChooser {
    private static final Logger LOGGER = KompeterLogger.getLogger(ImageChooser.class);

    private ImagePanel chosenImage;
    private File chosenImageFile;
    private DropTarget dropTarget;
    private FileNameExtensionFilter fileNameExtensionFilter;
    private ImageChooserDtdeListener imageChooserDtdeListener;
    private ImageChooserMouseListener imageChooserMouseListener;
    private JLabel imageLabel;

    private AtomicBoolean initialized;

    private Runnable listener;

    public ImageChooser(final Runnable listener) {
        this.listener = listener;
        final String imageDirectory = ApplicationConfig.getInstance().getConfig().getProperty("images.directory",
                String.format("%s%sPictures", SystemInfo.USER_HOME, File.separator));
        final File imageDirectoryFile = new File(imageDirectory);

        if (!imageDirectoryFile.exists()) {
            JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                    String.format("Directory %s does not exist. We cannot open a file chooser to choose your image."
                            + " Please modify it in settings.", imageDirectory),
                    "Cannot Open Directory", JOptionPane.ERROR_MESSAGE);

            return;
        }

        setCurrentDirectory(imageDirectoryFile);

        imageChooserMouseListener = new ImageChooserMouseListener();
        imageChooserDtdeListener = new ImageChooserDtdeListener();
        chosenImage = new ImagePanel(null);
        imageLabel = new JLabel(HtmlUtils.wrapInHtml("<p align='center'>Drag & Drop product image"));

        chosenImage.setScaleMode(ImagePanel.ScaleMode.COVER);

        imageLabel.setPreferredSize(new Dimension(200, 100));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel
                .setBorder(BorderFactory.createCompoundBorder(
                        KompeterBorderFactory.createDashedBorder(10f, 2f, new float[]{5f, 5f}, 0f,
                                UIManager.getColor("foreground.background")),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.putClientProperty(FlatClientProperties.STYLE, "font: 11;");
        initialized = new AtomicBoolean(false);

        dropTarget = new DropTarget(imageLabel, null);
    }

    public ImagePanel chosenImage() {
        return chosenImage;
    }

    public File chosenImageFile() {
        return chosenImageFile;
    }

    public void clear() {
        chosenImage.setImage(null);
        chosenImageFile = null;
        listener.run();
    }

    public void destroy() {
        dropTarget.removeDropTargetListener(imageChooserDtdeListener);
        imageLabel.removeMouseListener(imageChooserMouseListener);
        initialized.set(false);
    }

    public JLabel imageLabel() {
        return imageLabel;
    }

    public void initialize() {
        imageLabel.addMouseListener(imageChooserMouseListener);

        try {
            dropTarget.addDropTargetListener(imageChooserDtdeListener);
        } catch (final TooManyListenersException err) {
            LOGGER.log(Level.SEVERE, err.getMessage(), err);
        }

        initialized.set(true);
    }

    public boolean initialized() {
        return initialized.get();
    }

    public void setAllowedImageExtensions(final FileNameExtensionFilter fileNameExtensionFilter) {
        this.fileNameExtensionFilter = fileNameExtensionFilter;

        setFileFilter(fileNameExtensionFilter);
        setAcceptAllFileFilterUsed(false);
    }

    private class ImageChooserDtdeListener extends DropTargetAdapter {
        @Override
        public void drop(final DropTargetDropEvent dtde) {
            try {
                if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
                    dtde.rejectDrop();

                    return;
                }

                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                final Transferable transferable = dtde.getTransferable();

                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    @SuppressWarnings("unchecked")
                    final List<File> droppedFiles = (List<File>) transferable
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    if (droppedFiles.isEmpty()) {
                        dtde.dropComplete(false);
                        return;
                    }

                    final File file = droppedFiles.get(0);

                    fileNameExtensionFilter.getExtensions();

                    if (fileNameExtensionFilter != null) {
                        if (Arrays.stream(fileNameExtensionFilter.getExtensions())
                                .anyMatch((s) -> file.getName().toLowerCase().endsWith(s))) {
                            chosenImageFile = file;
                            chosenImage.setImage(ImageIO.read(file));
                            listener.run();
                        } else {
                            JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                                    "Unsupported images. Only png and jpegs are allowed", "Invalid Extensions",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        chosenImageFile = file;
                        chosenImage.setImage(ImageIO.read(file));
                    }
                }
            } catch (final Exception err) {
                err.printStackTrace();
            }
        }
    }

    private class ImageChooserMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            final int result = showOpenDialog(KompeterDesktopApp.getRootFrame());

            if (result == JFileChooser.APPROVE_OPTION) {
                final File sFile = getSelectedFile();

                try {
                    chosenImageFile = sFile;
                    chosenImage.setImage(ImageIO.read(sFile));
                    listener.run();
                } catch (final IOException e1) {
                    JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(), e1.getMessage(),
                            e1.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                    LOGGER.log(Level.SEVERE, "Failed to set Image", e1);
                }

                System.out.println("Selected PNG: " + sFile.getAbsolutePath());
            }
        }
    }
}
