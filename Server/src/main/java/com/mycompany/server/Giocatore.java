/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

/**
 *
 * @author User
 */
public class Giocatore{

    public String nickname;
    public int livello;
    public String ip;

    public String lista;

    public Giocatore(String nickname, int livello, String ip){
        this.nickname = nickname;
        this.livello = livello;
        this.ip = ip;
        this.lista = null;
    }
    
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    
    public void setInt(int livello){
        this.livello = livello;
    }
    
    public void setIp(String ip){
        this.ip = ip;
    }
    
    public String getNickname(){
        return nickname;
    }
    
    public int getLivello(){
        return livello;
    }
    
    public String getIp(){
        return ip;
    }
}