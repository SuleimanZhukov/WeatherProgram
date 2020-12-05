package com.suleiman.weather;

import com.google.gson.Gson;
import com.suleiman.weather.model.WeatherRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static final String URL_UNITS = "&units=";
    private static String units = "metric";
    private static final String REST_OF_URL = "&appid=";
    private static final String WEATHER_API_KEY = "65924ad2b3c04751bab085b9e4269cb9"; // Paste your api key here. Website "https://openweathermap.org/"
    private static String tempUnit = "°C";

    // So it looks more clean
    @FXML
    public AnchorPane settingsMenu;
    @FXML
    public CheckBox checkFahrenheit;
    @FXML
    public ImageView mainWeatherIcon;
    @FXML
    private Label degree;
    @FXML
    private Label cityName;
    @FXML
    private Label day1, date1, day2, date2, day3, date3, day4, date4, day5, date5, day6, date6, day7, date7, day8, date8, day9, date9;
    @FXML
    public Label hour1, hour2, hour3, hour4, hour5, hour6, hour7, hour8, hour9;
    @FXML
    public TextField searchField;

    private Calendar calendar = Calendar.getInstance(Locale.getDefault());
    private Date today = calendar.getTime();
    private Date tomorrow;
    private Date third;
    private Date fourth;
    private Date fifth;
    private Date sixth;
    private Date seventh;
    private Date eighth;
    private Date ninth;

    private SimpleDateFormat input = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat output = new SimpleDateFormat("h aa");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        downloadData();
        getDaysOfTheWeek();
        setAllDaysAndDate();
        setHours();
    }

    // Downloads all the data
    private void downloadData() {
        try {
            URL url = new URL(WEATHER_URL + cityName.getText() + URL_UNITS + units + REST_OF_URL + WEATHER_API_KEY);

            new Thread(() -> {
                try {
                    // Request setup
                    HttpsURLConnection connection;
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String result = getLines(in);

                    Gson gson = new Gson();

                    WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            weatherDataApply(weatherRequest);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Reads all the lines
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining());
    }

    // Applies all the data
    private void weatherDataApply(WeatherRequest weatherRequest) {
        getIcon(weatherRequest);
        int deg = (int) weatherRequest.getMain().getTemp();
        degree.setFont(Font.font("Verdana", FontPosture.REGULAR, 96));
        cityName.setFont(Font.font("Verdana", FontPosture.REGULAR, 26));
        degree.setText(deg + tempUnit);
        cityName.setText(weatherRequest.getName());

    }

    // Gets the needed icon
    private void getIcon(WeatherRequest weatherRequest) {
        Image img = new Image("/images/weather/" + weatherRequest.getWeather()[0].getIcon() + ".png");
        mainWeatherIcon.setImage(img);
    }

    @FXML
    private void refresh(MouseEvent mouseEvent) {
        downloadData();
    }

    @FXML
    public void search(MouseEvent mouseEvent) {
        cityName.setText(searchField.getText());
        searchField.clear();
        downloadData();
    }

    @FXML
    public void searchCity(ActionEvent actionEvent) {
        cityName.setText(searchField.getText());
        searchField.clear();
        downloadData();
    }

    @FXML
    public void settings(MouseEvent mouseEvent) {
        if (settingsMenu.isVisible()) {
            settingsMenu.setVisible(false);
        } else {
            settingsMenu.setVisible(true);
        }
        if (checkFahrenheit.isSelected()) {
            units = "imperial";
            tempUnit = "°F";
            downloadData();
        } else {
            units = "metric";
            tempUnit = "°C";
            downloadData();
        }
    }

    // Sets needed labels to show the right time
    private void setHours() {
        hour1.setText(getHour());
        hour2.setText(getSecondHour());
        hour3.setText(getThirdHour());
        hour4.setText(getFourthHour());
        hour5.setText(getFifthHour());
        hour6.setText(getSixthHour());
        hour7.setText(getSeventhHour());
        hour8.setText(getEighthHour());
        hour9.setText(getNinthHour());
    }

    // Sets needed labels to show the right day and date
    private void setAllDaysAndDate() {
        day1.setText(getCurrentDay());
        date1.setText(getCurrentDate());
        day2.setText(getTomorrowDay());
        date2.setText(getTomorrowDate());
        day3.setText(getThirdDay());
        date3.setText(getThirdDate());
        day4.setText(getFourthDay());
        date4.setText(getFourthDate());
        day5.setText(getFifthDay());
        date5.setText(getFifthDate());
        day6.setText(getSixthDay());
        date6.setText(getSixthDate());
        day7.setText(getSeventhDay());
        date7.setText(getSeventhDate());
        day8.setText(getEighthDay());
        date8.setText(getEighthDate());
        day9.setText(getNinthDay());
        date9.setText(getNinthDate());
    }

    // Adds a day to 9 coming days to show the right day and date
    private void getDaysOfTheWeek() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        tomorrow = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        third = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        fourth = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        fifth = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        sixth = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        seventh = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        eighth = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        ninth = calendar.getTime();
    }

    // Gets the right hour
    private String getHour() {
        String out = "";
        try {
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getSecondHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getThirdHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getFourthHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getFifthHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getSixthHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getSeventhHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getEighthHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String getNinthHour() {
        String out = "";
        try {
            calendar.add(Calendar.HOUR, 1);
            today = calendar.getTime();
            today = input.parse(today.toString().split("\\s")[3]);
            out = output.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    // Gets day and date from Calendar class
    private String getCurrentDay() {
        return today.toString().split("\\s")[0];
    }

    private String getCurrentDate() {
        return today.toString().split("\\s")[2];
    }

    private String getTomorrowDay() {
        return tomorrow.toString().split("\\s")[0];
    }

    private String getTomorrowDate() {
        return tomorrow.toString().split("\\s")[2];
    }

    private String getThirdDay() {
        return third.toString().split("\\s")[0];
    }

    private String getThirdDate() {
        return third.toString().split("\\s")[2];
    }

    private String getFourthDay() {
        return fourth.toString().split("\\s")[0];
    }

    private String getFourthDate() {
        return fourth.toString().split("\\s")[2];
    }

    private String getFifthDay() {
        return fifth.toString().split("\\s")[0];
    }

    private String getFifthDate() {
        return fifth.toString().split("\\s")[2];
    }

    private String getSixthDay() {
        return sixth.toString().split("\\s")[0];
    }

    private String getSixthDate() {
        return sixth.toString().split("\\s")[2];
    }

    private String getSeventhDay() {
        return seventh.toString().split("\\s")[0];
    }

    private String getSeventhDate() {
        return seventh.toString().split("\\s")[2];
    }

    private String getEighthDay() {
        return eighth.toString().split("\\s")[0];
    }

    private String getEighthDate() {
        return eighth.toString().split("\\s")[2];
    }

    private String getNinthDay() {
        return ninth.toString().split("\\s")[0];
    }

    private String getNinthDate() {
        return ninth.toString().split("\\s")[2];
    }
}
