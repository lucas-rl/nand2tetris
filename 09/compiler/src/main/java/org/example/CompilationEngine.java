package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.TokenType.*;

public class CompilationEngine {
    private final Tokenizer tokenizer;
    private final BufferedWriter output;

    private static final List<String> op = new ArrayList<>();
    private static final List<String> unaryOp= new ArrayList<>();

    static {
        op.add("+");
        op.add("-");
        op.add("*");
        op.add("/");
        op.add("&amp;");//&
        op.add("|");
        op.add("&lt;");//<
        op.add("&gt;");//>
        op.add("=");

        unaryOp.add("-");
        unaryOp.add("~");

    }

    public CompilationEngine(Tokenizer tokenizer, BufferedWriter output){
        this.tokenizer = tokenizer;
        this.output = output;
    }

    private void advance() throws IOException {
        if(tokenizer.hasMoreTokens()){
            tokenizer.advance();
            System.out.println(tokenizer.keyword());
        }
    }

    public void compileClass() throws IOException {
        if(!tokenizer.hasMoreTokens()) return;
        advance();
        output.write("<class>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        output.write("<identifier>" + tokenizer.identifier() + "</identifier>");
        advance();
        output.write("<symbol>" + tokenizer.symbol() + "</symbol>");
        advance();
        while (KEYWORD.equals(tokenizer.tokenType()) && ("static".equals(tokenizer.keyword()) || "field".equals(tokenizer.keyword()))){
            compileClassVarDec();
        }
        while (!(SYMBOL.equals(tokenizer.tokenType()) && "}".equals(tokenizer.symbol()))){
            compileSubroutine();
        }
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//}
        output.write("</class>");
    }

    public void compileClassVarDec() throws IOException {
        output.write("<classVarDec>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        compileType();
        advance();
        output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
        advance();
        while(!";".equals(tokenizer.symbol())){
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//,
            advance();
            output.write("<identifier>"+tokenizer.identifier()+"</identifier>");//var
            advance();
        }
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//;
        output.write("</classVarDec>");
    }

    public void compileSubroutine() throws IOException {
        output.write("<subroutineDec>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        compileType();
        output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
        advance();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //(
        advance();
        compileParameterList();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //)
        advance();

        output.write("<subroutineBody>");
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //{
        advance();
        while(KEYWORD.equals(tokenizer.tokenType()) && "var".equals(tokenizer.keyword())){
            compileVarDec();
        }
        while (!(SYMBOL.equals(tokenizer.tokenType()) && "}".equals(tokenizer.symbol()))){
            compileStatements();
        }

        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //}
        advance();

        output.write("</subroutineBody>");
        output.write("</subroutineDec>");
    }

    public void compileStatements() throws IOException {
        output.write("<statements>");
        while (KEYWORD.equals(tokenizer.tokenType())){
            switch (tokenizer.keyword()){
                case "let" -> compileLet();
                case "if" -> compileIf();
                case "while" -> compileWhile();
                case "do" -> compileDo();
                case "return" -> compileReturn();
            }
        }
        output.write("</statements>");
    }

    public void compileLet() throws IOException {
        output.write("<letStatement>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
        advance();
        if("[".equals(tokenizer.symbol())){
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //[
            advance();
            compileExpression();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //]
            advance();
        }
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //=
        advance();
        compileExpression();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //;
        advance();
        output.write("</letStatement>");
    }

    public void compileIf() throws IOException {
        output.write("<ifStatement>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //(
        advance();
        compileExpression();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //)
        advance();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //{
        advance();
        compileStatements();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //}
        advance();
        if(KEYWORD.equals(tokenizer.tokenType()) && "else".equals(tokenizer.keyword())){
            output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
            advance();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //(
            advance();
            compileExpression();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //)
            advance();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //{
            advance();
            compileStatements();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //}
            advance();
        }
        output.write("</ifStatement>");
    }

    public void compileWhile() throws IOException {
        output.write("<whileStatement>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //(
        advance();
        compileExpression();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //)
        advance();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //{
        advance();
        compileStatements();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //}
        advance();
        output.write("</whileStatement>");
    }

    public void compileDo() throws IOException {
        output.write("<doStatement>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
        advance();
        if("(".equals(tokenizer.symbol())){
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//(
            advance();
            compileExpressionList();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//)
        }  else {
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//.
            advance();
            output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
            advance();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//(
            advance();
            compileExpressionList();
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//)
        }
        advance();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //;
        advance();
        output.write("</doStatement>");

    }

    public void compileReturn() throws IOException {
        output.write("<returnStatement>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        if(SYMBOL.equals(tokenizer.tokenType()) && ";".equals(tokenizer.symbol())){
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");
            advance();
            output.write("</returnStatement>");
            return;
        }
        compileExpression();
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>");
        advance();
        output.write("</returnStatement>");
    }

    public void compileExpression() throws IOException {
        output.write("<expression>");
        compileTerm();
        while (SYMBOL.equals(tokenizer.tokenType()) && op.contains(tokenizer.symbol())){
            output.write("<symbol>"+ tokenizer.symbol() + "</symbol>");
            advance();
            compileTerm();
        }
        output.write("</expression>");
    }

    public void compileTerm() throws IOException {
        output.write("<term>");
        TokenType type = tokenizer.tokenType();
        switch (type){
            case INT_CONST -> {
                output.write("<"+type+">"+tokenizer.intVal()+"</"+type+">");
                advance();
            }
            case STRING_CONST -> {
                output.write("<"+type+">"+tokenizer.stringVal()+"</"+type+">");
                advance();
            }
            case KEYWORD -> {
                output.write("<keyword>"+tokenizer.keyword()+"</keyword>");
                advance();
            }
            case SYMBOL -> {
                output.write("<symbol>"+tokenizer.symbol()+"</symbol>");
                advance();
                if(unaryOp.contains(tokenizer.symbol())){
                    compileTerm();
                } else {
                    compileExpression();
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//)
                    advance();
                }
            }
            case IDENTIFIER -> {
                output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
                advance();
                if("(".equals(tokenizer.symbol())){
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//(
                    advance();
                    compileExpressionList();
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//)
                    advance();
                } else if ("[".equals(tokenizer.symbol())) {
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//[
                    advance();
                    compileExpression();
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//[
                    advance();
                } else if(".".equals(tokenizer.symbol())){
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//.
                    advance();
                    output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
                    advance();
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//(
                    advance();
                    compileExpressionList();
                    output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//)
                    advance();
                }
            }
        }
        output.write("</term>");
    }

    public void compileExpressionList() throws IOException {
        output.write("<expressionList>");
        if(SYMBOL.equals(tokenizer.tokenType()) && ")".equals(tokenizer.symbol())){
            output.write("</expressionList>");
            return;
        }
        compileExpression();
        while (SYMBOL.equals(tokenizer.tokenType()) && ",".equals(tokenizer.symbol())){
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>");//,
            advance();
            compileExpression();
        }
        output.write("</expressionList>");
    }

    public void compileParameterList() throws IOException {
        output.write("<parameterList>");
        if(SYMBOL.equals(tokenizer.tokenType()) && ")".equals(tokenizer.symbol())){
            output.write("</parameterList>");
            return;
        }
        compileType();
        output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
        advance();
        while(",".equals(tokenizer.symbol())){
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //,
            compileType();
            output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
            advance();
        }
        output.write("</parameterList>");
    }

    public void compileVarDec() throws IOException {
        output.write("<varDec>");
        output.write("<keyword>" + tokenizer.keyword() + "</keyword>");
        advance();
        compileType();
        output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
        advance();
        while(",".equals(tokenizer.symbol())){
            output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //,
            advance();
            output.write("<identifier>"+tokenizer.identifier()+"</identifier>");
            advance();
        }
        output.write("<symbol>"+tokenizer.symbol()+"</symbol>"); //;
        output.write("</varDec>");

        advance();
    }

    private void compileType() throws IOException {
        output.write(
                "<"+tokenizer.tokenType().toString()+">"+
                        (tokenizer.tokenType().equals(KEYWORD) ? tokenizer.keyword() : tokenizer.identifier())+
                        "</"+tokenizer.tokenType().toString()+">"
        );
        advance();
    }

}
