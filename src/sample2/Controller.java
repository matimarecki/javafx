package sample2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    Canvas mycanvas;

    @FXML
    TextField dirpath;

    @FXML
    Stage stage;

    @FXML
    VBox leftpanel;

    String path = "res";

    List<String> files;
    int fileIndex = 0;
    int IMG_SIZE;
    int IMG_PAD = 5;

    //Funkcja wykonywana na początku tworzenia sceny
    public void initialize() {
        dirpath.setText("res");
        readDir();
        System.out.println(IMG_PAD);
        IMG_SIZE = Math.min(300,
                (int)Math.min(mycanvas.getHeight() - 2 * IMG_PAD, mycanvas.getWidth() - 2 * IMG_PAD));
        System.out.println(IMG_SIZE);

        ListView<String> list = new ListView<>();
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> items = FXCollections.observableArrayList (
                "Single", "Double", "Suite", "Family App", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite"
                , "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite"
                , "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite", "Double", "Suite");
        list.setItems(items);

        leftpanel.getChildren().add(list);
    }


    public void sayIt() {
        System.out.println("sayit");
    }

    public void showDialog() {
        System.out.println("dialog");
    }

    public void alertujUsera() {
        System.out.println("alert!");
    }

    public void showPicture() {
        if (files.size()==0) return;
        Image currentlySelected = new Image(getClass().getResourceAsStream(path + files.get(fileIndex)));

        GraphicsContext gc = mycanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 400, 400);

        gc.strokeOval(50,70,100,50 + 30 * fileIndex);

        /*
         * Algo skalujący przesunięcia i obrazek tak, aby:
         * - dla dowolnego canvas-u pole obrazka (IMG_SIZE) mieściło się w widoku
         * - obrazek był otoczony prostokątem oddalonym o IMG_PAD od niego (padding)
         * - nie-kwadratowy obrazek był skalowany proporcjonalnie tak, by cały się mieścił w polu obrazka
         */

        int offsetX = ((int)mycanvas.getWidth() - IMG_SIZE - 2 * IMG_PAD) / 2;
        int offsetY = ((int)mycanvas.getHeight() - IMG_SIZE - 2 * IMG_PAD) / 2;

        //rysowanie prostokąta
        gc.setLineWidth(1);
        gc.strokeRect(offsetX, offsetY, IMG_SIZE + 2 * IMG_PAD, IMG_SIZE + 2 * IMG_PAD);

        //rysowanie obrazka
        int w = (int)currentlySelected.getWidth();
        int h = (int)currentlySelected.getHeight();
        int rescaledHeight = 0, rescaledWidth = 0;
        int deltaX = 0, deltaY = 0;
        if (w>=h) {
            //obrazek szerszy niż wyższy
            rescaledWidth = IMG_SIZE;
            double f = 1.0 * IMG_SIZE / w;
            rescaledHeight = (int)(h * f);
            deltaY = (IMG_SIZE - rescaledHeight) / 2;
        } else {
            //wyższy niż szerszy
            rescaledHeight = IMG_SIZE;
            double f = 1.0 * IMG_SIZE / h;
            rescaledWidth = (int) (w * f);
            deltaX = (IMG_SIZE - rescaledWidth) / 2;
        }
        gc.drawImage(currentlySelected, offsetX + IMG_PAD + deltaX, offsetY + IMG_PAD + deltaY,
                rescaledWidth, rescaledHeight);

    }

    public void nextFile() {
        fileIndex += 1;
        if (fileIndex>=files.size()) {
            fileIndex -= files.size();
        }
        showPicture();
    }

    public void previousFile() {
        fileIndex -= 1;
        if (fileIndex<0) {
            fileIndex += files.size();
        }
        showPicture();
    }

    //przechodzi po folderze zczytanym z pola dirpath
    //znalezc wszystkie pliki konczace sie na .jpg albo .png
    public void readDir() {
        path = dirpath.getText();
        //daje ~/IdeaProjects/Java2_FX_2017/out/production/Java2_FX_2017/zajecia7_recap/
        URL url = getClass().getResource(".");
        File dir = new File(url.getPath() + File.separator + path);
        files = new ArrayList<>();
        if (!dir.isDirectory()) return;
        for(String filename : dir.list()) {
            if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png")) {
                files.add("/" + filename);
            }
        }
        fileIndex = 0;
        System.out.println("Files: " + files);
    }

    /*
     * todo: wykorzystac to do wybierania folderu (narazie są relative path)
     */
    public void myOpenFile() {
        DirectoryChooser chooser = new DirectoryChooser();

        URL url = getClass().getResource(".");
        File defaultDir = null;
        try {
            defaultDir = new File(URLDecoder.decode(url.getPath(),"UTF-8") + File.separator + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        chooser.setInitialDirectory(defaultDir);

        File selectedDirectory = chooser.showDialog(stage);
        System.out.println(selectedDirectory);

    }



}