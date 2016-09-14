package ca.tokenizer.wicket.panels.menu;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.util.NamedList;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.util.SolrUtils;
import ca.tokenizer.wicket.pages.home.HomePage;

public class HeaderNavContainer extends Panel
{

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(HeaderNavContainer.class);

    public HeaderNavContainer(final String id)
    {
        super(id);

        final SolrQuery solrQuery = getSolrQuery();

        final Map<String, Collection<String>> categories = getCategories(solrQuery);

        final RepeatingView categoriesView = new RepeatingView("categoriesView");
        add(categoriesView);

        for (final String category : categories.keySet())
        {

            //LOG.debug("category: {}", category);

            final AbstractItem item = new AbstractItem(category);
            categoriesView.add(item);

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(0, category);

            final BookmarkablePageLink<HomePage> categoryLink = new BookmarkablePageLink<>("categoryLink",
                    HomePage.class,
                    pageParameters);

            final Label categoryLabel = new Label("categoryLabel", category);
            categoryLink.add(categoryLabel);

            item.add(categoryLink);

            final RepeatingView subcategoriesView = new RepeatingView("subcategoriesView");
            item.add(subcategoriesView);

            for (final String subcategory : categories.get(category))
            {
                //LOG.debug("subcategory: {}", subcategory);

                if (StringUtils.isBlank(subcategory))
                {
                    continue;
                }
                final AbstractItem item2 = new AbstractItem(subcategory);
                subcategoriesView.add(item2);

                final PageParameters pageParametersSubcategory = new PageParameters();
                pageParametersSubcategory.set(0, category);
                pageParametersSubcategory.set(1, subcategory);

                final BookmarkablePageLink<HomePage> subcategoryLink = new BookmarkablePageLink<>("subcategoryLink2",
                        HomePage.class,
                        pageParametersSubcategory);

                item2.add(subcategoryLink);

                final Label subcategoryLabel = new Label("subcategoryLabel", subcategory);
                subcategoryLink.add(subcategoryLabel);

            }

        }

    }

    private SolrQuery getSolrQuery()
    {

        final SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setStart(0);
        solrQuery.setRows(0);
        solrQuery.setFacet(true);
        solrQuery.setFacetSort(FacetParams.FACET_SORT_INDEX);

        solrQuery.setFacetLimit(Integer.MAX_VALUE);
        solrQuery.setFacetMinCount(1);

        solrQuery.addFacetPivotField("merchantCategory,merchantSubcategory");

        return solrQuery;

    }

    private Map<String, Collection<String>> getCategories(final SolrQuery solrQuery)
    {

        final Map<String, Collection<String>> categories = new TreeMap<>();

        QueryResponse queryResponse;
        try
        {
            queryResponse = SolrUtils.getSolrServer().query(solrQuery);
        }
        catch (final SolrServerException e)
        {
            LOG.error("", e);
            return categories;
        }

        final NamedList<List<PivotField>> pivots = queryResponse.getFacetPivot();

        for (final PivotField f : pivots.get("merchantCategory,merchantSubcategory"))
        {
            final Collection<String> entries = new TreeSet<>();
            categories.put((String) f.getValue(), entries);
            for (final PivotField f2 : f.getPivot())
            {
                entries.add((String) f2.getValue());
            }
        }

        return categories;

    }

    @Override
    public void renderHead(final IHeaderResponse response)
    {
        super.renderHead(response);
        if (!isEnabledInHierarchy())
        {
            return;
        }

        //add bundled JQuery
        final IJavaScriptLibrarySettings javaScriptSettings =
                getApplication().getJavaScriptLibrarySettings();
        response.render(JavaScriptHeaderItem.
                forReference(javaScriptSettings.getJQueryReference()));

        //add package resources
        response.render(JavaScriptHeaderItem.
                forReference(new PackageResourceReference(getClass(), "jquery-ui-1.11.1/jquery-ui.min.js")));
        response.render(JavaScriptHeaderItem.
                forReference(new PackageResourceReference(getClass(), "jquery-ui-1.11.1/jquery-ui-i18n.min.js")));
        //response.render(CssHeaderItem.
        //       forReference(new PackageResourceReference(getClass(), "jquery-ui-1.11.1/jquery-ui.css")));

    }

}
