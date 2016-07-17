package com.zzheads.blog.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogEntry {
    private Date mDate;
    private String mTitle;
    private String mText;
    private String mSlug;
    private ArrayList<Comment> mComments;
    private String mTagsString;
    private ArrayList<String> mTags;

    public BlogEntry(Date date, String title, String text, String tags) {
        mDate = date;
        mTitle = title;
        mText = text;
        mComments = new ArrayList<>();
        mTagsString = tags;
        mTags = getTagsSplit(tags);

        Slugify slg;
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

    public ArrayList<String> getTags () {
        return mTags;
    }

    public void setTags(String tags) {
        mTagsString = tags;
        mTags = getTagsSplit(tags);
    }

    public static ArrayList<String> getTagsSplit (String string) {
        ArrayList<String> result = new ArrayList<>();
        Pattern regexp = Pattern.compile("#(\\w+)"); // Regular expression to get HashTags from string (\s|\A)#(\w+)
        Matcher mat = regexp.matcher(string);
        while (mat.find()) {
            result.add(mat.group(0));
        }

        return result;
    }

    // Just in case we'll need get slug from string
    public static String getSlugify (String string) {
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
