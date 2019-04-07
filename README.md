# Forums
A FullStack project made with Spring boot, JPA, Hibernate and Spring Security for backend, Angular 7 for Frontend. Designed to be a clone of generic forums eg. Reddit Facebook etc

# Features
* CRUD Restful api/service 
* Secured APIS using Spring Security (ROLE BASED)
* JWT token based Authentication 
* Jpa/hibernate @ManyToOne Relationships 

# APIS
URI|REQUEST|DESCRIPTION| 
---|---|---
*POST*
/post|GET| Retrive all Posts
/post/{id}|GET| Retrive Post by id
/post|POST| Insert a Post with Account taken from bearer token
/post|DEL| Retrive a Post by
*COMMENTS*
/comment|GET| Retrive all comment
/comment/{id}|GET| Retrive comment by id
/comment|POST| Insert a comment with Account taken from bearer token
/comment|DEL| Delete a comment by
*AUTH*
/auth/signin|POST| generate a token from credentials in request body
/auth/user/signup|POST| insert user details with ROLE_USER
/auth/admin/signup|POST| insert user details with ROLE_USER and ROLE_ADMIN
/auth/me|GET| Retrive user details from bearer token



