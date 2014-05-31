package com.csciDIW.threeprimeevils.Gaming.shared;

import java.io.Serializable;
import java.util.Date;

public class BestBuyReview implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int reviewId;
	private int sku;
	private String reviewer;
	private double rating;
	private String title;
	private String comment;
	private Date submission;
	
	public BestBuyReview(){};
	
	public BestBuyReview(int reviewId, int skuId, String reviewer,
			double rating, String title, String comment, Date submission) {
		this.reviewId = reviewId;
		this.sku = skuId;
		this.reviewer = reviewer;
		this.rating = rating;
		this.title = title;
		this.comment = comment;
		this.submission = submission;
	}

	public int getReviewId() {
		return reviewId;
	}

	public int getSkuId() {
		return sku;
	}

	public String getReviewer() {
		return reviewer;
	}

	public double getRating() {
		return rating;
	}

	public String getTitle() {
		return title;
	}

	public String getComment() {
		return comment;
	}

	public Date getSubmission() {
		return submission;
	}
	
	public void printReviewDetails(){
		System.out.println("ReviewId:" + reviewId);
		System.out.println("SkuId:" + sku);
		System.out.println("Reviewer:" + reviewer);
		System.out.println("Rating:" + rating);
		System.out.println("Title:" + title);
		System.out.println("Comment:" + comment);
		System.out.println("Submission Time:" + submission.toString());
		System.out.println();
	}
	
}
