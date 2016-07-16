package com.zzheads.blog;

import com.zzheads.blog.model.Blog;
import com.zzheads.blog.model.BlogEntry;
import com.zzheads.blog.model.Comment;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by zzheads on 15.07.2016.
 */
public class Main {

    public static void main(String[] args) {
        Blog blog = new Blog();
        BlogEntry blogEntry1 = new BlogEntry(new Date(),"Blog entry #1. I tested a little with Selenium now", "Blog entry #1. \n I tested a little with Selenium now. 300 connections and 5 messages per second is no problem at least, but running that many browsers/tabs maxes out my CPU/RAM, so I can't increase the load. If you need a better answer than this, you should probably look at Jetty's documentation, but my guess is that it can handle a lot more.");
        BlogEntry blogEntry2 = new BlogEntry(new Date(), "Blog entry #2. Well, that was fast! ","We have a working real-time chat application implemented without polling, written in a total of less than 100 lines of Java and JavaScript. The implementation is very basic though, and we should at least split up the sending of the userlist and the messages (so that we don’t rebuild the user list every time anyone sends a message), but since the focus of this tutorial was supposed to be on WebSockets, I chose to do the implementation as minimal as I could be comfortable with.");
        BlogEntry blogEntry3 = new BlogEntry(new Date(), "Blog entry #3. The approach we will use is very straightforward:"," Add the user to our userUsernameMap when he connects, remove him when he disconnects, and send all his messages to all users. Since we want usernames, but don’t want complexity, we will generate usernames based on when someone connects to the server:");
        BlogEntry blogEntry4 = new BlogEntry(new Date(), "Blog entry #4. We also need to create a few methods"," for sending messages to all our connected users. We will only send messages to users whose session has the status open (Session::isOpen). We use a stream and a filter to reduce our list (the keySet of our userUsernameMap), then send out a JSON structure containing a HTML message and a list of usernames (the values of our userUsernameMap):");
        BlogEntry blogEntry5 = new BlogEntry(new Date(), "Blog entry #5. In this tutorial we will create a simple real-time chat application. ","It will feature a chat-panel that stores messages received after you join, a list of currently connected users, and an input field to send messages from. We will be using WebSockets for this, as WebSockets provides us with full-duplex communication channels over a single TCP connection, meaning we won’t have to make additional HTTP requests to send and receive messages.");

        Comment comment1 = new Comment(new Date(),"Some comment 1 with some shat", "Alexey Papin");
        Comment comment2 = new Comment(new Date(),"Some comment 2 with some shat", "Alexey Papin");
        Comment comment3 = new Comment(new Date(),"Some comment 3 with some shat", "Alexey Papin");
        Comment comment4 = new Comment(new Date(),"Some comment 4 with some shat", "Alexey Papin");

        blogEntry2.addComment(comment1);
        blogEntry2.addComment(comment2);
        blogEntry2.addComment(comment3);
        blogEntry2.addComment(comment4);

        blog.addEntry(blogEntry1);
        blog.addEntry(blogEntry2);
        blog.addEntry(blogEntry3);
        blog.addEntry(blogEntry4);
        blog.addEntry(blogEntry5);

        Spark.staticFileLocation("/public");


        get("/entries", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Blog");
            model.put("blogEntry", blog.findAllEntries());
            return new ModelAndView(model, "entries.hbs");
        }, new HandlebarsTemplateEngine());

        get("/new", (rq, rs) -> {
            return new ModelAndView(null, "new.hbs");
        }, new HandlebarsTemplateEngine());

        post("/new", (rq, rs) -> {
            BlogEntry entry = new BlogEntry(new Date(), rq.queryParams("title"), rq.queryParams("text"));
            blog.addEntry(entry);
            rs.redirect("/entries");
            return null;
        });

        get("/entry/:slug", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("comments", blog.findEntryBySlug(rq.params("slug")).getComments());
            model.put("entry", blog.findEntryBySlug(rq.params("slug")));
            return new ModelAndView(model, "entry.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
