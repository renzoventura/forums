package com.forums;

import static java.lang.StrictMath.random;

import com.forums.model.Account;
import com.forums.model.Comment;
import com.forums.model.Post;
import com.forums.repository.AccountRepository;
import com.forums.repository.CommentRepository;
import com.forums.repository.PostRepository;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired private PostRepository postRepository;

  @Autowired private CommentRepository commentRepository;

  @Autowired AccountRepository accountRepository;

  @Autowired PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {

    ArrayList<Account> accounts = new ArrayList<>(2);

    Account user = new Account();
    user.setUsername("user");
    user.setPassword(this.passwordEncoder.encode("password"));
    user.setRoles(Arrays.asList("ROLE_USER"));

    Account admin = new Account();
    admin.setUsername("admin");
    admin.setPassword(this.passwordEncoder.encode("password"));
    admin.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

    accountRepository.save(user);
    accountRepository.save(admin);

    accounts.add(user);
    accounts.add(admin);

    String[] postTitleList = {
      "I merged Dark souls 3 gameplay with Stanley Parable Narration",
      "Sue me, but champion gundyr is my fave boss any day",
      "But why though",
      "Just noticed something about the black and silver knights...",
      "Less multiplayer than usual?"
    };

    String[] postDescriptionList = {
      "Wanted to do a fun video editing project and given a love for the dark souls franchise, "
          + "I thought doing a mashup of DS3 and Stanley parable would be a hilarious attempt.",
      "This past week I've been getting less invasion and less summoning than usual. I find almost"
          + " no white signs where it used to be full of them and every time I want to invade I ",
      "I kinda feel like it's losing it's meaning. Long gone were the days when you reserved the"
          + " point down for tiggering scrubs or for after gank spanks, long gone are the days when it evoked an emotional response.",
      "It's no secret that huge bosses are hard to do right - dragon bosses all the more so."
          + " I went into Midir's arena very fearful but very excited also - I'd heard about ",
      "Who's harder on your opinion? Dancer kicked my ass at least 15 times. Whereas,"
          + " Dragon Slayer I beat first try. What do you guys think?"
    };

    String[] commentDescriptionList = {
      "I didn't realize till after I beat him there was a summon",
      "On first playthrough I killed dancer first try, but died 10~ish times on Dragon Slayer",
      "After many, many, many playthroughs they’re both pretty easy for me, but I would say Dancer is definitely the harder of the 2.",
      "I didn’t find dancer very hard. I held up shield while dodging mostly and if I didn’t dodge properly the shield would absorb most hits",
      "Neither. Nameless king. I found both of those bosses easy. Especially dragonslayer armor. "
          + "I honestly don't understand what people find so hard about it. Dancer I can understand"
    };

    for (int i = 0; i < 7; i++) {
      Post newPost = new Post();
      System.out.println("============" + accounts.size());
      newPost.setAccount(accounts.get((int) (random() * accounts.size())));
      newPost.setPostTitle(postTitleList[(int) (random() * (postTitleList.length - 1))]);
      newPost.setDescription(
          postDescriptionList[(int) (random() * ((postDescriptionList.length - 1)))]);
      postRepository.save(newPost);
      for (int x = 0; x < ((int) (random() * ((10) + 1))); x++) {
        Comment newComment = new Comment();
        newComment.setAccount(accounts.get((int) (random() * accounts.size())));
        newComment.setCommentDescription(
            commentDescriptionList[(int) (random() * (commentDescriptionList.length - 1))]);
        newComment.setPost(newPost);
        commentRepository.save(newComment);
      }
    }
  }
}
