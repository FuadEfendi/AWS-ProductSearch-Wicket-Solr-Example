package ca.tokenizer.wicket.panels.departments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.shareasale.model.Product;
import ca.tokenizer.util.SolrUtils;
import ca.tokenizer.wicket.StaticImage;
import ca.tokenizer.wicket.pages.home.HomePage;
import ca.tokenizer.wicket.panels.category.Entry;

public class Custom3 extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(Custom3.class);

    public Custom3(final String id, final PageParameters parameters)
    {
        super(id);

        final SolrQuery solrQuery = getSolrQuery(parameters);

        final List<Entry> entries = getFacets(solrQuery);

        final RepeatingView repeating = new RepeatingView("custom3_view");
        add(repeating);

        for (final Entry e : entries)
        {

            if (StringUtils.isBlank(e.getName()))
            {
                continue;
            }

            final Product product = getProduct(parameters, e.getName());

            final String name = (e.getName() == null) || e.getName().isEmpty() ? "Other" : e.getName();

            final AbstractItem item = new AbstractItem(name);
            repeating.add(item);

            final PageParameters pageParameters = new PageParameters();

            int i = 0;
            for (; i < parameters.getIndexedCount(); i++)
            {
                pageParameters.set(i, parameters.get(i));
            }

            pageParameters.set(i, name);

            final BookmarkablePageLink<HomePage> link = new BookmarkablePageLink<>("link", HomePage.class,
                    pageParameters);

            final Label l = new Label("text", name + " (" + e.getCount() + ") ");
            link.add(l);

            final BookmarkablePageLink<HomePage> link2 = new BookmarkablePageLink<>("link2", HomePage.class,
                    pageParameters);

            item.add(link);

            item.add(link2);

            link2.add(new StaticImage("deptImageLink", new Model<String>(product.getLinkToImage().toExternalForm())));

        }

    }

    private List<Entry> getFacets(final SolrQuery solrQuery)
    {

        QueryResponse queryResponse;
        try
        {
            queryResponse = SolrUtils.getSolrServer().query(solrQuery);
        }
        catch (final SolrServerException e)
        {
            LOG.error("", e);
            return null;
        }

        final List<Entry> entries = new ArrayList<>();

        final FacetField facetField = queryResponse.getFacetField("custom3");

        final List<Count> counts = facetField.getValues();

        for (final Count c : counts)
        {
            entries.add(new Entry(c.getName(), c.getCount()));
        }

        return entries;

    }

    private Product getProduct(final PageParameters parameters, final String custom3)
    {

        final SolrQuery solrQuery = getSolrQuery(parameters, custom3);

        QueryResponse queryResponse;
        try
        {
            queryResponse = SolrUtils.getSolrServer().query(solrQuery);
        }
        catch (final SolrServerException e)
        {
            LOG.error("", e);
            return null;
        }

        final List<Product> results = queryResponse.getBeans(Product.class);

        return results.get(0);

    }

    private SolrQuery getSolrQuery(final PageParameters parameters)
    {

        final SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setStart(0);
        solrQuery.setRows(0);
        solrQuery.setFacet(true);
        solrQuery.setFacetSort(FacetParams.FACET_SORT_INDEX);
        solrQuery.setFacetLimit(Integer.MAX_VALUE);
        solrQuery.setFacetMinCount(1);

        solrQuery.addFilterQuery("merchantCategory:\"" + parameters.get(0).toString() + "\"");
        solrQuery.addFilterQuery("merchantSubcategory:\"" + parameters.get(1).toString() + "\"");
        solrQuery.addFacetField("custom3");

        return solrQuery;

    }

    private SolrQuery getSolrQuery(final PageParameters parameters, final String custom3)
    {

        final SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setStart(0);
        solrQuery.setRows(1);

        solrQuery.addFilterQuery("merchantCategory:\"" + parameters.get(0).toString() + "\"");
        solrQuery.addFilterQuery("merchantSubcategory:\"" + parameters.get(1).toString() + "\"");
        solrQuery.addFilterQuery("custom3:\"" + custom3 + "\"");

        return solrQuery;

    }

}
