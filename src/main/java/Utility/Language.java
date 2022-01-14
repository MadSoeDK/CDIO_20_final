package Utility;

import java.io.BufferedReader;
import java.io.FileReader;

public class Language {

    private static BufferedReader CSV;
    private static String[][] file;
    private static String language;

    public static String getText(String text) {
        for (int i = 0; i < file.length; i++) {
            String varName = file[i][0];
            if (varName.equals(text)) {
                if (language.equals("English")) {
                    return file[i][2];
                } else {
                    return file[i][1];
                }
            }
        }
        return "Text not found";
    }
    public static void setLanguage(String chosenLanguage) {
        language = chosenLanguage;
    }
    public static void read() {
        String row = "";
        file = new String[100][3];
        String path = "src/main/resources/language.csv";
        try {
            CSV = new BufferedReader(new FileReader(path));

            int i = 0;
            boolean firstline = true;
            while ((row = CSV.readLine()) != null) {

                if (firstline) {
                    firstline = false;
                    continue;
                }

                file[i] = row.split(",");

                i++;
            }
            CSV.close();

        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
