package ca.tokenizer.aws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.ws.Holder;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ECS.client.jax.AWSECommerceService;
import com.ECS.client.jax.AWSECommerceServicePortType;
import com.ECS.client.jax.Item;
import com.ECS.client.jax.ItemSearchRequest;
import com.ECS.client.jax.Items;
import com.ECS.client.jax.OperationRequest;

//http://www.a2sdeveloper.com/
public class SampleItemLookup
{

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");

    private static EntityManager manager = factory.createEntityManager();

    private static final Logger LOG = LoggerFactory.getLogger(SampleItemLookup.class);

    private static final String AWS_ACCESS_KEY_ID = "AKIAJR4Y73IBRG2ETFKA";

    private static final ObjectMapper mapper = new ObjectMapper();
    static
    {
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_EMPTY);
    }

    public static void main(final String[] args)
    {

        final AWSECommerceService service = new AWSECommerceService();

        // http://code.google.com/p/prod-viz/
        // http://stackoverflow.com/questions/3189979/amazon-product-advertising-api-signed-request-with-java

        service.setHandlerResolver(new AwsHandlerResolver("0EN0ptKtg2F2pQuXPvm432m7sbF9M0VtG8x+vSa1"));

        final AWSECommerceServicePortType port = service.getAWSECommerceServicePort();

        // http://docs.aws.amazon.com/AWSECommerceService/latest/DG/ItemSearch.html

        // http://docs.aws.amazon.com/AWSECommerceService/latest/DG/MarketplaceDomainParameter.html
        final String marketplaceDomain = "www.amazonsupply.com";

        final String xmlEscaping = "Single";

        final String validate = "False";

        final ItemSearchRequest itemRequest = new ItemSearchRequest();

        //itemRequest.setBrowseNode("All");
        itemRequest.setKeywords("Com/4i XI ISA 4-Port w/daughterboard");
        itemRequest.setSearchIndex("All");

        // http://www.browsenodes.com/
        //itemRequest.setBrowseNode("565108");

        itemRequest.setIncludeReviewsSummary("true");

        itemRequest.getResponseGroup().add("OfferFull");
        itemRequest.getResponseGroup().add("BrowseNodes");
        itemRequest.getResponseGroup().add("ItemAttributes");

        final OperationRequest operationRequest = new OperationRequest();

        final Holder<List<Items>> items = new Holder(new ArrayList<>());

        /**
         * 
         public void itemSearch(
         * 
         * @WebParam(name = "MarketplaceDomain", targetNamespace =
         *                "http://webservices.amazon.com/AWSECommerceService/2011-08-01" ) String marketplaceDomain,
         * @WebParam(name = "AWSAccessKeyId", targetNamespace =
         *                "http://webservices.amazon.com/AWSECommerceService/2011-08-01" ) String awsAccessKeyId,
         * @WebParam(name = "AssociateTag", targetNamespace =
         *                "http://webservices.amazon.com/AWSECommerceService/2011-08-01" ) String associateTag,
         * @WebParam(name = "XMLEscaping", targetNamespace =
         *                "http://webservices.amazon.com/AWSECommerceService/2011-08-01" ) String xmlEscaping,
         * @WebParam(name = "Validate", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01"
         *                ) String validate,
         * @WebParam(name = "Shared", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01" )
         *                ItemSearchRequest shared,
         * @WebParam(name = "Request", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01"
         *                ) List<ItemSearchRequest> request,
         * @WebParam(name = "OperationRequest", targetNamespace =
         *                "http://webservices.amazon.com/AWSECommerceService/2011-08-01" , mode = WebParam.Mode.OUT)
         *                Holder<OperationRequest> operationRequest,
         * @WebParam(name = "Items", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01" ,
         *                mode = WebParam.Mode.OUT) Holder<List<Items>> items);
         */

        port.itemSearch(
                //marketplaceDomain,
                null,
                AWS_ACCESS_KEY_ID,
                "tokenizer-20",
                xmlEscaping,
                validate,
                itemRequest,
                null,
                new Holder(operationRequest),
                items);

        final List<Items> response = items.value;

        for (final Items i : response)
        {

            System.out.println(toString(i));

            for (final Item itemObj : i.getItem())
            {

                System.out.println(itemObj.getItemAttributes().getTitle()); // Title
                System.out.println(itemObj.getDetailPageURL()); // Amazon URL
            }
        }

        System.out.println("API Test stopped");

    }

    public static String toString(final Object o)
    {
        try
        {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        }
        catch (final JsonMappingException e)
        {
            LOG.error("", e);
        }
        catch (final JsonGenerationException e)
        {
            LOG.error("", e);
        }
        catch (final IOException e)
        {
            LOG.error("", e);
        }
        return "";
    }

    public static void save(final Object o)
    {

        final EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try
        {
            manager.merge(o);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        tx.commit();

    }
}
