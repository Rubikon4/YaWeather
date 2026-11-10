import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // WARNING !!! REMOVE API_KEY before PUSH into GitHub !!!
        String API_KEY = "";

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите широту: ");
        double lat = Double.parseDouble(scanner.nextLine());

        System.out.println("Введите долготу: ");
        double lon = Double.parseDouble(scanner.nextLine());

        System.out.println("Введите число дней прогноза:");
        int limit = Integer.parseInt(scanner.nextLine());
        if (limit <= 0) {
            System.out.println("Количество дней должно быть больше нуля.");
            return;
        }

        URI uriRequest = URI.create("https://api.weather.yandex.ru/v2/forecast?"
                + "lat=" + lat
                + "&lon=" + lon
                + "&limit=" + limit);
        System.out.printf("%nИтоговый запрос к Yandex API: %s%n", uriRequest);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriRequest)
                .header("X-Yandex-Weather-Key", API_KEY)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.printf("Запрос отправлен...%nОтвет сервера: %d.%n%n", response.statusCode());
        System.out.printf("Тело ответа: %s%n", response.body());

        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
        int temp = root.get("fact").getAsJsonObject().get("temp").getAsInt();
        System.out.printf("Текущая температура: %d градусов Цельсия.%n", temp);

        JsonArray days = root.getAsJsonArray("forecasts");
        if (days.size() < limit) {
            System.out.printf("Недостаточно данных: запрошено %d дней, получено %d.%n", limit, days.size());
            return;
        }

        double generalTemp = 0;
        for (int i = 0; i < limit; i++) {
            JsonObject obj = days.get(i).getAsJsonObject();
            JsonObject parts = obj.get("parts").getAsJsonObject();
            JsonObject day = parts.get("day").getAsJsonObject();
            generalTemp += day.get("temp_avg").getAsDouble();
        }
        double generalAvgTemp = generalTemp / limit;
        System.out.printf("Средняя температура за %d дней: %.1f градусов Цельсия.%n", limit, generalAvgTemp);
    }
}
