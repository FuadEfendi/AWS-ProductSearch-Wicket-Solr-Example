package ca.tokenizer.aws;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils
{

    public static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static final ObjectMapper mapper = new ObjectMapper();
    static
    {
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        // mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
        // mapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_NULL);
    }

    public static String toString(final Object o)
    {
        try
        {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        }
        catch (final JsonMappingException e)
        {
            LOG.error("", e);
        }
        catch (final JsonGenerationException e)
        {
            LOG.error("", e);
        }
        catch (final IOException e)
        {
            LOG.error("", e);
        }
        return StringUtils.EMPTY;
    }

}
