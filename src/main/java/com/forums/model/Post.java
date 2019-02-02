package com.forums.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "Posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "postid")
  @NotNull
  private Long postId;

  @Column(name = "postTitle")
  private String postTitle;

  @Column(name = "creationDate")
  @UpdateTimestamp
  private Date creationDate;

  public Long getPostId() {
    return postId;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }
}
