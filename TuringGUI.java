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
import java.util.ArrayList;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class TuringGUI extends Application
{

    int head = 0;
    ArrayList<Transition> delta = new ArrayList<Transition>(); 
    String s1, v1, s2, v2, dir;
    Boolean halt, accept, ruleFound;
    int count;
    char[] nOperations;

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
        // Loop until the machine decides
        if (!halt)
        {
            // Process the tape based on the delta rules
            ruleFound = false;
            count = 0;

            // Loop through each rule in delta to find what the next operations should be
            while (!ruleFound && count < delta.size())
            {
                // If the current rule's cState and cValue match up, execute that state.
                if (delta.get(count).getCState() == cState && delta.get(count).getCValue() == tape.charAt(head))
                {
                    ruleFound = true;

                    // Get the new state, value to write, and direction to move from the rule
                    nOperations = delta.get(count).execute();

                    // Use the array of next operations:

                    // Change the state and check for accept
                    cState = nOperations[0];
                    if (cState == acceptState)
                    {
                        accept = true;
                        halt = true;
                    }

                    // Write to the head location unless the character provided is a ~
                    if (nOperations[1] != '~')
                    {
                        StringBuilder modifiedTape = new StringBuilder(tape);
                        modifiedTape.setCharAt(head, nOperations[1]);
                        tape = modifiedTape.toString();
                    }

                    // Move the head left or right
                    // Dont move left if the head is at the left end
                    if (nOperations[2] == 'L' && head > 0)
                    {
                        head--;
                    }
                    else if (nOperations[2] == 'R')
                    {
                        if (head == tape.length() - 1)
                        {
                            tape = extendTape(tape);
                        }
                        head++;
                    }
                }
                else
                {
                    count++;
                }
            }

            // If count exceeds the # of rules in delta, no rule applies to the current state of the machine
            if (count >= delta.size())
            {
                halt = true;
            }

            // Output tape for testing
            for (int i=0; i<tape.length(); i++)
            {
                if (head == i)
                {
                    System.out.print(cState);
                }

                System.out.print(tape.charAt(i));
            }
            System.out.println();
        }
    }
    public void processPrev(ActionEvent event)
    {
        
    }

    private static String extendTape(String oldTape)
    {
        String newTape = "";
        for (int i=0; i<oldTape.length(); i++)
        {
            newTape += oldTape.charAt(i);
        }
        newTape += '_';

        return newTape;
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}