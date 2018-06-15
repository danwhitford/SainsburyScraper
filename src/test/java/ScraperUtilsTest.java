
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import model.Results;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import scraper.ScraperUtils;

import java.io.IOException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class ScraperUtilsTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());


    @Test
    public void scrapeProductsFromUrl() {
        stubFor(get(urlEqualTo("/test_resource"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody("Hello world")));

        int port = wireMockRule.port();

        Results result = null;
        try {
            result = ScraperUtils.scrapeProductsFromUrl("http://0.0.0.0:" + port + "/test_resource");
            Assert.assertEquals(result.toString(), "Something");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
