package ru.mirea.lozhnichenkoas.mireaproject.ui.сurrency;

import android.os.AsyncTask;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentCurrencyBinding;

public class DownloadPageCurrency extends AsyncTask<String, Void, String> {
    private FragmentCurrencyBinding binding;
    // конструктор
    public DownloadPageCurrency(FragmentCurrencyBinding binding) {
        this.binding = binding;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        binding.textViewLoad.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadIpInfo(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    //выводим значения локации и смотрим погоду
    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject responseJSON = new JSONObject(result);
            JSONObject valuteCourse = responseJSON.getJSONObject("Valute");
            JSONArray keys = valuteCourse.names(); // Получаем массив ключей
            StringBuilder valuteName = new StringBuilder("Валюта\n");
            StringBuilder course = new StringBuilder("Курс\n");
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            if (keys != null) {
                for (int i = 0; i < keys.length(); i++) {
                    if (keys.getString(i).equals("USD") || keys.getString(i).equals("EUR") ||
                            keys.getString(i).equals("CNY") || keys.getString(i).equals("JPY") ||
                            keys.getString(i).equals("TRY") || keys.getString(i).equals("AED") ||
                            keys.getString(i).equals("PLN"))
                    {
                        String key = keys.getString(i);
                        JSONObject valute = valuteCourse.getJSONObject(key);

                        valuteName.append("\n").append(valute.getString("Name"))
                                .append(" ").append(valute.getString("CharCode"));
                        course.append("\n").append(decimalFormat.format(valute.getDouble("Value")));
                    }
                }
            }
            binding.textViewLoad.setVisibility(View.GONE);
            binding.textViewValute.setText(valuteName.toString());
            binding.textViewCourse.setText(course.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }

    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + "Error Code " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}