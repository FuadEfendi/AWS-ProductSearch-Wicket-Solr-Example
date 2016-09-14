package ca.tokenizer.wicket.panels.breadcrumbs;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.wicket.pages.home.HomePage;

public class Breadcrumbs extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(Breadcrumbs.class);

    public Breadcrumbs(final String id, final PageParameters parameters, final int numFound)
    {
        super(id);

        Fragment fragment;

        if (parameters.getIndexedCount() == 0)
        {
            fragment = new Fragment("placeholder", "fragment0", this);
        }
        else if (parameters.getIndexedCount() == 1)
        {
            fragment = new Fragment("placeholder", "fragment1", this);
        }
        else if (parameters.getIndexedCount() == 2)
        {
            fragment = new Fragment("placeholder", "fragment2", this);
        }
        else if (parameters.getIndexedCount() == 3)
        {
            fragment = new Fragment("placeholder", "fragment3", this);
        }
        else
        {
            //throw new RuntimeException("Not Implemented!");
            fragment = new Fragment("placeholder", "fragment3", this);
        }

        add(fragment);

        if (parameters.getIndexedCount() >= 1)
        {
            final PageParameters p1 = new PageParameters();
            p1.set(0, parameters.get(0));
            final BookmarkablePageLink<HomePage> categoryLink = new BookmarkablePageLink<>("categoryLink",
                    HomePage.class,
                    p1);
            final Label categoryLabel = new Label("categoryLabel", parameters.get(0));
            categoryLink.add(categoryLabel);
            fragment.add(categoryLink);
        }

        if (parameters.getIndexedCount() >= 2)
        {
            final PageParameters p2 = new PageParameters();
            p2.set(0, parameters.get(0));
            p2.set(1, parameters.get(1));
            final BookmarkablePageLink<HomePage> subcategoryLink = new BookmarkablePageLink<>("subcategoryLink",
                    HomePage.class,
                    p2);
            final Label subcategoryLabel = new Label("subcategoryLabel", parameters.get(1));
            subcategoryLink.add(subcategoryLabel);
            fragment.add(subcategoryLink);
        }

        if (parameters.getIndexedCount() >= 3)
        {
            final PageParameters p3 = new PageParameters();
            p3.set(0, parameters.get(0));
            p3.set(1, parameters.get(1));
            p3.set(2, parameters.get(2));
            final BookmarkablePageLink<HomePage> custom3Link = new BookmarkablePageLink<>("custom3Link",
                    HomePage.class,
                    p3);
            final Label custom3Label = new Label("custom3Label", parameters.get(2));
            custom3Link.add(custom3Label);
            fragment.add(custom3Link);
        }

        if (numFound != 0)
        {
            final Label productCount = new Label("productCount", "(" + numFound + " items)");
            add(productCount);
        }
        else
        {
            final Label productCount = new Label("productCount", "");
            add(productCount);
        }

    }

}
