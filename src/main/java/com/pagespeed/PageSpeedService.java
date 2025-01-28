package com.pagespeed;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
public class PageSpeedService {

    private static final String BASE_URL = "https://pagespeedonline.googleapis.com/pagespeedonline/v5/runPagespeed";
    private static final String API_KEY = "AIzaSyCsA8gMP44kKC7RWkP-EkaZvVBPmsSOimw";

    public static void analyzeUrl(String url, String strategy, ExcelWriter excelWriter) {
        Response response = RestAssured
                .given()
                .queryParam("url", url)
                .queryParam("category", "ACCESSIBILITY")
                .queryParam("category", "BEST_PRACTICES")
                .queryParam("category", "PERFORMANCE")
                .queryParam("category", "SEO")
                .queryParam("strategy", strategy)
                .queryParam("key", API_KEY)
                .header("Accept", "application/json")
                .get(BASE_URL);

        String responseBody = response.getBody().asString();

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject lighthouseResult = jsonObject.getJSONObject("lighthouseResult");
        JSONObject categories = lighthouseResult.getJSONObject("categories");

        double accessibilityScore = categories.getJSONObject("accessibility").getDouble("score") * 100;
        double bestPracticesScore = categories.getJSONObject("best-practices").getDouble("score") * 100;
        double performanceScore = categories.getJSONObject("performance").getDouble("score") * 100;
        double seoScore = categories.getJSONObject("seo").getDouble("score") * 100;

        excelWriter.writeResults(url, strategy, accessibilityScore, bestPracticesScore, performanceScore, seoScore);
    }
}
