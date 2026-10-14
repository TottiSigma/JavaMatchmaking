/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

/**
 *
 * @author User
 */
public class Giocatore {

    public final String nickname;
    public final int livello;
    public final String ip;

    public String lista;

    public Giocatore(String nickname, int livello, String ip) {
        this.nickname = nickname;
        this.livello = livello;
        this.ip = ip;
        this.lista = null;
    }
}