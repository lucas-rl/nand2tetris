package org.example.translate;

import java.util.ArrayList;
import java.util.List;

import static org.example.Util.randomLabel;

public class Lt implements ITranslate{
    @Override
    public List<String> toAsm(String[] args) {
        String labelTrue = randomLabel();
        String labelContinua = randomLabel();

        List<String> asm = new ArrayList<>();
        asm.add("//Eq");
        asm.add("@SP");
        asm.add("A=M-1");
        asm.add("A=A-1");
        asm.add("D=M");
        asm.add("@SP");
        asm.add("A=M-1");
        asm.add("D=D-M");

        asm.add("@"+labelTrue);
        asm.add("D;JLT");

        //false
        asm.add("@SP");
        asm.add("A=M-1");
        asm.add("A=A-1");
        asm.add("M=0");

        asm.add("@"+labelContinua);
        asm.add("0;JMP");

        //true
        asm.add("("+labelTrue+")");
        asm.add("@SP");
        asm.add("A=M-1");
        asm.add("A=A-1");
        asm.add("M=1");


        asm.add("("+labelContinua+")");
        asm.add("@SP");
        asm.add("M=M-1");
        return asm;
    }
}
