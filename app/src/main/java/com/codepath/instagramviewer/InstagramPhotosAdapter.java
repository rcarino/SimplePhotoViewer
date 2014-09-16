package com.codepath.instagramviewer;

import android.content.Context;
import android.graphics.Point;
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

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentsCount);
        TextView tvFirstComment = (TextView) convertView.findViewById(R.id.tvFirstComment);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvRelativeTimestamp = (TextView) convertView.findViewById(R.id.tvRelativeTimestamp);
        TextView tvSecondComment = (TextView) convertView.findViewById(R.id.tvSecondComment);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        CircleImageView imgProfile = (CircleImageView) convertView.findViewById(R.id.imgProfile);

        tvCaption.setText(photo.getCaption());
        tvCommentsCount.setText(photo.getCommentsCount());
        tvFirstComment.setText(photo.getCommentAtIndex(0));
        tvLikes.setText(photo.getLikes());
        tvRelativeTimestamp.setText(photo.getRelativeTimestamp());
        tvSecondComment.setText(photo.getCommentAtIndex(1));
        tvUserName.setText(photo.userName);

        imgProfile.setImageResource(0);

        Picasso.with(getContext()).load(photo.profileImageUrl).into(imgProfile);

        loadAndResizeMainImage(photo, imgPhoto);

        return convertView;
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
