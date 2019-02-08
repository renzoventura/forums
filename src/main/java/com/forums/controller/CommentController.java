package com.forums.controller;

import com.forums.model.Comment;
import com.forums.model.Post;
import com.forums.repository.AccountRepository;
import com.forums.repository.CommentRepository;
import com.forums.repository.PostRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
  private void insertComment(@PathVariable long postId, @RequestBody Comment comment, @AuthenticationPrincipal
      UserDetails userDetails){
    Post foundPost = postRepository.findById(postId).get();
    comment.setPost(foundPost);
    comment.setAccount(accountRepository.findByUsername(userDetails.getUsername()).get());
    commentRepository.save(comment);
  }

  @DeleteMapping("/{id}")
  private void deleteComment(@PathVariable long commentId){
    commentRepository.deleteById(commentId);
  }

}
