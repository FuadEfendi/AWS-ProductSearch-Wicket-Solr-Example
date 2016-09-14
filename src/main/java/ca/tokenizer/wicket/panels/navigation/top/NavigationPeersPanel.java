package ca.tokenizer.wicket.panels.navigation.top;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.aws.db.AwsBrowseNode;
import ca.tokenizer.config.IConfig;
import ca.tokenizer.wicket.pages.home.HomePage;

import com.google.inject.Inject;

@SuppressWarnings("serial")
public class NavigationPeersPanel extends Panel
{

    private static final Logger LOG = LoggerFactory.getLogger(NavigationPeersPanel.class);

    @Inject
    private IConfig config;

    public NavigationPeersPanel(final String id, final PageParameters request)
    {
        super(id);

        final RepeatingView view = new RepeatingView("view");
        add(view);

        final EntityManager em = config.getEntityManager();

        String nodeId = null;
        if (StringUtils.isBlank(nodeId = request.get("node").toString()))
        {
            nodeId = config.getConfiguration().getString("root.node.id");
        }

        final AwsBrowseNode rootNode = em.find(AwsBrowseNode.class, nodeId);

        for (final AwsBrowseNode node : rootNode.getChildren())
        {
            final AbstractItem item = new AbstractItem(node.getBrowseNodeId());
            view.add(item);
            final PageParameters newParameters = new PageParameters();
            newParameters.set("node", node.getBrowseNodeId());

            final BookmarkablePageLink<HomePage> link = new BookmarkablePageLink<>("link",
                    HomePage.class,
                    newParameters);

            final Label label = new Label("label", node.getName());
            link.add(label);

            item.add(link);

        }

    }

    /*
        private Collection<AwsBrowseNode> getNodes(final String nodeId)
        {

            final EntityManager em = config.getEntityManager();
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<AwsBrowseNode> cq = cb.createQuery(AwsBrowseNode.class);
            final Root<AwsBrowseNode> pet = cq.from(AwsBrowseNode.class);
            cq.select(pet);
            final TypedQuery<AwsBrowseNode> q = em.createQuery(cq);
            final List<AwsBrowseNode> allPets = q.getResultList();

        }
    */
    private List<String> getPeersAmazon(final String parentNodeId)
    {
        final List entities = config.getEntityManager().createQuery(
                "SELECT c FROM MyBrowseNode c WHERE c.parentNodeId=?1")
                .setParameter(1, parentNodeId)
                .getResultList();

        final List<String> names = new ArrayList<>();
        for (final Object o : entities)
        {
            final AwsBrowseNode node = (AwsBrowseNode) o;
            names.add(node.getName());

        }
        return names;
    }

}
