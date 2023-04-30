module software_i.project {
    requires javafx.controls;
    requires javafx.fxml;


    //opens software_i.project to javafx.fxml;
    //exports software_i.project;
    exports Controller;
    opens Controller to javafx.fxml;

    exports software_i.project;
    opens software_i.project to javafx.fxml;
    exports Model;
    opens Model to javafx.fxml;
    //exports model;
    //opens model to javafx.fxml;
}