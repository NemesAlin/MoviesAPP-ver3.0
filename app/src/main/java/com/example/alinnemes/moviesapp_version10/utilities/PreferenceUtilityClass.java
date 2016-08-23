package com.example.alinnemes.moviesapp_version10.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.alinnemes.moviesapp_version10.R;

/**
 * Created by alin.nemes on 01-Aug-16.
 */
public class PreferenceUtilityClass {

    public static String getPreferredSorting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sorting_key), context.getString(R.string.pref_sorting_default));
    }

    public static boolean getPreferredFavorite(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.pref_favorite_key), false);
    }

}
