package x.snowroller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Trådar
//Trådpool, ExecutorService
//Atomic operations



public class Main {
    //Skriv en server som sparar inkommande information
    // och sen returnerar all sparad information som svar

    public static List<String> billboard = Collections.synchronizedList(new ArrayList<>());



    public static void main(String[] args) {


        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(5050)) {
            while (true) {
                Socket client = serverSocket.accept();
               executorService.submit(() -> handleConnection(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConnection(Socket client) {
        try {
            System.out.println(client.getInetAddress());
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(500);
            var inputFromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));

            List<String> templist = new ArrayList<>();

            while (true) {
                var line = inputFromClient.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                templist.add(line);
                System.out.println(line);
            }

            synchronized (billboard){
                billboard.addAll(templist);
            }


            var outputToClient = new PrintWriter(client.getOutputStream());
            //outputToClient.print("HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n");
            synchronized (billboard) {
                for (String line : billboard) {
                    outputToClient.print(line + "\r\n");
                }
            }
            outputToClient.print("\r\n");
            outputToClient.flush();
            inputFromClient.close();
            outputToClient.close();
            client.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
