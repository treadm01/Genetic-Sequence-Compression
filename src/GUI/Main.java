package GUI;

import ArithmeticCoder.AdaptiveArithmeticCompress;
import ArithmeticCoder.AdaptiveArithmeticDecompress;
import GrammarCoder.*;
import Search.Search;
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
    private String PATH = System.getProperty("user.dir");
    private static final int WIDTH = 560;
    private static final int HEIGHT = 250;
    private static final int SPACING = 10;
    private static final int WIDTH_BUFFER = 12;
    private static final int HEIGHT_BUFFER = 15;

    public static void main(String[] args) {
        launch(args);
    }

    //todo needs breaking down into separate methods for correct gui code
    @Override
    public void start(Stage primaryStage) {
        final Rule[] searchFileRule = new Rule[1]; // array used to store and output search results
        primaryStage.setTitle("ProjectGC"); // set window title
        InputOutput io = new InputOutput(); // new io for reading and writing necessary files

        // text window for compression and search output
        TextArea textOutput = new TextArea();
        textOutput.textProperty().addListener((observable, oldValue, newValue) -> textOutput.setText(newValue));

        // file selection button
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(PATH));

        // drop down options for compression, search, decompress etc
        ChoiceBox<String> choiceBox = new ChoiceBox<String>();
        choiceBox.getItems().add("Search");
        choiceBox.getItems().add("Decompress");
        choiceBox.getItems().add("Compress (With Edits)");
        choiceBox.getItems().add("Compress");
        choiceBox.setValue("Compress");

        // button to select file in relation to option above
        Button compressButton = new Button("Select File");
        compressButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) { //todo needs handling for different file types
                switch (choiceBox.getValue()) {
                    case "Compress":
                    case "Compress (With Edits)":
                        Compress c = new Compress();
                        c.processInput(io.readFile(selectedFile), !choiceBox.getValue().equals("Compress"));
                        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
                        try {
                            AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
                            textOutput.setText(aac.constructCompressionOutput(selectedFile.length()));

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case "Decompress":
                        try {
                            AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(selectedFile);
                            Decompress d = new Decompress();
                            io.writeToFile(d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
                            textOutput.setText("File decompressed");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    default: // file selected for search
                        try {
                            AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(selectedFile);
                            Decompress d = new Decompress();
                            searchFileRule[0] = d.buildGrammar(aad.getImplicitEncoding());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                }
            }
        });

        // search bar for input and button
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");

        // perform search
        searchButton.setOnAction(value ->  {
            if (searchFileRule[0] != null) {
                Search s = new Search(searchFileRule[0]);
                textOutput.setText(String.valueOf(s.search(String.valueOf(searchField.getCharacters()))));
            }
        });


        // layout of gui elements, positioning of search bar, buttons etc
        BorderPane border = new BorderPane();
        HBox hBox = new HBox(choiceBox, compressButton, searchField, searchButton);
        hBox.setPadding(new Insets(HEIGHT_BUFFER, WIDTH_BUFFER, HEIGHT_BUFFER, WIDTH_BUFFER));
        hBox.setSpacing(SPACING);
        border.setTop(hBox);

        HBox fileAndOutput = new HBox(textOutput);
        fileAndOutput.setSpacing(SPACING);

        VBox vBox = new VBox(fileAndOutput);
        vBox.setPadding(new Insets(0, WIDTH_BUFFER, HEIGHT_BUFFER, WIDTH_BUFFER));
        vBox.setSpacing(SPACING);

        border.setCenter(vBox);

        Scene scene = new Scene(border, WIDTH, HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}