package com.zzheads.blog.model;

import java.util.Date;

public class Comment {
    private Date mDate;
    private String mText;
    private String mAuthor;

    public Comment(Date date, String text, String author) {
        mDate = date;
        mText = text;
        mAuthor = author;
    }

    public Date getDate() {
        return mDate;
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

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }
}
