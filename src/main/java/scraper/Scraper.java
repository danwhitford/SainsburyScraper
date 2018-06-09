package scraper;

import model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    private static Double priceStringToDouble (String priceString) {
        priceString = priceString.substring(1);
        priceString = priceString.split("/")[0];
        return Double.parseDouble(priceString);
    }

    public static void main(String... s) throws Exception {
        List<Product> productsList = new ArrayList<>();
        String url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        Document soup = Jsoup.connect(url).get();
        Elements products = soup.getElementsByClass("product");
        for(Element product : products) {
            Element linkTo = product.selectFirst("a");
            String title = product.selectFirst("a").text();
            String pricePerUnitString = product.selectFirst("p.pricePerUnit").text();
            Double pricePerUnit = priceStringToDouble(pricePerUnitString);
            String pricePerKiloString = product.select("p.pricePerMeasure").text();
            Double pricePer100g = priceStringToDouble(pricePerKiloString) / 10;

            String extraInfoLink = linkTo.attr("href");
            extraInfoLink = (new URL( new URL(url), extraInfoLink)).toString();
            Document detailSoup = Jsoup.connect(extraInfoLink).get();
            Element infoElement  = detailSoup.getElementById("information");
            String description = infoElement.selectFirst(".productText").text();

            productsList.add( new Product(title, description, pricePerUnit, pricePer100g) );
        }

        for (Product p : productsList) {
            System.out.println(p);
        }
    }
}
