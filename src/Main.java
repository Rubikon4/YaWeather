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

        URI uriRequest = URI.create("https://api.weather.yandex.ru/v2/forecast?"
                + "lat=" + lat
                + "&lon=" + lon
                + "&limit=" + limit);
        System.out.println("Итоговый запрос к Yandex API: " + uriRequest);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriRequest)
                .header("X-Yandex-Weather-Key", API_KEY)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}