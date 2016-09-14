package ca.tokenizer.wicket.panels.searchresults;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import ca.tokenizer.shareasale.model.Product;
import ca.tokenizer.wicket.StaticImage;

public class SearchResultsPanel_v1 extends Panel
{
    private static final long serialVersionUID = 1L;

    public SearchResultsPanel_v1(final String id, final SolrQuery solrQuery, final int rows,
            final PageParameters parameters)
    {
        super(id);

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

                final WebMarkupContainer brand = new WebMarkupContainer("brand");
                brand.add(new AttributeModifier("content", p.getManufacturer()));
                item.add(brand);

                final WebMarkupContainer description = new WebMarkupContainer("description");
                description.add(new AttributeModifier("content", p.getDescription()));
                item.add(description);

                final Label brandLabel = new Label("brandLabel", p.getManufacturer());
                item.add(brandLabel);

                final Label descriptionLabel = new Label("descriptionLabel", p.getDescription());
                item.add(descriptionLabel);

                final ExternalLink linkToProduct = new ExternalLink("linkToProduct", p.getLinkToProduct()
                        .toExternalForm(),
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
                    linkToProduct
                            .add(new StaticImage("linkToImage", new Model<String>(p.getLinkToThumbnail()
                                    .toExternalForm())));
                }
                else
                {
                    linkToProduct
                            .add(new Image("linkToImage", new Model<String>("no_image.jpg")));
                    ;
                }

                item.add(linkToProduct);

                final ExternalLink linkToProduct2 = new ExternalLink("linkToProduct2", p.getLinkToProduct()
                        .toExternalForm(),
                        p.getProductName());

                item.add(linkToProduct2);

                String fragmentId;

                if (p.getPrice().compareTo(p.getRetailPrice()) != 0)
                {
                    fragmentId = "fragmentSale";
                }
                else
                {
                    fragmentId = "fragmentRegular";
                }

                final Fragment fragment = new Fragment(
                        "placeholderForFragmentedListItem", fragmentId, this);

                item.add(fragment);

                final Label regularPrice = new Label("regularPrice", p.getRetailPrice().toString());
                fragment.add(regularPrice);

                final Label salePrice = new Label("salePrice", p.getPrice().toString());
                fragment.add(salePrice);

                final WebMarkupContainer clear_div =
                        new WebMarkupContainer("clear_div");

                if ((i % 4) == 0)
                {
                    clear_div.add(new AttributeModifier("class", "clear_div"));
                }
                item.add(clear_div);

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
