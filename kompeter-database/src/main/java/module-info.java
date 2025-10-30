module kompeter.database {
    requires transitive java.sql;
    requires transitive kompeter.utilities;
    requires org.xerial.sqlitejdbc;
    requires io.github.classgraph;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires static org.jetbrains.annotations;

    exports com.github.ragudos.kompeter.database;
    exports com.github.ragudos.kompeter.database.dao.inventory;
    exports com.github.ragudos.kompeter.database.dao.sales;
    exports com.github.ragudos.kompeter.database.dao.user;
    exports com.github.ragudos.kompeter.database.dto.inventory;
    exports com.github.ragudos.kompeter.database.dto.sales;
    exports com.github.ragudos.kompeter.database.dto.user;
    exports com.github.ragudos.kompeter.database.dto.enums;
    exports com.github.ragudos.kompeter.database.migrations;
    exports com.github.ragudos.kompeter.database.seeder;
    exports com.github.ragudos.kompeter.database.sqlite;
    exports com.github.ragudos.kompeter.database.sqlite.dao.inventory;
    exports com.github.ragudos.kompeter.database.sqlite.dao.sales;
    exports com.github.ragudos.kompeter.database.sqlite.dao.user;
    exports com.github.ragudos.kompeter.database.sqlite.migrations;
    exports com.github.ragudos.kompeter.database.dto.monitoring;
    exports com.github.ragudos.kompeter.database.sqlite.dao.monitoring;
    exports com.github.ragudos.kompeter.database.sqlite.seeder;
}
