package com.pagespeed;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PageSpeedInsightsTest {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

        String[] urlsToAnalyze = {
                "https://sitio.consorcio.cl/home",
                "https://sitio.consorcio.cl/banca-personas/credito-de-consumo",
                "https://sitio.consorcio.cl/banca-personas/credito-hipotecario",
                "https://sitio.consorcio.cl/banca-personas/cuenta-vista",
                "https://sitio.consorcio.cl/banca-personas/cuenta-corriente",
                "https://sitio.consorcio.cl/banca-personas/cuenta-corriente-digital",
                "https://sitio.consorcio.cl/beneficios",
                "https://sitio.consorcio.cl/banca-personas/tarjeta-de-credito",
                "https://sitio.consorcio.cl/banca-personas/cuentas-bancarias",
                "https://sitio.consorcio.cl/banca-personas/deposito-a-plazo-online",
                "https://sitio.consorcio.cl/rentas-vitalicias",
                "https://sitio.consorcio.cl/seguro-automotriz",
                "https://sitio.consorcio.cl/seguros-de-salud",
                "https://sitio.consorcio.cl/seguro-hogar/seguro-hogar-digital",
                "https://sitio.consorcio.cl/seguro-hogar",
                "https://sitio.consorcio.cl/seguro-vida/seguro-apv",
                "https://sitio.consorcio.cl/seguro-vida",
                "https://sitio.consorcio.cl/seguro-vida/seguro-de-vida-proteccion",
                "https://sitio.consorcio.cl/seguro-vida/seguro-de-vida-con-ahorro",
                "https://sitio.consorcio.cl/seguro-vida/seguro-accidentes-personales",
                "https://sitio.consorcio.cl/seguro-vida/seguro-vida-digital",
                "https://sitio.consorcio.cl/seguro-vida/seguro-de-vida-con-ahorro/formulario",
                "https://sitio.consorcio.cl/seguro-vida/seguro-de-vida-proteccion/formulario",
                "https://sitio.consorcio.cl/seguro-vida/seguro-apv/apv-prime",
                "https://sitio.consorcio.cl/seguro-vida/seguro-apv/apv-mas",
                "https://sitio.consorcio.cl/seguro-vida/seguro-apv/apv-fondo-experto",
                "https://sitio.consorcio.cl/denuncia-tu-siniestro",
                "https://sitio.consorcio.cl/denuncia-tu-siniestro/seguimiento-de-siniestros",
                "https://sitio.consorcio.cl/comunicados/actualiza-tus-datos",
                "https://sitio.consorcio.cl/home/sucursales",
                "https://sitio.consorcio.cl/comunicados/formulario-de-contacto",
                "https://sitio.consorcio.cl/banca-personas/nuevo-cliente"
        };

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "resultados_pagespeed_" + timestamp + ".xlsx";

        Path resourcesDir = Paths.get("src/main/resources/");

        Path filePath = resourcesDir.resolve(fileName);

        ExcelWriter excelWriter = new ExcelWriter(filePath.toString());
        excelWriter.writeHeader();

        List<Callable<Void>> tasks = new ArrayList<>();
        for (String url : urlsToAnalyze) {
            tasks.add(() -> {
                PageSpeedService.analyzeUrl(url, "DESKTOP", excelWriter);
                PageSpeedService.analyzeUrl(url, "MOBILE", excelWriter);
                return null;
            });
        }

        List<Future<Void>> futures = executorService.invokeAll(tasks);

        for (Future<Void> future : futures) {
            future.get();
        }

        excelWriter.saveToFile();

        executorService.shutdown();
        System.out.println("Resultados guardados en " + fileName);
    }
}
