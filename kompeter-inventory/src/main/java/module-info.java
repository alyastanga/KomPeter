module kompeter.inventory {
    requires transitive kompeter.database;
    requires org.jetbrains.annotations;
    requires kompeter.cryptography;

    exports com.github.ragudos.kompeter.inventory;
}
