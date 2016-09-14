package ca.tokenizer.aws;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.aws.db.AwsBrowseNode;
import ca.tokenizer.aws.db.AwsItem;

import com.ECS.client.jax.AWSECommerceService;
import com.ECS.client.jax.AWSECommerceServicePortType;
import com.ECS.client.jax.BrowseNode;
import com.ECS.client.jax.BrowseNodeLookupRequest;
import com.ECS.client.jax.BrowseNodes;
import com.ECS.client.jax.TopItemSet;
import com.ECS.client.jax.TopItemSet.TopItem;

public class AwsQueue
{

    public static final Logger LOG = LoggerFactory.getLogger(AwsQueue.class);

    // "3375251"
    public static final String ROOT_NODE_ID = "7141123011";

    public static final String AWS_ACCESS_KEY = "AKIAIM5SVUKJNME6JFVA";

    public static final String AWS_SECRET_KEY = "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";

    public static final String ASSOCIATE_TAG = "tokenizer-20";

    private final AWSECommerceServicePortType port;

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");

    private final EntityManager em = factory.createEntityManager();

    private final BlockingQueue<TaskDescriptor> queue = new LinkedBlockingQueue<>();

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl(
            "aws-task"));

    public AwsQueue()
    {
        final AWSECommerceService service = new AWSECommerceService();
        service.setHandlerResolver(new AwsHandlerResolver(AWS_SECRET_KEY));
        port = service.getAWSECommerceServicePort();

    }

    private List<BrowseNodes> browseNodeLookup(final String nodeId)
    {
        final BrowseNodeLookupRequest req = new BrowseNodeLookupRequest();
        req.getBrowseNodeId().add(nodeId);

        req.getResponseGroup().add("BrowseNodeInfo");
        //req.getResponseGroup().add("MostGifted");
        //req.getResponseGroup().add("NewReleases");
        //req.getResponseGroup().add("MostWishedFor");
        req.getResponseGroup().add("TopSellers");

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

        return response;

    }

    public void save(final Object o)
    {

        final EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            em.merge(o);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        tx.commit();

    }

    public static void main(final String[] args)
    {

        final AwsQueue q = new AwsQueue();

        q.startMain(ROOT_NODE_ID);

        //q.startTest();

    }

    private void startMain(final String nodeId)
    {

        final TaskDescriptor td = new TaskDescriptor(TaskType.BROWSE_NODE, nodeId);
        queue.add(td);

        final Future<?> queuePoller = executor.scheduleAtFixedRate(new AwsTask(), 1, 1, TimeUnit.SECONDS);

    }

    private void startTest()
    {
        final List<BrowseNodes> browseNodesList = browseNodeLookup("3375251");

        for (final BrowseNodes browseNodes : browseNodesList)
        {

            System.out.println(Utils.toString(browseNodes));
            final List<BrowseNode> nodes = browseNodes.getBrowseNode();
            for (final BrowseNode node : nodes)
            {
                save(node);
            }

        }

    }

    private void save(final BrowseNode o)
    {

        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        AwsBrowseNode node = em.find(AwsBrowseNode.class, o.getBrowseNodeId());

        if (node == null)
        {
            node = new AwsBrowseNode();
            node.setBrowseNodeId(o.getBrowseNodeId());
        }
        node.setName(o.getName());
        if (o.isIsCategoryRoot() != null)
        {
            node.setCategoryRoot(o.isIsCategoryRoot());
        }
        //em.persist(node);

        final Set<AwsItem> topSellers = new HashSet<>();

        for (final TopItemSet topItemSet : o.getTopItemSet())
        {

            for (final TopItem topItem : topItemSet.getTopItem())
            {

                final AwsItem awsItem = new AwsItem();
                awsItem.setAsin(topItem.getASIN());
                awsItem.setTitle(topItem.getTitle());
                awsItem.setDetailPageURL(topItem.getDetailPageURL());
                awsItem.setProductGroup(topItem.getProductGroup());
                awsItem.setActor(topItem.getActor());
                awsItem.setArtist(topItem.getArtist());
                awsItem.setAuthor(topItem.getAuthor());

                topSellers.add(awsItem);

                //System.out.println(awsItem);
                // em.merge(awsItem);
            }

        }
        node.setTopSellers(topSellers);

        final Set<AwsBrowseNode> children = new HashSet<>();

        if (o.getChildren() != null)
        {
            for (final BrowseNode browseNode : o.getChildren().getBrowseNode())
            {
                AwsBrowseNode node2 = em.find(AwsBrowseNode.class, browseNode.getBrowseNodeId());
                if (node2 == null)
                {
                    node2 = new AwsBrowseNode();
                    node2.setBrowseNodeId(browseNode.getBrowseNodeId());
                }
                node2.setName(browseNode.getName());
                if (browseNode.isIsCategoryRoot() != null)
                {
                    node2.setCategoryRoot(browseNode.isIsCategoryRoot());
                }

                children.add(node2);

                queue.offer(new TaskDescriptor(TaskType.BROWSE_NODE, browseNode.getBrowseNodeId()));

            }
            node.setChildren(children);
        }

        if (o.getAncestors() != null)
        {
            final Set<AwsBrowseNode> ancestors = new HashSet<>();
            for (final BrowseNode browseNode : o.getAncestors().getBrowseNode())
            {
                AwsBrowseNode node2 = em.find(AwsBrowseNode.class, browseNode.getBrowseNodeId());
                if (node2 == null)
                {
                    node2 = new AwsBrowseNode();
                    node2.setBrowseNodeId(browseNode.getBrowseNodeId());
                }
                node2.setName(browseNode.getName());
                if (browseNode.isIsCategoryRoot() != null)
                {
                    node2.setCategoryRoot(browseNode.isIsCategoryRoot());
                }
                ancestors.add(node2);

                // TODO: check if we need that
                em.merge(node2);

                // queue.offer(new TaskDescriptor(TaskType.BROWSE_NODE, node2.getBrowseNodeId()));

            }

            //node.setAncestors(ancestors);

        }

        node.setTimestamp(new Date());
        em.merge(node);

        tx.commit();
    }

    private class ThreadFactoryImpl implements ThreadFactory
    {
        private final ThreadGroup threadGroup;

        private final AtomicInteger id = new AtomicInteger(1);

        public ThreadFactoryImpl(final String groupName)
        {
            threadGroup = new ThreadGroup(groupName);
        }

        @Override
        public Thread newThread(final Runnable task)
        {
            final Thread result = new Thread(threadGroup, task, "th-"
                    + id.getAndIncrement());
            result.setPriority(Thread.NORM_PRIORITY - 1);
            return result;
        }
    }

    private class AwsTask implements Runnable
    {

        public AwsTask()
        {
        }

        @Override
        public void run()
        {

            TaskDescriptor td = null;
            try
            {

                td = queue.take();

                System.out.println("Task taken: " + td);

                while (td != null)
                {
                    final AwsBrowseNode testNode = em.find(AwsBrowseNode.class, td.nodeId);
                    if ((testNode != null)
                            && (testNode.getTimestamp() != null)
                            && ((testNode.getTimestamp().getTime() - (24 * 7 * 3600 * 1000L)) < System
                                    .currentTimeMillis()))
                    {
                        for (final AwsBrowseNode child : testNode.getChildren())
                        {
                            queue.offer(new TaskDescriptor(TaskType.BROWSE_NODE, child.getBrowseNodeId()));
                            System.out.println("Task offered: " + td);
                            System.out.println("queue size: " + queue.size());
                        }
                        td = queue.take();
                        System.out.println("Task taken: " + td);
                        System.out.println("queue size: " + queue.size());
                    }
                    else
                    {
                        break;
                    }
                }

            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                executor.shutdown();
            }

            if (td == null)
            {
                return;
            }

            try
            {
                final List<BrowseNodes> browseNodesList = browseNodeLookup(td.nodeId);

                for (final BrowseNodes browseNodes : browseNodesList)
                {

                    System.out.println(Utils.toString(browseNodes));
                    final List<BrowseNode> nodes = browseNodes.getBrowseNode();
                    for (final BrowseNode node : nodes)
                    {
                        save(node);
                    }

                }
            }
            catch (final Throwable e)
            {
                e.printStackTrace();
                executor.shutdown();
            }
        }
    }

    private class TaskDescriptor
    {

        private final TaskType type;

        private final String nodeId;

        public TaskDescriptor(final TaskType type, final String nodeId)
        {
            this.type = type;
            this.nodeId = nodeId;
        }

        /**
         * Getter method for type
         * 
         * @return the type
         */
        public TaskType getType()
        {
            return type;
        }

        /**
         * Getter method for nodeId
         * 
         * @return the nodeId
         */
        public String getNodeId()
        {
            return nodeId;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "TaskDescriptor [type=" + type + ", nodeId=" + nodeId + "]";
        }

    }

    private enum TaskType
    {
        BROWSE_NODE,
        ITEM_SEARCH;
    }

}
