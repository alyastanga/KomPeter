module kompeter.app.desktop {
    requires java.desktop;
    requires kompeter.configuration;
    requires kompeter.utilities;
    requires kompeter.cryptography;
    requires kompeter.inventory;
    requires kompeter.monitoring;
    requires kompeter.pointofsale;
    requires static org.jetbrains.annotations;
    requires com.miglayout.swing;
    requires io.github.classgraph;
    requires com.formdev.flatlaf;

    exports com.github.ragudos.kompeter.app.desktop;
    exports com.github.ragudos.kompeter.app.desktop.scenes.inventory;
}
