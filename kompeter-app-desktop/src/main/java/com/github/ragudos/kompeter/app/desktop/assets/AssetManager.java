package com.github.ragudos.kompeter.app.desktop.assets;

import com.github.ragudos.kompeter.utilities.cache.LRU;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.renderer.SVGRenderingHints;
import com.github.weisj.jsvg.view.FloatSize;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;

public class AssetManager {
    private static final Logger LOGGER = KompeterLogger.getLogger(AssetManager.class);

    private static final LRU<String, Image> IMAGES = new LRU<>(50);
    private static final LRU<String, ImageIcon> icons = new LRU<>(50);

    public static SVGDocument getDocument(String path) {
        SVGLoader svgLoader = new SVGLoader();
        URL url = AssetManager.class.getResource(path);

        SVGDocument svgDocument = svgLoader.load(url, LoaderContext.builder().build());

        return svgDocument;
    }

    private static ImageIcon loadIcon(@NotNull final String path) {
        SVGDocument svgDocument = getDocument(path);
        FloatSize size = svgDocument.size();
        BufferedImage icon = new BufferedImage((int) size.width, (int) size.height, BufferedImage.TYPE_INT_ARGB);
        var g = icon.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(
                RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(
                RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(
                RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_SIZE_FIT);

        // Will use the value of RenderingHints.KEY_ANTIALIASING by default
        g.setRenderingHint(
                SVGRenderingHints.KEY_IMAGE_ANTIALIASING, SVGRenderingHints.VALUE_IMAGE_ANTIALIASING_ON);
        g.setRenderingHint(
                SVGRenderingHints.KEY_SOFT_CLIPPING, SVGRenderingHints.VALUE_SOFT_CLIPPING_ON);
        g.setRenderingHint(
                SVGRenderingHints.KEY_MASK_CLIP_RENDERING,
                SVGRenderingHints.VALUE_MASK_CLIP_RENDERING_ACCURACY);

        svgDocument.render(null, g);
        g.dispose();
        return new ImageIcon(icon);
    }

    public static ImageIcon getOrLoadIcon(@NotNull final String path) {
        var icon = icons.get(path);

        if (icon == null) {
            icon = loadIcon(path);
        }

        icons.update(path, icon);

        return icon;
    }

    public static Optional<Image> getImage(@NotNull final String path) {
        Image image = IMAGES.get(path);

        if (image != null) {
            return Optional.of(image);
        }

        try {
            Image img = AssetLoader.loadImage(path);

            IMAGES.update(path, img);

            return Optional.of(img);
        } catch (IOException | InterruptedException | IllegalArgumentException err) {
            LOGGER.log(Level.SEVERE, "Cannot load image: " + path, err);
        }

        return Optional.empty();
    }
}
