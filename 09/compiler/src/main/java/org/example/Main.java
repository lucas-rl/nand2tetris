package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        Tokenizer tokenizer = new Tokenizer(reader);
        while (tokenizer.hasMoreTokens()){
            tokenizer.advance();
            System.out.print(tokenizer.tokenType() + ":  ");
            String token = "";
            switch (tokenizer.tokenType()){
                case KEYWORD -> token = tokenizer.keyword();
                case SYMBOL -> token = tokenizer.symbol();
                case STRING_CONST -> token = tokenizer.stringVal();
                case IDENTIFIER -> token = tokenizer.identifier();
                case INT_CONST -> token = tokenizer.intVal();
            }
            System.out.println(token);
        }
        reader.close();
    }
}