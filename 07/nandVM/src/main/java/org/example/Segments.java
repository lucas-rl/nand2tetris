package org.example;

public enum Segments {
    SP(""),
    LCL("local"),
    ARG("argument"),
    THIS("this"),
    THAT("that"),
    TEMP("temp"),
    POINTER("pointer"),
    STATIC("static");

    String commamd;

    Segments(String commamd){
        this.commamd = commamd;
    }

    public static Segments find(String commamd){
        for(Segments pointer : Segments.values()){
            if(pointer.commamd.equals(commamd)){
                return pointer;
            }
        }
        return null;
    }
}
