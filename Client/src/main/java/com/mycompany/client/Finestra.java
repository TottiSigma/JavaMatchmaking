/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client;

/**
 *
 * @author User
 */

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Finestra extends JFrame {

    private JTextField campoIp;
    private JTextField campoPorta;
    private JTextField campoNickname;
    private JTextField campoLivello;

    private JTextArea areaMessaggi;

    public Finestra() {

        super("Client");

        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        campoIp = new JTextField("127.0.0.1");
        campoPorta = new JTextField("50000");
        campoNickname = new JTextField();
        campoLivello = new JTextField();

        areaMessaggi = new JTextArea();
        areaMessaggi.setEditable(false);

        JButton bottone = new JButton("Connetti");

        bottone.addActionListener(e -> connetti());

        JPanel pannelloCampi = new JPanel(new GridLayout(4, 2, 10, 10));

        pannelloCampi.add(new JLabel("IP server:"));
        pannelloCampi.add(campoIp);

        pannelloCampi.add(new JLabel("Porta:"));
        pannelloCampi.add(campoPorta);

        pannelloCampi.add(new JLabel("Nickname:"));
        pannelloCampi.add(campoNickname);

        pannelloCampi.add(new JLabel("Livello:"));
        pannelloCampi.add(campoLivello);

        JPanel pannelloAlto = new JPanel(new BorderLayout());
        pannelloAlto.add(pannelloCampi, BorderLayout.CENTER);
        pannelloAlto.add(bottone, BorderLayout.SOUTH);

        setLayout(new BorderLayout());

        add(pannelloAlto, BorderLayout.NORTH);
        add(new JScrollPane(areaMessaggi), BorderLayout.CENTER);
    }

    private void connetti() {

        String ip = campoIp.getText().trim();
        String testoPorta = campoPorta.getText().trim();
        String nickname = campoNickname.getText().trim();
        String testoLivello = campoLivello.getText().trim();

        if (testoPorta.isEmpty() || nickname.isEmpty() || testoLivello.isEmpty()) {
            areaMessaggi.setText("Compila porta, nickname e livello.");
            return;
        }

        int porta;
        int livello;

        try {
            porta = Integer.parseInt(testoPorta);
            livello = Integer.parseInt(testoLivello);
        } catch (NumberFormatException e) {
            areaMessaggi.setText("Porta e livello devono essere numeri.");
            return;
        }

        try {

            Socket socket = new Socket(ip, porta);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            areaMessaggi.setText("Connesso al server.\n");
            areaMessaggi.append("Richiesta inviata...\n");

            out.println(nickname + " " + livello);

            String riga;

            while ((riga = in.readLine()) != null) {

                if (riga.equals("FINE")) {
                    break;
                }

                areaMessaggi.append(riga + "\n");
            }

            areaMessaggi.append("Connessione terminata.");

            socket.close();

        } catch (IOException e) {

            areaMessaggi.setText("Errore client: " + e.getMessage());
        }
    }
}