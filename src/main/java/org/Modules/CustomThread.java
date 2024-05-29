package org.Modules;

import javafx.scene.control.Slider;

public class CustomThread extends Thread{
    volatile boolean goflag; //Volatile sirve para entrar más rapido a la memoria
    Slider slider;
    CustomThread (String name, Slider slider, boolean goflag){
        super(name);
        this.goflag = goflag;
        this.slider = slider;
    }
    public void run(){
        try{
            while(true){
                slider.setValue(slider.getValue()+1);
                Thread.sleep(1000);
                synchronized (this){
                    while (!goflag){
                        wait();
                    }
                }
            }
        } catch(InterruptedException e){

        }
    }
    public void newSuspend(){
        goflag = false;
    }
    synchronized public void newResume(){
        goflag = true;
        notify();
    }
}
