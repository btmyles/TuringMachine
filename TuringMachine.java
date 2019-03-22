
// Turing machine main
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class TuringMachine
{

    public static void main(String[] args)
    {
        String input;
        char cState;
        ArrayList<Transition> delta = new ArrayList<Transition>(); 
        char s1, v1, s2, v2, dir;

        Scanner in = null;
        try
        {
            in = new Scanner(new File(args[0]));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Input file not found");
        }

        // Read the values from the text file
        input = in.nextLine();
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
        if (in != null)
            in.close();

        // Output transition functions for testing
        for (int i=0; i<delta.size(); i++)
        {
            System.out.println(delta.get(i));
        }
    }
}