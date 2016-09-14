package ca.tokenizer.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrUtils
{

    private static final Logger LOG = LoggerFactory.getLogger(SolrUtils.class);

    private static final String SOLR_URL_BASE = "http://localhost:38080/solr";

    private static SolrServer solrServer = null;

    private static SolrServer solrServerMessages = null;

    private static DefaultHttpClient httpClient;

    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;

    private static final int DEFAULT_CONNECTION_TIMEOUT = 60000;

    private static final int DEFAULT_MAX_THREADS = 128;

    private static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 128;

    private static final int DEFAULT_KEEP_ALIVE_DURATION = 10000;

    private static final int DEFAULT_MAX_RETRY_COUNT = 3;

    private static IdleConnectionMonitorThread monitor;

    private final static String SOLR_CORE_NAME = "7124-fanatics.com";

    // private final static String SOLR_CORE_NAME = "21462-ezsystems";

    public static SolrServer getSolrServer()
    {
        if (solrServer == null)
        {
            synchronized (SolrUtils.class)
            {
                if (solrServer == null)
                {

                    /*                    final HttpSolrServer httpSolrServer =
                                                new HttpSolrServer(
                                                        SOLR_URL_BASE + "/" + SOLR_CORE_NAME,
                                                        getHttpClient());
                    */

                    final HttpSolrServer httpSolrServer = new HttpSolrServer(
                            SOLR_URL_BASE + "/" + SOLR_CORE_NAME);

                    solrServer = httpSolrServer;
                }
            }
        }
        return solrServer;
    }

    private static HttpClient getHttpClient()
    {
        if (httpClient == null)
        {
            synchronized (SolrUtils.class)
            {
                if (httpClient != null)
                {
                    return httpClient;
                }
                final HttpParams params = new BasicHttpParams();

                HttpConnectionParams.setSoTimeout(params,
                        DEFAULT_SOCKET_TIMEOUT);
                HttpConnectionParams.setConnectionTimeout(params,
                        DEFAULT_CONNECTION_TIMEOUT);
                HttpConnectionParams.setTcpNoDelay(params, true);

                /*
                 * CoreConnectionPNames.STALE_CONNECTION_CHECK=
                 * 'http.connection.stalecheck': determines whether stale
                 * connection check is to be used. Disabling stale connection
                 * check may result in a noticeable performance improvement (the
                 * check can cause up to 30 millisecond overhead per request) at
                 * the risk of getting an I/O error when executing a request
                 * over a connection that has been closed at the server side.
                 * This parameter expects a value of type java.lang.Boolean. For
                 * performance critical operations the check should be disabled.
                 * If this parameter is not set, the stale connection check will
                 * be performed before each request execution.
                 * 
                 * We don't need I/O exceptions in case if Server doesn't
                 * support Kee-Alive option; our client by default always tries
                 * keep-alive.
                 */
                // Even with stale checking enabled, a connection can "go stale"
                // between the check and the
                // next request. So we still need to handle the case of a closed
                // socket (from the server side),
                // and disabling this check improves performance.
                HttpConnectionParams.setStaleCheckingEnabled(params, false);

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

                HttpProtocolParams.setUserAgent(params, "Tokenizer");
                HttpProtocolParams.setContentCharset(params, "UTF-8");
                HttpProtocolParams.setHttpElementCharset(params, "UTF-8");

                HttpProtocolParams.setUseExpectContinue(params, true);
                params.setIntParameter(CoreProtocolPNames.WAIT_FOR_CONTINUE,
                        5000);

                // Create and initialize scheme registry
                final SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", 80,
                        PlainSocketFactory.getSocketFactory()));

                final PoolingClientConnectionManager poolingClientConnectionManager = new PoolingClientConnectionManager(
                        schemeRegistry);
                poolingClientConnectionManager.setMaxTotal(DEFAULT_MAX_THREADS);
                poolingClientConnectionManager
                        .setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_HOST);

                httpClient = new DefaultHttpClient(
                        poolingClientConnectionManager, params);

                httpClient
                        .setHttpRequestRetryHandler(new MyRequestRetryHandler(
                                DEFAULT_MAX_RETRY_COUNT));

                HttpClientParams.setAuthenticating(params, false);
                HttpClientParams.setCookiePolicy(params,
                        CookiePolicy.BROWSER_COMPATIBILITY);

                httpClient
                        .setKeepAliveStrategy(new MyConnectionKeepAliveStrategy());

                monitor = new IdleConnectionMonitorThread(
                        poolingClientConnectionManager);
                monitor.start();

            }
        }

        return httpClient;
    }

    private static class MyRequestRetryHandler implements
            HttpRequestRetryHandler
    {
        private final int _maxRetryCount;

        public MyRequestRetryHandler(final int maxRetryCount)
        {
            _maxRetryCount = maxRetryCount;
        }

        @Override
        public boolean retryRequest(final IOException exception,
                final int executionCount, final HttpContext context)
        {
            if (LOG.isTraceEnabled())
            {
                LOG.trace("Decide about retry #" + executionCount
                        + " for exception " + exception.getMessage());
            }

            if (executionCount >= _maxRetryCount)
            {
                // Do not retry if over max retry count
                return false;
            }
            else if (exception instanceof NoHttpResponseException)
            {
                // Retry if the server dropped connection on us
                return true;
            }
            else if (exception instanceof SSLHandshakeException)
            {
                // Do not retry on SSL handshake exception
                return false;
            }

            final HttpRequest request = (HttpRequest) context
                    .getAttribute(ExecutionContext.HTTP_REQUEST);
            final boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            // Retry if the request is considered idempotent
            return idempotent;
        }
    }

    public static class MyConnectionKeepAliveStrategy implements
            ConnectionKeepAliveStrategy
    {

        @Override
        public long getKeepAliveDuration(final HttpResponse response,
                final HttpContext context)
        {
            if (response == null)
            {
                throw new IllegalArgumentException(
                        "HTTP response may not be null");
            }
            final HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext())
            {
                final HeaderElement he = it.nextElement();
                final String param = he.getName();
                final String value = he.getValue();
                if ((value != null) && param.equalsIgnoreCase("timeout"))
                {
                    try
                    {
                        return Long.parseLong(value) * 1000;
                    }
                    catch (final NumberFormatException ignore)
                    {
                    }
                }
            }
            return DEFAULT_KEEP_ALIVE_DURATION;
        }
    }

    public static class IdleConnectionMonitorThread extends Thread
    {

        private final ClientConnectionManager connMgr;

        public IdleConnectionMonitorThread(final ClientConnectionManager connMgr)
        {
            super();
            this.connMgr = connMgr;
            this.setDaemon(true);
        }

        @Override
        public void run()
        {
            while (!interrupted())
            {
                // Close expired connections
                connMgr.closeExpiredConnections();
                // Optionally, close connections
                // that have been idle longer than 30 sec
                connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                try
                {
                    Thread.currentThread().sleep(30000);
                }
                catch (final InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
