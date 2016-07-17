package com.zzheads.blog.dao;

import com.zzheads.blog.model.BlogEntry;

import java.util.ArrayList;
import java.util.List;

public interface BlogDao {
    boolean addEntry(BlogEntry blogEntry);
    boolean deleteEntry(BlogEntry blogEntry);
    List<BlogEntry> findAllEntries();
    List<BlogEntry> entriesSelectedByTag (String tag);
    BlogEntry findEntryBySlug(String slug);
    ArrayList<String> getAllTags ();
}
