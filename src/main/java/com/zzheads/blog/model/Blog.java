package com.zzheads.blog.model;

import com.zzheads.blog.dao.BlogDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzheads on 15.07.2016.
 */
public class Blog implements BlogDao {
    private ArrayList<BlogEntry> mBlogEntries;

    public Blog() {
        mBlogEntries = new ArrayList<BlogEntry>();
    }

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        mBlogEntries.add(blogEntry);
        return true;
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return mBlogEntries;
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        for (BlogEntry blog : mBlogEntries) {
            if (blog.getSlug().equals(slug)) return blog;
        }
        return null;
    }
}
