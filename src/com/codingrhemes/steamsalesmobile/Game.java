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

public class Game {
    String id, type, name, discounted, discount_percent, original_price,
            final_price, currency, large_capsule_img, small_capsule_img,
            discount_expiration, header_image;
    Bitmap header_bitmap;

    private final String STEAM_URL = "https://store.steampowered.com/app/";

    public String getId() {
        if (id.equals(null))
            return "0";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscounted() {
        return discounted;
    }

    public void setDiscounted(String discounted) {
        this.discounted = discounted;
    }

    public String getDiscount_percent() {
        return discount_percent;
    }

    public Bitmap getHeader_bitmap() {
        return header_bitmap;
    }

    public void setHeader_bitmap(Bitmap header_bitmap) {
        this.header_bitmap = header_bitmap;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLarge_capsule_img() {
        return large_capsule_img;
    }

    public void setLarge_capsule_img(String large_capsule_img) {
        this.large_capsule_img = large_capsule_img;
    }

    public String getSmall_capsule_img() {
        return small_capsule_img;
    }

    public void setSmall_capsule_img(String small_capsule_img) {
        this.small_capsule_img = small_capsule_img;
    }

    public String getDiscount_expiration() {
        return discount_expiration;
    }

    public void setDiscount_expiration(String discount_expiration) {
        this.discount_expiration = discount_expiration;
    }

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

    public String getAppUrl() {
        return STEAM_URL.concat(getId().concat("/?snr=1_702_4_"));
    }
}