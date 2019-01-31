/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread{
    int a;
    int b;
    public CountThread(int x, int y){
        a = x;
        b = y;
    }
    public void run(){
        for(int i = a; i < b; i++){
            System.out.println(i);
            try{
                Thread.sleep(1);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
