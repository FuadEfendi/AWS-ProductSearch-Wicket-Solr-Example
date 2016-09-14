package ca.tokenizer.wicket.panels.searchresults;

import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class MyPagingLabelProvider implements IPagingLabelProvider
{

    private static final long serialVersionUID = 1L;

    /**
     * @see org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider#getPageLabel(long)
     */
    @Override
    public String getPageLabel(final long page)
    {
        return String.valueOf(page);
    }

}
