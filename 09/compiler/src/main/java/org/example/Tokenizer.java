package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.TokenType.*;

public class Tokenizer {
    private BufferedReader stream;
    private String currentToken;
    private ArrayList<String> tokens = new ArrayList<>();
    private String nextLine;
    private boolean openComment = false;

    static private final ArrayList<String> keywords = new ArrayList<>();
    static private final ArrayList<Character> symbols = new ArrayList<>();

    static {
        keywords.add("class");
        keywords.add("constructor");
        keywords.add("function");
        keywords.add("method");
        keywords.add("field");
        keywords.add("static");
        keywords.add("var");
        keywords.add("int");
        keywords.add("char");
        keywords.add("boolean");
        keywords.add("void");
        keywords.add("true");
        keywords.add("false");
        keywords.add("null");
        keywords.add("this");
        keywords.add("let");
        keywords.add("do");
        keywords.add("if");
        keywords.add("else");
        keywords.add("while");
        keywords.add("return");

        symbols.add('{');
        symbols.add('}');
        symbols.add('(');
        symbols.add(')');
        symbols.add('[');
        symbols.add(']');
        symbols.add('.');
        symbols.add(',');
        symbols.add(';');
        symbols.add('+');
        symbols.add('-');
        symbols.add('*');
        symbols.add('/');
        symbols.add('&');
        symbols.add('|');
        symbols.add('<');
        symbols.add('>');
        symbols.add('=');
        symbols.add('~');
    }

    public Tokenizer(BufferedReader stream) throws IOException {
        this.stream= stream;
        this.nextLine = stream.readLine();
    }

    public void advance() {
        currentToken = tokens.removeFirst();
    }

    public boolean hasMoreTokens() throws IOException {
        while (tokens.isEmpty() && nextLine != null){
            nextLine = nextLine.trim();
            nextLine = removeComment(nextLine);
            populateTokens();
        }
        return !tokens.isEmpty();
    }

    public TokenType tokenType(){
        if(currentToken.charAt(0) == '\"') return STRING_CONST;
        if(currentToken.length() == 1 && symbols.contains(currentToken.charAt(0))) return SYMBOL;
        if(keywords.contains(currentToken)) return KEYWORD;
        try{
            Integer.parseInt(currentToken);
            return INT_CONST;
        } catch (NumberFormatException ex){
            return IDENTIFIER;
        }
    }

    String keyword(){
        return currentToken;
    }
    String symbol(){
        return switch (currentToken){
            case "<" -> "&lt;";
            case ">" -> "&gt;";
            case "\"" -> "&quot;";
            case "&" -> "&amp;";
            default -> currentToken;
        };
    }
    String identifier(){
        return currentToken;
    }
    String intVal(){
        return currentToken;
    }
    String stringVal(){
        return currentToken.replace("\"", "");
    }

    private void populateTokens() throws IOException {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder tokenBuilder = new StringBuilder();
        boolean openString = false;
        char[] chars = nextLine.toCharArray();
        for(char c : chars){
            if(c == '\"'){
                openString = !openString;
                tokenBuilder.append(c);
                if(!openString){
                    tokens.add(tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                }
            }
            else if (openString){
                tokenBuilder.append(c);
            }
            else if (symbols.contains(c)){
                if(!tokenBuilder.isEmpty()){
                    tokens.add(tokenBuilder.toString());
                }
                tokens.add(String.valueOf(c));
                tokenBuilder = new StringBuilder();
            }
            else if(c == ' ') {
                if(!tokenBuilder.isEmpty()){
                    tokens.add(tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                }
            }
            else{
                tokenBuilder.append(c);
            }
        }
        if (!tokenBuilder.isEmpty()) tokens.add(tokenBuilder.toString());

        this.tokens.addAll(tokens);
        nextLine = stream.readLine();
    }

    private String removeComment(String line){
        char[] chars = line.toCharArray();
        StringBuilder noCommentLineBuilder = new StringBuilder();
        for(int i = 0 ; i < chars.length ; i++){
            if(openComment){
                if( i > 0 && chars[i] == '/' && chars[i-1] == '*'){
                    openComment = false;
                }
                continue;
            }
            if(chars[i] == '/' && chars[i+1] == '*'){
                openComment = true;
                continue;
            }
            if(chars[i] == '/' && chars[i+1] == '/'){
                break;
            }
            noCommentLineBuilder.append(chars[i]);
        }
        return noCommentLineBuilder.toString();
    }

}