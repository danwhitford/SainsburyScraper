package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
    public static void main(String... s) throws Exception {
        String url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        Document soup = Jsoup.connect(url).get();
        Elements products = soup.getElementsByClass("product");
        for(Element product : products) {
            Element linkTo = product.selectFirst("a");
            String title = linkTo.text();
            String pricePerUnit = product.selectFirst("p.pricePerUnit").text();

            System.out.println(title + pricePerUnit);
        }
    }
}
