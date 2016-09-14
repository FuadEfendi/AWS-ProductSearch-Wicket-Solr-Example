package ca.tokenizer.wicket.panels.searchresults;

import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.shareasale.model.Product;
import ca.tokenizer.util.SolrUtils;

public class ProductsDataProvider implements IDataProvider<Product>
{

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ProductsDataProvider.class);

    SolrQuery solrQuery;

    public ProductsDataProvider(final SolrQuery solrQuery)
    {
        this.solrQuery = solrQuery;
    }

    @Override
    public long size()
    {
        QueryResponse queryResponse;
        try
        {
            queryResponse = SolrUtils.getSolrServer().query(solrQuery);
        }
        catch (final SolrServerException e)
        {
            LOG.error("", e);
            return 0;
        }

        return queryResponse.getResults().getNumFound() > 1000L ? 1000L : queryResponse.getResults().getNumFound();
    }

    @Override
    public IModel<Product> model(final Product object)
    {
        return new Model<Product>(object);
    }

    /**
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    @Override
    public void detach()
    {
    }

    /**
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long,
     *      long)
     */
    @Override
    public Iterator<? extends Product> iterator(final long first, final long count)
    {

        if (first >= 0L)
        {
            solrQuery.setStart((int) first);
        }
        else
        {
            solrQuery.setStart(0);
        }

        solrQuery.setRows((int) count);

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

        return results.iterator();

    }
}
