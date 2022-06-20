module at.htl.test4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens at.htl.test4 to javafx.fxml;
    exports at.htl.test4;
}