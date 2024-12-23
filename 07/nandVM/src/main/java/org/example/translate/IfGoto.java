package org.example.translate;

import java.util.ArrayList;
import java.util.List;

public class IfGoto implements ITranslate{
    @Override
    public List<String> toAsm(String[] args) {
        List<String> asm = new ArrayList<>();
        asm.add("@SP");
        asm.add("A=A-1");
        asm.add("D=A");
        asm.add("@" + args[1]);
        asm.add("D;JNE");
        return asm;
    }
}
