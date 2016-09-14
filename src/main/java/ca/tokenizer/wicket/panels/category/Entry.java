package ca.tokenizer.wicket.panels.category;

import java.io.Serializable;

public class Entry implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final String name;

    private final long count;

    public Entry(final String name, final long count)
    {
        super();
        this.name = name;
        this.count = count;
    }

    public String getName()
    {
        return name;
    }

    public long getCount()
    {
        return count;
    }

}
