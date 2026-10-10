/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.client;

/**
 *
 * @author alberto
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        Scanner tastiera = new Scanner(System.in);

        System.out.print("IP server [127.0.0.1]: ");
        String ip = tastiera.nextLine().trim();

        if (ip.isEmpty()) {
            ip = "127.0.0.1";
        }

        System.out.print("Porta [50000]: ");
        String testoPorta = tastiera.nextLine().trim();

        int porta = 50000;

        if (!testoPorta.isEmpty()) {
            porta = Integer.parseInt(testoPorta);
        }

        System.out.print("Nickname: ");
        String nickname = tastiera.nextLine().trim();

        System.out.print("Livello: ");
        int livello = Integer.parseInt(tastiera.nextLine().trim());

        try (
            Socket s = new Socket(ip, porta);

            PrintWriter out = new PrintWriter(
                s.getOutputStream(),
                true
            );

            BufferedReader in = new BufferedReader(
                new InputStreamReader(s.getInputStream())
            )
        ) {
            System.out.println("Connesso al server.");
            System.out.println("Richiesta inviata. Attendo giocatori...");

            out.println(nickname + " " + livello);

            String riga;

            while ((riga = in.readLine()) != null) {

                if (riga.equals("FINE")) {
                    break;
                }

                System.out.println(riga);
            }

            System.out.println("Connessione terminata.");

        } catch (IOException e) {
            System.out.println("Errore client: " + e.getMessage());
        }
    }
}