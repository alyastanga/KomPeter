package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.formdev.flatlaf.util.UIScale;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.components.ImageChooser;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.components.scroller.ScrollerFactory;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.inventory.Inventory;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;
import raven.modal.slider.PanelSlider.SliderCallback;

@SystemForm(name = "Add Product", description = "Add product to inventory", tags = { "Inventory" })
public class FormInventoryAddProduct extends Form {
    private static final Logger LOGGER = KompeterLogger.getLogger(FormInventoryAddProduct.class);

    private JPanel headerPanel;
    private JSplitPane bodyPane;

    private SlidePane slidePane;
    private AtomicBoolean isBusy;

    private RightPanel rightPanel;
    private JPanel leftPanel;
    private FirstStepForm firstStepForm;
    private SecondStepForm secondStepForm;
    private ThirdStepForm thirdStepForm;
    private FourthStepForm fourthStepForm;

    private StepProgressBar progressBar;

    private JPanel leftPanelButtonsContainer;
    private JButton nextConfirmButton;
    private JButton backButton;
    private SVGIconUIColor submitIcon;
    private SVGIconUIColor nextIcon;
    private SVGIconUIColor backIcon;

    private class RightPanel extends JPanel {
        public RightPanel() {
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

            final String markString = loadMarkdown();

            final Parser parser = Parser.builder().build();
            final HtmlRenderer renderer = HtmlRenderer.builder().build();
            final Node node = parser.parse(markString);
            final String htmlString = renderer.render(node);

            final JTextPane textPane = new JTextPane();

            textPane.setContentType("text/html");
            textPane.setEditable(false);
            textPane.setCaret(new DefaultCaret() {
                @Override
                public void paint(final Graphics g) {
                }
            });

            textPane.setText(String.format("""
                    <html>
                    <head>
                        <style>
                            body {
                                font-family: "Montserrat", sans-serif;
                                padding: 12px;
                            }
                        </style>
                    </head>
                    <body>
                        %s
                    </body>
                    </html>
                    """, htmlString));

            final JScrollPane scroller = new JScrollPane(textPane);

            scroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                    "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
            scroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                    "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
            scroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            scroller.getVerticalScrollBar().setUnitIncrement(16);
            scroller.getHorizontalScrollBar().setUnitIncrement(16);
            scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            setLayout(new MigLayout("insets 0", "[grow, center, fill]", "[grow, center, fill]"));

            add(scroller, "grow");
        }
    }

    private class FourthStepForm extends JPanel {
        ImageChooser productImageChooser;
        JComboBox<ItemStatus> statusBox;

        public FourthStepForm() {
            productImageChooser = new ImageChooser();
            statusBox = new JComboBox<>();

            productImageChooser.initialize();
        }
    }

    private class ThirdStepForm extends JPanel {
        JSpinner priceSpinner;
        JSpinner minQtySpinner;
        JLabel minQtyError;
        QuantityPanel qtyPanel;

        private class QuantityPanel extends JPanel {
            public QuantityPanel() {
                setBorder(BorderFactory.createTitledBorder("Quantities per location"));
            }
        }

        public ThirdStepForm() {
            priceSpinner = new JSpinner(new SpinnerNumberModel(1.00, 1.00, Double.MAX_VALUE, 1.00));
        }
    }

    private class SecondStepForm extends JPanel {
        private JComboBox<ItemBrandDto> brands;
        private CategoryPanel categoryPanel;

        public SecondStepForm() {

        }

