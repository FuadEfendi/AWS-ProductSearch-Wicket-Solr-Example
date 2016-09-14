package ca.tokenizer.wicket.panels.category;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EntriesProvider implements IDataProvider<Entry>
{

    private static final long serialVersionUID = 1L;

    private final List<Entry> entries;

    public EntriesProvider(final List<Entry> entries)
    {
        this.entries = entries;
    }

    /**
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long,
     *      long)
     */
    @Override
    public Iterator<? extends Entry> iterator(final long first, final long count)
    {

        return entries.listIterator((int) first);
    }

    /**
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    @Override
    public void detach()
    {
        // no-op
    }

    /**
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
     */
    @Override
    public long size()
    {
        return entries.size();
    }

    /**
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
     */
    @Override
    public IModel<Entry> model(final Entry object)
    {
        return new Model<>(object);
    }
}