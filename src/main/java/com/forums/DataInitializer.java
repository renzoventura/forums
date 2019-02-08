package com.forums;

import com.forums.model.Account;
import com.forums.model.Comment;
import com.forums.model.Post;
import com.forums.repository.AccountRepository;
import com.forums.repository.CommentRepository;
import com.forums.repository.PostRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentRepository commentRepository;


  @Autowired
  AccountRepository accountRepository;

  @Autowired
  PasswordEncoder passwordEncoder;


  @Override
  public void run(String... args) {
    Post post1 = new Post();
    post1.setPostTitle("This is my first post!");

    Post post2 = new Post();
    post2.setPostTitle("This is my second post!");

    Comment comment1 = new Comment();
    comment1.setCommentDescription("This comment1 belongs to post1");
    comment1.setPost(post1);

    Comment comment3 = new Comment();
    comment3.setCommentDescription("This comment3 belongs to post 1");
    comment3.setPost(post1);

    Comment comment2 = new Comment();
    comment2.setCommentDescription("This comment2 belongs to post2");
    comment2.setPost(post2);

    postRepository.save(post1);
    postRepository.save(post2);
    commentRepository.save(comment1);
    commentRepository.save(comment2);
    commentRepository.save(comment3);


    Account user = new Account();
    user.setUsername("user");
    user.setPassword(this.passwordEncoder.encode("password"));
    user.setRoles(Arrays.asList("ROLE_USER"));

    Account admin = new Account();
    admin.setUsername("admin");
    admin.setPassword(this.passwordEncoder.encode("password"));
    admin.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

    this.accountRepository.save(user);
    this.accountRepository.save(admin);


  }
}
