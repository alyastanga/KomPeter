module kompeter.inventory {
    requires transitive kompeter.database;
    requires org.jetbrains.annotations;
    requires kompeter.cryptography;
    requires org.apache.commons.text;
    requires lombok;

    exports com.github.ragudos.kompeter.inventory;
}
