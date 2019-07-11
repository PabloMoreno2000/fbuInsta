package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fbuinsta.PostDetailActivity;
import com.example.fbuinsta.R;
import com.parse.Parse;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;

import models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    List<Post> posts;
    Context context;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> posts) {
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View postView = layoutInflater.inflate(R.layout.post_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(postView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);

        viewHolder.post = post;

        ParseUser user = post.getUser();

        if(!post.getImage().getUrl().equals("")) {
            Glide.with(context).load(post.getImage().getUrl()).into(viewHolder.ivMedia);
            Log.i("Adapter", post.getImage().getUrl());
            //viewHolder.ivMedia.setParseFile(post.getImage());
        }



        viewHolder.tvDescription.setText(post.getDescription());
        viewHolder.tvUser.setText(user.getUsername());

        viewHolder.ivComment.setImageResource(R.drawable.ufi_comment);
        viewHolder.ivLike.setImageResource(R.drawable.ufi_heart);
        viewHolder.ivSend.setImageResource(R.drawable.ufi_new_direct);
        viewHolder.ivSave.setImageResource(R.drawable.ufi_save);
        //viewHolder.ivProfile.setImageResource(R.drawable.profileimage);

        Glide.with(context)
                .load(R.drawable.profileimage)
                //.bitmapTransform(new RoundedCornersTransformation(this, 25, 0))
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.ivProfile);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ParseImageView ivProfile;
        ParseImageView ivMedia;
        ImageView ivLike;
        ImageView ivComment;
        ImageView ivSend;
        ImageView ivSave;
        TextView tvUser;
        TextView tvDescription;

        Post post;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = (ParseImageView) itemView.findViewById(R.id.ivProfile);
            ivMedia = (ParseImageView) itemView.findViewById(R.id.ivPostImage);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
            ivComment = (ImageView) itemView.findViewById(R.id.ivComment);
            ivSend = (ImageView) itemView.findViewById(R.id.ivSend);
            ivSave = (ImageView) itemView.findViewById(R.id.ivSave);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            tvDescription = (TextView) itemView.findViewById(R.id.tvPostDescription);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent detailIntent = new Intent(context, PostDetailActivity.class);
            detailIntent.putExtra("username", tvUser.getText().toString());
            detailIntent.putExtra("description", tvDescription.getText().toString());
            detailIntent.putExtra("image", post.getImage().getUrl());
            ((Activity)(context)).startActivity(detailIntent);
        }
    }
}
