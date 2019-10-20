package javamodularity.easytext.gui;

import java.util.ServiceLoader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javamodularity.easytext.analysis.api.Analyzer;
import static javamodularity.easytext.analysis.api.Preprocessing.toSentences;

public class Main extends Application {

    private static Iterable<Analyzer> analyzers;
    private static ComboBox<String> analysis;
    private static TextArea input;
    private static Text output;
    private static String sampleText;

    public static void main(String[] args) {
        analyzers = ServiceLoader.load(Analyzer.class); // Load services here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EasyText");
        Button btn = new Button();
        btn.setText("Calculate");
        btn.setOnAction(event ->
          output.setText(analyze(input.getText(), (String) analysis.getValue()))
        );

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(3));
        vbox.setSpacing(3);
        Text title = new Text("Choose an analysis:");
        analysis = new ComboBox<>();
        for(Analyzer analyzer: analyzers) {
            analysis.getItems().add(analyzer.getName());
        }

        vbox.getChildren().add(title);
        vbox.getChildren().add(analysis);
        vbox.getChildren().add(btn);

        input = new TextArea();
        input.setText(sampleText);
        output = new Text();
        BorderPane pane = new BorderPane();
        pane.setRight(vbox);
        pane.setCenter(input);
        pane.setBottom(output);
        primaryStage.setScene(new Scene(pane, 600, 450));
        primaryStage.show();
    }

    private String analyze(String input, String analysis) {
        for(Analyzer analyzer: analyzers) {
            if(analyzer.getName().equals(analysis)) {
                return analysis + ": " + analyzer.analyze(toSentences(input));
            }
        }

        return "No analyzer found for " + analysis;
    }

    static {
      try {
        sampleText = new String (java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("testinput.txt")));
      } catch (java.io.IOException ioe) {
        sampleText = "Paste text here";
      }
    }
}
