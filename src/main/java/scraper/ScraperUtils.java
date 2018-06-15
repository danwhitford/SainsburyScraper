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


public class ScraperUtils {

    private static Double getDoubleFromString(String s) {
        s = s.split(" ")[0];
        String cleanString = s.replaceAll("[^0-9.]", "");
        return Double.parseDouble(cleanString);
    }

    private static Product processProductElement(Element product, String baseUrl) throws IOException {
        Element linkTo = product.selectFirst("a");
        String title = product.selectFirst("a").text();
        String pricePerUnitString = product.selectFirst("p.pricePerUnit").text();
        Double pricePerUnit = getDoubleFromString(pricePerUnitString);

        String extraInfoLink = linkTo.attr("href");
        extraInfoLink = (new URL( new URL(baseUrl), extraInfoLink)).toString();
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

        return new Product(title, description, pricePerUnit, kCalPer100g);
    }

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
