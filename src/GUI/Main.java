package GUI;

import ArithmeticCoder.AdaptiveArithmeticCompress;
import GrammarCoder.Compress;
import GrammarCoder.ImplicitEncoder;
import GrammarCoder.InputOutput;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends Application {
    String PATH = System.getProperty("user.dir");
    String SOURCE_PATH = PATH + "/sourceFiles";
    String COMPRESSED_PATH = PATH + "/compressedFiles";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ProjectGC");
        InputOutput io = new InputOutput();
        Compress c = new Compress();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(SOURCE_PATH));

        BorderPane border = new BorderPane();

        ChoiceBox choiceBox = new ChoiceBox();

        choiceBox.getItems().add("Grammar");
        choiceBox.getItems().add("Implicit");
        choiceBox.getItems().add("Binary");

        choiceBox.setValue("Binary");

        Button compressButton = new Button("File to compress");
        compressButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                if (choiceBox.getValue().equals("Binary")) {
                    c.processInput(io.readFile(selectedFile));
                    ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
                    try {
                        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        Button decompressButton = new Button("File to decompress");
        decompressButton.setOnAction(e -> {
            System.out.println("nope");
        });

        HBox hBox = new HBox(choiceBox, compressButton, decompressButton);
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        border.setTop(hBox);


        // search bar
        TextField searchField = new TextField();
        searchField.setText("Search...");

        // display compressed files
        ListView listView = new ListView();

        File compressedFolder = new File(COMPRESSED_PATH);
        File[] compressedFiles = compressedFolder.listFiles();
        for (File f : compressedFiles) {
            listView.getItems().add(f);
        }

        // next to it is output for search
        TextArea textOutput = new TextArea();

        HBox fileAndOutput = new HBox(listView, textOutput);
        fileAndOutput.setSpacing(10);

        VBox vBox = new VBox(searchField, fileAndOutput);
        vBox.setPadding(new Insets(0, 12, 15, 12));
        vBox.setSpacing(10);

        border.setCenter(vBox);

        Scene scene = new Scene(border, 900, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}