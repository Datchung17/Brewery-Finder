package com.techelevator.dao;

import com.techelevator.model.Brewery;
import com.techelevator.model.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcReviewDao implements reviewDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcReviewDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public List<Review> getReviewsForBeerById(int beer_id) {
        List<Review> allReviews = new ArrayList<>();
        String sqlSelectAllReviews = "SELECT * FROM reviews WHERE beer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllReviews, beer_id);

        while(results.next()) {
            Review review = mapRowToReview(results);
            allReviews.add(review);
        }
        return allReviews;
    }

    @Override
    public List<Review> getReviewsForBreweryById(int brewery_id) {
        List<Review> allReviews = new ArrayList<>();
        String sqlSelectAllReviews = "SELECT reviews.*, beers.name, users.username " +
                "FROM reviews " +
                "INNER JOIN beers ON reviews.beer_id = beers.beer_id " +
                "INNER JOIN users ON reviews.user_id = users.user_id " +
                "WHERE reviews.brewery_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllReviews, brewery_id);

        while(results.next()) {
            Review review = mapRowToReview(results);
            allReviews.add(review);
        }
        return allReviews;
    }

    @Override
    public void addReview(Review review) {
        String sqlAddReview = "INSERT INTO reviews(user_id, brewery_id, beer_id, text, date, rating, is_for_beers) VALUES(?,?,?,?,?,?,?);";
        jdbcTemplate.update(sqlAddReview, review.getUserId(), review.getBreweryId(), review.getBeerId(), review.getText(), review.getDate(), review.getRating(), review.getIsForBeer());
    }

    @Override
    public void deleteReview(long reviewId) {
        String sqlDeleteReview = "DELETE FROM reviews WHERE review_id = ?;";
        jdbcTemplate.update(sqlDeleteReview, reviewId);
    }

    @Override
    public List<Review> getReviewsForBeerByName(String name) {
        List<Review> allReviews = new ArrayList<>();
        String sqlSelectAllReviews = "SELECT * FROM reviews JOIN beers ON reviews.beer_id = beers.beer_id WHERE UPPER(beers.name) = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllReviews, name);

        while(results.next()) {
            Review review = mapRowToReview(results);
            allReviews.add(review);
        }
        return allReviews;
    }

    @Override
    public List<Review> getReviewsForBreweryByName(String name) {
        List<Review> allReviews = new ArrayList<>();
        String sqlSelectAllReviews = "SELECT * FROM reviews JOIN brewery ON reviews.brewery_id = brewery.brewery_id WHERE UPPER(brewery.name) = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllReviews, name);

        while(results.next()) {
            Review review = mapRowToReview(results);
            allReviews.add(review);
        }
        return allReviews;
    }

    private Review mapRowToReview(SqlRowSet rs){
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setBeerName(rs.getString("name"));
        review.setBreweryId(rs.getInt("brewery_id"));
        review.setBeerId(rs.getInt("beer_id"));
        review.setText(rs.getString("text"));
        review.setDate(rs.getDate("date"));
        review.setRating(rs.getInt("rating"));
        review.setUsername(rs.getString("username"));
        review.setIsForBeer(rs.getBoolean("is_for_beers"));
        return review;
    }
}
