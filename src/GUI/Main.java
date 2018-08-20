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

public class Main extends Application {
    String PATH = System.getProperty("user.dir") + "/sourceFiles";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ProjectGC");
        InputOutput io = new InputOutput();
        Compress c = new Compress();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(PATH));

        BorderPane border = new BorderPane();

        MenuItem grammarOption = new MenuItem("Grammar");
        MenuItem ImpOption = new MenuItem("Implicit Encoding");
        MenuItem BinOption = new MenuItem("Binary");

        MenuButton menuButton = new MenuButton("Compression Options", null, grammarOption, ImpOption, BinOption);

        Button compressButton = new Button("File to compress");
        compressButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                c.processInput(io.readFile(selectedFile));
                ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
                try {
                    AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Button decompressButton = new Button("File to decompress");
        decompressButton.setOnAction(e -> {
            System.out.println("nope");
        });

        HBox hBox = new HBox(menuButton, compressButton, decompressButton);
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        border.setTop(hBox);


        // search bar
        TextField searchField = new TextField();
        searchField.setText("Search...");

        // display compressed files
        ListView listView = new ListView();
        listView.getItems().add("Item 1");
        listView.getItems().add("Item 2");
        listView.getItems().add("Item 3");

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