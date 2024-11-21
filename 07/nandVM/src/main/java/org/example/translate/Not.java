package org.example.translate;

import java.util.ArrayList;
import java.util.List;

public class Not implements ITranslate{
    @Override
    public List<String> toAsm(String[] args) {
        List<String> asm = new ArrayList<>();
        asm.add("//not");
        asm.add("@SP");
        asm.add("A=M-1");
        asm.add("M=!M");
        return asm;
    }
}
