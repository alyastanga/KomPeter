module kompeter.app.desktop {
    requires java.desktop;
    requires kompeter.auth;
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
    requires com.formdev.flatlaf.extras;

    exports com.github.ragudos.kompeter.app.desktop;
}
