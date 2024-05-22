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
