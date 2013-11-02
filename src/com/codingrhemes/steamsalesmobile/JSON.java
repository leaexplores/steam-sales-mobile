/*
Steam Sales Mobile - Android application to keep track of the steam sales.
        Copyright (C) 2013  Mathieu Rhéaume <mathieu@codingrhemes.com>

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.
*/

package com.codingrhemes.steamsalesmobile;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSON {
    public static String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }

    public static List<Game> ParseJSONFromAPI(JSONObject validObject) {
        List<Game> lstGames = new ArrayList<Game>();

        JSONArray gamesInSale = null;
        JSONObject gameObject;
        Game agame;
        try {
            gamesInSale = validObject.getJSONObject("specials").getJSONArray("items");
        } catch (JSONException e) {
            // Failed to parse it :(.
            Log.d("JSON.ParseJSONFromAPI", e.getMessage());
        }
        for (int i = 0; i < gamesInSale.length(); i++) {
            agame = new Game();
            try {
                gameObject = gamesInSale.getJSONObject(i);
                agame.setCurrency(gameObject.optString("currency"));
                agame.setDiscount_expiration(gameObject.optString("discount_expiration"));
                agame.setDiscount_percent(gameObject.optString("discount_percent"));
                agame.setDiscounted(gameObject.optString("discounted"));
                agame.setFinal_price(gameObject.optString("final_price"));
                agame.setHeader_image(gameObject.optString("header_image"));
                agame.setLarge_capsule_img(gameObject.optString("large_capsule_image"));
                agame.setSmall_capsule_img(gameObject.optString("small_capsule_image"));
                agame.setName(gameObject.optString("name"));
                agame.setId(gameObject.optString("id"));
                new DownloadPictureTask(((Game) agame)).execute(agame.getSmall_capsule_img());
                lstGames.add(agame);
            } catch (JSONException e) {
                // Failed to parse it :(.
                Log.d("JSON.ParseJSONFromAPI", e.getMessage());
            }
        }
        return lstGames;
    }


    public static class DownloadPictureTask extends AsyncTask<String, Void, Bitmap> {
        Game pGame;

        public DownloadPictureTask(Game game) {
            pGame = game;

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // Download that picture!
            return HttpThumbnails.readPictureFromTheWeb(params[0].toString());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            pGame.setHeader_bitmap(bitmap);
            // We can do the lazy way as we made gamesFragment a singleton!!
            GameSaleActivity.gamesFragment.reloadList();
        }

    }

    public static Game ParseDealOfTheDayJSONFromAPI(JSONObject validObject) {
        Game theDealOfTheDay = null;
        JSONArray gamesInSale;
        try {
            gamesInSale = validObject.getJSONObject("dailyDeal").getJSONArray("items");
            theDealOfTheDay = new Game();
            theDealOfTheDay.setName(gamesInSale.getJSONObject(0).optString("name"));
            theDealOfTheDay.setId(gamesInSale.getJSONObject(0).optString("id"));
            theDealOfTheDay.setFinal_price(gamesInSale.getJSONObject(0).optString("final_price"));
            theDealOfTheDay.setHeader_image(gamesInSale.getJSONObject(0).optString("header_image"));
        } catch (JSONException e) {
            // Failed to parse it :(.
            Log.d("JSON.ParseJSONFromAPI", e.getMessage());
        }
        return theDealOfTheDay;

    }


}
