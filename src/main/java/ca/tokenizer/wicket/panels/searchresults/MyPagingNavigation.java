package ca.tokenizer.wicket.panels.searchresults;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ca.tokenizer.wicket.pages.home.HomePage;

public class MyPagingNavigation extends PagingNavigation
{

    private static final long serialVersionUID = 1L;

    final PageParameters parameters;

    public MyPagingNavigation(final String id, final IPageable pageable, final PageParameters parameters)
    {
        super(id, pageable);
        this.parameters = parameters;
    }

    public MyPagingNavigation(final String id, final IPageable pageable,
            final IPagingLabelProvider labelProvider, final PageParameters parameters)
    {
        super(id, pageable, labelProvider);
        this.parameters = parameters;
    }

    @Override
    protected AbstractLink newPagingNavigationLink(final String id, final IPageable pageable, final long pageIndex)
    {
        final PageParameters p = new PageParameters(parameters);
        p.remove("page");
        p.add("page", pageIndex + 1);
        final BookmarkablePageLink<HomePage> link = new BookmarkablePageLink(id, HomePage.class, p);
        return link;

    }

}
