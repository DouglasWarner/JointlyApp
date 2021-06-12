package com.douglas.jointlyapp.data.converters;

import android.net.Uri;

import androidx.room.TypeConverter;

/**
 * Converters Uri / String to SQLite DB
 */
public class ConverterUri {

    /**
     * Convert Uri to String
     * @param uri
     * @return String
     */
    @TypeConverter
    public String fromUri(Uri uri){
        return (uri == null) ? null : uri.toString();
    }

    /**
     * Convert String to Uri
     * @param path
     * @return Uri
     */
    @TypeConverter
    public Uri fromPath(String path) {
        return (path == null) ? null : Uri.parse(path);
    }
}
