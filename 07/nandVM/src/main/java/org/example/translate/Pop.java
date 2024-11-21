package org.example.translate;

import org.example.Segments;

import java.util.ArrayList;
import java.util.List;

public class Pop implements ITranslate{
    @Override
    public List<String> toAsm(String[] args) {
        List<String> asm = new ArrayList<>();
        asm.add("//pop");

        //Poe em R13 endereço que vai receber valor
        Segments pointer = Segments.find(args[0]);
        asm.add("@"+args[1]);
        asm.add("D=A");
        if(pointer.equals(Segments.TEMP)){
            asm.add("@5");
        } else if(pointer.equals(Segments.POINTER)){
            asm.add("@3");
        } else {
            asm.add("@" + pointer.name());
        }
        asm.add("D=D+M");
        asm.add("@R13");
        asm.add("M=D");

        //Poe em D o valor e volta um endereço o SP
        asm.add("@SP");
        asm.add("M=M-1");
        asm.add("A=M");
        asm.add("D=M");

        //Poe valor no endereço correto
        asm.add("@R13");
        asm.add("A=M");
        asm.add("M=D");

        return asm;
    }
}
