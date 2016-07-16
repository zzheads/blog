package com.zzheads.blog.model;

import com.github.slugify.Slugify;
import spark.Spark;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BlogEntry {
    private Date mDate;
    private String mTitle;
    private String mText;
    private String mSlug;
    private ArrayList<Comment> mComments;

    public BlogEntry(Date date, String title, String text) {
        mDate = date;
        mTitle = title;
        mText = text;
        mComments = new ArrayList<>();

        Slugify slg = null;
        try {
            slg = new Slugify();
            mSlug = slg.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addComment(Comment comment) {
        mComments.add(comment);
        // Store these comments!
        return true;
    }

    public Date getDate() {
        return mDate;
    }

    public String getDateAsString () {
        String result = "";
        SimpleDateFormat formatString = new SimpleDateFormat("dd/MM/yy HH:mm");
        result = formatString.format(mDate);
        return result;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public ArrayList<Comment> getComments() {
        return mComments;
    }

    public void setComments(ArrayList<Comment> comments) {
        mComments = comments;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSlug() {
        return mSlug;
    }

    public String getSlugify (String string) {
        String result="";
        try {
            Slugify slugify = new Slugify();
            result = slugify.slugify(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogEntry that = (BlogEntry) o;

        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;
        return mText != null ? mText.equals(that.mText) : that.mText == null;

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mText != null ? mText.hashCode() : 0);
        return result;
    }


}
