package ca.tokenizer.aws.db;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "aws_item")
public class AwsItem
{

    @Id
    private String asin;

    @Column(length = 2048)
    private String title;

    @Column(length = 2048)
    private String detailPageURL;

    private String productGroup;

    private List<String> author;

    private List<String> artist;

    private List<String> actor;

    /**
     * Getter method for asin
     * 
     * @return the asin
     */
    public String getAsin()
    {
        return asin;
    }

    /**
     * Setter method for asin
     * 
     * @param asin
     *            the asin to set
     */
    public void setAsin(final String asin)
    {
        this.asin = asin;
    }

    /**
     * Getter method for title
     * 
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Setter method for title
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(final String title)
    {
        this.title = title;
    }

    /**
     * Getter method for detailPageURL
     * 
     * @return the detailPageURL
     */
    public String getDetailPageURL()
    {
        return detailPageURL;
    }

    /**
     * Setter method for detailPageURL
     * 
     * @param detailPageURL
     *            the detailPageURL to set
     */
    public void setDetailPageURL(final String detailPageURL)
    {
        this.detailPageURL = detailPageURL;
    }

    /**
     * Getter method for productGroup
     * 
     * @return the productGroup
     */
    public String getProductGroup()
    {
        return productGroup;
    }

    /**
     * Setter method for productGroup
     * 
     * @param productGroup
     *            the productGroup to set
     */
    public void setProductGroup(final String productGroup)
    {
        this.productGroup = productGroup;
    }

    /**
     * Getter method for author
     * 
     * @return the author
     */
    public List<String> getAuthor()
    {
        return author;
    }

    /**
     * Setter method for author
     * 
     * @param author
     *            the author to set
     */
    public void setAuthor(final List<String> author)
    {
        this.author = author;
    }

    /**
     * Getter method for artist
     * 
     * @return the artist
     */
    public List<String> getArtist()
    {
        return artist;
    }

    /**
     * Setter method for artist
     * 
     * @param artist
     *            the artist to set
     */
    public void setArtist(final List<String> artist)
    {
        this.artist = artist;
    }

    /**
     * Getter method for actor
     * 
     * @return the actor
     */
    public List<String> getActor()
    {
        return actor;
    }

    /**
     * Setter method for actor
     * 
     * @param actor
     *            the actor to set
     */
    public void setActor(final List<String> actor)
    {
        this.actor = actor;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((asin == null) ? 0 : asin.hashCode());
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
        final AwsItem other = (AwsItem) obj;
        if (asin == null)
        {
            if (other.asin != null)
            {
                return false;
            }
        }
        else if (!asin.equals(other.asin))
        {
            return false;
        }
        return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "AwsItem [asin=" + asin + ", title=" + title + ", detailPageURL=" + detailPageURL + ", productGroup="
                + productGroup + ", author=" + author + ", artist=" + artist + ", actor=" + actor + "]";
    }

}
