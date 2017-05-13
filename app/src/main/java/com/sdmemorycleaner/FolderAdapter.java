package com.sdmemorycleaner;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

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
        File[] directories = FileUtils.getDirectories(ROOT_PATH);
        File[] subdirectories;
        for (File file: directories) {
            subdirectories = FileUtils.getDirectories(file.getPath());
            for (File subfile: subdirectories) {
                if (FileUtils.getFolderSize(subfile) != 0) {
                    folders.add(subfile);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = folders.get(position);
        holder.title.setText(file.getParent());
        holder.package_name.setText(file.getName());
        holder.memory_size.setText(FileUtils.getFolderSizeString(file));
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

        @BindView(R.id.memory_size)
        TextView memory_size;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
