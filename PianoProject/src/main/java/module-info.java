module com.example.pianoproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.pianoproject to javafx.fxml;
    exports com.example.pianoproject;
    exports com.example.pianoproject.Controllers;
    opens com.example.pianoproject.Controllers to javafx.fxml;
}