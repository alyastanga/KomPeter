module kompeter.app.desktop {
    requires transitive java.desktop;
    requires java.management;
    requires java.base;
    requires kompeter.auth;
    requires kompeter.configuration;
    requires kompeter.utilities;
    requires kompeter.cryptography;
    requires kompeter.inventory;
    requires kompeter.monitoring;
    requires kompeter.pointofsale;
    requires com.formdev.flatlaf.extras;
    requires com.miglayout.swing;
    requires com.formdev.flatlaf;
    requires com.github.lgooddatepicker;
    requires io.github.classgraph;
    requires org.jfree.jfreechart;
    requires org.apache.pdfbox;
    requires org.apache.commons.text;
    requires static org.jetbrains.annotations;
    requires modal.dialog;

    exports com.github.ragudos.kompeter.app.desktop;
}
