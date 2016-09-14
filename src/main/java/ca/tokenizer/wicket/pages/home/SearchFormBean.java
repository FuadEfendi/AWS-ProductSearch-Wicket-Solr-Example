package ca.tokenizer.wicket.pages.home;

import java.io.Serializable;

public class SearchFormBean implements Serializable
{

    private String query = "";

    /**
     * Getter method for query
     * 
     * @return the query
     */
    public String getQuery()
    {
        return query;
    }

    /**
     * Setter method for query
     * 
     * @param query
     *            the query to set
     */
    public void setQuery(final String query)
    {
        this.query = query;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "SearchFormBean [query=" + query + "]";
    }

}
