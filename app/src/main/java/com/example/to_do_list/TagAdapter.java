package com.example.to_do_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abc on 2017/11/16.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{
    private List<Tag> mTagList;
    ViewHolder holder;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagContent;
        View tagView;

        public ViewHolder(View itemView) {
            super(itemView);
            tagView=itemView;
            tagContent = (TextView) itemView.findViewById(R.id.tag_content);
        }
    }

    public TagAdapter(List<Tag> mTagList) {
        this.mTagList = mTagList;
    }

    public void setmTagList(List<Tag> mTagList) {
        this.mTagList = mTagList;
        notifyItemRangeChanged(0,this.mTagList.size()+1);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item,parent,false);
        holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Tag tag = mTagList.get(position);
        holder.tagContent.setText(tag.getContent());
        holder.tagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ThirdActivity.class);
                intent.putExtra("tagPosition",position);
                intent.putExtra("tagTitle",mTagList.get(position).getContent());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }
}