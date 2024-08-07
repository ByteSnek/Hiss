package xyz.snaker.jsnake.logger;

/**
 * Created by SnakerBone on 4/11/2023
 **/
public class LogMarker
{
    public static final LogMarker INFO = new LogMarker("INFO");
    public static final LogMarker DEBUG = new LogMarker("DEBUG");
    public static final LogMarker WARN = new LogMarker("WARN");
    public static final LogMarker ERROR = new LogMarker("ERROR");

    private final String value;

    public LogMarker(String value)
    {
        this.value = value.toUpperCase();
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}
