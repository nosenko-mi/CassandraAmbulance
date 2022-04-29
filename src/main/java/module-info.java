module org.coursework.cassandraambulance {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.datastax.oss.driver.core;
    requires com.datastax.oss.driver.querybuilder;

    opens org.coursework.cassandraambulance to javafx.fxml;
    exports org.coursework.cassandraambulance;
    exports org.coursework.cassandraambulance.models;
    opens org.coursework.cassandraambulance.models to javafx.fxml;
    exports org.coursework.cassandraambulance.controllers;
    opens org.coursework.cassandraambulance.controllers to javafx.fxml;
    exports org.coursework.cassandraambulance.tables;
    opens org.coursework.cassandraambulance.tables to javafx.fxml;
}