package com.rodrigues.pedroschwarz.awesomeplaces.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.model.Comment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        Picasso.get().load(comment.getAuthor().getImage()).placeholder(R.drawable.profile_image).into(holder.itemComImage);
        holder.itemComName.setText(comment.getAuthor().getUsername());
        holder.itemComBody.setText(comment.getBody());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy kk:mm");
        holder.itemComCreatedAt.setText(formatter.format(comment.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView itemComImage;
        TextView itemComName;
        TextView itemComBody;
        TextView itemComCreatedAt;

        public ViewHolder(View itemView) {
            super(itemView);
            itemComImage = itemView.findViewById(R.id.item_com_image);
            itemComName = itemView.findViewById(R.id.item_com_name);
            itemComBody = itemView.findViewById(R.id.item_com_body);
            itemComCreatedAt = itemView.findViewById(R.id.item_com_created_at);
        }
    }
}
