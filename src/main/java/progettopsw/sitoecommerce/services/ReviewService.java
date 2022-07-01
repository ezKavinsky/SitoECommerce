package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.Review;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.repositories.ProductRepository;
import progettopsw.sitoecommerce.repositories.ReviewRepository;
import progettopsw.sitoecommerce.repositories.UserRepository;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ReviewAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.ReviewNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Review addReview(Review review) throws UserNotFoundException, ProductNotFoundException, ReviewAlreadyExistsException{
        Review result;
        if(!reviewRepository.existsById(review.getId())){
            if(productRepository.existsById(review.getProduct().getId())){
                if(userRepository.existsById(review.getUser().getId())){
                    result = reviewRepository.save(review);
                    result.getUser().getReviews().add(review);
                    result.getProduct().getReviews().add(review);
                    result.getProduct().setScore((result.getProduct().getScore()+result.getStars())/result.getProduct().getReviews().size());
                    entityManager.refresh(result);
                } else {
                    throw new UserNotFoundException();
                }
            } else {
               throw new ProductNotFoundException();
            }
        } else {
            throw new ReviewAlreadyExistsException();
        }
        return result;
    }//addReview

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Review removeReview(Review review) throws ReviewNotFoundException{
        Review result;
        if(reviewRepository.existsById(review.getId())){
            result = reviewRepository.getById(review.getId());
            result.getProduct().setScore((result.getProduct().getScore()-result.getStars())/result.getProduct().getReviews().size()-1);
            result.getProduct().getReviews().remove(result);
            result.getUser().getReviews().remove(result);
            reviewRepository.delete(review);
        } else {
            throw new ReviewNotFoundException();
        }
        return result;
    }//removeReview

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateComment(String comment, Review review) throws ReviewNotFoundException{
        if(reviewRepository.existsById(review.getId())){
            reviewRepository.getById(review.getId()).setComment(comment);
        } else {
            throw new ReviewNotFoundException();
        }
    }//updateComment

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateStars(int stars, Review review) throws ReviewNotFoundException{
        if(reviewRepository.existsById(review.getId())){
            Product product = review.getProduct();
            product.setScore((product.getScore()-review.getStars())/product.getReviews().size()-1);
            reviewRepository.getById(review.getId()).setStars(stars);
            product.setScore((product.getScore()+ review.getStars())/product.getReviews().size());
        } else {
            throw new ReviewNotFoundException();
        }
    }//updateStars

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateTitle(String title, Review review) throws ReviewNotFoundException{
        if(reviewRepository.existsById(review.getId())){
            reviewRepository.getById(review.getId()).setTitle(title);
        } else {
            throw new ReviewNotFoundException();
        }
    }//updateTitle

    @Transactional(readOnly = true)
    public List<Review>  showReviewsByAdvancedSearch(String title, int lowStars, int highStars, Product product, User user){
        return reviewRepository.advancedSearch(title, lowStars, highStars, product, user);
    }//showReviewsByAdvancedSearch

    @Transactional(readOnly = true)
    public List<Review>  showReviewsByAdvancedPagedSearch(int pageNumber, int pageSize, String sortBy, String title, int lowStars, int highStars,
                                                          Product product, User user){
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<Review> pagedResult = reviewRepository.advancedPagedSearch(title, lowStars, highStars, product, user, paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }//showReviewsByAdvancedSearch

}//ReviewService
