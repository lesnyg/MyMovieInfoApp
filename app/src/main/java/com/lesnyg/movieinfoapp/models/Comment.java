package com.lesnyg.movieinfoapp.models;

public class Comment {
    private int uid;
    private String comment;
    private int movieId;

    public Comment() {
    }

    public Comment(int uid, String comment, int movieId) {
        this.uid = uid;
        this.comment = comment;
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getId() {
        return uid;
    }

    public void setId(int uid) {
        this.uid = uid;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
