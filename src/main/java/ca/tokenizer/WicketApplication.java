package ca.tokenizer;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbsSettings;

import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import ca.tokenizer.wicket.pages.home.HomePage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see ca.tokenizer.Start#main(String[])
 * 
 *      http://www.wicket-library.com/wicket-examples/guice/?0
 * 
 */
public class WicketApplication extends WebApplication
{
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage()
    {
        //return Index.class;
        //return HomePage.class;
        return HomePage.class;

    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init()
    {
        super.init();

        mountPage("/", HomePage.class);

        new BreadcrumbsSettings()
                .withDecoratedBookmarkableLinks()
                .collapseWhenRepeated(2)
                .install(this);

        getDebugSettings().setDevelopmentUtilitiesEnabled(true);
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this));

        //final BootstrapSettings settings = new BootstrapSettings();
        //settings.setThemeProvider(new SingleThemeProvider(BootswatchTheme.Cyborg));
        //Bootstrap.install(this, settings);

    }
}
