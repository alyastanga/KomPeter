module kompeter.monitoring {
    requires transitive kompeter.database;
    requires kompeter.utilities;
    requires java.logging;

    exports com.github.ragudos.kompeter.monitoring.service;
}
