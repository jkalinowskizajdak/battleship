package com.battleship;

import com.battleship.boundary.filter.AuthFilter;
import com.battleship.control.GameUtil;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class BattleshipServer {

    private static final String HOST = "http://localhost";
    private static final String PORT = "8080";
    private static final String REST_PATH = "rest";
    private static final String BASE_URI = HOST + ":" + PORT + "/" + REST_PATH;
    private static final Logger logger = Logger.getLogger(BattleshipServer.class);
    private static final String START_INFO = "Battleship server has been started.";

    private BattleshipServer() {

    }

    private static HttpServer startServer() {
        ResourceConfig rc = new ResourceConfig().packages("com.battleship.boundary");
        rc.register(AuthFilter.class);
        rc.register(JsonMapper.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        return server;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpServer server = startServer();
        server.start();
        logger.info(START_INFO);
        System.out.println(START_INFO);
        System.out.println("Host : " + BASE_URI + GameUtil.GAME_PATH);
        System.out.println("Stop the server using CTRL+C");
        Thread.currentThread().join();
    }
}
