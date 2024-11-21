package org.example.translate;

import org.example.Segments;

import java.util.ArrayList;
import java.util.List;

public class Push implements ITranslate{

    @Override
    public List<String> toAsm(String[] args) {
        List<String> asm = new ArrayList<>();
        Segments pointer = Segments.find(args[0]);
        asm.add("//push");
        asm.add("@"+args[1]);
        asm.add("D=A");
        if (pointer != null) {
            if(pointer.equals(Segments.TEMP)){
                asm.add("@5");
            } else if(pointer.equals(Segments.POINTER)){
                asm.add("@3");
            } else {
                asm.add("@" + pointer.name());
            }
            asm.add("A=D+M");
            asm.add("D=M");
        }
        asm.add("@SP");
        asm.add("A=M");
        asm.add("M=D");
        asm.add("@SP");
        asm.add("M=M+1");
        return asm;
    }
}
