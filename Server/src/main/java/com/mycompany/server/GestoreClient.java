/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author User
 */
public class GestoreClient implements Runnable{

    private Socket s;
    private Matchmaker matchmaker;

    public GestoreClient(Socket s, Matchmaker matchmaker){
        this.s = s;
        this.matchmaker = matchmaker;
    }

    @Override
    public void run(){

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            String riga = in.readLine();

            if (riga == null || riga.equals("")){
                s.close();
                return;
            }

            String parti[] = riga.split(" ");

            if (parti.length != 2){
                out.println("ERRORE: invia nickname livello");
                s.close();
                return;
            }

            String nickname = parti[0];
            int livello = Integer.parseInt(parti[1]);

            String ip = s.getInetAddress().getHostAddress();

            Giocatore giocatore = new Giocatore(nickname, livello, ip);

            matchmaker.Attesa(giocatore);

            out.println("GRUPPO TROVATO");
            out.println(giocatore.getLista());
            out.println("FINE");

            s.close();

        }catch(NumberFormatException e){
            System.out.println("Livello non valido");

        }catch(InterruptedException e){
            System.out.println("Thread interrotto");

        }catch(IOException e){
            System.out.println("Client disconnesso: " + e.getMessage());
        }
    }
}