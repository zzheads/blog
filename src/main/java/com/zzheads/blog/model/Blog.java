package com.zzheads.blog.model;

import com.zzheads.blog.dao.BlogDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zzheads on 15.07.2016.
 */
public class Blog implements BlogDao {
    public static final String ALL_ENTRIES_KEY = "All entries";
    private ArrayList<BlogEntry> mBlogEntries;

    public Blog() {
        mBlogEntries = new ArrayList<BlogEntry>();
    }

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        if (blogEntry != null) {
            mBlogEntries.add(blogEntry);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEntry(BlogEntry blogEntry) {
        return mBlogEntries.remove(blogEntry);
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return mBlogEntries;
    }

    @Override
    public List<BlogEntry> entriesSelectedByTag(String tag) {
        ArrayList <BlogEntry> result = new ArrayList<>();
        for (BlogEntry blog : mBlogEntries) {
            ArrayList<String> tags = blog.getTags();
            for (int j=0;j<tags.size();j++) {
                if (tags.get(j).equals(tag)) result.add(blog);
            }
        }
        return result;
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        for (BlogEntry blog : mBlogEntries) {
            if (blog.getSlug().equals(slug)) return blog;
        }
        return null;
    }

    @Override
    public ArrayList<String> getAllTags() {
        ArrayList <String> result = new ArrayList<>();
        Set <String> hs = new HashSet<>();
        for (BlogEntry blog : mBlogEntries) {
            for (String tag : blog.getTags()) {
                result.add(tag);
            }
        }
        result.add(ALL_ENTRIES_KEY);
        hs.addAll(result);
        result.clear();
        result.addAll(hs);
        result.sort(String::compareTo);
        return result;
    }

}
