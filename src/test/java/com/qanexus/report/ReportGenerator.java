package com.qanexus.report;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.util.Collections;

public class ReportGenerator {

    public static void main(String[] args) {
        File reportOutputDir = new File("target/cucumber-advanced-report");
        File jsonFile = new File("target/cucumber-report.json");

        Configuration config = new Configuration(reportOutputDir, "QA Nexus Project");

        ReportBuilder reportBuilder = new ReportBuilder(
                Collections.singletonList(jsonFile.getAbsolutePath()),
                config
        );

        reportBuilder.generateReports();
    }
}
