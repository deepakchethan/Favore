package com.favoreme.favore.Models;

/**
 * Object for the Post
 */


public class Post {
    String poster;
    String post;
    Loci location;
    String poster_profile_url;
    String post_id;
    long time,favors;
    public String getPoster_profile_url() {
        return poster_profile_url;
    }

    public void setPoster_profile_url(String poster_profile_url) {
        this.poster_profile_url = poster_profile_url;
    }

    public long getFavors() {
        return favors;
    }

    public void setFavors(long favors) {
        this.favors = favors;
    }




    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }


    public Post(String post_id, String poster, String post, Loci location, long time, long favors, String poster_profile_url) {
        this.poster = poster;
        this.post = post;
        this.post_id = post_id;
        this.location = location;
        this.time = time;
        this.favors = favors;
        this.poster_profile_url = poster_profile_url;
    }

    public Post(String poster, String post) {
        this.poster = poster;
        this.post = post;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Loci getLocation() {
        return location;
    }

    public void setLocation(Loci location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



}
