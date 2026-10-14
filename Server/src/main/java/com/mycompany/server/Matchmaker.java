/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author User
 */
public class Matchmaker {

    private final List<Giocatore> pool;
    private final int ngg;
    private final int dml;

    public Matchmaker(int ngg, int dml) {
        this.pool = new ArrayList<>();
        this.ngg = ngg;
        this.dml = dml;
    }

    public synchronized void registraEAttendi(Giocatore giocatore)
            throws InterruptedException {

        pool.add(giocatore);

        System.out.println(
            "Giocatore in attesa: " + giocatore.nickname
            + " - livello " + giocatore.livello
            + " - pool: " + pool.size()
        );

        provaACreareGruppi();

        while (giocatore.lista == null) {
            wait();
        }
    }

    private void provaACreareGruppi() {

        boolean gruppoTrovato = true;

        while (gruppoTrovato) {
            gruppoTrovato = false;

            if (pool.size() < ngg) {
                return;
            }

            List<Giocatore> ordinati = new ArrayList<>(pool);

            ordinati.sort(
                Comparator.comparingInt(giocatore -> giocatore.livello)
            );

            for (int i = 0; i <= ordinati.size() - ngg; i++) {

                Giocatore primo = ordinati.get(i);
                Giocatore ultimo = ordinati.get(i + ngg - 1);

                int differenza = ultimo.livello - primo.livello;

                if (differenza <= dml) {

                    List<Giocatore> gruppo = new ArrayList<>();

                    for (int j = 0; j < ngg; j++) {
                        gruppo.add(ordinati.get(i + j));
                    }

                    String lista = creaLista(gruppo);

                    for (Giocatore giocatore : gruppo) {
                        giocatore.lista = lista;
                        pool.remove(giocatore);
                    }

                    System.out.println("Gruppo creato:");
                    System.out.println(lista);

                    notifyAll();

                    gruppoTrovato = true;
                    break;
                }
            }
        }
    }

    private String creaLista(List<Giocatore> gruppo) {

        StringBuilder risultato = new StringBuilder();

        for (Giocatore giocatore : gruppo) {
            risultato.append(giocatore.nickname)
                     .append(" | IP: ")
                     .append(giocatore.ip)
                     .append(" | Livello: ")
                     .append(giocatore.livello)
                     .append("\n");
        }

        return risultato.toString();
    }
}