module org.example.memorycardgameproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.media;


    opens org.example.memorycardgameproject to javafx.fxml;
    exports org.example.memorycardgameproject;
}