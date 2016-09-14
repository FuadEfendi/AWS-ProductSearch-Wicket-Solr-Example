package ca.tokenizer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.tokenizer.shareasale.model.Product;

public class CsvParser
{

    private static final Logger LOG = LoggerFactory.getLogger(CsvParser.class);

    //private static final String CSV_FILE = "data/test.txt";

    //private static final String CSV_FILE = "data/7124.txt"; // fanatics.com
    private static final String CSV_FILE = "data/21462.txt"; // EZ Systems
    
    public static void main(final String[] args) throws IOException
    {

        final CSVFormat f = CSVFormat.DEFAULT.withDelimiter('|');
        try (
                //final Reader in = new FileReader("src/main/resources/example-csv.txt");
                //final Reader in = new FileReader("data/7124.txt"); // Fanatics.com
                //final Reader in = new FileReader("data/21462.txt"); // EZ-Systems
                // ?ó?Ç?ô

                //final Reader in = new FileReader(CSV_FILE);
                final Reader in = new InputStreamReader(new FileInputStream(CSV_FILE), "windows-1252");
                final CSVParser records = f.parse(in);)

        {
            int i = 0;
            for (final CSVRecord record : records)
            {
                final String productId = record.get(0);
                final String productName = record.get(1);
                final String merchantId = record.get(2);
                final String merchantName = record.get(3);

                final String linkToProduct = record.get(4);
                final String linkToThumbnail = record.get(5);
                final String linkToImage = record.get(6);
                final String price = record.get(7);
                final String retailPrice = record.get(8);

                final String category = record.get(9);
                final String subcategory = record.get(10);
                final String description = record.get(11);
                final String custom1 = record.get(12);
                final String custom2 = record.get(13);

                String custom3 = record.get(14);
                if (StringUtils.isBlank(custom3))
                {
                    custom3 = "@@@@@";
                }

                final String custom4 = record.get(15);
                final String custom5 = record.get(16);

                final String lastupdated = record.get(17);
                final String status = record.get(18);

                final String manufacturer = record.get(19);
                final String partNumber = record.get(20);

                String merchantCategory = record.get(21);
                if (StringUtils.isBlank(merchantCategory))
                {
                    merchantCategory = "Baseball";
                }

                if ("College".equals(merchantCategory))
                {
                    merchantCategory = "College Athletics";
                }

                String merchantSubcategory = record.get(22);
                if (StringUtils.isBlank(merchantSubcategory))
                {
                    merchantSubcategory = "@@@";
                }

                final String shortDescription = record.get(23);

                final String isbn = record.get(24);
                final String upc = record.get(25);
                final String sku = record.get(26);
                final String crossSell = record.get(27);
                final String merchantGroup = record.get(28);

                final String merchantSubgroup = record.get(29);
                final String compatibleWith = record.get(30);
                final String compareTo = record.get(31);
                final String quantityDiscount = record.get(32);
                final String bestseller = record.get(33);

                final String addToCartURL = record.get(34);
                final String reviewsRSSURL = record.get(35);
                final String option1 = record.get(36);
                final String option2 = record.get(37);
                final String option3 = record.get(38);
                final String option4 = record.get(39);
                final String option5 = record.get(40);

                final String mobileURL = record.get(41);
                final String mobileImage = record.get(42);
                final String mobileThumbnail = record.get(43);

                final Product product = new Product(
                        productId,
                        productName,
                        merchantId,
                        merchantName,
                        linkToProduct,
                        linkToThumbnail,
                        linkToImage,
                        price,
                        retailPrice,
                        category,
                        subcategory,
                        description,
                        custom1,
                        custom2,
                        custom3,
                        custom4,
                        custom5,
                        lastupdated,
                        status,
                        manufacturer,
                        partNumber,
                        merchantCategory,
                        merchantSubcategory,
                        shortDescription,
                        isbn,
                        upc,
                        sku,
                        crossSell,
                        merchantGroup,
                        merchantSubgroup,
                        compatibleWith,
                        compareTo,
                        quantityDiscount,
                        bestseller,
                        addToCartURL,
                        reviewsRSSURL,
                        option1,
                        option2,
                        option3,
                        option4,
                        option5,
                        mobileURL,
                        mobileImage,
                        mobileThumbnail);

                System.out.println(i + "\t" + product);
                i++;

                try
                {
                    SolrUtils.getSolrServer().add(product.toSolr());
                    //SolrUtils.getSolrServer().commit();
                }
                catch (final SolrServerException e)
                {
                    LOG.error("", e);
                }
                catch (final IOException e)
                {
                    LOG.error("", e);
                }
                catch (final Exception e)
                {
                    LOG.error("", e);
                }

            }
        }
    }
}
