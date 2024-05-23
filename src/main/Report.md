# Quackstagram - A Social Media Platform with SQL Backend

## Course Project
**Deadline:** Sunday, May 26, 2024

## Students

| Student name | Student ID | Contributions |
| ------------ | ---------- | ------------- |
| H.PHAM       | i6358696   |               |
| KAROL        |            |               |

  
---  

## Table of Contents
1. [Project Overview](#project-overview)
2. [Part A – Design a Relational Database Schema](#part-a)
3. [Part B – Implement a MySQL-Compatible Relational Database Schema](#part-b)
4. [Part C – Integration and Functional Application Development](#part-c)
5. [Part D – Profit Maximization SQL Queries](#part-d)
6. [Part E – Report](#part-e)
7. [Submission Guidelines](#submission-guidelines)
8. [Marking Scheme](#marking-scheme)

---  

## Project Description
This version of the project aims
to enhance the capabilities of Quackstagram by integrating a MySQL-compatible relational database schema.
The Database Schema will be designed based on the features and functionalities of Quackstagram,
and the application will be modified to interact with the database instead of text files.
The project will also include the implementation of SQL queries to answer specific questions for Cheapo Technologies.


## Part A – Design a Relational Database Schema
### Objective
Design a relational database schema based on Quackstagram’s features and functionalities inferred from the Java source code.

### Tasks
1. **Detailed Entity Analysis**
   - Identify Entities: users, posts, image_data, follows, sessions
   - Identify Attributes:
      - users: user_id, username, password, bio
      - posts: post_id, liker_id, image_id, timestamp
      - image_data: image_id, user_id, image_bio, post_time, file_path
      - follows: follower_id, followee_id
      - sessions: session_id, user_id

2. **Relationship Mapping**  
   2.1. Determine Relationship Types
   1. Users to Image Data: One-to-Many
      - One user can have many image
      - Relationship: `user_id` in `image_data` references `user_id` in `users`.
   2. Users to Posts: One-to-Many
      - One user can have multiple activity (likes).
      - Relationship: `liker_id` in `posts` references `user_id` in `users`.
   3. User to User (Follows): Many-to-Many
      - One user can have multiple users and be followed by multiple users.
      - Relationship: `follow_id` and`followee_id` in `follows` both reference `user_id` in `users`.
   4. User and Session: One-to-Many
      - One user can have many logged-in sessions.
      - Relationship: `user_id` in `sessions` references `user_id` in `users`
   5. Post and Image Data: One to Many
      - One post can be liked multiple times.
      - Relationship: `image_id` in `posts` references `image_id` in `image_data`.

3. **Normalization and Functional Dependencies**
   3.1 Functional Dependencies
   3.1.1 Functional Dependencies for `users` Table'
      - user_id -> username, password, bio
      - username -> user_id, password, bio
   3.1.2 Functional Dependencies for `posts` Table 
      - post_id -> liker_id, image_id, timestamp
      - liker_id, image_id -> post_id, timestamp
   3.1.3 Functional Dependencies for `image_data` Table
      - image_id -> user_id, image_bio, post_time, file_path
   3.1.4 Functional Dependencies for `follows` Table
      - follower_id, followee_id -> Unique key
   3.1.5 Functional Dependencies for `sessions` Table
      - session_id -> user_id
   3.2 Normalize Tables up to 3NF or BCNF
   3.2.1. `users` Table: 3NF
      - Attributes: user_id, username, password, bio
      - Primary Key: user_id
      Proof of 3NF:
      - Each attribute contains only atomic -> 1NF
      - All non-prime attributes are fully functionally dependent on the primary key -> 2NF
      - No transitive dependencies because there are no non-prime attributes dependent on other non-prime attributes. -> 3NF
   3.2.2. `post` Table: 3NF
      - Attributes: post_id, liker_id, image_id, timestamp
      - Primary Key: post_id
      - Candidate key: post_id, liker_id and image_id
      - Prime Attribute: post_id, liker_id, image_id
      - Non-Prime Attribute: timestamp
      Proof of 3NF:
      - Each attribute contains only atomic -> 1NF
      - No partial dependencies (`timestamp` is not partially dependent on liker_id or image_id) -> 2NF
      - No transitive dependencies because there are no non-prime attributes dependent on other non-prime attributes. -> 3NF
   3.2.3. `image_data` Table: 3NF
      - Attributes: image_id, user_id, image_bio, post_time, file_path
      - Primary Key: image_id
      - Candidate key: image_id
      - Prime Attribute: image_id
      - Non-Prime Attribute: user_id, image_bio, post_time, file_path
      Proof of 3NF:
      - Each attribute contains only atomic -> 1NF
      - All non-prime attributes are fully functionally dependent on the primary key (`image_id`) -> 2NF
      - No non-prime attributes dependent on other non-prime attributes. -> 3NF
   3.2.4 `follows` Table: 3NF
      - Attributes: follower_id, followee_id
      - Primary Key: follower_id, followee_id
      - Candidate key: follower_id, followee_id
      - Prime Attribute: follower_id, followee_id
      - Non-Prime Attribute: None 
      Proof of 3NF:
      - Each attribute contains only atomic -> 1NF
      - All non-prime attributes are fully functionally dependent on the primary key (`follower_id`, `followee_id`) -> 2NF
      - No non-prime attributes dependent on other non-prime attributes. -> 3NF
   3.2.5 `sessions` Table: 3NF
      - Attributes: session_id, user_id
      - Primary Key: session_id
      - Candidate key: session_id
      - Prime Attribute: session_id
      - Non-Prime Attribute: user_id
      Proof of 3NF:
      - Each attribute contains only atomic -> 1NF
      - All non-prime attributes are fully functionally dependent on the primary key (`session_id`) -> 2NF
      - No non-prime attributes dependent on other non-prime attributes. -> 3NF
   
4. **ERD**  
   ![ERD Diagram](src/main/ProjectImg/Screenshot 2024-05-21 at 10.21.43.png)


---  

## Part B – Implement a MySQL-Compatible Relational Database Schema
### Objective
Develop a MySQL database schema based on the design, and prepare it for integration with the Java application.

### Tasks
1. **Schema.sql**
   The content of this will be included in the schema.sql file.

2. **Views.sql**
   1. Create 3 Views for User Behavior, Content Popularity, and System Analytics
      1.1 View to display the most active user in a period of time (user who likes more than 5 posts).
         <br/> Rationale: This view will help track user activity and engagement on the platform.
      CREATE VIEW ActiveUsers AS
      SELECT liker_id, COUNT(*) AS post_count
      FROM posts
      WHERE time >= '2024-05-01' AND time <= '2024-05-31'
      GROUP BY liker_id
      HAVING COUNT(*) > 5;

   2. Create Indexes for Performance Optimization
      2.1. Index on `user_id` in `posts` table   
         <br/> Rationale: The fetching process is used frequently in the application. Besides, this fetching requires a join operation with the `users` table, then it is beneficial to have an index on the `user_id` column in the `posts` table.  
         <br/> Performance before indexing: 0.005 seconds  
         <br/> Performance after indexing: 0.003 seconds, 60% faster.

3. **Triggers.sql**
   1. Triggers to prevent users from liking their posts. This will help maintain the integrity and consistency of the database.
      DELIMITER //

   CREATE TRIGGER prevent_self_like
   BEFORE INSERT
   ON QuackstagramDB.posts
   FOR EACH ROW
   BEGIN
   DECLARE owner_id INT;
   
       SELECT user_id into owner_id
       FROM QuackstagramDB.image_data id 
       WHERE NEW.image_id = id.image_id;
       
       IF NEW.user_id = owner_id THEN
           SIGNAL SQLSTATE '45000'
           SET MESSAGE_TEXT = 'A user cannot like their own post';
       END IF;
   END;
   //

   DELIMITER ;
   2. Stored Procedure to generate report of number of likes and number of posts for each user in the a specific month and year.
      DELIMITER //

   CREATE PROCEDURE QuackstagramDB.generateUserReport (
   IN userID INT,
   IN reportMonth INT,
   IN reportYear INT
   )
   BEGIN
   DECLARE post_count INT DEFAULT 0;
   DECLARE like_count INT DEFAULT 0;
   DECLARE start_date DATE;
   DECLARE end_date DATE;
   
       -- Calculate the start and end dates
       SET start_date = STR_TO_DATE(CONCAT(reportYear, '-', reportMonth, '-01'), '%Y-%m-%d');
       SET end_date = LAST_DAY(start_date);
   
       -- Calculate total posts within the date range
       SELECT COUNT(image_id)
       INTO post_count
       FROM QuackstagramDB.image_data id
       WHERE id.user_id = userID AND post_time BETWEEN start_date AND end_date;
   
       -- Calculate total likes received within the date range
       SELECT COUNT(p.liker_id)
       INTO like_count
       FROM QuackstagramDB.posts p
       JOIN QuackstagramDB.image_data id ON p.image_id = id.image_id
       WHERE id.user_id = userID AND p.time BETWEEN start_date AND end_date;
   
       SELECT post_count AS TotalPosts, like_count AS TotalLikesReceived;
   
   END //
   
   DELIMITER ;
  
---  

## Part C – Integration and Functional Application Development
### Objective
Integrate the designed database with the Java application to create a fully functional Quackstagram platform.

### Tasks
1. **Database Connection**
   - Set up JDBC Connection
   - Provide Code for Database Connection, Querying, and Data Manipulation

2. **Feature Implementation**
   - Replace Text Files with Database for Key Features

3. **Error Handling and Security**
   - Implement Error Handling for Database Interactions

---  

## Part D – Profit Maximization SQL Queries
### Objective
Write SQL queries to answer specific questions for Cheapo Technologies.

### Questions
1. List all users who have more than X followers.

   SELECT f.followee_id as user_has_more_than
   FROM QuackstagramDB.follows f INNER JOIN QuackstagramDB.users u ON f.followee_id = u.user_id
   GROUP BY f.followee_id
   HAVING COUNT(follower_id) > X

2. Show the total number of posts made by each user.

   SELECT COUNT(image_id) as number_of_posts
   FROM QuackstagramDB.image_data id INNER JOIN QuackstagramDB.users u ON id.user_id = u.user_id
   WHERE id.user_id = X
   GROUP BY id.user_id

3. Find all comments made on a particular user’s post. 
   We did not have a comment function working last period. So, we will not have a comment table to query.
4. Display the top X most liked posts.

   SELECT COUNT(liker_id) as number_likes
   FROM QuackstagramDB.posts p
   GROUP BY image_id
   ORDER BY number_likes DESC
   LIMIT X 
   -- Where X is the number of top posts you want to display

5. Count the number of posts each user has liked.

   SELECT liker_id,COUNT(image_id)
   FROM QuackstagramDB.posts p
   GROUP BY p.liker_id

6. List all users who haven’t made a post yet.

   SELECT u.user_id, u.username
   FROM QuackstagramDB.users u LEFT JOIN QuackstagramDB.image_data id ON u.user_id = id.user_id
   WHERE image_id is NULL
   ORDER BY user_id ASC

7. List users who follow each other.

   SELECT f1.follower_id, f1.followee_id
   FROM QuackstagramDB.follows f1 LEFT JOIN QuackstagramDB.follows f2 ON f1.followee_id = f2.follower_id
   WHERE f1.follower_id = f2.followee_id AND  f1.followee_id > f1.follower_id

8. Show the user with the highest number of posts.

   SELECT temp.user_id 
   FROM (SELECT u.user_id, COUNT(image_id) as number_of_posts
   FROM QuackstagramDB.image_data id INNER JOIN QuackstagramDB.users u ON id.user_id = u.user_id
   GROUP BY id.user_id) as temp
   ORDER BY temp.number_of_posts DESC
   LIMIT 1

9. List the top X users with the most followers.

   SELECT temp.user_id
   FROM(SELECT COUNT(follower_id) as number_of_follows, u.user_id
   FROM QuackstagramDB.follows f INNER JOIN QuackstagramDB.users u ON f.followee_id = u.user_id
   GROUP BY f.followee_id
   ORDER BY number_of_follows DESC
   LIMIT X) as temp
   -- X is the number of top users you want to display

   10. Find posts that have been liked by all users.

   SELECT image_id
   FROM QuackstagramDB.posts p
   GROUP BY image_id
   HAVING COUNT(p.liker_id) = (SELECT COUNT(*) FROM QuackstagramDB.users);

   
11. Display the most active user.
12. Find the average number of likes per post for each user.
13. Show posts that have more comments than likes.
14. List the users who have liked every post of a specific user.
15. Display the most popular post of each user.
16. Find the user(s) with the highest ratio of followers to following.
17. Show the month with the highest number of posts made.
18. Identify users who have not interacted with a specific user’s posts.
19. Display the user with the greatest increase in followers in the last X days.
20. Find users who are followed by more than X% of the platform users.

---  

## Part E – Report
### Contents
1. Names and IDs of Group Members and Their Contributions
2. Database Description and Usage in Quackstagram
3. Entity-Relationship Diagram
4. Example of Each Table with Data and Primary Key Attributes
5. List of Functional Dependencies for Each Table
6. Proof of 3NF for Each Table
7. Justification for Views Proposed in Part B
8. Analysis of Query Speeds and Index Justification
9. Justification for Triggers and Procedures
10. SQL Queries with Answers from Part D

---  

## Submission Guidelines
- **Deadline:** End of Week 6
- **Format:** Submit a `.zip` file containing the detailed report (Part E) and all code files (Parts B, C, and D) on Canvas.

---  

## Marking Scheme (Total 25 Points)
1. **Schema Design (2 points)**
   - Thoughtfulness, Correctness, and Efficiency

2. **Normalization (4 points)**
   - Proper Normalization for 4 Tables

3. **Application Integration and Functionality (6 points)**
   - Effective Integration with Proper Error Handling

4. **Views, Triggers, and Stored Procedures (5 points)**
   - Implementation of Views, Indexes, Triggers, and Procedures

5. **Profit Maximization SQL Queries (8 points)**
   - Appropriate and Non-Hardcoded Queries

---  

### Grading Rubric

| Criterion                          | Grading Scheme                                     | Points |  
|------------------------------------|----------------------------------------------------|--------|  
| Schema Design                      | Thoughtful, correct, and efficient design          | 2      |  
|                                    | Incomplete or inefficient design                   | 1      |  
|                                    | No design provided                                 | 0      |  
| Normalization                      | All tables properly normalized (1 point per table) | 4      |  
|                                    | Some tables not properly normalized                | 1-3    |  
|                                    | No normalization performed                         | 0      |  
| Application Integration and        | Well-integrated code with proper error handling    | 6      |  
| Functionality                      | Integrated code, but violates OOP principles       | 4      |  
|                                    | Code not well-integrated or clear issues           | < 4    |  
|                                    | Lack of proper error handling                      | -2     |  
| Views                              | All 3 required views implemented correctly         | 1.5    |  
|                                    | Some views implemented correctly                   | 0.5-1  |  
|                                    | No views implemented                               | 0      |  
| Indexes                            | Appropriate indexes implemented                    | 1      |  
|                                    | Some appropriate indexes implemented               | 0.5    |  
|                                    | No indexes implemented                             | 0      |  
| Triggers                           | Well-written triggers adhering to requirements     | 1.5    |  
|                                    | Partially correct triggers                         | 0.75-1.5|  
|                                    | No triggers implemented                            | 0      |  
| Stored Procedures and Functions    | Appropriate procedures and functions implemented   | 1      |  
|                                    | Some appropriate procedures and functions          | 0.5    |  
|                                    | No procedures or functions implemented             | 0      |  
| Profit Maximization SQL Queries    | Appropriate and non-hardcoded queries              | 8      |  
|                                    | Hardcoded or incorrect queries                     | 0      |  
  
---  

## Conclusion
This template provides a structured outline for completing and documenting your final project.