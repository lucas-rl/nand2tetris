package org.example.translate;

import java.util.ArrayList;
import java.util.List;

public class Label implements ITranslate{
    @Override
    public List<String> toAsm(String[] args) {
        List<String> asm = new ArrayList<>();
        asm.add("("+args[1]+")");
        return asm;
    }
}
