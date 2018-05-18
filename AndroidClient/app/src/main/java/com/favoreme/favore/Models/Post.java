package com.favoreme.favore.Models;

/**
 * Object for the Post
 */


public class Post {
    String poster;
    String post_text;
    boolean isImage;
    String post_image_url;
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


    public Post(String post_id, String poster, String post, long time, long favors, String poster_profile_url,Boolean isImage, String post_image_url) {
        this.poster = poster;
        this.post_text = post;
        this.post_id = post_id;
        this.time = time;
        this.favors = favors;
        this.poster_profile_url = poster_profile_url;
        this.isImage = isImage;
        if (this.isImage){
            this.post_image_url = post_image_url;
        }
    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post) {
        this.post_text = post_text;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



}
