package com.forums.model;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="Posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "postid")
  @NotNull
  private Long postId;

  @Column(name="postTitle")
  private String title;

  @Column(name="creationDate")
  @UpdateTimestamp
  private Date creationDateTime;

  public Long getPostId() {
    return postId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
