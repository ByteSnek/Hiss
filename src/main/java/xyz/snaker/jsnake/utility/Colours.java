package xyz.snaker.jsnake.utility;

import xyz.snaker.jsnake.math.Maths;
import xyz.snaker.jsnake.sneaky.Sneaky;

import java.awt.*;
import java.util.Random;

/**
 * Created by SnakerBone on 15/08/2023
 **/
public class Colours
{
    /**
     * Converts a hexidecimal to an integer
     *
     * @param hexCode The hexidecimal to convert
     * @return The hexidecimal as an integer
     **/
    public static int hexToInt(String hexCode)
    {
        return Integer.parseInt(strip(hexCode), 16);
    }

    /**
     * Converts a hexidecimal to a float
     *
     * @param hexCode The hexidecimal to convert
     * @return The hexidecimal as a float
     **/
    public static float hexToFloat(String hexCode)
    {
        return Float.parseFloat(strip(hexCode));
    }

    /**
     * Generates a random hexidecimal
     *
     * @return A random hexidecimal as an integer
     **/
    public static int randomHex()
    {
        Random random = new Random();
        return random.nextInt(0xffffff + 1);
    }

    public static Color middleColour(String first, String second)
    {
        int colourA = Integer.parseInt(strip(first), 16);
        int colourB = Integer.parseInt(strip(second), 16);

        int redA = (colourA >> 16) & 0xFF;
        int greenA = (colourA >> 8) & 0xFF;
        int blueA = colourA & 0xFF;

        int redB = (colourB >> 16) & 0xFF;
        int greenB = (colourB >> 8) & 0xFF;
        int blueB = colourB & 0xFF;

        int red = (redA + redB) / 2;
        int green = (greenA + greenB) / 2;
        int blue = (blueA + blueB) / 2;

        return new Color(red, green, blue);
    }

    public static String middleColourHex(String first, String second)
    {
        int colourA = Integer.parseInt(strip(first), 16);
        int colourB = Integer.parseInt(strip(second), 16);

        int redA = (colourA >> 16) & 0xFF;
        int greenA = (colourA >> 8) & 0xFF;
        int blueA = colourA & 0xFF;

        int redB = (colourB >> 16) & 0xFF;
        int greenB = (colourB >> 8) & 0xFF;
        int blueB = colourB & 0xFF;

        int red = (redA + redB) / 2;
        int green = (greenA + greenB) / 2;
        int blue = (blueA + blueB) / 2;

        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public static int hsvToRGB(float hueColour, float saturationColour, float vibranceColour)
    {
        float normal = hueColour - Maths.floor(hueColour);

        int normal6 = Sneaky.cast(normal * 6);

        float shift = normal * 6 - normal6;

        float hue = vibranceColour * (1 - saturationColour);
        float sat = vibranceColour * (1 - shift * saturationColour);
        float val = vibranceColour * (1 - (1 - shift) * saturationColour);

        switch (normal6) {
            case 0: {
                return fastRGB(vibranceColour, val, hue);
            }
            case 1: {
                return fastRGB(sat, vibranceColour, hue);
            }
            case 2: {
                return fastRGB(hue, vibranceColour, val);
            }
            case 3: {
                return fastRGB(hue, sat, vibranceColour);
            }
            case 4: {
                return fastRGB(val, hue, vibranceColour);
            }
            case 5: {
                return fastRGB(vibranceColour, hue, sat);
            }
            default: {
                throw new RuntimeException();
            }
        }
    }

    public static int fastRGB(float red, float green, float blue)
    {
        int r = (int) (red * 255F);
        int g = (int) (green * 255F);
        int b = (int) (blue * 255F);

        int r0 = (int) (r + 0.5F);
        int g0 = (int) (g + 0.5F);
        int b0 = (int) (b + 0.5F);

        int r1 = r0 & 0xFF;
        int g1 = g0 & 0xFF;
        int b1 = b0 & 0xFF;

        return r1 << 16 | g1 << 8 | b1;
    }

    static String strip(String hexCode)
    {
        return hexCode.replaceAll("#", "");
    }
}
