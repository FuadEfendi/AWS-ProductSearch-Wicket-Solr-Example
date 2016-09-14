package ca.tokenizer.config;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.util.ResourceUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Config implements IConfig
{

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    private final CompositeConfiguration configuration = new CompositeConfiguration();

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");

    private static EntityManager em = factory.createEntityManager();

    @Inject
    protected Config()
    {
        final File f = new File(ResourceUtils.getApplicationBaseDirectory(),
                "WEB-INF/classes/tokenizer-wicket.properties");

        LOG.info("File: {} exists: {}", f.getPath(), f.exists());
        try
        {
            configuration.addConfiguration(new PropertiesConfiguration(f));
        }
        catch (final ConfigurationException e)
        {
            e.printStackTrace();
            LOG.error("", e);
        }
    }

    /**
     * @see ca.tokenizer.config.IConfig#getConfiguration()
     */
    @Override
    public CompositeConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * @see ca.tokenizer.config.IConfig#getEntityManager()
     */
    @Override
    public EntityManager getEntityManager()
    {
        return em;
    }

}
