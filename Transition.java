public class Transition
{
    // c = Current 
    // n = Next 
    char cState;
    char cValue;
    char nState;
    char nValue;
    char dir;

    public Transition(char cState, char cValue, char nState, char nValue, char dir)
    {
        this.cState = cState;
        this.cValue = cValue;
        this.nState = nState;
        this.nValue = nValue;
        this.dir = dir;
    }

    public String toString()
    {
        return String.format("%c %c %c %c %c", cState, cValue, nState, nValue, dir);
    }
}