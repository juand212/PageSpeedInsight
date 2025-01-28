package com.pagespeed;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
public class ExcelWriter {
    private final String filePath;
    private final XSSFWorkbook workbook;
    private final Sheet sheet;

    public ExcelWriter(String filePath) {
        this.filePath = filePath;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Resultados PageSpeed");
    }

    public void writeHeader() {
        Row headerRow = sheet.createRow(0);
        String[] columns = {"URL", "Estrategia", "Accesibilidad", "Mejores prácticas", "Rendimiento", "SEO"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }
    }

    public void writeResults(String url, String strategy, double accessibilityScore, double bestPracticesScore, double performanceScore, double seoScore) {
        int rowNum = sheet.getPhysicalNumberOfRows();
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(url);
        row.createCell(1).setCellValue(strategy);
        row.createCell(2).setCellValue(accessibilityScore);
        row.createCell(3).setCellValue(bestPracticesScore);
        row.createCell(4).setCellValue(performanceScore);
        row.createCell(5).setCellValue(seoScore);
    }

    public void saveToFile() throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }
}
