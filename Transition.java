public class Transition
{
    // c = Current 
    // n = Next 
    String cState;
    String cValue;
    String nState;
    String nValue;
    String dir;

    // Constructor
    public Transition(String cState, String cValue, String nState, String nValue, String dir)
    {
        this.cState = cState;
        this.cValue = cValue;
        this.nState = nState;
        this.nValue = nValue;
        this.dir = dir;
    }

    // Getters
    public String getCState()
    {
        return cState;
    }

    public String getCValue()
    {
        return cValue;
    }

    // Returns a string of the operations to be completed.
    public String execute()
    {
        return nState + nValue + dir;
    }

    public String toString()
    {
        return String.format("%s %s %s %s %s", cState, cValue, nState, nValue, dir);
    }
}