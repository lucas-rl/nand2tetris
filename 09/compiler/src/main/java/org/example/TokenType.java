package org.example;

public enum TokenType {
    KEYWORD,
    SYMBOL,
    IDENTIFIER,
    INT_CONST,
    STRING_CONST;

    @Override
    public String toString(){
        return this.name().toLowerCase();
    }
}
