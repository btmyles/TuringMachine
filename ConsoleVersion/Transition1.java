public class Transition1
{
    // c = Current 
    // n = Next 
    char cState;
    char cValue;
    char nState;
    char nValue;
    char dir;

    // Constructor
    public Transition1(char cState, char cValue, char nState, char nValue, char dir)
    {
        this.cState = cState;
        this.cValue = cValue;
        this.nState = nState;
        this.nValue = nValue;
        this.dir = dir;
    }

    // Getters
    public char getCState()
    {
        return cState;
    }

    public char getCValue()
    {
        return cValue;
    }

    // Returns an array of the operations to be completed.
    public char[] execute()
    {
        return new char[] {nState, nValue, dir};
    }

    public String toString()
    {
        return String.format("%c %c %c %c %c", cState, cValue, nState, nValue, dir);
    }
}