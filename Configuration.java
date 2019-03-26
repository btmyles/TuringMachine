import javafx.scene.control.Label;

public class Configuration
{
    Label leftConfig;
    Label midConfig;
    Label rightConfig;
    String midFormat;

    public Configuration(String l, String m, String r)
    {
        leftConfig = new Label(l);
        midConfig = new Label(m);
        rightConfig = new Label(r);
        midFormat = "";
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

    public Label[] getConfig()
    {
        return new Label[] {leftConfig, midConfig, rightConfig};
    }
}