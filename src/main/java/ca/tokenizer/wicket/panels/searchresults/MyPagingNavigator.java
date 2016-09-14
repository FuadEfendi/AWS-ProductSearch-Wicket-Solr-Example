package ca.tokenizer.wicket.panels.searchresults;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ca.tokenizer.wicket.pages.home.HomePage;

public class MyPagingNavigator extends Panel
{

    private static final long serialVersionUID = 1L;

    private PagingNavigation pagingNavigation;

    private final IPageable pageable;

    private final IPagingLabelProvider labelProvider;

    private final PageParameters parameters;

    public MyPagingNavigator(final String id, final IPageable pageable, final PageParameters parameters)
    {
        this(id, pageable, null, parameters);
    }

    public MyPagingNavigator(final String id, final IPageable pageable,
            final IPagingLabelProvider labelProvider, final PageParameters parameters)
    {
        super(id);
        this.pageable = pageable;
        this.labelProvider = labelProvider;
        this.parameters = parameters;
    }

    public final IPageable getPageable()
    {
        return pageable;
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        // Get the navigation bar and add it to the hierarchy
        pagingNavigation = newNavigation("navigation", pageable, labelProvider);
        add(pagingNavigation);

        // Add additional page links
        add(newPagingNavigationLink("first", pageable, 0).add(
                new TitleAppender("PagingNavigator.first")));
        add(newPagingNavigationIncrementLink("prev", pageable, -1).add(
                new TitleAppender("PagingNavigator.previous")));
        add(newPagingNavigationIncrementLink("next", pageable, 1).add(
                new TitleAppender("PagingNavigator.next")));

        // BUG FIX: 
        add(newPagingNavigationLink("last", pageable, (int) pageable.getPageCount() - 1).add(
                new TitleAppender("PagingNavigator.last")));
    }

    protected AbstractLink newPagingNavigationIncrementLink(final String id, final IPageable pageable,
            final int increment)
    {

        final long pageIndex = getPageNumber(increment);
        final PageParameters p = new PageParameters(parameters);
        p.remove("page");
        p.add("page", pageIndex + 1);
        final BookmarkablePageLink<HomePage> link = new BookmarkablePageLink(id, HomePage.class, p);

        return link;
    }

    protected AbstractLink newPagingNavigationLink(final String id, final IPageable pageable, final int pageNumber)
    {
        final PageParameters p = new PageParameters(parameters);
        p.remove("page");
        p.add("page", pageNumber + 1);
        final BookmarkablePageLink<HomePage> link = new BookmarkablePageLink(id, HomePage.class, p);

        return link;
    }

    protected PagingNavigation newNavigation(final String id, final IPageable pageable,
            final IPagingLabelProvider labelProvider)
    {
        return new MyPagingNavigation(id, pageable, labelProvider, parameters);
    }

    public final PagingNavigation getPagingNavigation()
    {
        return pagingNavigation;
    }

    public final long getPageNumber(final int increment)
    {
        final long idx = getPageable().getCurrentPage() + increment;
        return Math.max(0, Math.min(getPageable().getPageCount() - 1, idx));
    }

    /**
     * Appends title attribute to navigation links
     * 
     * @author igor.vaynberg
     */
    private final class TitleAppender extends Behavior
    {
        private static final long serialVersionUID = 1L;

        private final String resourceKey;

        /**
         * Constructor
         * 
         * @param resourceKey
         *            resource key of the message
         */
        public TitleAppender(final String resourceKey)
        {
            this.resourceKey = resourceKey;
        }

        /** {@inheritDoc} */
        @Override
        public void onComponentTag(final Component component, final ComponentTag tag)
        {
            tag.put("title", MyPagingNavigator.this.getString(resourceKey));
        }
    }

}
