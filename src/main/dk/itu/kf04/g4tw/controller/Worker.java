package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.HTTPConstants;

import java.net.Socket;

/**
 * A worker thread for the WebServer.
 */
public class Worker implements HTTPConstants, Runnable {

    private Socket socket;
    
    Worker(Socket socket) {
        this.socket = socket;
    }

    public synchronized void run() {
        while (true) {

        }
    }

}
