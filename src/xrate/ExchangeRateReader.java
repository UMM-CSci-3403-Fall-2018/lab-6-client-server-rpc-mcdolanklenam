package xrate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.net.URL;
import java.util.Properties;


/**
 * Provide access to basic currency exchange rate services.
 *
 * @author mcdolanklenam
 */
public class ExchangeRateReader {

    private String accessKey;
    private String baseURL;

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     *
     * @param baseURL
     *            the base URL for requests
     */
    public ExchangeRateReader(String baseURL) throws IOException {
        // TODO Your code here
        this.baseURL = baseURL;
        /*
         * DON'T DO MUCH HERE!
         * People often try to do a lot here, but the action is actually in
         * the two methods below. All you need to do here is store the
         * provided `baseURL` in a field so it will be accessible later.
         */
        readAccessKeys();
    }

    private void readAccessKeys() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            // Don't change this filename unless you know what you're doing.
            // It's crucial that we don't commit the file that contains the
            // (private) access keys. This file is listed in `.gitignore` so
            // it's safe to put keys there as we won't accidentally commit them.
            in = new FileInputStream("etc/access_keys.properties");
        } catch (FileNotFoundException e) {
            /*
             * If this error gets generated, make sure that you have the desired
             * properties file in your project's `etc` directory. You may need
             * to rename the file ending in `.sample` by removing that suffix.
             */
            System.err.println("Couldn't open etc/access_keys.properties; have you renamed the sample file?");
            throw(e);
        }
        properties.load(in);
        // This assumes we're using Fixer.io and that the desired access key is
        // in the properties file in the key labelled `fixer_io`.
        accessKey = properties.getProperty("fixer_io");
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     *
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException, UnsupportedOperationException {
        // TODO Your code here


        String targetURL = baseURL + year + "-" + correctDate(month) + "-" + correctDate(day) + "?access_key=" + accessKey;
        //Creates an URL
        URL url = new URL(targetURL);
        //Opens the URL
        InputStream inputStream = url.openStream();
        //Reads through the URL
        Reader reader = new InputStreamReader(inputStream);
        //Creates a JsonObject containing the json of the webpage
        JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
        //Creates a JsonObject of the rates in fullobj
        JsonObject data = object.getAsJsonObject("rates");
        //Returns the exchange rate
        return data.get(currencyCode).getAsFloat();

}

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     *
     * @param fromCurrency
     *            the currency code we're exchanging *from*
     * @param toCurrency
     *            the currency code we're exchanging *to*
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(
            String fromCurrency, String toCurrency,
            int year, int month, int day) throws IOException {
        // TODO Your code here
        String targetURL = baseURL + year + "-" + correctDate(month) + "-" + correctDate(day) + "?access_key=" + accessKey;
        //Creates an URL
        URL url = new URL(targetURL);
        //Opens the URL
        InputStream inputStream = url.openStream();
        //Reads the input Stream
        Reader reader = new InputStreamReader(inputStream);
        //Creates a JSON object inputing from the webpage
        JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
        //Creates the rates object from the JSON
        JsonObject data = object.getAsJsonObject("rates");
        //Converts the two currencies to return the desired exchange rate
        return rates.get(fromCurrency).getAsFloat() / rates.get(toCurrency).getAsFloat();

    }
    //Pads to ensure if the date is less than 9, it is turned into a two digit integer
    public String correctDate(int num)
    {
        if(num < 10)
        {
            return "0" + Integer.toString(num);
        }
        else
        {
            return Integer.toString(num);
        }
    }
}


