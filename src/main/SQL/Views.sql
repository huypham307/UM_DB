-- View to display the most active user within a period of time (most active means that user has liked more than 5 posts)
CREATE VIEW ActiveUsers AS
SELECT liker_id, COUNT(*) AS post_count
FROM posts
WHERE time >= '2024-05-01' AND time <= '2024-05-31'
GROUP BY liker_id
HAVING COUNT(*) > 5;


-- View to 5 most liked photos in 2024. This will provide quick access to the most popular posts of this year
CREATE VIEW five_most_liked_2024 AS
SELECT image_id, COUNT(liker_id) as count
FROM posts
WHERE YEAR(time) = 2024
GROUP BY image_id
HAVING Count(post_id) > 2
ORDER BY count DESC
LIMIT 5;

-- View to number of photos with at least 2 likes for each year. This will provide information on how active the platform is every year for system analytics
CREATE VIEW posts_with_2_likes_per_year AS
SELECT YEAR(time) as year, Count(image_id) as post_count
FROM posts
WHERE image_id IN
        (SELECT image_id
        FROM posts
        GROUP BY image_id
        HAVING COUNT(liker_id) > 2)
GROUP BY year;
