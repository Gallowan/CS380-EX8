/**
 * CS 380.01 - Computer Networks
 * Professor: NDavarpanah
 *
 * Exercise 8
 * WebServer
 *
 * Justin Galloway
 */

import java.io.*;
import java.net.*;

public class WebServer {

    public static void main(String[]args) {
        System.out.println("\nServer Port: 8080 - Started");
        try {
            ServerSocket socket = new ServerSocket( 8080 );
            while(true) {
                Socket listener = socket.accept();
                httpRequest(listener);
                listener.close();
            }
        } catch (Exception e) {}
    }

    // Check if file exists and prepare it
    public static void httpResponse(Socket s, String filePath) throws Exception {

        File f = new File("www" + filePath );

        if(f.exists()) {
            System.out.println("\n\n\t" + f.getAbsolutePath());
            pageLoader(s, f, "200 OK");
        } else {
            System.out.println("\n\n\tFile does not exist: " + filePath);
            File notFound = new File("www/fileNotFound.html");
            pageLoader(s, notFound, "404 Not Found");
        }

    }

    // Read HTTP request
    public static void httpRequest(Socket s) throws Exception {
        InputStreamReader inputStr = new InputStreamReader(s.getInputStream());
        BufferedReader buffRead = new BufferedReader(inputStr);
        String request = "";
        String filePath = "";

        while((request = buffRead.readLine()) != null) {
            if(request.startsWith("GET")) {
                String[] split = request.split(" ");
                filePath = split[1];
                httpResponse(s, filePath);
                break;
            }

        }
    }

   // Send HTTP response with data
    public static void pageLoader(Socket s, File f, String status) throws Exception {

        PrintWriter printW = new PrintWriter(s.getOutputStream());
        BufferedReader buffRead = new BufferedReader(new FileReader(f));

        printW.println("HTTP/1.1 " + status);
        printW.println("Content-type: text/html");
        printW.println("Content-length: " + f.length());
        printW.println("\r\n");

        for(String lineCheck; (lineCheck = buffRead.readLine()) != null; ) {
            printW.println(lineCheck);
        }
        printW.flush();
        System.out.println("\tResponse - " + status);
    }
}