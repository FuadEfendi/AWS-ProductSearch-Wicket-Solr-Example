package ca.tokenizer.wicket.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.config.IConfig;

import com.google.inject.Inject;

public class BasePage extends WebPage
{

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

    @Inject
    private IConfig config;

    protected IModel<String> pageTitle = new Model<>("");

    public BasePage()
    {
        super();
    }

    public BasePage(final IModel<?> model)
    {
        super(model);
    }

    public BasePage(final PageParameters parameters)
    {
        super(parameters);
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        add(new Label("pageTitle", pageTitle));
    }
}
