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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Game> {
    private final LayoutInflater mInflater;

    public CustomArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Game> myGames) {
        clear();
        if (myGames != null) {
            for (Game appEntry : myGames) {
                add(appEntry);
            }
        }
    }

    /**
     * Populate new games in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.fragment_game_sales, parent, false);
        } else {
            view = convertView;
        }

        Game item = getItem(position);
        // Getting the imageview then downloading the thumbnail in a thread.
        ((ImageView) view.findViewById(R.id.picture_item)).setImageBitmap(item.getHeader_bitmap());
        // Setting the text views
        ((TextView) view.findViewById(R.id.text_item)).setText(item.getName());
        ((TextView) view.findViewById(R.id.price_item)).setText(item.getFinal_price());
        return view;
    }
} 
