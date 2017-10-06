package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class Controller {

    @FXML
    TextField tf1;

    @FXML
    TextField tf2;

    public void aaa() {
        String s = tf1.getText();
        tf2.setText(s + s);

        int n = 10;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println(i + " x "  + j + " = " + (i*j));
            }
        }



    }
}
