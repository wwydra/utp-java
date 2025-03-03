package zad1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TravelData {
    private final List<String> list = new ArrayList<>();

    public TravelData(File argFile) {
        File[] files = argFile.listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    list.add(reader.readLine());
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getOffersDescriptionsList(String loc, String dateFormat) {
        List<String> resList = new ArrayList<>();
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat(dateFormat, new Locale(loc));

        for (String offer : list) {
            resList.add(parse(offer, loc, sourceFormat, targetFormat));
        }

        return resList;
    }

    public String parse(String offer, String loc, SimpleDateFormat sourceFormat, SimpleDateFormat targetFormat) {
        String[] parts = offer.split("\t");

        try {
            Date startDate = sourceFormat.parse(parts[2]);
            Date endDate = sourceFormat.parse(parts[3]);
            String sdFormatted = targetFormat.format(startDate);
            String edFormatted = targetFormat.format(endDate);

            String country = parts[1];
            String[] locParts = loc.split("_");

            Locale targetLocale = new Locale(locParts[0], locParts[1]);
            Locale startLocale;
            String[] startLocaleParts = parts[0].split("_");
            if (startLocaleParts.length == 2) {
                startLocale = new Locale(startLocaleParts[0], startLocaleParts[1]);
            }else{
                startLocale = new Locale(startLocaleParts[0]);
            }

            char targetLetter = parts[1].charAt(0);
            List<Locale> localesStartingWithTargetLetter = getLocalesStartingWith(targetLetter, startLocale);

            for (Locale availableLocale : localesStartingWithTargetLetter) {
                if (availableLocale.getDisplayCountry(startLocale).equals(country)) {
                    country = availableLocale.getDisplayCountry(targetLocale);
                }
            }

            ResourceBundle resourceBundle = ResourceBundle.getBundle("Res", targetLocale);
            String place = resourceBundle.getString(parts[4]);

            String cleanedPrice = parts[5].replaceAll("[.,](?=[^.,]*[.,])", "");
            cleanedPrice = cleanedPrice.replace(',', '.');
            NumberFormat numberFormat = NumberFormat.getNumberInstance(targetLocale);
            String formattedPrice = numberFormat.format(Double.parseDouble(cleanedPrice));

            String currency = parts[6];

            return String.format("%s %s %s %s %s %s", country, sdFormatted, edFormatted, place, formattedPrice, currency);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Locale> getLocalesStartingWith(char targetLetter, Locale startLocale) {
        return Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getDisplayCountry(startLocale).toUpperCase().startsWith(String.valueOf(targetLetter)))
                .collect(Collectors.toList());
    }

    public List<String> getList() {
        return list;
    }
}
