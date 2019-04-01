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
    String nextRule;

    char cState;
    char acceptState;
    Label[] labels;

    Label configLabel;
    Configuration currentConfig, copy;
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
        currentConfig.changeHeadStyle("-fx-background-color: #ffe100;");

        // Get labels from currentConfig
        labels = currentConfig.getConfig();

        // Add config to the pastConfig list
        pastConfigs = new ArrayList<Configuration>();
        copy = new Configuration(labels[0].getText(), labels[1].getText(), labels[2].getText(), currentConfig.getConfigNumber());
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
        // If the next configuration has already been calculated and stored on pastConfigs
        if (configShowing != pastConfigs.size()-1 && !halt)
        {
            // Show the configuration snapshot of the config which is ahead of the current
            // Show a snapshot of the config after currentConfig
            configShowing++;
            Configuration nextConfig = pastConfigs.get(configShowing);
            currentConfig.clear();
            currentConfig.appendLeft(nextConfig.getLeftConfig().getText());
            currentConfig.appendMid(nextConfig.getMidConfig().getText());
            currentConfig.appendRight(nextConfig.getRightConfig().getText());
        }
        // Find the applicable rule, execute it, and determine if the machine should halt
        else if (!halt)
        {
            // Process the tape based on the delta rules
            // Loop through each rule in delta until the next rule to be executed is found
            ruleFound = false;
            count = 0;

            while (!ruleFound && count < delta.size())
            {
                // If the current rule's cState and cValue match up, execute that rule.
                if (delta.get(count).getCState().equals(Character.toString(cState)) && delta.get(count).getCValue().equals(tape.substring(head, head+1)))
                {
                    ruleFound = true;

                    // Get the new state, value to write, and direction to move from the rule
                    nextRule = delta.get(count).execute();

                    // Use the array of next operations:

                    // Change the state and check for accept
                    cState = nextRule.charAt(0);
                    if (cState == acceptState)
                    {
                        accept = true;
                        halt = true;
                        haltMessage.setText("Accept");
                        haltMessage.setStyle("-fx-background-color: #4ff958");
                        currentConfig.changeHeadStyle("-fx-background-color: #4ff958;");

                    }

                    // Write to the head location unless the character provided is a ~
                    if (nextRule.charAt(1) != '~')
                    {
                        // Stringbuilder used to change ONLY the character at the head
                        StringBuilder modifiedTape = new StringBuilder(tape);
                        modifiedTape.setCharAt(head, nextRule.charAt(1));
                        tape = modifiedTape.toString();
                    }

                    // Move the head left or right
                    // Dont move left if the head is at the left end
                    if (nextRule.charAt(2) == 'L' && head > 0)
                    {
                        head--;
                    }
                    // Extend tape if head is at the right end (simulate infinite tape)
                    else if (nextRule.charAt(2) == 'R')
                    {
                        if (head == tape.length() - 1)
                        {
                            tape = extendTape(tape);
                        }
                        head++;
                    }

                    // updating the configuration showing

                    currentConfig.clear();
                                      
                    // If the head is at the left side, set the midLabel to the left character of the tape
                    if (head == 0)
                    {
                        currentConfig.appendMid(tape.substring(0, 1));                
                    }
                    else
                    {
                        currentConfig.appendLeft(tape.substring(0, head));
                        currentConfig.appendMid(tape.substring(head, head+1));
                    }
                    currentConfig.appendRight(tape.substring(head+1));
                    
                    // Add currentConfig to pastConfigs
                    currentConfig.incrementConfigNumber();
                    labels = currentConfig.getConfig();
                    copy = new Configuration(labels[0].getText(), labels[1].getText(), labels[2].getText(), currentConfig.getConfigNumber());
                    pastConfigs.add(copy);
                    configShowing++;
                }
                else
                {
                    // Increment count since the rule was not found.
                    // If cound exceeds the number of rules, the input is invalid
                    count++;
                }
            }

            // If count exceeds the # of rules in delta, no rule applies to the current state of the machine
            if (count >= delta.size())
            {
                halt = true;
                haltMessage.setText("Reject");
                haltMessage.setStyle("-fx-background-color: #ff5744");
                currentConfig.changeHeadStyle("-fx-background-color: #ff5744;");
            }
        }
    }
    public void processPrev(ActionEvent event)
    {
        if (configShowing > 0)
        {
            halt = false;

            // Show a snapshot of the config before currentConfig
            configShowing--;
            labels = pastConfigs.get(configShowing).getConfig();
            setCurrentConfig(labels);
        }
    }

    private void setCurrentConfig(Label[] labels)
    {
        currentConfig.clear();
        currentConfig.appendLeft(labels[0].getText());
        currentConfig.appendMid(labels[1].getText());
        currentConfig.appendRight(labels[2].getText());
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