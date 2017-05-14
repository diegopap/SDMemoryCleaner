package com.sdmemorycleaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by diego on 14/05/17.
 */

public class GooglePlayUtils {

    private static final String TAG = "GooglePlayUtils";
    private static final String URL = "https://play.google.com/store/apps/details?id=";
    private static final String HTTPS = "https:";
    private static final String MARKET = "market://details?id=";
    private static final int TEXT_COLOR_LINK = Color.parseColor("#7bc9c2");

    public static void getApp(Context context, String packageName, FolderAdapter.ViewHolder holder) {
        try {
            new LongOperation(context, holder).execute(packageName, null, null);
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    private static class LongOperation extends AsyncTask<String, Void, App> {

        Context context;
        FolderAdapter.ViewHolder holder;

        public LongOperation(Context context, FolderAdapter.ViewHolder holder) {
            this.context = context;
            this.holder = holder;
        }

        @Override
        protected App doInBackground(String... params) {

            App app = null;
            String content;
            String packageId = params[0];
            try {
                content = convert(new URL(URL + packageId).openStream());
                Document doc = Jsoup.parse(content);
                Element image = doc.select("[itemprop=image]").first();
                Element name = doc.select("[itemprop=name]").first();
                app = new App(HTTPS + image.attr("src").replace("=w300-rw","=w50-rw"),
                        name.text(),
                        packageId);
            } catch (IOException e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return app;
        }

        @Override
        protected void onPostExecute(final App app) {
            if (app != null) {
                holder.title.setText(app.title);
                try {
                    Glide.with(context).load(Uri.parse(app.image)).into(holder.cover_image);
                } catch (Exception e) {
                    Log.d(TAG, e.getLocalizedMessage());
                }
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openInStore(context, app.packageId);
                    }
                };
                holder.package_name.setOnClickListener(clickListener);
                holder.title.setOnClickListener(clickListener);
                holder.package_name.setPaintFlags(holder.package_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                holder.package_name.setTextColor(TEXT_COLOR_LINK);
                holder.title.setTextColor(TEXT_COLOR_LINK);
                holder.cover_image.setOnClickListener(clickListener);
            } else {
                holder.title.setText(context.getString(R.string.app_not_in_store));
                holder.cover_image.setImageResource(R.drawable.image_unavailable);
            }
            super.onPostExecute(app);
        }
    }

    private static String convert(InputStream inputStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString();

    }

    public static void openInStore(Context context, String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL + appPackageName)));
        }
    }

}
