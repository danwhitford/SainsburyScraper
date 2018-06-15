
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import model.Product;
import model.Results;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import scraper.ScraperUtils;

import java.io.IOException;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class ScraperUtilsTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private Optional<Results> getResultsFromStub(String responseBody, String extraInfoBody) {
        stubFor(get(urlEqualTo("/test_resource"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody(responseBody)));

        stubFor(get(urlEqualTo("/extra"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody(extraInfoBody)));

        int port = wireMockRule.port();

        try {
            return Optional.of( ScraperUtils.scrapeProductsFromUrl("http://0.0.0.0:" + port + "/test_resource") );
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Test
    public void testEmpty() {
        String blank = "";
        Optional<Results> results = getResultsFromStub(blank, blank);
        Assert.assertTrue( results.isPresent() );
        String expectedResponse = "{\n" +
                "  \"results\" : [ ],\n" +
                "  \"total\" : {\n" +
                "    \"gross\" : 0.0,\n" +
                "    \"vat\" : 0.0\n" +
                "  }\n" +
                "}";
        Assert.assertEquals(expectedResponse, results.get().toString());
    }

    @Test
    public void testOneProduct() {
        String response = "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Strawberries</a>" +
                "            <p class=\"pricePerUnit\">£3.50</p>" +
                "        </div>" +
                "    </body>" +
                "</html>";
        String extraInfo =  "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "          <div id=\"information\">" +
                "               <p class=\"productText\">A strawberry</p>" +
                "           </div>" +
                "    </body>" +
                "</html>";

        Optional<Results> results = getResultsFromStub(response, extraInfo);
        Assert.assertTrue( results.isPresent() );
        Assert.assertEquals(1, results.get().getProducts().size());
        Product product = results.get().getProducts().get(0);
        Assert.assertEquals("Strawberries", product.getTitle());
        Assert.assertEquals(3.50, product.getPricePerUnit(), 0.0001);
        Assert.assertEquals("A strawberry", product.getDescription());
        Assert.assertNull(product.getKcalPer100G());

        Assert.assertEquals(3.50, results.get().getGross(), 0.0001 );
        Assert.assertEquals(3.50 * 0.2, results.get().getVat(), 0.0001 );
    }

    @Test
    public void testProductWithCalories() {
        String response = "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Mangoes</a>" +
                "            <p class=\"pricePerUnit\">£6.23</p>" +
                "        </div>" +
                "    </body>" +
                "</html>";
        String extraInfo =  "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "          <div id=\"information\">" +
                "               <p class=\"productText\">Some mangoes</p>" +
                "           </div>" +
                "                <table class=\"nutritionTable\">" +
                "                        <thead>" +
                "                        <tr class=\"tableTitleRow\">" +
                "                        <th scope=\"col\">Per 100g</th><th scope=\"col\">Per 100g&nbsp;</th><th scope=\"col\">% based on RI for Average Adult</th>" +
                "                        </tr>" +
                "                        </thead>" +
                "                        <tbody><tr class=\"tableRow1\">" +
                "                        <th scope=\"row\" class=\"rowHeader\" rowspan=\"2\">Energy</th><td class=\"tableRow1\">189kJ</td><td class=\"tableRow1\">-</td>" +
                "                        </tr>" +
                "                        <tr class=\"tableRow0\">" +
                "                        <td class=\"tableRow0\">45kcal</td><td class=\"tableRow0\">2%</td>" +
                "                        </tr>" +
                "                        </tbody>" +
                "                    </table>" + 
                "    </body>" +
                "</html>";

        Optional<Results> results = getResultsFromStub(response, extraInfo);
        Assert.assertTrue( results.isPresent() );
        Assert.assertEquals(1, results.get().getProducts().size());
        Product product = results.get().getProducts().get(0);
        Assert.assertEquals("Mangoes", product.getTitle());
        Assert.assertEquals(6.23, product.getPricePerUnit(), 0.0001);
        Assert.assertEquals("Some mangoes", product.getDescription());
        Assert.assertEquals(45, product.getKcalPer100G(), 0.0001);

        Assert.assertEquals(6.23, results.get().getGross(), 0.0001 );
        Assert.assertEquals(6.23 * 0.2, results.get().getVat(), 0.0001 );
    }

    @Test
    public void testManyProducts() {
        String response = "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Strawberries</a>" +
                "            <p class=\"pricePerUnit\">£3.50</p>" +
                "        </div>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Eggs</a>" +
                "            <p class=\"pricePerUnit\">£2.50</p>" +
                "        </div>" +
                "        <div class=\"product\">" +
                "            <a href=\"/extra\">Bread</a>" +
                "            <p class=\"pricePerUnit\">£1.50</p>" +
                "        </div>" +
                "    </body>" +
                "</html>";
        String extraInfo =  "<!DOCTYPE html>" +
                "<html>" +
                "    <head><title>Test</title></head>" +
                "    <body>" +
                "          <div id=\"information\">" +
                "               <p class=\"productText\">Extra information</p>" +
                "           </div>" +
                "    </body>" +
                "</html>";

        Optional<Results> results = getResultsFromStub(response, extraInfo);
        Assert.assertTrue( results.isPresent() );
        Assert.assertEquals(3, results.get().getProducts().size());

        String[] titles = { "Strawberries", "Eggs", "Bread" };
        double[] prices = { 3.50, 2.50, 1.50 };

        for(int i=0; i < results.get().getProducts().size(); ++i) {
            Product product = results.get().getProducts().get(i);
            Assert.assertEquals(titles[i], product.getTitle());
            Assert.assertEquals(prices[i], product.getPricePerUnit(), 0.0001);
            Assert.assertEquals("Extra information", product.getDescription());
            Assert.assertNull(product.getKcalPer100G());
        }

        Assert.assertEquals(7.50, results.get().getGross(), 0.0001 );
        Assert.assertEquals(7.50 * 0.2, results.get().getVat(), 0.0001 );
    }
}
