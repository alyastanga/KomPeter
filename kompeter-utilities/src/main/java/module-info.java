module kompeter.utilities {
    requires transitive java.logging;
    requires java.desktop;
    requires static org.jetbrains.annotations;

    exports com.github.ragudos.kompeter.utilities;
    exports com.github.ragudos.kompeter.utilities.logger;
    exports com.github.ragudos.kompeter.utilities.validator;
    exports com.github.ragudos.kompeter.utilities.io;
    exports com.github.ragudos.kompeter.utilities.constants;
    exports com.github.ragudos.kompeter.utilities.cache;
    exports com.github.ragudos.kompeter.utilities.platform;
    exports com.github.ragudos.kompeter.utilities.observer;
}
