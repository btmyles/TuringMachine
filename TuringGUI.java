import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class TuringGUI extends Application
{
    String tape;
    char cState;
    char acceptState;
    String s1, v1, s2, v2, dir;
    ArrayList<Transition> delta = new ArrayList<Transition>(); 

    Configuration currentConfig;
    // Label leftConfig;
    // Label midConfig;
    // Label rightConfig;

    Button next;
    Button prev;

    public void start(Stage primaryStage) throws Exception
    {
        // Read the values from the text file
        Scanner in = new Scanner(new File("testCase.txt"));

        tape = in.nextLine();
        cState = in.nextLine().charAt(0);
        acceptState = in.nextLine().charAt(0);

        while (in.hasNextLine())
            {
                s1 = in.next();
                v1 = in.next();
                s2 = in.next();
                v2 = in.next();
                dir = in.next();
                delta.add(new Transition(s1, v1, s2, v2, dir));
            }

        in.close();

        Label input = new Label(String.format("Input: %s \nStart State: %c \nAccept State: %c", tape, cState, acceptState));

        // Create configuration object
        currentConfig = new Configuration("", tape.substring(0,1), tape.substring(1));
        currentConfig.changeStyle("-fx-font-size: 45px;");
        currentConfig.changeHeadStyle("-fx-background-color: #66e293");

        // leftConfig = new Label();
        // midConfig = new Label(tape.substring(0,1));
        // midConfig.setStyle("-fx-background-color: #66e293");
        // rightConfig = new Label(tape.substring(1));

        // Put configuration in a flowpane
        Label[] labels = currentConfig.getConfig();
        FlowPane config = new FlowPane(labels[0], labels[1], labels[2]);

        // a spacer to separate config from other inputs
        Region spacer = new Region();
        spacer.setPrefHeight(40);

        // Create buttons for next and previous configs
        next = new Button("Next");
        prev = new Button("Prev");
        next.setStyle("-fx-font-size: 25px;");
        prev.setStyle("-fx-font-size: 25px;");
        FlowPane buttons = new FlowPane(next, prev);

        // When buttons are clicked:
        next.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
                processNext(e);
            }
        });
        prev.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
                processPrev(e);
            }
        });

        // Place elements in the outer gridpane
        GridPane mainGrid = new GridPane();
        mainGrid.add(input, 0, 0);
        mainGrid.add(spacer, 0, 1);
        mainGrid.add(config, 0, 2);
        mainGrid.add(buttons, 0, 3);

        Scene mainScene = new Scene(mainGrid, 600, 400);
        primaryStage.setTitle("Turing Machine");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void processNext(ActionEvent event)
    {

    }
    public void processPrev(ActionEvent event)
    {
        
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}