
// Turing machine main
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class TuringMachine
{

    public static void main(String[] args)
    {
        char[] tape;
        int head = 0;
        char cState;
        ArrayList<Transition> delta = new ArrayList<Transition>(); 
        char s1, v1, s2, v2, dir;
        Boolean found;
        int count;

        Scanner in = null;
        try
        {
            in = new Scanner(new File(args[0]));

            // Read the values from the text file
            tape = in.nextLine().toCharArray();
            cState = in.nextLine().charAt(0);

            while (in.hasNextLine())
            {
                s1 = in.next().charAt(0);
                v1 = in.next().charAt(0);
                s2 = in.next().charAt(0);
                v2 = in.next().charAt(0);
                dir = in.next().charAt(0);
                delta.add(new Transition(s1, v1, s2, v2, dir));
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Input file not found");
        }
        finally
        {
            if (in != null)
            in.close();
        }

        // Process the tape based on the delta rules
        found = false;
        count = 0;
        // Loop through each rule in delta
        while (!found && count < delta.size())
        {
            // If the current rule's cState and cValue match up, execute that state.
            if (delta.get(count).getCState() == cState && delta.get(count).getCValue() == tape[head])
            {
                found = true;

                // Get the new state, value to write, and direction to move from the rule
                delta.get(count).execute();
            }
            else
            {
                count++;
            }
        }

        // If count exceeds the # of rules in delta, no rule applies to the current state of the machine
        if (count >= delta.size())
        {
            // Print error message
        }

        // Output transition functions for testing
        for (int i=0; i<delta.size(); i++)
        {
            System.out.println(delta.get(i));
        }
    }
}