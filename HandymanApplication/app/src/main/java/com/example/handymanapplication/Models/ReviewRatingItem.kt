package com.example.handymanapplication.Models


data class ReviewRatingItem(
    var id: Int,
    var feedback: String,
    var rating: Double,
    var client_name: String,
    var client_image: String
) {
}