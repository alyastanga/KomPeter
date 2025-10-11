module kompeter.auth {
    requires org.jetbrains.annotations;
    requires transitive kompeter.database;
    requires kompeter.utilities;
    requires kompeter.configuration;
    requires kompeter.cryptography;

    exports com.github.ragudos.kompeter.auth;
}
