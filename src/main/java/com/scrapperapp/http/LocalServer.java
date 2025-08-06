package com.scrapperapp.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

public class LocalServer {

    private static final Logger logger = Logger.getLogger(LocalServer.class.getName());
    public HttpServer server;

    public LocalServer() throws IOException{
        server = HttpServer.create(new InetSocketAddress(8080), 0);
    }

    public void Initialize() throws IOException {
        server.setExecutor(null);
        server.start();

        logger.info("Server started");
    }
}
