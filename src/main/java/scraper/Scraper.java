package scraper;

import model.Results;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scraper {

    private static final Logger LOGGER = Logger.getLogger( ScraperUtils.class.getName() );

    public static void main(String... s) {
        String url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        try {
            Results results = ScraperUtils.scrapeProductsFromUrl(url);
            System.out.println(results);

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to read from url: " + url);
        }
    }
}
