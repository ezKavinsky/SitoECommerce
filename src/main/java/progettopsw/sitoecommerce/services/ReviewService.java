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
    public void removeReview(String id) throws ReviewNotFoundException{
        int ident = Integer.parseInt(id);
        if(reviewRepository.existsById(ident)){
            Review review = reviewRepository.getById(ident);
            review.getProduct().setScore((review.getProduct().getScore()-review.getStars())/review.getProduct().getReviews().size()-1);
            review.getProduct().getReviews().remove(review);
            review.getUser().getReviews().remove(review);
            reviewRepository.delete(review);
        } else {
            throw new ReviewNotFoundException();
        }
    }//removeReview

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Review updateComment(String comment, String id) throws ReviewNotFoundException{
        int ident = Integer.parseInt(id);
        Review result;
        if(reviewRepository.existsById(ident)){
            result = reviewRepository.getById(ident);
            result.setComment(comment);
        } else {
            throw new ReviewNotFoundException();
        }

        return result;
    }//updateComment

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Review updateStars(int stars, String id) throws ReviewNotFoundException{
        int ident = Integer.parseInt(id);
        Review result;
        if(reviewRepository.existsById(ident)){
            result = reviewRepository.getById(ident);
            Product product = result.getProduct();
            product.setScore((product.getScore()-result.getStars())/product.getReviews().size()-1);
            reviewRepository.getById(result.getId()).setStars(stars);
            product.setScore((product.getScore()+ result.getStars())/product.getReviews().size());
        } else {
            throw new ReviewNotFoundException();
        }
        return result;
    }//updateStars

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Review updateTitle(String title, String id) throws ReviewNotFoundException{
        Review result;
        int ident = Integer.parseInt(id);
        if(reviewRepository.existsById(ident)){
            result = reviewRepository.getById(ident);
            result.setTitle(title);
        } else {
            throw new ReviewNotFoundException();
        }
        return result;
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
