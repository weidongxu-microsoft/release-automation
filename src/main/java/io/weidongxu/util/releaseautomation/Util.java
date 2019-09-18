package io.weidongxu.util.releaseautomation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Util {

    public static void processFile(File file, BiFunction<List<String>, String, List<String>> processFunc) throws IOException {
        List<String> lines = new ArrayList<>();
        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines = processFunc.apply(lines, line);
            }
        }

        Util.writeToFile(file, lines);
    }

    public static void writeToFile(File file, String text) throws IOException {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter out = new BufferedWriter(fw)) {
            out.write(text);
        }
    }

    public static void writeToFile(File file, List<String> lines) throws IOException {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter out = new BufferedWriter(fw)) {
            for (String line : lines) {
                out.write(line);
                out.write("\n");
            }
        }
    }
}
