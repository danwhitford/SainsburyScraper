
import com.github.tomakehurst.wiremock.junit.WireMockRule;
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

    private Optional<Results> getResultsFromStub(String responseBody) {
        stubFor(get(urlEqualTo("/test_resource"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody(responseBody)));

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
        Optional<Results> results = getResultsFromStub(blank);
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
}
