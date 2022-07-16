package progettopsw.sitoecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.Review;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.services.ReviewService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ReviewAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.ReviewNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/products/{id}/reviews")
public class ReviewingController {
    @Autowired
    private ReviewService reviewService;

    @DeleteMapping("/{id2}")
    public void delete(@PathVariable String id2){
        reviewService.removeReview(id2);
    }//delete

    @PutMapping("/{id2}/updateComment")
    public ResponseEntity updateComment(@RequestBody String comment, @PathVariable String id2){
        try{
            Review review = reviewService.updateComment(comment, id2);
            return new ResponseEntity(review, HttpStatus.OK);
        }catch(ReviewNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateComment

    @PutMapping("/{id2}/updateStars")
    public ResponseEntity updateStars(@RequestBody int stars, @PathVariable String id2){
        try{
            Review review = reviewService.updateStars(stars, id2);
            return new ResponseEntity(review, HttpStatus.OK);
        }catch(ReviewNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateStars

    @PutMapping("/{id2}/updateTitle")
    public ResponseEntity updateTitle(@RequestBody String title, @PathVariable String id2){
        try{
            Review review = reviewService.updateTitle(title, id2);
            return new ResponseEntity(review, HttpStatus.OK);
        }catch(ReviewNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateTitle

    @GetMapping("/{id2}")
    public Review getReview(@PathVariable String id2){
        return reviewService.getReview(id2);
    }//getReview

}//ReviewingController
