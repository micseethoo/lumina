module com.example.lumina {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.xml.dom;
    requires java.sql;


    opens com.example.lumina to javafx.fxml;
    exports com.example.lumina;
}