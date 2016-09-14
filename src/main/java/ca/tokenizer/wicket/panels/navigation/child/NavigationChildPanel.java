package ca.tokenizer.wicket.panels.navigation.child;

import java.util.ArrayList;
import java.util.List;

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

public class NavigationChildPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(NavigationChildPanel.class);

    @Inject
    private IConfig config;

    public NavigationChildPanel(final String id, final PageParameters parameters)
    {
        super(id);

        final List<String> entries;
        if (parameters.getIndexedCount() > 0)
        {
            entries = getEntries(parameters);
        }
        else
        {
            entries = getPeers();
        }

        final RepeatingView repeater = new RepeatingView("repeater");
        add(repeater);

        int k = 0;
        for (final String e : entries)
        {

            final AbstractItem item = new AbstractItem(k++);
            repeater.add(item);

            final PageParameters p = new PageParameters(parameters);
            p.clearNamed();
            p.set(parameters.getIndexedCount(), e);
            final BookmarkablePageLink<HomePage> link = new BookmarkablePageLink<>(
                    "link",
                    HomePage.class,
                    p);
            final Label l = new Label("anchor", e);
            link.add(l);
            item.add(link);
        }

    }

    private List<String> getEntries(final PageParameters parameters)
    {

        final List<String> names = new ArrayList<>();

        if (parameters.getIndexedCount() == 0)
        {
            return names;
        }

        final String current = parameters.get(parameters.getIndexedCount() - 1).toString();

        if ("spacer.gif".equals(current) || "favicon.ico".equals(current))
        {
            return names;
        }

        final List<AwsBrowseNode> parents = config.getEntityManager().createQuery(
                "SELECT c FROM MyBrowseNode c WHERE c.name=?1")
                .setParameter(1, current)
                .getResultList();

        if (parents.size() == 0)
        {
            LOG.error("Result set is empty for the name: {}", current);
            return names;
        }

        final AwsBrowseNode parent = parents.get(0);

        final List entities = config.getEntityManager().createQuery(
                "SELECT c FROM MyBrowseNode c WHERE c.parentNodeId=?1")
                .setParameter(1, parent.getBrowseNodeId())
                .getResultList();

        for (final Object o : entities)
        {
            final AwsBrowseNode node = (AwsBrowseNode) o;
            names.add(node.getName());

        }

        return names;

    }

    private List<String> getPeers()
    {

        final String parentNodeId = "541966";

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
