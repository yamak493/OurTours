package net.enabify.ourTours.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.enabify.ourTours.data.Tour;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class TourApiService {
    private static final String API_URL = "https://made-by-free.com/tour/?code=";
    private final Gson gson = new Gson();

    public CompletableFuture<TourApiResult> fetchTour(String tourCode) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(API_URL + tourCode);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    try {
                        Tour tour = gson.fromJson(response.toString(), Tour.class);
                        return new TourApiResult(tour, null);
                    } catch (JsonSyntaxException e) {
                        return new TourApiResult(null, "JSONの解析に失敗しました: " + e.getMessage());
                    }
                } else if (responseCode == 404) {
                    return new TourApiResult(null, "ツアーが見つかりません。ツアーコードに入力ミスがないか確認してください。");
                } else {
                    return new TourApiResult(null, "サーバーエラーが発生しました（コード: " + responseCode + "）。管理者に連絡してください。");
                }
            } catch (IOException e) {
                return new TourApiResult(null, "ネットワークエラーが発生しました: " + e.getMessage());
            }
        });
    }

    public static class TourApiResult {
        private final Tour tour;
        private final String error;

        public TourApiResult(Tour tour, String error) {
            this.tour = tour;
            this.error = error;
        }

        public Tour getTour() {
            return tour;
        }

        public String getError() {
            return error;
        }

        public boolean isSuccess() {
            return tour != null;
        }
    }
}
