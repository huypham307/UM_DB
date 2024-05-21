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

## Project Overview
Cheapo Software Solutions (CSS Inc) is looking to enhance their Quackstagram platform by adding a database for better data management and to provide analytics. This project involves designing a relational database, implementing it in MySQL, integrating it with Java code, and developing various SQL queries for analytics.

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
   - Document Functional Dependencies for 4 Tables
   - Normalize Tables up to 3NF or BCNF

4. **ERD**  
   ![ERD Diagram](src/main/ProjectImg/Screenshot 2024-05-21 at 10.21.43.png)


---  

## Part B – Implement a MySQL-Compatible Relational Database Schema
### Objective
Develop a MySQL database schema based on the design, and prepare it for integration with the Java application.

### Tasks
1. **Schema.sql**
   - Write `CREATE TABLE` Statements
   - Include Column Constraints and Types

2. **Views.sql**
   1. Create 3 Views for User Behavior, Content Popularity, and System Analytics
   2. Create Indexes for Performance Optimization
      1. Index on `user_id` in `posts` table   
         <br/> Rationale: The fetching process is used frequently in the application. Besides, this fetching requires a join operation with the `users` table, then it is beneficial to have an index on the `user_id` column in the `posts` table.  
         <br/> Performance before indexing: 0.005 seconds  
         <br/> Performance after indexing: 0.003 seconds, 60% faster.

3. **Triggers.sql**
   - Create 1 Procedure, 1 Function, and 2 Triggers

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
2. Show the total number of posts made by each user.
3. Find all comments made on a particular user’s post.
4. Display the top X most liked posts.
5. Count the number of posts each user has liked.
6. List all users who haven’t made a post yet.
7. List users who follow each other.
8. Show the user with the highest number of posts.
9. List the top X users with the most followers.
10. Find posts that have been liked by all users.
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
This template provides a structured outline for completing and documenting your final project. Ensure you follow the specifications closely and provide clear, concise documentation and code. Good luck with your project!