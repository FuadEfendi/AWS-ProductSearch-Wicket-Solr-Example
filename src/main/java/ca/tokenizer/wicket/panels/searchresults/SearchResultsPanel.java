package ca.tokenizer.wicket.panels.searchresults;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.string.StringValue;

import ca.tokenizer.shareasale.model.Product;
import ca.tokenizer.wicket.StaticImage;

public class SearchResultsPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    public SearchResultsPanel(final String id, final SolrQuery solrQuery, final int rows,
            final PageParameters parameters)
    {
        super(id);

        final PackageResourceReference spacer =
                new PackageResourceReference(getClass(), "spacer.gif");

        final DataView<Product> dataView = new DataView<Product>("list_of_products",
                new ProductsDataProvider(solrQuery), rows)
        {
            private static final long serialVersionUID = 1L;

            int i = 0;

            @Override
            protected void populateItem(final Item<Product> item)
            {
                i++;

                final Product p = item.getModelObject();

                item.add(new Image("spacer1", spacer));
                item.add(new Image("spacer2", spacer));

                // price:
                String price;
                if (StringUtils.isBlank(p.getPrice()))
                {
                    price = p.getRetailPrice();
                }
                else
                {
                    price = p.getPrice();
                }
                final Label priceLabel = new Label("price", price);
                item.add(priceLabel);

                final Label productName0 = new Label("productName0", p.getProductName());
                item.add(productName0);

                final Label manufacturer = new Label("manufacturer", p.getManufacturer());
                item.add(manufacturer);

                final Label description = new Label("description", p.getDescription());
                item.add(description);

                final ExternalLink linkToProduct0 = new ExternalLink("linkToProduct0", p.getLinkToProduct()
                        .toExternalForm());
                item.add(linkToProduct0);

                final Label productName1 = new Label("productName1", p.getProductName());
                linkToProduct0.add(productName1);

                final Label customCategory = new Label("customCategory", p.getMerchantSubcategory());
                item.add(customCategory);

                final Label upc = new Label("sku", p.getSku());
                item.add(upc);

                final ExternalLink linkToProduct1 = new ExternalLink(
                        "linkToProduct1",
                        p.getLinkToProduct().toExternalForm(),
                        p.getProductName()) {
                    @Override
                    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
                    {
                        // Bug in Wicket; workaround:
                        renderAll(markupStream, openTag);
                    }

                };

                if (p.getLinkToThumbnail() != null)
                {
                    linkToProduct1
                            .add(new StaticImage("linkToImage", new Model<String>(p.getLinkToThumbnail()
                                    .toExternalForm())));
                }
                else
                {
                    linkToProduct1
                            .add(new Image("linkToImage",
                                    new PackageResourceReference(SearchResultsPanel.class, "no_image.jpg")));
                }

                item.add(linkToProduct1);

            }
        };

        final StringValue s = parameters.get("page");
        if ((s != null) && !s.isEmpty())
        {
            dataView.setCurrentPage(s.toInt() - 1);
        }

        add(dataView);

        add(new MyPagingNavigator("navigator", dataView, parameters));
        //add(new PagingNavigator("navigator", dataView));

    }
}
