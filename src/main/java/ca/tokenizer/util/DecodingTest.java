package ca.tokenizer.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecodingTest
{
    //private static final String CSV_FILE = "data/test.txt";
    private static final Logger LOG = LoggerFactory.getLogger(DecodingTest.class);

    private static final String CSV_FILE = "data/7124.txt"; // fanatics.com

    // http://localhost:38080/solr/7124-fanatics.com/select?q=productId%3A549968137&wt=json&indent=true

    public static void main2(final String[] args) throws IOException
    {

        final CSVFormat f = CSVFormat.DEFAULT.withDelimiter('|');
        try (
                //final Reader in = new FileReader("src/main/resources/example-csv.txt");
                //final Reader in = new FileReader("data/7124.txt"); // Fanatics.com
                //final Reader in = new FileReader("data/21462.txt"); // EZ-Systems
                // ?ó?Ç?ô

                //final Reader in = new FileReader(CSV_FILE);
                final Reader in = new InputStreamReader(new FileInputStream(CSV_FILE), "UTF-8");
                final CSVParser records = f.parse(in);)

        {
            int i = 0;
            for (final CSVRecord record : records)
            {
                final String productId = record.get(0);
                final String productName = record.get(1);
                i++;

                System.out.println(i + "\t" + productId + "\t" + productName);

            }
        }

    }

    public static void main(final String[] args) throws IOException
    {

        int i = 0;

        try (

                final Reader in = new InputStreamReader(new FileInputStream(CSV_FILE), "windows-1252");

                BufferedReader br = new BufferedReader(in))
        {
            for (String line; (line = br.readLine()) != null;)
            {
                i++;

                System.out.println(i + "\t" + line);
                LOG.debug(i + "\t" + line);

                if (line.startsWith("486914312"))
                {
                    break;
                }
            }
        }
        System.out
                .println("Rajon Rondo Boston Celtics adidas Revolution 30 Alternate Replica Jersey – Kelly Green/Black");

    }

}
