package ca.tokenizer.aws.db;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
@Entity
@Table(name = "aws_browse_node")
//@Table(name = "browsenode")
public class AwsBrowseNode implements Comparable
{

    @Id
    private String browseNodeId;

    //private int priority;

    private String name;

    private Set<AwsItem> topSellers;

    private final Set<AwsBrowseNode> children = new TreeSet<>();

    //private Set<AwsBrowseNode> ancestors = new HashSet<>();

    private boolean categoryRoot = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * Getter method for browseNodeId
     * 
     * @return the browseNodeId
     */
    public String getBrowseNodeId()
    {
        return browseNodeId;
    }

    /**
     * Setter method for browseNodeId
     * 
     * @param browseNodeId
     *            the browseNodeId to set
     */
    public void setBrowseNodeId(final String browseNodeId)
    {
        this.browseNodeId = browseNodeId;
    }

    /**
     * Getter method for name
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Setter method for name
     * 
     * @param name
     *            the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Getter method for categoryRoot
     * 
     * @return the categoryRoot
     */
    public boolean isCategoryRoot()
    {
        return categoryRoot;
    }

    /**
     * Setter method for categoryRoot
     * 
     * @param categoryRoot
     *            the categoryRoot to set
     */
    public void setCategoryRoot(final boolean categoryRoot)
    {
        this.categoryRoot = categoryRoot;
    }

    /**
     * Getter method for topSellers
     * 
     * @return the topSellers
     */
    public Set<AwsItem> getTopSellers()
    {
        return topSellers;
    }

    /**
     * Setter method for topSellers
     * 
     * @param topSellers
     *            the topSellers to set
     */
    public void setTopSellers(final Set<AwsItem> topSellers)
    {
        this.topSellers = topSellers;
    }

    /**
     * Getter method for children
     * 
     * @return the children
     */
    public Set<AwsBrowseNode> getChildren()
    {
        return children;
    }

    /**
     * Setter method for children
     * 
     * @param children
     *            the children to set
     */
    public void setChildren(final Set<AwsBrowseNode> children)
    {
        this.children.clear();
        this.children.addAll(children);
    }

    /**
     * Getter method for timestamp
     * 
     * @return the timestamp
     */
    public Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * Setter method for timestamp
     * 
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(final Date timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "AwsBrowseNode [browseNodeId=" + browseNodeId + ", name=" + name + ", topSellers=" + topSellers
                + ", children=" + children + ", categoryRoot=" + categoryRoot + ", timestamp=" + timestamp + "]";
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((browseNodeId == null) ? 0 : browseNodeId.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final AwsBrowseNode other = (AwsBrowseNode) obj;
        if (browseNodeId == null)
        {
            if (other.browseNodeId != null)
            {
                return false;
            }
        }
        else if (!browseNodeId.equals(other.browseNodeId))
        {
            return false;
        }
        return true;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object obj)
    {
        final AwsBrowseNode other = (AwsBrowseNode) obj;
        return name.compareTo(other.name);
    }

}
