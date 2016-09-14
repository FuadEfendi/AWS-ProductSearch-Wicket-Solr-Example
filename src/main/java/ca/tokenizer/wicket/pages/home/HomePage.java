package ca.tokenizer.wicket.pages.home;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.INamedParameters.NamedPair;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.IMyService;
import ca.tokenizer.config.IConfig;
import ca.tokenizer.util.SolrUtils;
import ca.tokenizer.wicket.pages.BasePage;
import ca.tokenizer.wicket.panels.breadcrumbs.Breadcrumbs;
import ca.tokenizer.wicket.panels.left_side_panel.LeftSidePanel;
import ca.tokenizer.wicket.panels.navigation.top.NavigationPeersPanel;
import ca.tokenizer.wicket.panels.searchresults.SearchResultsPanel;

import com.google.inject.Inject;

public class HomePage extends BasePage
{

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(HomePage.class);

    private final String[] facetFields = new String[] {
            "merchantCategory",
            "merchantSubcategory",
            "custom3"
    };

    @Inject
    private IMyService service;

    @Inject
    private IConfig config;

    static int i = 0;

    public HomePage(final PageParameters parameters)
    {
        super(parameters);
        i++;
        LOG.warn(i + "\t" + "new instance created...");

        final NavigationPeersPanel peersPanel = new NavigationPeersPanel("navigation_header", parameters);

        add(peersPanel);

        final LeftSidePanel left_side_panel = new LeftSidePanel("left_side_panel", parameters, getSolrQuery(parameters));

        // chip.cool
        //final NavigationChildPanel left_side_panel = new NavigationChildPanel("left_side_panel", parameters);

        add(left_side_panel);

        Panel landingPageContentContainer;

        if (parameters.getIndexedCount() == 3)
        {
            //landingPageContentContainer = new SearchResultsPanel("landingPageContentContainer",
            //        getSolrQueryProducts(parameters), 100, parameters);
            final Breadcrumbs b = new Breadcrumbs("breadcrumbs", parameters, getCount(parameters));
            add(b);
        }

        else
        {
            //landingPageContentContainer = new Custom3("landingPageContentContainer", parameters);
            final Breadcrumbs b = new Breadcrumbs("breadcrumbs", parameters, getCount(parameters));
            add(b);

        }

        landingPageContentContainer = new SearchResultsPanel("landingPageContentContainer",
                getSolrQueryProducts(parameters), 10, parameters);

        add(landingPageContentContainer);

        //final SearchResultsPanel p = new SearchResultsPanel("search_results", getSolrQuery2(parameters), 1);
        //add(p);

        String title = "SHOP";

        for (int i = parameters.getIndexedCount() - 1; i >= 0; i--)
        {
            title = title + " - " + parameters.get(i);
        }

        for (final NamedPair pair : parameters.getAllNamed())
        {
            if ("q".equals(pair.getKey()))
            {
                continue;
            }
            title = title + " - " + pair.getKey() + " " + pair.getValue();
        }

        final SearchFormBean searchFormBean = new SearchFormBean();

        final SearchForm f = new SearchForm("form", new CompoundPropertyModel<SearchFormBean>(searchFormBean),
                parameters);
        add(f);

        add(new Label("query2", searchFormBean.getQuery()));

        if (!StringUtils.isBlank(searchFormBean.getQuery()))
        {
            title = title + " - " + searchFormBean.getQuery();
        }
        title = title + " - www.bambarbia.com";

        pageTitle.setObject(title);

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

        if (parameters.getIndexedCount() == 0)
        {
            solrQuery.addFacetField(facetFields[0]);
        }
        else if (parameters.getIndexedCount() == 1)
        {
            solrQuery.addFilterQuery(facetFields[0] + ":\"" + parameters.get(0).toString() + "\"");
            solrQuery.addFacetField(facetFields[1]);
        }
        else
        {
            solrQuery.addFilterQuery(facetFields[0] + ":\"" + parameters.get(0).toString() + "\"");
            solrQuery.addFilterQuery(facetFields[1] + ":\"" + parameters.get(1).toString() + "\"");
            solrQuery.addFacetField(facetFields[2]);
        }

        return solrQuery;

    }

