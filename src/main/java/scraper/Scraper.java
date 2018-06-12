package scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Product;
import model.Results;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Scraper {

    private static Double getDoubleFromString(String s) {
        s = s.split(" ")[0];
        String cleanString = s.replaceAll("[^0-9.]", "");
        try {
            return Double.parseDouble(cleanString);
        } catch (Exception e) {
            System.err.println(s + " " + cleanString);
            throw e;
        }
    }

    public static List<Product> scrapeProductsFromUrl(String url) throws IOException {
        List<Product> productsList = new ArrayList<>();
        Document soup = Jsoup.connect(url).get();
        Elements products = soup.getElementsByClass("product");
        for(Element product : products) {
            Element linkTo = product.selectFirst("a");
            String title = product.selectFirst("a").text();
            String pricePerUnitString = product.selectFirst("p.pricePerUnit").text();
            Double pricePerUnit = getDoubleFromString(pricePerUnitString);

            String extraInfoLink = linkTo.attr("href");
            extraInfoLink = (new URL( new URL(url), extraInfoLink)).toString();
            Document detailSoup = Jsoup.connect(extraInfoLink).get();
            Element infoElement  = detailSoup.getElementById("information");
            String description = infoElement.selectFirst(".productText").text();

            Element nutritionTable = detailSoup.selectFirst(".nutritionTable");
            Double kCalPer100g = null;
            if (nutritionTable != null) {
                Element calorieCell = nutritionTable.selectFirst("table > tbody > tr:nth-child(2) > td:nth-child(1)");
                if (calorieCell != null) {
                    String calorieString = calorieCell.text();
                    kCalPer100g = getDoubleFromString(calorieString);
                }
            }

            productsList.add( new Product(title, description, pricePerUnit, kCalPer100g) );
        }
        return productsList;
    }

    public static void main(String... s) {
        String url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        try {
            List<Product> products = Scraper.scrapeProductsFromUrl(url);
            Results results = new Results(products);
            System.out.println(results);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
