/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author alberto
 */
public class Server {

    public static void main(String[] args) {

        Scanner tastiera = new Scanner(System.in);

        System.out.print("Numero giocatori per gruppo (ngg): ");
        int ngg = tastiera.nextInt();

        System.out.print("Differenza massima livelli (dml): ");
        int dml = tastiera.nextInt();

        int porta = 50000;

        Matchmaker matchmaker = new Matchmaker(ngg, dml);

        try (ServerSocket socketAccettazione = new ServerSocket(porta)) {

            System.out.println("Server avviato sulla porta " + porta);
            System.out.println("ngg = " + ngg + ", dml = " + dml);

            while (true) {
                Socket s = socketAccettazione.accept();

                System.out.println(
                    "Client connesso: "
                    + s.getInetAddress().getHostAddress()
                );

                GestoreClient gestore = new GestoreClient(
                    s,
                    matchmaker
                );

                Thread threadClient = new Thread(gestore);
                threadClient.start();
            }

        } catch (IOException e) {
            System.out.println("Errore server: " + e.getMessage());
        }
    }
}