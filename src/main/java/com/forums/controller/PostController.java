package com.forums.controller;

import com.forums.model.Post;
import com.forums.repository.AccountRepository;
import com.forums.repository.PostRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
  private Post getPostById(@PathVariable Long id){
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.get();
  }

  @PostMapping
  @CrossOrigin(origins = "http://localhost:4200")
  private void insertPost(@RequestBody Post post, @AuthenticationPrincipal UserDetails userDetails){
    post.setAccount(accountRepository.findByUsername(userDetails.getUsername()).get());
    postRepository.save(post);
  }

  @DeleteMapping("/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  private void deletePostById(@PathVariable Long id){
    Optional<Post> optionalPost = postRepository.findById(id);
    postRepository.delete(optionalPost.get());
  }

}
