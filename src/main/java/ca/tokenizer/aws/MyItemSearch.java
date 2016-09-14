package ca.tokenizer.aws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Holder;

import com.ECS.client.jax.AWSECommerceService;
import com.ECS.client.jax.ItemSearchRequest;
import com.ECS.client.jax.Items;

public class MyItemSearch extends MyBrowseNodeLookup
{

    public static void main(final String[] args) throws InterruptedException
    {

        final AWSECommerceService service = new AWSECommerceService();
        service.setHandlerResolver(new AwsHandlerResolver(AWS_SECRET_KEY));
        port = service.getAWSECommerceServicePort();
        //browse("-1");
        // Camera & Photo (502394)
        itemSearch("olympus camera");

    }

    private static void itemSearch(final String nodeId)
    {

        final ItemSearchRequest req = new ItemSearchRequest();

        //req.setKeywords("Com/4i XI ISA 4-Port w/daughterboard");
        req.setSearchIndex("PCHardware");

        // BrowseNodes 
        // req.getResponseGroup().add("BrowseNodes");

        req.getResponseGroup().add("ItemAttributes");

        req.setBrowseNode("11036071");

        // Availability=Available

        final Holder<List<Items>> holder = new Holder(new ArrayList<>());

        /*
             public void itemSearch(
        @WebParam(name = "MarketplaceDomain", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01")
        String marketplaceDomain,
        @WebParam(name = "AWSAccessKeyId", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01")
        String awsAccessKeyId,
        @WebParam(name = "AssociateTag", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01")
        String associateTag,
        @WebParam(name = "XMLEscaping", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01")
        String xmlEscaping,
        @WebParam(name = "Validate", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01")
        String validate,
        @WebParam(name = "Shared", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01")
        ItemSearchRequest shared,
        @WebParam(name = "Request", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01")
        List<ItemSearchRequest> request,
        @WebParam(name = "OperationRequest", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01", mode = WebParam.Mode.OUT)
        Holder<OperationRequest> operationRequest,
        @WebParam(name = "Items", targetNamespace = "http://webservices.amazon.com/AWSECommerceService/2011-08-01", mode = WebParam.Mode.OUT)
        Holder<List<Items>> items);

         */

        port.itemSearch(
                null /*marketplaceDomain*/,
                AWS_ACCESS_KEY,
                ASSOCIATE_TAG,
                null,
                null,
                req,
                null,
                null /*operationRequest*/,
                holder);

        final List<Items> response = holder.value;

        for (final Items i : response)
        {
            System.out.println(Utils.toString(i));
        }
    }

}
