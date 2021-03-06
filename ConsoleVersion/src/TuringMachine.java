// Turing machine main
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class TuringMachine
{
    private static char[] tape;
    public static void main(String[] args)
    {
        int head = 0;
        char cState, acceptState;
        ArrayList<Transition1> delta = new ArrayList<Transition1>(); 
        char s1, v1, s2, v2, dir;
        Boolean halt, accept, ruleFound;
        int count;
        char[] nOperations;

        Scanner in = null;
        try
        {
            in = new Scanner(new File(args[0]));

            // Read the values from the text file
            tape = in.nextLine().toCharArray();
            cState = in.nextLine().charAt(0);
            acceptState = in.nextLine().charAt(0);

            // Print initial tape, current initial state, accept state.
            System.out.print("Input: ");
            // Output tape
            for (int i=0; i<tape.length; i++)
            {
                if (head == i)
                {
                    System.out.print(cState);
                }

                System.out.print(tape[i]);
            }
            System.out.println();

            System.out.println("Initial State: " + cState);
            System.out.println("Accept State: " + acceptState);
            
            while (in.hasNextLine())
            {
                s1 = in.next().charAt(0);
                v1 = in.next().charAt(0);
                s2 = in.next().charAt(0);
                v2 = in.next().charAt(0);
                dir = in.next().charAt(0);
                delta.add(new Transition1(s1, v1, s2, v2, dir));
            }
        }
        catch(FileNotFoundException e)
        {
            // Crashes program if the file is not found
            System.out.println("Input file not found");
            return;
        }
        finally
        {
            // Close the scanner
            if (in != null)
                in.close();
        }

        halt = false;
        accept = false;

        // Loop until the machine decides
        while (!halt)
        {
            // Process the tape based on the delta rules
            ruleFound = false;
            count = 0;

            // Loop through each rule in delta to find what the next operations should be
            while (!ruleFound && count < delta.size())
            {
                // If the current rule's cState and cValue match up, execute that state.
                if (delta.get(count).getCState() == cState && delta.get(count).getCValue() == tape[head])
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
                        tape[head] = nOperations[1];

                    // Move the head left or right
                    // Dont move left if the head is at the left end
                    if (nOperations[2] == 'L' && head > 0)
                    {
                        head--;
                    }
                    else if (nOperations[2] == 'R')
                    {
                        if (head == tape.length - 1)
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

            // Output tape
            for (int i=0; i<tape.length; i++)
            {
                if (head == i)
                {
                    System.out.print(cState);
                }

                System.out.print(tape[i]);
            }
            System.out.println();
        }

        System.out.println((accept) ? "Accept" : "Reject");
    }

    private static char[] extendTape(char[] oldTape)
    {
        char[] newTape = new char[oldTape.length+1];
        for (int i=0; i<oldTape.length; i++)
        {
            newTape[i] = oldTape[i];
        }
        newTape[oldTape.length] = '_';

        return newTape;
    }
}