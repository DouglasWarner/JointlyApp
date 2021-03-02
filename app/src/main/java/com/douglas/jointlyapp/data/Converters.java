package com.douglas.jointlyapp.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import androidx.room.Ignore;
import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class Converters {

    /**
     * Escribe la imagen a la base de datos
     * @param bitmap
     * @return
     */
    @TypeConverter
    public byte[] fromBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().length, options);
        options.inSampleSize = calculateInSampleSize(options, 100, 100);
        options.inJustDecodeBounds = false;

        Bitmap result = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().length, options);
        outputStream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Lee la imagen de la base de datos
     * @param byteArray
     * @return
     */
    @TypeConverter
    public Bitmap toBitmap(byte[] byteArray)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        options.inSampleSize = calculateInSampleSize(options, 100, 100);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    @Ignore
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
