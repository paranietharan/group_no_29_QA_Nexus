package com.qanexus.report;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReportServer {
    public static void main(String[] args) throws IOException {
        String root = args.length > 0 ? args[0] : "target/cucumber-advanced-report";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8081;

        Path rootPath = Paths.get(root).toAbsolutePath().normalize();
        System.out.println("Looking for reports in: " + rootPath);

        Path htmlRoot = rootPath.resolve("cucumber-html-reports");
        if (Files.isDirectory(htmlRoot)) {
            System.out.println("Found cucumber-html-reports subdirectory");
            rootPath = htmlRoot;
        }

        System.out.println("\nFiles in report directory:");
        if (Files.exists(rootPath) && Files.isDirectory(rootPath)) {
            Files.list(rootPath).forEach(p -> System.out.println("  - " + p.getFileName()));
        } else {
            System.err.println("ERROR: Report directory does not exist: " + rootPath);
            System.err.println("Please run: mvn clean test");
            System.err.println("Then run: mvn -q exec:java@report");
            System.exit(1);
        }

        String indexFile = "overview-features.html";
        if (Files.exists(rootPath.resolve("index.html"))) {
            indexFile = "index.html";
        } else if (Files.exists(rootPath.resolve("overview-features.html"))) {
            indexFile = "overview-features.html";
        } else {
            System.err.println("WARNING: Neither index.html nor overview-features.html found!");
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticHandler(rootPath, indexFile));
        server.start();

        System.out.println("\n==============================================");
        System.out.println("Report server running successfully!");
        System.out.println("==============================================");
        System.out.println("Open in browser: http://localhost:" + port + "/" + indexFile);
        System.out.println("Or simply: http://localhost:" + port);
        System.out.println("\nPress Ctrl+C to stop the server");
        System.out.println("==============================================\n");
    }

    private static class StaticHandler implements HttpHandler {
        private final Path root;
        private final String indexFile;

        private StaticHandler(Path root, String indexFile) {
            this.root = root;
            this.indexFile = indexFile;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            System.out.println("Request: " + path);

            if (path.equals("/")) {
                path = "/" + indexFile;
            }

            String relativePath = path.startsWith("/") ? path.substring(1) : path;
            Path file = root.resolve(relativePath).normalize();

            if (!file.startsWith(root)) {
                System.err.println("Security violation: " + file);
                exchange.sendResponseHeaders(403, -1);
                return;
            }

            if (!Files.exists(file)) {
                System.err.println("File not found: " + file);
                String msg = "404 - File not found: " + path;
                exchange.sendResponseHeaders(404, msg.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(msg.getBytes());
                }
                return;
            }

            if (Files.isDirectory(file)) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            byte[] data = Files.readAllBytes(file);
            Headers headers = exchange.getResponseHeaders();

            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                String name = file.getFileName().toString().toLowerCase();
                if (name.endsWith(".html")) contentType = "text/html";
                else if (name.endsWith(".css")) contentType = "text/css";
                else if (name.endsWith(".js")) contentType = "application/javascript";
                else if (name.endsWith(".json")) contentType = "application/json";
                else if (name.endsWith(".png")) contentType = "image/png";
                else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) contentType = "image/jpeg";
            }

            if (contentType != null) {
                headers.set("Content-Type", contentType);
            }

            System.out.println("Serving: " + file.getFileName() + " (" + data.length + " bytes)");
            exchange.sendResponseHeaders(200, data.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        }
    }
}
