package GUI;

import ArithmeticCoder.AdaptiveArithmeticCompress;
import ArithmeticCoder.AdaptiveArithmeticDecompress;
import GrammarCoder.Compress;
import GrammarCoder.Decompress;
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

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(SOURCE_PATH));

        BorderPane border = new BorderPane();

        ChoiceBox choiceBox = new ChoiceBox();

        choiceBox.getItems().add("Search");
        choiceBox.getItems().add("Decompress");
        choiceBox.getItems().add("Compress");

        choiceBox.setValue("Compress");

        Button compressButton = new Button("Select File");
        compressButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                if (choiceBox.getValue() == "Compress") {
                    Compress c = new Compress();
                    c.processInput(io.readFile(selectedFile));
                    ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
                    try {
                        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else if (choiceBox.getValue() == "Decompress") {
                    //todo need to return from adaptive arithmetic and write to file
//                    Decompress d = new Decompress();
//                    d.processInput(io.readFile(selectedFile));
  //                  ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
                    try {
                        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(selectedFile);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else {
//                    try {
//                        // currently decompressing completely todo how to search accessed file
//                        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(selectedFile);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
                }
            }
        });

        // search bar
        TextField searchField = new TextField();

        Button button = new Button("Search");

        button.setOnAction(value ->  {
            System.out.println(searchField.getCharacters());
        });

        HBox hBox = new HBox(choiceBox, compressButton, searchField, button);
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        border.setTop(hBox);

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

        VBox vBox = new VBox(fileAndOutput);
        vBox.setPadding(new Insets(0, 12, 15, 12));
        vBox.setSpacing(10);

        border.setCenter(vBox);

        Scene scene = new Scene(border, 900, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}