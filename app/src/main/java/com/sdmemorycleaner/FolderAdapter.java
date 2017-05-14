package com.sdmemorycleaner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diego on 13/05/17.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<File> folders;
    private ArrayList<Boolean> selectedFolders;

    public FolderAdapter(Context context) {
        setContext(context);
        reset(context);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void reset(Context context) {
        selectedFolders = new ArrayList<>();
        folders = FileUtils.getFolders(context);
        for (File ignored : folders) {
            selectedFolders.add(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.checkBox.setChecked(selectedFolders.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedFolders.set(holder.getAdapterPosition(), isChecked);
            }
        });
        File file = folders.get(position);
        String packageId = file.getName();
        holder.location.setText(file.getParent());
        holder.package_name.setText(packageId);
        holder.memory_size.setText(FileUtils.getFolderSizeString(file));
        GooglePlayUtils.getApp(context, packageId, holder);
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

        @BindView(R.id.location)
        TextView location;

        @BindView(R.id.memory_size)
        TextView memory_size;

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        @BindView(R.id.cover_image)
        ImageView cover_image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void selectAll() {
        for (int i = 0; i < selectedFolders.size(); i++) {
            selectedFolders.set(i, true);
        }
        notifyDataSetChanged();
    }

    public void deleteSelected() {
        for (int i = 0; i < selectedFolders.size(); i++) {
            if (selectedFolders.get(i)) {
                FileUtils.deleteRecursive(folders.get(i));
            }
        }
        reset(context);
        notifyDataSetChanged();
    }

}
