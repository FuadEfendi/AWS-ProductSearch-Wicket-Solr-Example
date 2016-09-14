package ca.tokenizer.aws;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.aws.db.AwsBrowseNode;

import com.ECS.client.jax.AWSECommerceService;
import com.ECS.client.jax.AWSECommerceServicePortType;
import com.ECS.client.jax.BrowseNode;
import com.ECS.client.jax.BrowseNodeLookupRequest;
import com.ECS.client.jax.BrowseNodes;

/**
 * http://code.google.com/p/prod-viz/
 * http://stackoverflow.com/questions/3189979/
 * amazon-product-advertising-api-signed-request-with-java
 * 
 * http://docs.aws.amazon.com/AWSECommerceService/latest/DG/ItemSearch.html
 * 
 * http://docs.aws.amazon.com/AWSECommerceService/latest/DG/
 * MarketplaceDomainParameter.html
 * 
 * http://www.browsenodes.com/
 * 
 */
public class MyBrowseNodeLookup
{

    public static final Logger LOG = LoggerFactory.getLogger(MyBrowseNodeLookup.class);

    public static final String AWS_ACCESS_KEY = "AKIAIM5SVUKJNME6JFVA";

    public static final String AWS_SECRET_KEY = "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";

    public static final String ASSOCIATE_TAG = "tokenizer-20";

    public static EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");

    public static EntityManager manager = factory.createEntityManager();

    public static AWSECommerceServicePortType port;

    public static final String HOME_NODE_ID = "-2000";

    public static void main(final String[] args) throws InterruptedException
    {

        final AWSECommerceService service = new AWSECommerceService();
        service.setHandlerResolver(new AwsHandlerResolver(AWS_SECRET_KEY));
        port = service.getAWSECommerceServicePort();

        //browse("-1");
        // Camera & Photo (502394)
        browseFromRoot("502394", "Camera & Photo");

        //Car Toys (10963061)
        browseFromRoot("10963061", "Car Toys");

        //Cell Phones & Accessories (2335753011)
        browseFromRoot("2335753011", "Cell Phones & Accessories");

        //Computer & Video Games (468642)
        browseFromRoot("468642", "Computer & Video Games");

        // Computers (541966)
        browseFromRoot("541966", "Computers");

        //Electronics (172282) 
        browseFromRoot("172282", "Electronics");

        //Software (229534)
        browseFromRoot("229534", "Software");

        //Industrial & Scientific (16310161) 
        browseFromRoot("16310161", "Industrial & Scientific");

        // Wireless	 	 	 	 	 	 	 	 	 	508494
        browseFromRoot("508494", "Wireless");

        // WirelessAccessories	 	 	 	 	 	 	 	 	 	13900851
        browseFromRoot("13900851", "Wireless Accessories");
    }

    static int i = 1;

    private static void browse(final String nodeId) throws InterruptedException
    {
        Thread.currentThread().sleep(950L);

        i++;

        LOG.info("Call No.: " + i + "; \t Node ID: " + nodeId);

        final BrowseNodeLookupRequest req = new BrowseNodeLookupRequest();
        req.getBrowseNodeId().add(nodeId);

        // MostGifted  | NewReleases | MostWishedFor | TopSellers 
        //req.getResponseGroup().add("MostGifted");
        //req.getResponseGroup().add("NewReleases");
        //req.getResponseGroup().add("MostWishedFor");
        //req.getResponseGroup().add("TopSellers");

        final Holder<List<BrowseNodes>> holder = new Holder(new ArrayList<>());

        port.browseNodeLookup(
                null /*marketplaceDomain*/,
                AWS_ACCESS_KEY,
                ASSOCIATE_TAG,
                null /*validate*/,
                null /*xmlEscaping*/,
                req,
                null,
                null /*operationRequest*/,
                holder);

        final List<BrowseNodes> response = holder.value;

        for (final BrowseNodes i : response)
        {

            //System.out.println(toString(i));

            for (final BrowseNode item : i.getBrowseNode())
            {
                handle(port, item);
            }
        }

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

    private static void handle(final AWSECommerceServicePortType port, final BrowseNode o) throws InterruptedException
    {
        final List<AwsBrowseNode> nodes = new ArrayList<>();
        if (o.getChildren() == null)
        {
            return;
        }
        for (final BrowseNode node : o.getChildren().getBrowseNode())
        {
            final AwsBrowseNode myBrowseNode = new AwsBrowseNode();
            //myBrowseNode.setParentNodeId(o.getBrowseNodeId());
            myBrowseNode.setBrowseNodeId(node.getBrowseNodeId());
            myBrowseNode.setName(node.getName());
            save(myBrowseNode);
            browse(node.getBrowseNodeId());
        }
    }

    public static void browseFromRoot(final String browseNodeId, final String name) throws InterruptedException
    {
        final AwsBrowseNode node = new AwsBrowseNode();
        //node.setParentNodeId(HOME_NODE_ID);
        node.setBrowseNodeId(browseNodeId);
        node.setName(name);
        save(node);
        browse(browseNodeId);
    }

}
