import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {
    private static final Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {
        LinkedHashMap<LocalDate, String> holidays;
        int[] holidaysPerWeekday;
        int years;

        System.out.print("How many years do you want to calculate: ");
        years = inputInt(1, 500);

        holidays = getAllHolidaysFromFile(years);
        holidaysPerWeekday = countHolidaysForEachWeekday(holidays);

        System.out.println();

        for (int i = 0; i < 5; i++) {
            System.out.printf("%9s:   %d%n", DayOfWeek.of(i + 1), holidaysPerWeekday[i]);
        }
    }

    private static LinkedHashMap<LocalDate, String> getAllHolidaysFromFile(int years) {
        LinkedHashMap<LocalDate, String> holidays = new LinkedHashMap<>();

        int year = 2020;
        String path = "C:\\Users\\schul\\OneDrive - HTL Anichstrasse\\4AHWII\\SWP Rubner Szabolcs\\4AHWII_SWP_OP\\Holidays\\src\\holidays.json";
        String jsonString = readFile(path).toString();

        JSONObject date = new JSONObject(jsonString);
        JSONArray array = date.getJSONArray("holidays");

        for (int j = 0; j < years; j++) {
            for (int i = 0; i < array.length(); i++) {
                date = array.getJSONObject(i);
                holidays.put(LocalDate.of(year + j, (int) date.get("month"), (int) date.get("day")), date.get("name").toString());
            }
        }

        return holidays;
    }

    private static int[] countHolidaysForEachWeekday(LinkedHashMap<LocalDate, String> holidays) {
        int monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0;
        int previousYear = 2020;
        int[] holidaysPerWeekday = new int[5];

        for (Map.Entry<LocalDate, String> e : holidays.entrySet()) {
            switch (e.getKey().getDayOfWeek()) {
                case MONDAY -> monday++;
                case TUESDAY -> tuesday++;
                case WEDNESDAY -> wednesday++;
                case THURSDAY -> thursday++;
                case FRIDAY -> friday++;
            }

            if (e.getKey().getYear() > previousYear) {
                System.out.println();
                previousYear = e.getKey().getYear();
            }

            System.out.printf("%s   %19s   %9s%n", e.getKey().toString(), e.getValue(), e.getKey().getDayOfWeek().toString());
        }

        holidaysPerWeekday[0] = monday;
        holidaysPerWeekday[1] = tuesday;
        holidaysPerWeekday[2] = wednesday;
        holidaysPerWeekday[3] = thursday;
        holidaysPerWeekday[4] = friday;

        return holidaysPerWeekday;
    }

    private static StringBuilder readFile(String filename) {
        StringBuilder s = new StringBuilder();
        File file = new File(filename);

        try {
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                s.append(data);
            }

            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return s;
    }

    private static int inputInt(int lower, int upper) {
        boolean retry = true;
        int integer = 0;

        do {
            try {
                integer = Integer.parseInt(reader.nextLine());
                System.out.println();
            } catch (Exception e) {
                System.out.println("NumberFormatException");
            }
            if (integer >= lower && integer <= upper) {
                retry = false;
            } else {
                System.out.println("Only " + lower + " to " + upper + ".");
            }
        } while (retry);
        System.out.println();

        return integer;
    }
}