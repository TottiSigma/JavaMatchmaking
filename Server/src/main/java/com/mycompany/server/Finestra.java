/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author User
 */
public class Finestra extends JFrame {

    private JTextField campoNumeroGiocatori;
    private JTextField campoDifferenza;
    private JTextField campoPorta;

    private JTextArea areaLog;

    public Finestra() {

        super("Server Matchmaking");

        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        campoNumeroGiocatori = new JTextField("2");
        campoDifferenza = new JTextField("5");
        campoPorta = new JTextField("50000");

        areaLog = new JTextArea();
        areaLog.setEditable(false);

        JButton bottone = new JButton("Avvia server");

        bottone.addActionListener(e -> avviaServer());

        JPanel pannelloCampi = new JPanel(new GridLayout(3, 2, 10, 10));

        pannelloCampi.add(new JLabel("Numero giocatori per gruppo:"));
        pannelloCampi.add(campoNumeroGiocatori);

        pannelloCampi.add(new JLabel("Differenza massima livelli:"));
        pannelloCampi.add(campoDifferenza);

        pannelloCampi.add(new JLabel("Porta:"));
        pannelloCampi.add(campoPorta);

        JPanel pannelloAlto = new JPanel(new BorderLayout());
        pannelloAlto.add(pannelloCampi, BorderLayout.CENTER);
        pannelloAlto.add(bottone, BorderLayout.SOUTH);

        setLayout(new BorderLayout());

        add(pannelloAlto, BorderLayout.NORTH);
        add(new JScrollPane(areaLog), BorderLayout.CENTER);
    }

    private void avviaServer() {

        String testoNumeroGiocatori = campoNumeroGiocatori.getText().trim();
        String testoDifferenza = campoDifferenza.getText().trim();
        String testoPorta = campoPorta.getText().trim();

        if (testoNumeroGiocatori.isEmpty()
                || testoDifferenza.isEmpty()
                || testoPorta.isEmpty()) {

            areaLog.setText("Compila tutti i campi.");
            return;
        }

        int ngg;
        int dml;
        int porta;

        try {
            ngg = Integer.parseInt(testoNumeroGiocatori);
            dml = Integer.parseInt(testoDifferenza);
            porta = Integer.parseInt(testoPorta);
        } catch (NumberFormatException e) {
            areaLog.setText("Inserisci solo numeri interi.");
            return;
        }

        if (ngg < 2) {
            areaLog.setText("Il numero di giocatori deve essere almeno 2.");
            return;
        }

        areaLog.setText("Avvio server sulla porta " + porta + "...\n");

        new Thread(new Runnable() {

            @Override
            public void run() {
                avviaConnessioni(ngg, dml, porta);
            }
        }).start();
    }

    private void avviaConnessioni(int ngg, int dml, int porta) {

        Matchmaker matchmaker = new Matchmaker(ngg, dml);

        try (ServerSocket socketAccettazione = new ServerSocket(porta)) {

            scrivi("Server avviato sulla porta " + porta + "\n");
            scrivi("ngg = " + ngg + ", dml = " + dml + "\n");

            while (true) {

                Socket socket = socketAccettazione.accept();

                String ip = socket.getInetAddress().getHostAddress();

                scrivi("Client connesso: " + ip + "\n");

                GestoreClient gestore = new GestoreClient(socket, matchmaker);

                new Thread(gestore).start();
            }

        } catch (IOException e) {
            scrivi("Errore server: " + e.getMessage() + "\n");
        }
    }

    private void scrivi(String messaggio) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                areaLog.append(messaggio);
            }
        });
    }
}
