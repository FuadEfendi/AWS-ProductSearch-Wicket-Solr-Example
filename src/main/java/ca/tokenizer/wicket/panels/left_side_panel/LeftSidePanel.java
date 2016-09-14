package ca.tokenizer.wicket.panels.left_side_panel;

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

public class LeftSidePanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(LeftSidePanel.class);

    public LeftSidePanel(final String id, final PageParameters parameters, final SolrQuery solrQuery)
    {
        super(id);

        final List<Entry> entries = getFacets(parameters, solrQuery);

        final RepeatingView repeater = new RepeatingView("repeater");
        add(repeater);

        int k = 0;
        for (final Entry e : entries)
        {

            final AbstractItem item = new AbstractItem(k++);
            repeater.add(item);

            final PageParameters p = new PageParameters();

            int i = 0;
            for (; (i < parameters.getIndexedCount()) && (i < 2); i++)
            {
                p.set(i, parameters.get(i));
            }
            p.set(i, e.getName());

            final BookmarkablePageLink<HomePage> link = new BookmarkablePageLink<>("link", HomePage.class,
                    p);

            final Label l = new Label("anchor", e.getName());
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

        String field = null;
        if (parameters.getIndexedCount() == 0)
        {
            field = "merchantCategory";
        }
        else if (parameters.getIndexedCount() == 1)
        {
            field = "merchantSubcategory";
        }
        else if (parameters.getIndexedCount() == 2)
        {
            field = "custom3";
        }
        else
        {
            field = "custom3";
        }

        final FacetField facetField = queryResponse.getFacetField(field);

        final List<Count> counts = facetField.getValues();

        for (final Count c : counts)
        {
            entries.add(new Entry(c.getName(), c.getCount()));
        }

        return entries;

    }

}
