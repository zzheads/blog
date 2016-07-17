package com.zzheads.blog;

import com.zzheads.blog.model.Blog;
import com.zzheads.blog.model.BlogEntry;
import com.zzheads.blog.model.Comment;
import com.zzheads.blog.model.NotFoundException;
import spark.ModelAndView;
import spark.Request;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.before;
import static spark.Spark.halt;

/**
 * Created by zzheads on 15.07.2016.
 */
public class Main {

    private static final java.lang.String FLASH_MESSAGE_KEY = "flash_message";
    private static final java.lang.String USERNAME_ADMIN = "admin";
    private static final java.lang.String PASSWORD_ADMIN = "admin";


    public static void main(String[] args) {
        Blog blog = new Blog();
        BlogEntry blogEntry1 = new BlogEntry(new Date(),"Blog entry #1. I tested a little with Selenium now", "Blog entry #1. \n I tested a little with Selenium now. 300 connections and 5 messages per second is no problem at least, but running that many browsers/tabs maxes out my CPU/RAM, so I can't increase the load. If you need a better answer than this, you should probably look at Jetty's documentation, but my guess is that it can handle a lot more.", "#TAG1, #TAG2, #TAG3");
        BlogEntry blogEntry2 = new BlogEntry(new Date(), "Blog entry #2. Well, that was fast! ","We have a working real-time chat application implemented without polling, written in a total of less than 100 lines of Java and JavaScript. The implementation is very basic though, and we should at least split up the sending of the userlist and the messages (so that we don’t rebuild the user list every time anyone sends a message), but since the focus of this tutorial was supposed to be on WebSockets, I chose to do the implementation as minimal as I could be comfortable with.", "#TAG1, #TAG2, #TAG3");
        BlogEntry blogEntry3 = new BlogEntry(new Date(), "Blog entry #3. The approach we will use is very straightforward:"," Add the user to our userUsernameMap when he connects, remove him when he disconnects, and send all his messages to all users. Since we want usernames, but don’t want complexity, we will generate usernames based on when someone connects to the server:", "#TAG1, #TAG2, #TAG3");
        BlogEntry blogEntry4 = new BlogEntry(new Date(), "Blog entry #4. We also need to create a few methods"," for sending messages to all our connected users. We will only send messages to users whose session has the status open (Session::isOpen). We use a stream and a filter to reduce our list (the keySet of our userUsernameMap), then send out a JSON structure containing a HTML message and a list of usernames (the values of our userUsernameMap):", "#TAG1, #TAG2, #TAG3");
        BlogEntry blogEntry5 = new BlogEntry(new Date(), "Blog entry #5. In this tutorial we will create a simple real-time chat application. ","It will feature a chat-panel that stores messages received after you join, a list of currently connected users, and an input field to send messages from. We will be using WebSockets for this, as WebSockets provides us with full-duplex communication channels over a single TCP connection, meaning we won’t have to make additional HTTP requests to send and receive messages.", "#TAG1, #TAG2, #TAG3");

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



        before((req, res) -> {
            String username = req.cookie("username");
            String password = req.cookie("password");
            if ((username != null)&&(password != null)) {
                if (username.equals(USERNAME_ADMIN)&&password.equals(PASSWORD_ADMIN)) {
                    req.attribute("username", username);
                    req.attribute("password", password);
                }
            }
        });

        before("/entries", (req, res) -> {
            if (req.attribute("username") == null) {
                setFlashMessage(req, "Whoops, please sign in first!");
                res.redirect("/");
                halt();
            }
        });

        before("/new", (req, res) -> {
            String password = req.cookie("password");
            if (password != PASSWORD_ADMIN) {
                setFlashMessage(req, "Whoops, you cant post new entries since you are not admin");
                res.redirect("/");
                halt();
            }

            if (req.attribute("username") == null) {
                setFlashMessage(req, "Whoops, please sign in first!");
                res.redirect("/");
                halt();
            }
        });

        before("/edit", (req, res) -> {
            String password = req.cookie("password");
            if (password != PASSWORD_ADMIN) {
                setFlashMessage(req, "Whoops, you cant edit entries since you are not admin");
                res.redirect("/");
                halt();
            }

            if (req.attribute("username") == null) {
                setFlashMessage(req, "Whoops, please sign in first!");
                res.redirect("/");
                halt();
            }
        });

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.attribute("username"));
            model.put("flashMessage", captureFlashMessage(req));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            res.cookie("username", username);
            res.cookie("password", password);
            res.redirect("/");
            return null;
        });


        get("/entries", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Blog");
            model.put("tags", blog.getAllTags());
            String selectedtag = rq.queryParams("selectedtag");
            if (selectedtag != null && selectedtag.equals(Blog.ALL_ENTRIES_KEY)) selectedtag = ""; // Clear selected tag - equal all tags
            if (selectedtag != null && !selectedtag.equals("")) {
                model.put("titleText", "Entries selected by TAG: "+selectedtag);
                model.put("blogEntry", blog.entriesSelectedByTag(selectedtag));
            } else {
                model.put("titleText", "All Entries:");
                model.put("blogEntry", blog.findAllEntries());
            }
            return new ModelAndView(model, "entries.hbs");
        }, new HandlebarsTemplateEngine());

        get("/new", (rq, rs) -> {
            return new ModelAndView(null, "new.hbs");
        }, new HandlebarsTemplateEngine());

        post("/new", (rq, rs) -> {
            BlogEntry entry = new BlogEntry(new Date(), rq.queryParams("title"), rq.queryParams("text"), rq.queryParams("tags"));
            blog.addEntry(entry);
            rs.redirect("/entries");
            return null;
        });

        get("/edit/:slug", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("entry", blog.findEntryBySlug(rq.params("slug")));
            model.put("slug", rq.params("slug"));
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/edit/:slug", (rq, rs) -> {
            BlogEntry entry = blog.findEntryBySlug(rq.params("slug"));
            String title = rq.queryParams("title");
            String text = rq.queryParams("text");
            String tags = rq.queryParams("tags");
            if (title!=null && !title.equals("")) entry.setTitle(title);
            if (text!=null && !text.equals("")) entry.setText(text);
            if (tags!=null && !tags.equals("")) entry.setTags(tags);
            rs.redirect("/entries");
            return null;
        });

        get("/delete/:slug", (rq, rs) -> {
            BlogEntry entry = blog.findEntryBySlug(rq.params("slug"));
            blog.deleteEntry(entry);
            rs.redirect("/entries");
            return null;
        });

        post("/entry/:slug/comment", (rq, rs) -> {
            BlogEntry entry = blog.findEntryBySlug(rq.params("slug"));
            String author = rq.queryParams("author");
            String text = rq.queryParams("comment");
            Comment comment = new Comment(new Date(), text, author);
            entry.addComment(comment);
                //            boolean added = idea.addVoter(req.attribute("username"));
                //            if (added) {
                //                setFlashMessage(req, "Thanks for your vote!");
                //            }
                //            else {
                //                setFlashMessage(req, "You already voted!");
                //            }
            String target = "/entry/"+rq.params("slug");
            rs.redirect(target);
            return null;
        });

        get("/entry/:slug", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            ArrayList<String> tagsSplit = blog.findEntryBySlug(rq.params("slug")).getTags();
            model.put("entry", blog.findEntryBySlug(rq.params("slug")));
            model.put("comments", blog.findEntryBySlug(rq.params("slug")).getComments());
            model.put("tags",tagsSplit);
            model.put("slug", rq.params("slug"));
            return new ModelAndView(model, "entry.hbs");
        }, new HandlebarsTemplateEngine());

        exception(NotFoundException.class, (exc, req, res) -> {
            res.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "not-found.hbs"));
            res.body(html);
        });
    }

    private static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY, message);
    }

    private static String getFlashMessage (Request req) {
        if (req.session(false) == null) {
            return null;
        }
        if (!req.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);
    }

    private static String captureFlashMessage (Request req) {
        String message = getFlashMessage(req);
        if (message != null) {
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }

}
