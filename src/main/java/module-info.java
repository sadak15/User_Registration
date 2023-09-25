module com.example.register {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.register to javafx.fxml;
    exports com.example.register;
}