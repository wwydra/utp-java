package zad1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Database {
    private TravelData travelData;
    private final String url;

    public Database(String url, TravelData travelData) {
        this.travelData = travelData;
        this.url = url;
    }

    public void create() {
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();

            statement.executeUpdate("DROP TABLE Offers");

            statement.executeUpdate("CREATE TABLE Offers (ID INT PRIMARY KEY, Reg VARCHAR(10), Country VARCHAR(50), Departure_date DATE, Return_date DATE, Place VARCHAR(20), Price VARCHAR(20), Currency VARCHAR(5))");

            int id = 1;
            for (String offer : travelData.getList()){
                String[] offerParts = offer.split("\t");
                statement.executeUpdate("INSERT INTO Offers " +
                        "VALUES (" + id + ", '" + offerParts[0] + "', '" + offerParts[1] + "', '" + offerParts[2] + "', '" + offerParts[3] + "', '" + offerParts[4] + "', '" + offerParts[5] + "', '" + offerParts[6] + "')");
                id++;
            }

            statement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void showGui() {
        try{
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Offers");
            Vector<Vector<Object>> data = new Vector<>();
            Vector<String> columns = new Vector<>();

            columns.add("Kraj");
            columns.add("Data_wyjazdu");
            columns.add("Data_powrotu");
            columns.add("Miejsce");
            columns.add("Cena");
            columns.add("Waluta");

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getString("Country"));
                row.add(resultSet.getString("Departure_date"));
                row.add(resultSet.getString("Return_date"));
                row.add(resultSet.getString("Place"));
                row.add(resultSet.getString("Price"));
                row.add(resultSet.getString("Currency"));
                data.add(row);
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columns);
            JTable table = new JTable(tableModel);

            JFrame frame = new JFrame("Oferty");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);

            JScrollPane scrollPane = new JScrollPane(table);
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            JLabel titleLabel = new JLabel("Oferty");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

            JButton button = new JButton("PL");

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.add(titleLabel, BorderLayout.WEST);
            titlePanel.add(button, BorderLayout.EAST);
            frame.getContentPane().add(titlePanel, BorderLayout.NORTH);
            frame.setVisible(true);

            button.addActionListener(e -> {
                String[] currLocale;
                if (button.getText().equals("PL")){
                    button.setText("EN");
                    currLocale = new String[]{"en", "GB"};
                } else if (button.getText().equals("EN")) {
                    button.setText("DE");
                    currLocale = new String[]{"de", "DE"};
                }else{
                    button.setText("PL");
                    currLocale = new String[]{"pl", "PL"};
                }
                updateLocale(currLocale, tableModel, columns, titleLabel, frame);
            });

            statement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void updateLocale(String[] locale, DefaultTableModel tableModel, Vector<String> columns, JLabel titleLabel, JFrame frame) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Res", new Locale(locale[0], locale[1]));

        tableModel.getDataVector().removeAllElements();

        for (int i = 0; i < columns.size(); i++) {
            columns.set(i, resourceBundle.getString(columns.get(i)));
        }
        tableModel.setColumnIdentifiers(columns);

        List<String> offers = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Offers");
            while (resultSet.next()) {
                String reg = resultSet.getString("Reg");
                String country = resultSet.getString("Country");
                String startDate = resultSet.getString("Departure_date");
                String endDate = resultSet.getString("Return_date");
                String place = resultSet.getString("Place");
                String price = resultSet.getString("Price");
                String currency = resultSet.getString("Currency");

                String formattedOffer = String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s", reg, country, startDate, endDate, place, price, currency);
                offers.add(formattedOffer);
            }
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale(locale[0], locale[1]));
        List<String> parsedOffers = new ArrayList<>();
        for (String o : offers) {
            String[] parts = o.split("\t");
            try {
                java.util.Date startDate = sourceFormat.parse(parts[2]);
                Date endDate = sourceFormat.parse(parts[3]);
                String sdFormatted = targetFormat.format(startDate);
                String edFormatted = targetFormat.format(endDate);

                String country = parts[1];

                Locale targetLocale = new Locale(locale[0], locale[1]);
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

                String place = resourceBundle.getString(parts[4]);

                String cleanedPrice = parts[5].replaceAll("[.,](?=[^.,]*[.,])", "");
                cleanedPrice = cleanedPrice.replace(',', '.');
                NumberFormat numberFormat = NumberFormat.getNumberInstance(targetLocale);
                String formattedPrice = numberFormat.format(Double.parseDouble(cleanedPrice));

                String currency = parts[6];

                parsedOffers.add(String.format("%s\t%s\t%s\t%s\t%s\t%s", country, sdFormatted, edFormatted, place, formattedPrice, currency));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (String parsedOffer : parsedOffers) {
            String[] parts = parsedOffer.split("\t");
            tableModel.addRow(parts);
        }

        frame.setTitle(resourceBundle.getString("Offers"));
        titleLabel.setText(resourceBundle.getString("Offers"));

        tableModel.fireTableDataChanged();
        frame.validate();
        frame.repaint();
    }

    public static List<Locale> getLocalesStartingWith(char targetLetter, Locale startLocale) {
        return Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getDisplayCountry(startLocale).toUpperCase().startsWith(String.valueOf(targetLetter)))
                .collect(Collectors.toList());
    }
}
