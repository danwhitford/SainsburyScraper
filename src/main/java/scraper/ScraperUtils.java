package scraper;

import model.Product;
import model.Results;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of static methods that carry out the actual scraping
 */
public class ScraperUtils {

    /**
     *
     * @param s A string containing one or more doubles
     * @return The first double in the string
     */
    private static Double getDoubleFromString(String s) {
        s = s.split(" ")[0];
        String cleanString = s.replaceAll("[^0-9.]", "");
        return Double.parseDouble(cleanString);
    }

    /**
     * Take an HTML element and create a Product object. Uses the link to fetch other information.
     * @param product The HTML element of the product
     * @param baseUrl The base url, used to calculate the hyperlink for extra information.
     * @return A Product object.
     * @throws IOException If the hyperlink cannot be accessed throws exception.
     */
    private static Product processProductElement(Element product, String baseUrl) throws IOException {
        Element linkTo = product.selectFirst("a");
        String title = product.selectFirst("a").text();
        String pricePerUnitString = product.selectFirst("p.pricePerUnit").text();
        Double pricePerUnit = getDoubleFromString(pricePerUnitString);

        String extraInfoLink = linkTo.attr("href");
        // Go to details page and get more information
        extraInfoLink = (new URL( new URL(baseUrl), extraInfoLink)).toString();
        Document detailSoup = Jsoup.connect(extraInfoLink).get();
        Element infoElement  = detailSoup.getElementById("information");
        Element productText = infoElement.selectFirst(".productText");
        String description = "";
        for (Element e : productText.getElementsByTag("p") ) {
            if (e.text().length() > 0) {
                description = e.text();
                break;
            }
        }

        Element nutritionTable = detailSoup.selectFirst(".nutritionTable");
        Double kCalPer100g = null;
        if (nutritionTable != null) {
            Element calorieCell = nutritionTable.selectFirst("table > tbody > tr:nth-child(2) > td:nth-child(1)");
            if (calorieCell != null) {
                String calorieString = calorieCell.text();
                kCalPer100g = getDoubleFromString(calorieString);
            }
        }

        return new Product(title, description, pricePerUnit, kCalPer100g);
    }

    /**
     * Connects to the base url and constructs a Results object
     * @param url The base url to scrape
     * @return The Results of the scrape
     * @throws IOException If the URL or any child URLs are not valid will throw an exception
     */
    public static Results scrapeProductsFromUrl(String url) throws IOException {
        List<Product> productsList = new ArrayList<>();
        Document soup = Jsoup.connect(url).get();
        Elements products = soup.getElementsByClass("product");

        for(Element productElement : products) {
            productsList.add( processProductElement(productElement, url) );
        }
        return new Results(productsList);
    }
}
