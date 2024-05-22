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