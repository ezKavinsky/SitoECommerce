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
import java.util.List;

@RestController("/reviews")
public class ReviewingController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Review review){
        try{
            Review result = reviewService.addReview(review);
            return new ResponseEntity(result, HttpStatus.CREATED);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }catch(ProductNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }catch(ReviewAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }//create

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        reviewService.removeReview(id);
    }//delete

    @PutMapping("/{id}/updateComment")
    public ResponseEntity updateComment(@RequestBody String comment, @PathVariable String id){
        try{
            Review review = reviewService.updateComment(comment, id);
            return new ResponseEntity(review, HttpStatus.OK);
        }catch(ReviewNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateComment

    @PutMapping("/{id}/updateStars")
    public ResponseEntity updateStars(@RequestBody int stars, @PathVariable String id){
        try{
            Review review = reviewService.updateStars(stars, id);
            return new ResponseEntity(review, HttpStatus.OK);
        }catch(ReviewNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateStars

    @PutMapping("/{id}/updateTitle")
    public ResponseEntity updateTitle(@RequestBody String title, @PathVariable String id){
        try{
            Review review = reviewService.updateTitle(title, id);
            return new ResponseEntity(review, HttpStatus.OK);
        }catch(ReviewNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateTitle

    @GetMapping
    public List<Review> getByAdvancedSearch(@RequestBody String title, @RequestBody int lowStars, @RequestBody int highStars,
                                            @RequestBody @Valid User user, @RequestBody @Valid Product product){
        return reviewService.showReviewsByAdvancedSearch(title, lowStars, highStars, product, user);
    }//getByAdvancedSearch

    @GetMapping("/reviews")
    public ResponseEntity getByAdvancedPagedSearch(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                   @RequestParam(value = "title", defaultValue = "null") String title,
                                                   @RequestParam(value = "lowStars", defaultValue = "null") int lowStars,
                                                   @RequestParam(value = "highStars", defaultValue = "null") int highStars,
                                                   @RequestParam(value = "product", defaultValue = "null") @Valid Product product,
                                                   @RequestParam(value = "user", defaultValue = "null") @Valid User user){
        List<Review> result = reviewService.showReviewsByAdvancedPagedSearch(pageNumber, pageSize, sortBy, title, lowStars, highStars, product, user);
        if(result.size() <= 0){
            return new ResponseEntity(new ResponseMessage("No results!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(result,HttpStatus.FOUND);
    }//getByAdvancedPagedSearch

    @GetMapping("/{id}")
    public Review getReview(@PathVariable String id){
        return reviewService.getReview(id);
    }//getReview

}//ReviewingController
