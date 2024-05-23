-- View to display the most active user within a period of time (most active means that user has liked more than 5 posts)
CREATE VIEW ActiveUsers AS
SELECT liker_id, COUNT(*) AS post_count
FROM posts
WHERE time >= '2024-05-01' AND time <= '2024-05-31'
GROUP BY liker_id
HAVING COUNT(*) > 5;
