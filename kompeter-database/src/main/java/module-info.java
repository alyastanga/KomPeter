module kompeter.database {
    requires transitive java.sql;
    requires kompeter.utilities;
    requires org.xerial.sqlitejdbc;
    requires io.github.classgraph;
    requires static org.jetbrains.annotations;

    exports com.github.ragudos.kompeter.database;
    exports com.github.ragudos.kompeter.database.dao;
    exports com.github.ragudos.kompeter.database.dto;
    exports com.github.ragudos.kompeter.database.dto.enums;
    exports com.github.ragudos.kompeter.database.migrations;
    exports com.github.ragudos.kompeter.database.seeder;
    exports com.github.ragudos.kompeter.database.sqlite;
    exports com.github.ragudos.kompeter.database.sqlite.dao;
    exports com.github.ragudos.kompeter.database.sqlite.migrations;
}
