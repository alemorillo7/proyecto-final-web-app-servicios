package com.app.servicios.excepciones;

public class MiExcepcion extends Exception{
    public MiExcepcion(String msg){
        super(msg);
    }
    public MiExcepcion(String msg, Exception e){
        super(msg, e);
    }

}