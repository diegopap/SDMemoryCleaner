package com.sdmemorycleaner;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

    public static void getApp(Context context, String packageName, FolderAdapter.ViewHolder holder) {
        try {
            new LongOperation(context, holder).execute(URL + packageName, null, null);
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
            try {
                content = convert(new URL(params[0]).openStream());
                Document doc = Jsoup.parse(content);
                Element image = doc.select("[itemprop=image]").first();
                Element name = doc.select("[itemprop=name]").first();
                app = new App(HTTPS + image.attr("src").replace("=w300-rw","=w50-rw"), name.text());
            } catch (IOException e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return app;
        }

        @Override
        protected void onPostExecute(App app) {
            if (app != null) {
                holder.title.setText(app.title);
                Glide.with(context).load(Uri.parse(app.image)).into(holder.cover_image);
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

}
