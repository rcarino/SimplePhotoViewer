package com.codepath.instagramviewer;

import android.content.Context;
import android.graphics.Point;
import android.text.Spanned;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    private Context context;

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, android.R.layout.simple_list_item_1, photos);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        CircleImageView imgProfile = (CircleImageView) convertView.findViewById(R.id.imgProfile);
        imgProfile.setImageResource(0);
        Picasso.with(getContext()).load(photo.profileImageUrl).into(imgProfile);

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        tvUserName.setText(photo.userName);

        TextView tvRelativeTimestamp = (TextView) convertView.findViewById(R.id.tvRelativeTimestamp);
        tvRelativeTimestamp.setText(photo.getRelativeTimestamp());

        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        loadAndResizeMainImage(photo, imgPhoto);

        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        tvLikes.setText(photo.getLikes());

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        setViewCaption(photo, tvCaption);

        TextView tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentsCount);
        TextView tvFirstComment = (TextView) convertView.findViewById(R.id.tvFirstComment);
        TextView tvSecondComment = (TextView) convertView.findViewById(R.id.tvSecondComment);
        setViewComments(photo, tvCommentsCount, tvFirstComment, tvSecondComment);

        return convertView;
    }

    private void setViewComments(InstagramPhoto photo, TextView tvCommentsCount, TextView tvFirstComment, TextView tvSecondComment) {
        if (photo.getCommentsCount() != "") {
            tvCommentsCount.setText(photo.getCommentsCount());
        } else {
            tvCommentsCount.setVisibility(View.INVISIBLE);
        }

        if (photo.getCommentAtIndex(0) != null) {
            tvFirstComment.setText(photo.getCommentAtIndex(0));
        } else {
            tvFirstComment.setVisibility(View.INVISIBLE);
        }

        if (photo.getCommentAtIndex(1) != null) {
            tvSecondComment.setText(photo.getCommentAtIndex(1));
        } else {
            tvSecondComment.setVisibility(View.INVISIBLE);
        }
    }

    private void setViewCaption(InstagramPhoto photo, TextView tvCaption) {
        Spanned caption = photo.getCaption();
        if (caption != null) {
            tvCaption.setText(photo.getCaption());
        } else {
            tvCaption.setVisibility(View.INVISIBLE);
        }
    }

    private void loadAndResizeMainImage(InstagramPhoto photo, ImageView target) {
        target.setImageResource(0);
        int screenWidth = getScreenWidth();
        int scaledHeight = screenWidth / photo.getAspectRatio();
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .resize(screenWidth, scaledHeight)
                .into(target);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }
}
