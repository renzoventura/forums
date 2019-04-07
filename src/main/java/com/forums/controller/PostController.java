package com.forums.controller;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

import com.forums.model.Post;
import com.forums.repository.AccountRepository;
import com.forums.repository.PostRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private AccountRepository accountRepository;

  @GetMapping
  @CrossOrigin(origins = "http://localhost:4200")
  public Iterable<Post> getAllPosts() {
    return postRepository.findAll();
  }

  @GetMapping("/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  private ResponseEntity getPostById(@PathVariable Long id){
    if (!postRepository.findById(id).isPresent()){
      return badRequest().body("Post cannot be found!");
    }
    return ok(postRepository.findById(id).get());
  }

  @PostMapping
  @CrossOrigin(origins = "http://localhost:4200")
  private ResponseEntity insertPost(@RequestBody Post post, @AuthenticationPrincipal UserDetails userDetails){
    try {
      if (post.getPostTitle().isEmpty() ||  post.getDescription().isEmpty()){
        return badRequest().body("A Post must have a title and description.");
      }
      post.setAccount(accountRepository.findByUsername(userDetails.getUsername()).get());
      postRepository.save(post);
      return ok(post);
    } catch (Exception e){
      return badRequest().body(e);
    }

  }

  @DeleteMapping("/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  private ResponseEntity deletePostById(@PathVariable Long id){
    if (postRepository.findById(id).isPresent()){
      postRepository.delete(postRepository.findById(id).get());
      return ok("Post deleted!");
    } else {
      return badRequest().body("The post you're trying to delete does not exist.");
    }
  }

}
