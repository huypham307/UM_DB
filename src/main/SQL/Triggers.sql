-- This file includes 2 triggers, 2 stored procedures and 1 function

-- Trigger 1: prevent_self_like
DELIMITER //

CREATE TRIGGER prevent_self_like
    BEFORE INSERT ON QuackstagramDB.posts
    FOR EACH ROW
BEGIN
    IF check_self_like(NEW.user_id, NEW.image_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A user cannot like their own post';
END IF;
END //

DELIMITER ;


-- Trigger 2: before_image_delete
DELIMITER //

CREATE TRIGGER before_image_delete
    BEFORE DELETE ON image_data
    FOR EACH ROW
BEGIN
    CALL moveToOldImages(
            OLD.image_id,
            OLD.user_id,
            OLD.image_bio,
            OLD.post_time,
            OLD.file_path
        );
END //

    DELIMITER ;


-- Function 1: check_self_like for trigger prevent_self_like
DELIMITER //

CREATE FUNCTION check_self_like(user_id INT, image_id VARCHAR(32))
    RETURNS BOOLEAN
BEGIN
    DECLARE owner_id INT;

SELECT id.user_id INTO owner_id
FROM QuackstagramDB.image_data id
WHERE id.image_id = image_id;

RETURN user_id = owner_id;
END //

DELIMITER ;


-- Stored procedure 2: moveToOldImages for trigger before_image_delete
DELIMITER //

CREATE PROCEDURE moveToOldImages(
    IN old_image_id VARCHAR(32),
    IN old_user_id INT,
    IN old_image_bio TEXT,
    IN old_post_time DATETIME,
    IN old_file_path VARCHAR(128)
)
BEGIN
INSERT INTO old_images (image_id, user_id, image_bio, post_time, file_path)
VALUES (old_image_id, old_user_id, old_image_bio, old_post_time, old_file_path);
END //

    DELIMITER ;


-- Extra stored procedure generateUserReport
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