    private SolrQuery getSolrQueryProducts(final PageParameters parameters)
    {

        final SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        // solrQuery.setStart(0);
        // solrQuery.setRows(0);
        // solrQuery.setFacet(true);
        // solrQuery.setFacetSort(FacetParams.FACET_SORT_INDEX);

        // solrQuery.setFacetLimit(Integer.MAX_VALUE);
        // solrQuery.setFacetMinCount(1);

        if (parameters.getIndexedCount() == 0)
        {
            //    solrQuery.addFacetField(facetFields[0]);
        }
        else if (parameters.getIndexedCount() == 1)
        {
            solrQuery.addFilterQuery(facetFields[0] + ":\"" + parameters.get(0).toString() + "\"");
            //  solrQuery.addFacetField(facetFields[1]);
        }
        else if (parameters.getIndexedCount() == 2)
        {
            solrQuery.addFilterQuery(facetFields[0] + ":\"" + parameters.get(0).toString() + "\"");
            solrQuery.addFilterQuery(facetFields[1] + ":\"" + parameters.get(1).toString() + "\"");
            //solrQuery.addFacetField(facetFields[2]);
        }
        else
        {
            solrQuery.addFilterQuery(facetFields[0] + ":\"" + parameters.get(0).toString() + "\"");
            solrQuery.addFilterQuery(facetFields[1] + ":\"" + parameters.get(1).toString() + "\"");
            solrQuery.addFilterQuery("custom3:\"" + parameters.get(2).toString() + "\"");
            //solrQuery.addFacetField(facetFields[2]);
        }

        return solrQuery;

    }

    private SolrQuery getSolrQueryProductsEZSystems(final PageParameters parameters)
    {

        final SolrQuery solrQuery = new SolrQuery();

        /*
        for (int i = 0; i < parameters.getIndexedCount(); i++)
        {
            final String[] terms = parameters.get(i).toString().replaceAll("&", " ").split("\\s+");

            query = query + " (";
            for (final String term : terms)
            {
                query = query + " OR " + term;
            }
            query = query + ") AND ";

        }
        */

        String q = "";
        if ((parameters.get("q") != null) && !parameters.get("q").isEmpty())
        {
            q = parameters.get("q").toString();
            q = "{!q.op=AND}" + q;
        }

        if (parameters.getIndexedCount() > 0)
        {
            q = q + " " + parameters.get(parameters.getIndexedCount() - 1).toString();
        }

        solrQuery.setQuery(q);

        return solrQuery;
    }

    private int getCount(final PageParameters parameters)
    {

        final SolrQuery q = getSolrQueryProducts(parameters);
        q.setRows(0);
        QueryResponse queryResponse;
        try
        {
            queryResponse = SolrUtils.getSolrServer().query(q);
        }
        catch (final SolrServerException e)
        {
            LOG.error("", e);
            return 0;
        }

        return (int) queryResponse.getResults().getNumFound();

    }

    private Set<String> getPeers(final PageParameters parameters)
    {

        final SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setStart(0);
        solrQuery.setRows(0);
        solrQuery.setFacet(true);
        solrQuery.setFacetSort(FacetParams.FACET_SORT_INDEX);
        solrQuery.setFacetLimit(Integer.MAX_VALUE);
        solrQuery.setFacetMinCount(1);

        String facetFieldName = null;

        /*
        if ((parameters == null) || (parameters.getIndexedCount() == 0))
        {
            facetFieldName = "merchantCategory";
            solrQuery.addFacetField(facetFieldName);
        }
        else if (parameters.getIndexedCount() == 1)
        {
            solrQuery.addFilterQuery("merchantCategory:\"" + parameters.get(0).toString() + "\"");
            facetFieldName = "merchantSubcategory";
            solrQuery.addFacetField(facetFieldName);
        }
        else if (parameters.getIndexedCount() >= 2)
        {
            solrQuery.addFilterQuery("merchantCategory:\"" + parameters.get(0).toString() + "\"");
            solrQuery.addFilterQuery("merchantSubcategory:\"" + parameters.get(1).toString() + "\"");
            facetFieldName = "custom3";
            solrQuery.addFacetField(facetFieldName);
        }
        */
        facetFieldName = "merchantCategory";
        solrQuery.addFacetField(facetFieldName);

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

        final Set<String> entries = new TreeSet<>();

        final FacetField facetField = queryResponse.getFacetField(facetFieldName);

        final List<Count> counts = facetField.getValues();

        for (final Count c : counts)
        {
            entries.add(c.getName());
        }

        return entries;

    }

}
