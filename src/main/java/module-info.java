module com.example.multicastchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.multicastchat to javafx.fxml;
    exports com.example.multicastchat;
}