public class Transition
{
    char cState;
    char cValue;
    char nState;
    char nValue;
    char dir;

    public Transition(char cState, char cValue, char nState, char nValue, char dir)
    {
        this.cState = cState;
        this.cValue = cValue;
        this.nState = nValue;
        this.nValue = nValue;
        this.dir = dir;
    }

}