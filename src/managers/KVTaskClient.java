package managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVTaskClient {
    HttpResponse.BodyHandler<String> handler;
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request;
    HttpResponse<String> response;
    private final String apiKey;
    private final String url;

    public KVTaskClient(String url) {
        this.url = url;
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.
                newBuilder().
                GET().
                uri(uri).
                header("Accept", "text/html").
                build();

        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        this.apiKey = response.body();
    }

    public String getApiKey() {
        return apiKey;
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiKey);
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/"+key+"?API_TOKEN=" + apiKey);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        request = requestBuilder
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }
}
