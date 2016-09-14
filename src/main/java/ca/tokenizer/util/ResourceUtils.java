package ca.tokenizer.util;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtils
{

    private static final Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);

    public static String applicationBasePath()
    {
        return getApplicationBaseDirectory().getAbsolutePath();
    }

    public static File getApplicationBaseDirectory()
    {
        final ServletContext servletContext = WebApplication.get().getServletContext();
        final String realPath = getResourcePath(servletContext, "/");
        if (realPath == null)
        {
            return null;
        }
        return new File(realPath);
    }

    public static String getResourcePath(
            final ServletContext servletContext,
            final String path)
    {
        String resultPath = servletContext.getRealPath(path);
        if (resultPath != null)
        {
            return resultPath;
        }
        try
        {
            final URL url = servletContext.getResource(path);
            resultPath = url.getFile();
        }
        catch (final Exception e)
        {
            LOG.info("Could not find resource path: " + path, e);
        }
        return resultPath;
    }

    public static File getUserHomeDirectory()
    {
        return new File(System.getProperty("user.home"));
    }

    public static File getUserTempDirectory()
    {
        return new File(getUserHomeDirectory(), "temp");
    }

}
