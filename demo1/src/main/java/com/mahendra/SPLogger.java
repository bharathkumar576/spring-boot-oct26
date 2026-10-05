package com.mahendra;

public class SPLogger {
    private Logger log;

    public SPLogger(Logger log){
        this.log = log;
    }

    public void log(String message){
        drawLine(message.length()+2);
        log.log(message);
        drawLine(message.length()+2);
    }

    private void drawLine(int length){
        for(int i = 0; i < length; i++){
            System.out.print("-");
        }
        System.out.println();
    }
}
