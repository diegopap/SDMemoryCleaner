package com.sdmemorycleaner;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diego on 13/05/17.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/Android";

    Context context;
    ArrayList<File> folders = new ArrayList<>();

    public FolderAdapter(Context context) {
        this.context = context;
        File[] directories = getDirectories(ROOT_PATH);
        File[] subdirectories;
        for (File file: directories) {
            subdirectories = getDirectories(file.getPath());
            Collections.addAll(folders, subdirectories);
        }
    }

    private File[] getDirectories(String path) {
        return new File(path).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(folders.get(position).getParent());
        holder.package_name.setText(folders.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.package_name)
        TextView package_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
