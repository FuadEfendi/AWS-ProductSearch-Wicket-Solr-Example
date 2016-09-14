package ca.tokenizer.wicket.panels.facets;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.util.SolrUtils;
import ca.tokenizer.wicket.pages.home.HomePage;
import ca.tokenizer.wicket.panels.category.Entry;

public class Facets extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(Facets.class);

    private final String[] facetFields = new String[] {
            "merchantCategory",
            "merchantSubcategory",
            "manufacturer"
    };

    public Facets(final String id, final PageParameters parameters, final SolrQuery solrQuery)
    {
        super(id);

        LOG.debug("parameter0: {}", parameters.get(0));

        final String selectedMerchantCategory = parameters.isEmpty() ? null : parameters.get(0).toString();

        final List<Entry> entries = getFacets(parameters, solrQuery);

        final RepeatingView repeating = new RepeatingView("repeating");
        add(repeating);

        for (final Entry e : entries)
        {

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

            item.add(link);

        }

    }

    private List<Entry> getFacets(final PageParameters parameters, final SolrQuery solrQuery)
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

        if (parameters.getIndexedCount() == 3)
        {
            return entries;
        }
        final FacetField facetField = queryResponse.getFacetField(facetFields[parameters.getIndexedCount()]);

        final List<Count> counts = facetField.getValues();

        for (final Count c : counts)
        {
            entries.add(new Entry(c.getName(), c.getCount()));
        }

        return entries;

    }

}
