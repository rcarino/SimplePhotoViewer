package com.codepath.instagramviewer;

import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstagramPhoto {
    public String caption;
    public JSONArray comments;
    public long createdTime;
    public int imageHeight;
    public String imageUrl;
    public int imageWidth;
    public int likesCount;
    public String profileImageUrl;
    public String userName;

    public int getAspectRatio() {
        return imageWidth / imageHeight;
    }

    public String getRelativeTimestamp() {
        return DateUtils.getRelativeTimeSpanString(createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

    public String getLikes() {
        return Integer.toString(likesCount) + " likes";
    }

    public Spanned getCaption() {
        if (caption == null) {
            return null;
        }
        String formattedCaption = String.format("<font color=\"#386C90\">%s</font> <font color=\"#878C90\">%s</font>", userName, caption);
        return Html.fromHtml(formattedCaption);
    }

    public String getCommentsCount() {
        if (isCommentsEmpty()) {
            return "";
        }

        return String.format("view all %d comments", comments.length());
    }

    private boolean isCommentsEmpty() {
        return (comments == null) || (comments.length() == 0);
    }

    public Spanned getCommentAtIndex(int i) {
        if (isCommentsEmpty()) {
            return null;
        }

        try {
            JSONObject firstComment = comments.getJSONObject(i);
            String userName = firstComment.getJSONObject("from").getString("username");
            String text = firstComment.getString("text");
            String formattedComment = String.format("<font color=\"#004877\">%s</font> <font color=\"\">%s</font>", userName, text);

            return Html.fromHtml(formattedComment);
        } catch (JSONException e) {
            return null;
        }
    }
}
