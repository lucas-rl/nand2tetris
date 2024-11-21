package org.example;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {
    private final BufferedReader buffer;

    private String[] currentInstruction;
    private String nextInstruction;

    public Parser(BufferedReader buffer) throws IOException {
        this.buffer = buffer;
        this.nextInstruction= buffer.readLine();
    }

    public boolean hasMoreCommands() {
        return nextInstruction != null;
    }

    public void advance() throws IOException {
        currentInstruction= nextInstruction.split(" ");
        nextInstruction = buffer.readLine();
    }

    public VmCommand commandType(){
        if(currentInstruction.length == 1) return VmCommand.ARITHMETIC;
        return VmCommand.valueOf(currentInstruction[0].toUpperCase());
    }

    public String[] getCurrentInstruction() {
        return currentInstruction;
    }

    public String arg1(){
        if(currentInstruction.length == 1) return currentInstruction[0];
        return currentInstruction[1];
    }

    public String arg2(){
        return currentInstruction[2];
    }

    public void close() throws IOException {
        buffer.close();
    }

}