        void populateBrands() {
            try {
                for (final ItemBrandDto brand : Inventory.getInstance().getAllItemBrandDtos()) {
                    brands.addItem(brand);
                }
            } catch (final InventoryException err) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                        "Failed to get categories", JOptionPane.ERROR_MESSAGE);
            }
        }

        class CategoryPanel extends JPanel implements ActionListener {
            private final JPanel rowsPanel;
            private final JButton addCategoryBtn;

            private final AtomicReference<String[]> allCategories;

            @Override
            public void actionPerformed(final ActionEvent e) {

            }

            void populate() {
                try {
                    allCategories.set(Inventory.getInstance().getAllItemCategories());
                } catch (final InventoryException err) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                            "Failed to get categories", JOptionPane.ERROR_MESSAGE);
                }
            }

            public CategoryPanel() {
                allCategories = new AtomicReference<>();

                setBorder(BorderFactory.createTitledBorder("Categories"));
                setLayout(new BorderLayout(0, 9));

                rowsPanel = new JPanel(new MigLayout("insets 4, gap 4, wrap 1", "[grow, fill]"));
                final JScrollPane scrollPane = new JScrollPane(rowsPanel);
                addCategoryBtn = new JButton("+ Add Category");

                add(scrollPane, BorderLayout.CENTER);
                add(addCategoryBtn, BorderLayout.SOUTH);

                addCategoryBtn.addActionListener(this);
            }

            void addCategoryRow(final String preselected) {
                final List<String> available = getAvailableCategories();

                if (available.isEmpty() && preselected == null) {
                    return;
                }

                final JComboBox<String> combo = new JComboBox<>(available.toArray(new String[0]));

                if (preselected != null) {
                    combo.setSelectedItem(preselected);
                }

                final JButton removeBtn = new JButton("Remove", new SVGIconUIColor("x.svg", 0.5f, "foreground.muted"));
                final JPanel row = new JPanel(new MigLayout("insets 0, gap 6", "[grow, fill][30!]"));

                row.add(combo);
                row.add(removeBtn);
                rowsPanel.add(row, "growx, wrap");
                rowsPanel.revalidate();

                removeBtn.addActionListener(this);
                combo.addActionListener(this);
            }

            List<String> getAvailableCategories() {
                final Set<String> chosen = getChosenCategories();
                final List<String> available = new ArrayList<String>();

                for (final String cat : allCategories.getAcquire()) {
                    if (!chosen.contains(cat)) {
                        available.add(cat);
                    }
                }

                return available;
            }

            Set<String> getChosenCategories() {
                final Set<String> chosen = new HashSet<String>();

                for (final Component comp : rowsPanel.getComponents()) {
                    if (comp instanceof final JPanel row) {
                        for (final Component c : row.getComponents()) {
                            if (c instanceof final JComboBox<?> combo) {
                                final Object val = combo.getSelectedItem();

                                if (val != null) {
                                    chosen.add(val.toString());
                                }
                            }
                        }
                    }
                }

                return chosen;
            }
        }

    }

    private void createHeader() {
        headerPanel = new JPanel(new MigLayout("insets 0, flowx"));

        final JLabel title = new JLabel("Add Product");
        final JLabel subtitle = new JLabel("Add a new product to inventory.");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h4");
        subtitle.putClientProperty(FlatClientProperties.STYLE, "foreground:$color.muted;font:11;");

        headerPanel.add(title, "wrap");
        headerPanel.add(subtitle, "wrap, gapy 4px");
    }

    private class FirstStepForm extends JPanel {
        JTextField nameTextField;
        JLabel nameError;
        JTextArea descriptionTextField;
        JLabel descriptionError;

        public boolean validateFields() {
            return false;

        }

        public void clearErrors() {
            nameTextField.putClientProperty("JComponent.outline", null);
            descriptionTextField.putClientProperty("JComponent.outline", null);
            nameError.setText("");
            descriptionError.setText("");
        }

        public FirstStepForm() {
            setLayout(new MigLayout("insets 0, wrap, flowx", "[grow, center, fill]"));

            final JLabel nameLabel = new JLabel("Product Name*");
            nameTextField = new JTextField();
            nameError = new JLabel();

            final JLabel descriptionLabel = new JLabel("Product Description");
            descriptionTextField = new JTextArea();
            descriptionError = new JLabel();

            descriptionTextField.setRows(5);
            descriptionTextField.setBorder(new FlatRoundBorder());
            descriptionTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                    "Enter product description...");

            nameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter product name...");

            nameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
            descriptionError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");

            nameError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
            descriptionError.putClientProperty(FlatClientProperties.STYLE, "font:9;");

            add(nameLabel, "wrap");
            add(nameTextField, "growx, wrap, gapy 2px");
            add(nameError, "gapy 1px, wrap");

            add(descriptionLabel, "wrap, gapy 4px");
            add(descriptionTextField, "growx, wrap, gapy 2px");
            add(descriptionError, "gapy 1px");
        }
    }

    @Override
    public void formInit() {
        init();
        formRefresh();
    }

    public FormInventoryAddProduct() {
    }

    @Override
    public void formRefresh() {
        new Thread(() -> {
        }, "Load data for FormInventoryAddProduct").start();
    }

    @Override
    public boolean formBeforeClose() {
        if (!isBusy.get()) {
            return true;
        }

        JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                "The current page is busy. Please wait until the page buttons are enabled again.", "Page Busy",
                JOptionPane.ERROR_MESSAGE);

        return false;
    }

    @Override
    public boolean formBeforeLogout() {
        return super.formBeforeLogout();
    }

    private void init() {
        isBusy = new AtomicBoolean(false);
        firstStepForm = new FirstStepForm();
        secondStepForm = new SecondStepForm();
        thirdStepForm = new ThirdStepForm();
        fourthStepForm = new FourthStepForm();
        bodyPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanelButtonsContainer = new JPanel(new MigLayout("insets 0", "[left]push[right]"));
        leftPanel = new JPanel(new MigLayout("insets 0 0 0 12", "[grow, fill, left]", "[top][top]"));
        final JPanel leftPanelWrapper = new JPanel(
                new MigLayout("insets 0", "[grow, left, fill]", "[grow, top, fill]"));
        slidePane = new SlidePane();
        rightPanel = new RightPanel();
        nextIcon = new SVGIconUIColor("move-right.svg", 0.75f, "foreground.primary");
        backIcon = new SVGIconUIColor("move-left.svg", 0.75f, "foreground.muted");
        submitIcon = new SVGIconUIColor("check.svg", 0.75f, "foreground.primary");
        nextConfirmButton = new JButton("Continue", nextIcon);
        backButton = new JButton("Back", backIcon);
        progressBar = new StepProgressBar(4);
        final JScrollPane scroller = ScrollerFactory.createScrollPane(leftPanelWrapper);

        setLayout(new MigLayout("insets 0 4 0 4, flowx, wrap", "[grow, center, fill]", "[top][grow, top, fill]"));
        createHeader();

        leftPanel.setMaximumSize(new Dimension(520, leftPanel.getMaximumSize().height));
        nextConfirmButton.setIconTextGap(16);
        nextConfirmButton.setHorizontalTextPosition(SwingConstants.LEFT);
        nextConfirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        backButton.setIconTextGap(16);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

        slidePane.addSlide(firstStepForm);

        leftPanelButtonsContainer.add(nextConfirmButton, "cell 1 0");

        leftPanel.add(progressBar, "grox, wrap");
        leftPanel.add(slidePane, "grow, wrap, gapy 10px");
        leftPanel.add(leftPanelButtonsContainer, "growx, gapy 8px");
        leftPanelWrapper.add(leftPanel, "grow");

        bodyPane.add(scroller);
        bodyPane.add(rightPanel);

        add(headerPanel, "growx");
        add(bodyPane, "gapy 16px, grow");

        bodyPane.setResizeWeight(0.4);
        bodyPane.setContinuousLayout(true);
        bodyPane.setOneTouchExpandable(true);

        backButton.addActionListener(new BackButtonActionListener());
        nextConfirmButton.addActionListener(new NextConfirmButtonActionListener());
    }

    private class BackButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            switch (progressBar.currentStep()) {
                case 2:
                    secondStep();
                    break;
                case 3:
                    thirdStep();
                    break;
                case 4:
                    fourthStep();
                    break;
            }
        }

        private void secondStep() {
        }

        private void thirdStep() {
        }

        private void fourthStep() {
        }
    }

    private class NextConfirmButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            switch (progressBar.currentStep()) {
                case 1:
                    firstStep();
                    break;
                case 2:
                    secondStep();
                    break;
                case 3:
                    thirdStep();
                    break;
                case 4:
                    fourthStep();
                    break;
            }
        }

        private void firstStep() {
            firstStepForm.clearErrors();

            if (!firstStepForm.validateFields()) {
                return;
            }

            slidePane.addSlide(secondStepForm, SlidePaneTransition.create(SlidePaneTransition.Type.ZOOM_IN),
                    new SliderCallback() {
                        @Override
                        public void complete() {

                        }
                    });
        }

        private void secondStep() {
        }

        private void thirdStep() {
        }

        private void fourthStep() {
        }
    }

    private String loadMarkdown() {
        final StringBuilder sb = new StringBuilder();

        try (InputStream st = FormInventoryAddProduct.class.getResourceAsStream("AddProductFormGuide.md")) {
            if (st == null) {
                LOGGER.severe("Cannot find AddProductFormGuide.md in resources.");

                return "";
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(st))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to read AddProductFormGuide.md", e);

            return "";
        }

        return sb.toString();
    }

    private class StepProgressBar extends JComponent {
        private final int totalSteps;
        private final AtomicInteger currentStep;

        public StepProgressBar(final int totalSteps) {
            this.totalSteps = totalSteps;
            currentStep = new AtomicInteger(1);
        }

        public void setCurrentStep(final int step) {
            currentStep.setRelease(step);

            repaint();
        }

        public int currentStep() {
            return currentStep.get();
        }

        @Override
        protected void paintComponent(final Graphics g) {
            final Graphics2D g2 = (Graphics2D) g.create();
            final int w = getWidth();
            final int h = getHeight();
            final int barY = h / 2;
            final int margin = UIScale.scale(24);
            final int stepSpacing = (w - margin * 2) / (totalSteps - 1);
            final int circleSize = UIScale.scale(12);
            final int stroke = UIScale.scale(2);
            final Color lineColor = UIManager.getColor("Component.borderColor");
            final Color progressColor = UIManager.getColor("Component.accentColor");
            final int filledX = margin + stepSpacing * (currentStep.get() - 1);
            final Font font = getFont().deriveFont(Font.BOLD, UIScale.scale(12f));

            if (progressColor == null) {
            }

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(margin, barY, w - margin, barY);

            g2.setColor(progressColor);
            g2.drawLine(margin, barY, filledX, barY);

            g2.setFont(font);

            final FontMetrics fm = g2.getFontMetrics();

            for (int i = 1; i <= totalSteps; ++i) {
                final int cx = margin + (i - 1) * stepSpacing;
                final int x = cx - circleSize / 2;
                final int y = barY - circleSize / 2;

                final boolean active = i <= currentStep();

                g2.setColor(active ? progressColor : lineColor);
                g2.fillOval(x, y, circleSize, circleSize);

                final String txt = String.valueOf(i);

                final int tx = cx - fm.stringWidth(txt) / 2;
                final int ty = y + (circleSize + fm.getAscent()) / 2 - 3;

                g2.drawString(txt, tx, ty);
            }

            g2.dispose();
        }
    }
}
