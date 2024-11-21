package org.example.translate;

import java.util.ArrayList;
import java.util.List;

public class Or implements ITranslate{
    @Override
    public List<String> toAsm(String[] args) {
        List<String> asm = new ArrayList<>();
        asm.add("//or");
        asm.add("@SP");
        asm.add("A=M-1");
        asm.add("D=M");
        asm.add("@SP");
        asm.add("A=M-1");
        asm.add("A=A-1");
        asm.add("D=D|M");
        asm.add("M=D");
        asm.add("@SP");
        asm.add("M=M-1");
        return asm;
    }
}
