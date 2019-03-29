import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TuringGUI extends Application
{

    int head = 0;
    String tape;

    ArrayList<Transition> delta = new ArrayList<Transition>(); 
    String s1, v1, s2, v2, dir;

    Boolean halt = false, accept, ruleFound;
    int count;
    String nOperations;

    char cState;
    char acceptState;
    Label[] labels;

    Label configLabel;
    Configuration currentConfig;
    ArrayList<Configuration> pastConfigs;
    int configShowing;

    Button next, prev;
    Label haltMessage;
    Label inputLabel, input;

    public void start(Stage primaryStage) throws Exception
    {
        // Read the values from the input file
        Scanner in = new Scanner(new File("testCase.txt"));
        
        tape = in.nextLine();
        cState = in.nextLine().charAt(0);
        acceptState = in.nextLine().charAt(0);

        // Scan transitions from input file
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

        // Display initial configuration info
        inputLabel = new Label("Input: ");
        inputLabel.setStyle("-fx-font-size: 45px;");
        input = new Label(String.format(tape));
        input.setStyle("-fx-font-size: 45px;");

        // Create configuration object
        currentConfig = new Configuration("", tape.substring(0,1), tape.substring(1), 0);
        currentConfig.changeStyle("-fx-font-size: 45px;");
        currentConfig.changeHeadStyle("-fx-background-color: #66e293");

        // Get labels from currentConfig
        labels = currentConfig.getConfig();

        // Add config to the pastConfig list
        pastConfigs = new ArrayList<Configuration>();
        Configuration copy = new Configuration(labels[0].getText(), labels[1].getText(), labels[2].getText(), currentConfig.getConfigNumber());
        pastConfigs.add(copy);

        // Put configuration in a flowpane
        FlowPane config = new FlowPane(labels[0], labels[1], labels[2]);

        // Spacer to separate config from other inputs
        Region spacer = new Region();
        spacer.setPrefHeight(40);

        // Create label to show where current configuration will be displayed
        configLabel = new Label("Config: ");
        configLabel.setStyle("-fx-font-size: 45px;");

        // Create buttons for next and previous configs
        next = new Button("Next");
        prev = new Button("Prev");
        next.setStyle("-fx-font-size: 25px;");
        prev.setStyle("-fx-font-size: 25px;");
        FlowPane buttons = new FlowPane(next, prev);

        // Create haltMessage text. Set to empty string since machine is not halted yet
        haltMessage = new Label("");
        haltMessage.setStyle("-fx-font-size: 35px;");

        // When buttons are clicked, run their process method
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
        mainGrid.add(inputLabel, 0, 0);
        mainGrid.add(input, 0, 1);
        mainGrid.add(spacer, 0, 2);
        mainGrid.add(configLabel, 0, 3);
        mainGrid.add(config, 0, 4);
        mainGrid.add(haltMessage, 1, 5);
        mainGrid.add(buttons, 0, 5);

        // Show the window
        Scene mainScene = new Scene(mainGrid, 600, 400);
        primaryStage.setTitle("Turing Machine");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void processNext(ActionEvent event)
    {
        if (configShowing != pastConfigs.size()-1 && !halt)
        {
            // Show the configuration snapshot of the config which is ahead of the current
            // Show a snapshot of the config after currentConfig
            configShowing++;
            Configuration newCon = pastConfigs.get(configShowing);
            currentConfig.clear();
            currentConfig.addLeftVariable(newCon.getLeftConfig().getText());
            currentConfig.addMidVariable(newCon.getMidConfig().getText());
            currentConfig.addRightVariable(newCon.getRightConfig().getText());
        }
        // find the applicable rule, execute it, and determine if the machine should halt
        else if (!halt)
        {
            // Process the tape based on the delta rules
            ruleFound = false;
            count = 0;

            // Loop through each rule in delta to find what the next operations should be
            while (!ruleFound && count < delta.size())
            {
                // If the current rule's cState and cValue match up, execute that state.
                if (delta.get(count).getCState().equals(Character.toString(cState)) && delta.get(count).getCValue().equals(tape.substring(head, head+1)))
                {
                    ruleFound = true;

                    // Get the new state, value to write, and direction to move from the rule
                    nOperations = delta.get(count).execute();

                    // Use the array of next operations:

                    // Change the state and check for accept
                    cState = nOperations.charAt(0);
                    if (cState == acceptState)
                    {
                        accept = true;
                        halt = true;
                        haltMessage.setText("Accept");
                    }

                    // Write to the head location unless the character provided is a ~
                    if (nOperations.charAt(1) != '~')
                    {
                        StringBuilder modifiedTape = new StringBuilder(tape);
                        modifiedTape.setCharAt(head, nOperations.charAt(1));
                        tape = modifiedTape.toString();
                    }

                    // Move the head left or right
                    // Dont move left if the head is at the left end
                    if (nOperations.charAt(2) == 'L' && head > 0)
                    {
                        head--;
                    }

                    else if (nOperations.charAt(2) == 'R')
                    {
                        if (head == tape.length() - 1)
                        {
                            tape = extendTape(tape);
                        }
                        head++;
                    }
                    // updating the configuration string

                    currentConfig.clear();
                                        
                    if (head != 0)
                    {
                        currentConfig.addLeftVariable(tape.substring(0, head));
                        currentConfig.addMidVariable(tape.substring(head, head+1));
                    }
                    else
                    {
                        currentConfig.addMidVariable(tape.substring(0, 1));
                    }
                    currentConfig.addRightVariable(tape.substring(head+1));
                    
                    // Add currentConfig to pastConfigs
                    currentConfig.setConfigNumber(currentConfig.getConfigNumber() + 1);
                    Configuration copy = new Configuration(currentConfig.getLeftConfig().getText(), currentConfig.getMidConfig().getText(), currentConfig.getRightConfig().getText(), currentConfig.getConfigNumber());
                    pastConfigs.add(copy);
                    configShowing++;

                    // Print entire list of configurations
                    System.out.println("\nList of past configs including current:");
                    for (int i=0; i<pastConfigs.size(); i++)
                    {
                        System.out.println(pastConfigs.get(i));
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
                haltMessage.setText("Reject");
            }
        }
    }
    public void processPrev(ActionEvent event)
    {
        if (configShowing > 0)
        {
            halt = false;
            //haltMessage.setText("");

            // Show a snapshot of the config before currentConfig
            configShowing--;
            Configuration newCon = pastConfigs.get(configShowing);
            currentConfig.clear();
            currentConfig.addLeftVariable(newCon.getLeftConfig().getText());
            currentConfig.addMidVariable(newCon.getMidConfig().getText());
            currentConfig.addRightVariable(newCon.getRightConfig().getText());

            // Print current for testing
            System.out.printf("\n%d config: \n", configShowing);
            System.out.println(currentConfig);
            //
        }
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