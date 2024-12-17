package org.example;

import java.io.*;

import static org.example.VmCommand.*;

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedReader input = new BufferedReader(new FileReader("/home/llima13/Documents/nand2tetris/projects/7/StackArithmetic/StackTest/StackTest.vm"));
        BufferedWriter output = new BufferedWriter(new FileWriter("/home/llima13/Documents/nand2tetris/projects/7/StackArithmetic/StackTest/StackTestVM.asm"));
        Parser parser = new Parser(input);
        CodeWriter codeWriter = new CodeWriter(output);

        while (parser.hasMoreCommands()){
            parser.advance();
            VmCommand commandType = parser.commandType();
            String[] argumentos = new String[2];
            argumentos[0] = parser.arg1();
            if(!commandType.equals(ARITHMETIC))
                argumentos[1] = parser.arg2();

            String[] instruction = parser.getCurrentInstruction();

            switch (commandType){
                case ARITHMETIC:
                    codeWriter.writeArithmetic(argumentos);
                    break;
                case PUSH:
                case POP:
                    codeWriter.writePushPop(commandType, argumentos);

            }
        }

        parser.close();
        codeWriter.save();

    }
}