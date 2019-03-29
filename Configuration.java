import javafx.scene.control.Label;

public class Configuration
{
    Label leftConfig;
    Label midConfig;
    Label rightConfig;
    String midFormat;
    int configNumber;

    public Configuration(String l, String m, String r, int configNumber)
    {
        leftConfig = new Label(l);
        midConfig = new Label(m);
        rightConfig = new Label(r);
        midFormat = "";
        this.configNumber = configNumber;
    }

    // Setters
    public void setConfigNumber(int newConfigNumber)
    {
        configNumber = newConfigNumber;
    }

    // Getters
    public int getConfigNumber()
    {
        return configNumber;
    }
    public Label getLeftConfig()
    {
        return leftConfig;
    }
    public Label getMidConfig()
    {
        return midConfig;
    }
    public Label getRightConfig()
    {
        return rightConfig;
    }

    public void incrementConfigNumber()
    {
        configNumber++;
    }

    public void changeStyle(String style)
    {
        midFormat += style;
        leftConfig.setStyle(style);
        midConfig.setStyle(midFormat);
        rightConfig.setStyle(style);
    }

    public void changeHeadStyle(String style)
    {
        midFormat += style;
        midConfig.setStyle(midFormat);
    }

    public void appendLeft(String tape)
    {
        leftConfig.setText(leftConfig.getText() + tape);
    }

    public void appendMid(String tape)
    {
        midConfig.setText(tape);
    }   

    public void appendRight(String tape)
    {
        rightConfig.setText(rightConfig.getText() + tape);
    }

    public void clear()
    {
        leftConfig.setText("");
        midConfig.setText("");
        rightConfig.setText("");
    }

    public Label[] getConfig()
    {
        return new Label[] {leftConfig, midConfig, rightConfig};
    }

    public String toString()
    {
        return String.format("%d: %s %s %s", configNumber, leftConfig.getText(), midConfig.getText(), rightConfig.getText());
    }
}