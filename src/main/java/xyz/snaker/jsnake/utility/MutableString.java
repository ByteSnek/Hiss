package xyz.snaker.jsnake.utility;

/**
 * Created by SnakerBone on 13/11/2023
 **/
public class MutableString
{
    private String value;

    public MutableString()
    {
        this.value = Strings.EMPTY;
    }

    public MutableString(String value)
    {
        this.value = value;
    }

    public String get()
    {
        return value;
    }

    public boolean set(String value)
    {
        this.value = value;
        return true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (obj instanceof MutableString) {
            return value.equals(((MutableString) obj).value);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return value == null ? 0 : value.hashCode();
    }

    @Override
    public String toString()
    {
        return value == null ? "null" : value;
    }
}
