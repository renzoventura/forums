package com.forums.controller;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

import com.forums.model.Comment;
import com.forums.model.Post;
import com.forums.repository.AccountRepository;
import com.forums.repository.CommentRepository;
import com.forums.repository.PostRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private AccountRepository accountRepository;

  @GetMapping("post/{postId}/comments")
  @CrossOrigin(origins = "http://localhost:4200")
  private ArrayList<Comment> getCommentsByPostId(@PathVariable long postId) {
    ArrayList<Comment> commentList = new ArrayList<Comment>();
    Post post = postRepository.findById(postId).get();

    for(Comment comment : commentRepository.findAll()){
      if (comment.getPost() == post){
        commentList.add(comment);
      }
    }
    return commentList;
  }

  @PostMapping("post/{postId}/comments")
  @CrossOrigin(origins = "http://localhost:4200")
  private ResponseEntity insertComment(@PathVariable long postId, @RequestBody Comment comment, @AuthenticationPrincipal
      UserDetails userDetails) {
    if (!postRepository.findById(postId).isPresent()) {
      return ResponseEntity.badRequest().body("Oops! The post you're commenting on does not exist!");
    }
    Post foundPost = postRepository.findById(postId).get();
    if (comment.getCommentDescription().isEmpty()){
      return ResponseEntity.badRequest().body("Oops! You can't post an empty comment!");
    }
    comment.setPost(foundPost);
    if (!accountRepository.findByUsername(userDetails.getUsername()).isPresent()) {
      return ResponseEntity.badRequest().body("Something went wrong.");
    }
    comment.setAccount(accountRepository.findByUsername(userDetails.getUsername()).get());
    commentRepository.save(comment);
    return ok(comment);
  }

  @DeleteMapping("/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  private ResponseEntity deleteComment(@PathVariable long commentId){
    if (commentRepository.findById(commentId).isPresent()) {
      commentRepository.deleteById(commentId);
      return ok("Comment successfully deleted!");
    } else {
      return badRequest().body("Comment you're trying to delete does not exist.");
    }
  }

}
