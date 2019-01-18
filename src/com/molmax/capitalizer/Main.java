package com.molmax.capitalizer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    //To define symbols after which a character should be capitalized
    private final static List<Character> SPECIAL_SYMBOLS =
            Arrays.asList(' ', '_', '-', '/', '\\', '?', '!', '*', '+', '#', '$', '&', '.', ',', '"', '\'', ':', ';', '(', ')', '@');
    private static String EMPTY_STRING = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Line Changer");
        Button button = new Button();
        button.setId("1");
        button.setText("Change Capitalization");
        final TextArea textArea = new TextArea();
        textArea.setId("2");
        textArea.setWrapText(true);
        textArea.setPrefWidth(500);
        textArea.setText("Input your text here...");
        Pane mainPane = new FlowPane();
        mainPane.getChildren().addAll(Arrays.asList(textArea, button));
        for (Node node : mainPane.getChildren()) {
            if (node.getClass().equals(Button.class) && node.getId().equals(button.getId())) {
                node.setTranslateX(5);
                node.setTranslateY(10);
            }
            if (node.getClass().equals(TextArea.class) && node.getId().equals(textArea.getId())) {
                node.setTranslateX(5);
                node.setTranslateY(5);
            }
        }
        Scene scene = new Scene(mainPane, 500, 250, Color.ALICEBLUE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        button.setOnAction(new EventHandler<ActionEvent>() {
            int j = 1;

            @Override
            public void handle(ActionEvent event) {
                if (j > 3) j = 1;
                String initialText = textArea.getText();
                String selectedText = textArea.getSelectedText();
                IndexRange selectionRange = textArea.getSelection();
                StringBuilder sb = new StringBuilder();
                if (EMPTY_STRING.equals(selectedText) || selectedText.length() == initialText.length()) {
                    String capitalizedString = changeCapitalization(initialText, j);
                    sb.append(capitalizedString);
                } else {
                    String capitalizedString = changeCapitalization(selectedText, j);
                    sb.append(initialText, 0, selectionRange.getStart())
                            .append(capitalizedString)
                            .append(initialText.substring(selectionRange.getEnd()));
                }
                textArea.setText(sb.toString());
                j++;
            }
        });
    }

    /**
     * @param capitalizationParameter 1 - All caps, 2 - All lower, 3 - First chars caps
     */
    private static String changeCapitalization(String input, int capitalizationParameter) {
        if (capitalizationParameter == 1) {
            input = input.toUpperCase();
        } else if (capitalizationParameter == 2) {
            input = input.toLowerCase();
        } else if (capitalizationParameter == 3) {
            char[] chars = input.toLowerCase().toCharArray();
            for (int i = 0; i < chars.length - 1; i++) {
                //Current symbol of input string is NOT a special symbol AND (IS first character OR preceding character IS a special symbol)
                //In other words we are changing only those (non-special) characters that are either situated at the beginning of the word or after the special symbol
                boolean shouldChange = !SPECIAL_SYMBOLS.contains(chars[i]) && (i == 0 || SPECIAL_SYMBOLS.contains(chars[i - 1]));
                if (shouldChange) {
                    chars[i] = (char) (chars[i] & 0x5f);
                }
            }
            return String.valueOf(chars);
        } else {
            System.out.println("Wrong capitalization parameter. 1, 2 or 3 are legal.");
        }
        return input;
    }
}
