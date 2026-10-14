/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienttest;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author User
 */
public class Finestra extends JFrame{

    private JTextField campoIp;
    private JTextField campoNumeroUtenti;

    private JTextArea areaUtenti;
    private JTextArea areaRisultati;

    private String[] utenti;
    private String[] risultati;

    private String[] nomi = {"Alberto", "Luca", "Marco", "Giulia", "Sara", "Andrea", "Matteo", "Chiara", "Davide", "Elisa"};
    private Random random = new Random();

    public Finestra(){

        super("Test matchmaking");

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        campoIp = new JTextField("127.0.0.1");
        campoNumeroUtenti = new JTextField("4");

        areaUtenti = new JTextArea();
        areaUtenti.setEditable(false);

        areaRisultati = new JTextArea();
        areaRisultati.setEditable(false);

        JButton bottoneGenera = new JButton("Genera utenti");
        JButton bottoneInvia = new JButton("Invia al server");

        bottoneGenera.addActionListener(e -> generaUtenti());
        bottoneInvia.addActionListener(e -> inviaUtenti());

        JPanel pannelloDati = new JPanel(new GridLayout(2, 2, 10, 10));

        pannelloDati.add(new JLabel("IP server:"));
        pannelloDati.add(campoIp);

        pannelloDati.add(new JLabel("Numero utenti:"));
        pannelloDati.add(campoNumeroUtenti);

        JPanel pannelloBottoni = new JPanel(new GridLayout(1, 2, 10, 10));

        pannelloBottoni.add(bottoneGenera);
        pannelloBottoni.add(bottoneInvia);

        JPanel pannelloAlto = new JPanel(new BorderLayout());

        pannelloAlto.add(pannelloDati, BorderLayout.CENTER);
        pannelloAlto.add(pannelloBottoni, BorderLayout.SOUTH);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 10, 10));

        pannelloCentrale.add(new JScrollPane(areaUtenti));
        pannelloCentrale.add(new JScrollPane(areaRisultati));

        setLayout(new BorderLayout());

        add(pannelloAlto, BorderLayout.NORTH);
        add(pannelloCentrale, BorderLayout.CENTER);

        generaUtenti();
    }

    private void generaUtenti(){

        String testoNumero = campoNumeroUtenti.getText().trim();

        int numero;

        try{
            numero = Integer.parseInt(testoNumero);
        }catch(NumberFormatException e){
            areaUtenti.setText("Inserisci un numero valido.");
            return;
        }

        utenti = new String[numero];
        risultati = new String[numero];
        String testo = "";
        for(int i = 0; i < numero; i++){

            String nickname = nomi[random.nextInt(nomi.length)] + random.nextInt(100);
            int livello = 1 + random.nextInt(50);

            utenti[i] = nickname + " " + livello;
            risultati[i] = "";

            testo = testo + utenti[i] + "\n";
        }

        areaUtenti.setText(testo);
        areaRisultati.setText("");
    }

    private void inviaUtenti(){

        String ip = campoIp.getText().trim();

        if(ip.isEmpty()){
            areaRisultati.setText("Inserisci l'IP del server.");
            return;
        }

        if(utenti == null || utenti.length == 0){
            areaRisultati.setText("Genera prima gli utenti.");
            return;
        }

        for(int i = 0; i < utenti.length; i++){
            int posizione = i;
            new Thread(new Runnable(){

                @Override
                public void run(){
                    connettiUtente(ip, posizione);
                }
            }).start();
        }
    }

    private void connettiUtente(String ip, int posizione){
        String utente = utenti[posizione];
        String messaggi = "UTENTE: " + utente + "\n";

        try{ 
            Socket socket = new Socket(ip, 50000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            out.println(utente);
            String riga;

            while((riga = in.readLine())!=null){
                if (riga.equals("FINE")){
                    break;
                }
                messaggi = messaggi + riga + "\n";
            }
            messaggi = messaggi + "Connessione terminata.\n";
            socket.close();
        }catch(IOException e){
            messaggi = messaggi + "Errore: " + e.getMessage() + "\n";
        }
        risultati[posizione] = messaggi;

        aggiornaRisultati();
    }

    private void aggiornaRisultati(){

        String testo = "";
        for(int i = 0; i < risultati.length; i++){
            if(risultati[i] != null){
                testo = testo + risultati[i] + "\n";
            }
        }
        areaRisultati.setText(testo);
    }
}