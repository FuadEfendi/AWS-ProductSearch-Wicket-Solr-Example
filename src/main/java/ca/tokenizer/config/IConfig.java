package ca.tokenizer.config;

import javax.persistence.EntityManager;

import org.apache.commons.configuration.CompositeConfiguration;

import com.google.inject.ImplementedBy;

@ImplementedBy(Config.class)
public interface IConfig
{
    CompositeConfiguration getConfiguration();

    EntityManager getEntityManager();

}
