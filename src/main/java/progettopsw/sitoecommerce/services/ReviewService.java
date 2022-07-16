package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.Review;
import progettopsw.sitoecommerce.repositories.ProductRepository;
import progettopsw.sitoecommerce.repositories.ReviewRepository;
import progettopsw.sitoecommerce.repositories.UserRepository;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ReviewAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.ReviewNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;

import javax.persistence.EntityManager;
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
        if(!reviewRepository.existsByProductAndBuyer(review.getProduct(), review.getBuyer())){
            if(productRepository.existsById(review.getProduct().getId())){
                if(userRepository.existsById(review.getBuyer().getId())){
                    review.getBuyer().getReviews().add(review);
                    review.getProduct().getReviews().add(review);
                    review.getProduct().setScore((review.getProduct().getScore()+review.getStars())/review.getProduct().getReviews().size());
                } else {
                    throw new UserNotFoundException();
                }
            } else {
               throw new ProductNotFoundException();
            }
        } else {
            throw new ReviewAlreadyExistsException();
        }
        return reviewRepository.save(review);
    }//addReview

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeReview(String id){
        int ident = Integer.parseInt(id);
        Review review = reviewRepository.getById(ident);
        float total = 0;
        for(Review r : review.getProduct().getReviews()){
            total += r.getStars();
        }
        review.getProduct().getReviews().remove(review);
        review.getProduct().setScore((total-review.getStars())/review.getProduct().getReviews().size());
        review.getBuyer().getReviews().remove(review);
        reviewRepository.delete(review);
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
            float total = 0;
            for(Review r : result.getProduct().getReviews()){
                total += r.getStars();
            }
            total -= result.getStars();
            result.getProduct().setScore(total/result.getProduct().getReviews().size()-1);
            result.setStars(stars);
            result.getProduct().setScore((total+result.getStars())/result.getProduct().getReviews().size());
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
    public Review getReview(String id){
        int ident = Integer.parseInt(id);
        return reviewRepository.getById(ident);
    }//getReview

    @Transactional(readOnly = true)
    public List<Review> getAll(String id){
        int ident = Integer.parseInt(id);
        return reviewRepository.getAllByProduct(ident);
    }//getAll

}//ReviewService
