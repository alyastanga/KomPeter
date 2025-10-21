module kompeter.pointofsale {
    requires transitive kompeter.database;
    requires transitive kompeter.utilities;
    requires org.jetbrains.annotations;
    requires kompeter.cryptography;

    exports com.github.ragudos.kompeter.pointofsale;
}
