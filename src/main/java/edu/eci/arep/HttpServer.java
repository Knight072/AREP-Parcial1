package edu.eci.arep;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    private int port;
    private String staticFiles;
    private static HttpServer instance;

    public HttpServer() {
        this.port = 8080;
    }

    public static HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }
        return instance;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor escuchando en el puerto " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            run(clientSocket);
        }
    }

    private void run(Socket clientSocket) throws IOException {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()) {
            String requestLine = in.readLine();
            if (requestLine == null) return;
            String method = requestLine.split(" ")[0];
            String resource = requestLine.split(" ")[1];
            callMethod(out, method, resource);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void callMethod(OutputStream out, String method, String resource) throws IOException {
        try {
            if (method.equals("GET")) {
                if (resource.startsWith("/calculadora/PI")) {
                    Method methodService = PathServices.findMappingMethod(resource, method);
                    String response = (String) methodService.invoke(null, null);

                    //if (response != null) sendResponse(out, "200 OK", "application/json", resource.getBytes());

                } else if (resource.equals("/calculadora/")) {
                    Method methodService = PathServices.findMappingMethod(resource, method);
                    String response = (String) methodService.invoke(null, null);
                    if (response != null) sendResponse(out, "200 OK", "application/json", response.getBytes());
                }
            } else {
                sendResponse(out, "405 Method Not Allowed", "text/plain", "Método no permitido".getBytes());
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    public void setStaticFiles(String staticFiles) {
        this.staticFiles = staticFiles;
    }

    public void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 " + status);
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + content.length);
        writer.println();
        writer.flush();

        out.write(content);
        out.flush();
    }


}

