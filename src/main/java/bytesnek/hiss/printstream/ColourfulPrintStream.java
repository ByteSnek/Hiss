package bytesnek.hiss.printstream;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import bytesnek.hiss.logger.LogColour;

/**
 * Created by SnakerBone on 5/11/2023
 **/
public class ColourfulPrintStream extends PrintStream
{
    private final String reset = LogColour.Style.RESET.value();
    private final String styles;

    public ColourfulPrintStream(LogColour.Style... styles)
    {
        super(System.out);
        this.styles = Arrays.stream(styles).map(LogColour.Style::value).collect(Collectors.joining(""));
    }

    @Override
    public void print(String string)
    {
        if (string == null) {
            string = "null";
        }

        for (char character : string.toCharArray()) {
            String colouredCharacter = styles + applyColour(character);
            super.print(colouredCharacter);
        }
    }

    public String applyColour(char character)
    {
        Random random = new Random();
        int colour = random.nextInt(216) + 16;

        return "\u001b[38;5;" + colour + "m" + character + reset;
    }
}
