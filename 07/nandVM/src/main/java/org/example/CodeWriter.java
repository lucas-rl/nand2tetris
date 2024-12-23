package org.example;

import org.example.translate.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeWriter {

    private static Map<String, ITranslate> translators =
            new HashMap<>();

    static {
        translators.put("PUSH", new Push());
        translators.put("POP", new Pop());
        translators.put("add", new Add());
        translators.put("and", new And());
        translators.put("eq", new Eq());
        translators.put("gt", new Gt());
        translators.put("lt", new Lt());
        translators.put("neg", new Neg());
        translators.put("not", new Not());
        translators.put("or", new Or());
        translators.put("sub", new Sub());
        translators.put("label", new Label());
        translators.put("goto", new Goto());
        translators.put("if-goto", new IfGoto());
    }

    BufferedWriter buffer;
    public CodeWriter(BufferedWriter buffer){
        this.buffer = buffer;
    }

    public void writeArithmetic(String[] command) throws IOException {
        ITranslate translator = translators.get(command[0]);
        writeCommand(translator.toAsm(null));
    }

    public void writePushPop(VmCommand command, String[] argumentos) throws IOException {
        ITranslate translator = translators.get(command.name());
        writeCommand(translator.toAsm(argumentos));
    }

    public void writeLabel(String[] argumentos) throws IOException {
        ITranslate translator = translators.get(argumentos[0]);
        writeCommand(translator.toAsm(argumentos));
    }

    private void writeCommand(List<String> asm) throws IOException {
        for(String linha : asm){
            buffer.write(linha);
            buffer.write("\n");
        }
    }

    public void save() throws IOException {
        buffer.flush();
        buffer.close();
    }

}
