package com.pagespeed;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PageSpeedInsights {

    private static final String API_KEY = "AIzaSyAqyQLlg4I4e7AVfBEDweHQoPU_pUEs204";
    private static final String BASE_URL = "https://pagespeedonline.googleapis.com/pagespeedonline/v5/runPagespeed";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        String[] urlsToAnalyze = {
                "https://sitio.consorcio.cl/home",
                "https://sitio.consorcio.cl/home"
        };

        List<Callable<Void>> tasks = new ArrayList<>();

        for (String url : urlsToAnalyze) {
            tasks.add(() -> {
                analyzeUrl(url, "DESKTOP");
                analyzeUrl(url, "MOBILE");
                return null;
            });
        }

        List<Future<Void>> futures = executorService.invokeAll(tasks);

        for (Future<Void> future : futures) {
            future.get();
        }

        executorService.shutdown();
    }

    private static void analyzeUrl(String url, String strategy) {

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

        System.out.println("Results for " + strategy + " - " + url + ":");

        double accessibilityScore = categories.getJSONObject("accessibility").getDouble("score") * 100;
        System.out.println("Accessibility Score: " + accessibilityScore);

        double bestPracticesScore = categories.getJSONObject("best-practices").getDouble("score") * 100;
        System.out.println("Best Practices Score: " + bestPracticesScore);

        double performanceScore = categories.getJSONObject("performance").getDouble("score") * 100;
        System.out.println("Performance Score: " + performanceScore);

        double seoScore = categories.getJSONObject("seo").getDouble("score") * 100;
        System.out.println("SEO Score: " + seoScore);

        System.out.println();
    }
}
