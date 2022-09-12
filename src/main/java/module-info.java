module main.java.sk.stuba.fei.xvillantova.bakalar {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.math3;


    exports sk.stuba.fei.xvillantova.bakalar;
    opens sk.stuba.fei.xvillantova.bakalar to javafx.fxml;
}
