package ca.tokenizer.wicket.pages.home;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class SearchForm extends Form<SearchFormBean>
{
    private static final long serialVersionUID = 1L;

    private final PageParameters parameters;

    /**
     * Constructor.
     * 
     * @param id
     * @param model
     */
    public SearchForm(final String id, final IModel<SearchFormBean> model, final PageParameters parameters)
    {
        super(id, model);

        this.parameters = parameters;

        if (parameters.get("q") != null)
        {
            model.getObject().setQuery(parameters.get("q").toString());
        }

        add(new TextField<String>("query").setRequired(true).setLabel(
                new Model<String>("Query")));

        //final PropertyModel<String> queryModel = new PropertyModel<String>(searchFormBean, "query");

    }

    @Override
    public void onSubmit()
    {
        final SearchFormBean bean = (SearchFormBean) getDefaultModelObject();

        final PageParameters p = new PageParameters(parameters);

        String current = null;
        if (parameters.get("q") != null)
        {
            current = parameters.get("q").toString();
        }

        if ((current == null) || !current.equals(bean.getQuery()))
        {
            p.remove("page");
        }

        p.remove("q");

        if (!StringUtils.isBlank(bean.getQuery()))
        {
            p.add("q", bean.getQuery());
        }

        setResponsePage(HomePage.class, p);

    }

    @Override
    public void onError()
    {
        final PageParameters p = new PageParameters(parameters);
        p.remove("q");
        setResponsePage(HomePage.class, p);
    }

    @Override
    protected boolean getStatelessHint()
    {
        return true;
    }

}
