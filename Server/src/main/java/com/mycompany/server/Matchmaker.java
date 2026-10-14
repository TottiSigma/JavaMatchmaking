/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Matchmaker{

    private final List<Giocatore> pool;
    private final int ngg;
    private final int dml;

    public Matchmaker(int ngg, int dml){
        this.pool = new ArrayList<>();
        this.ngg = ngg;
        this.dml = dml;
    }

    public synchronized void Attesa(Giocatore giocatore)
            throws InterruptedException{

        pool.add(giocatore);

        System.out.println("Giocatore in attesa: " + giocatore.getNickname() + " - livello " + giocatore.getLivello() + " - pool: " + pool.size());

        provaACreareGruppi();

        while(giocatore.getLista() == null){
            wait();
        }
    }

    private void provaACreareGruppi(){

        boolean gruppoTrovato = true;

        while(gruppoTrovato){
            gruppoTrovato = false;

            if(pool.size() < ngg){
                return;
            }

            List<Giocatore> ordinati = new ArrayList<>(pool);

            for(int i = 0; i < ordinati.size() - 1; i++){
                for(int j = 0; j < ordinati.size() - 1 - i; j++){
                    if(ordinati.get(j).getLivello() > ordinati.get(j + 1).getLivello()){
                        Giocatore temp = ordinati.get(j);
                        ordinati.set(j, ordinati.get(j + 1));
                        ordinati.set(j + 1, temp);
                    }
                }
            }

            for(int i = 0; i <= ordinati.size() - ngg; i++){
                Giocatore primo = ordinati.get(i);
                Giocatore ultimo = ordinati.get(i + ngg - 1);
                int differenza = ultimo.getLivello() - primo.getLivello();
                if(differenza <= dml){
                    List<Giocatore> gruppo = new ArrayList<>();
                    for(int j = 0; j < ngg; j++){
                        gruppo.add(ordinati.get(i + j));
                    }
                    String lista = creaLista(gruppo);

                    for(Giocatore giocatore : gruppo){
                        giocatore.setLista(lista);
                        pool.remove(giocatore);
                    }
                    
                    notifyAll();

                    System.out.println("Gruppo creato:");
                    System.out.println(lista);

                    gruppoTrovato = true;
                    break;
                }
            }
        }
    }

    private String creaLista(List<Giocatore> gruppo){
        String risultato = "";
        for (Giocatore giocatore : gruppo){
            risultato += giocatore.getNickname() + " | IP: " + giocatore.getIp() + " | Livello: " + giocatore.getLivello() + "\n";
        }
        return risultato;
    }
}