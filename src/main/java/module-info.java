module dev.thoq.zephyr {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires javafx.media;
    requires java.desktop;
    requires com.google.gson;

    opens dev.thoq.zephyr to javafx.fxml;
    exports dev.thoq.zephyr;
    exports dev.thoq.zephyr.utility.misc;
    exports dev.thoq.zephyr.utility.ui;
    exports dev.thoq.zephyr.views;
}