package ca.tokenizer;

import java.util.Arrays;
import java.util.TimeZone;

public class Test
{
    public static void main(final String[] args)
    {
        System.out.println(TimeZone.getTimeZone("Australia/Sydney").getID());
        System.out.println(TimeZone.getTimeZone("UTC").getID());

        System.out.println(Arrays.asList(TimeZone.getAvailableIDs()).contains("Australia/Sydney"));

    }
}
