package service;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;

public class CsvExportService {
    private static final String PATH = "./comenzi.csv";

    public static void salveazaComanda(String numeClient, double totalCos) {
        LocalDateTime now = LocalDateTime.now();
        String line = numeClient + "," + now + "," + totalCos + "\n";

        File file = new File(PATH);

        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
