-- View to identify the number of sessions of each user

CREATE VIEW QuackstagramDB.user_session AS
SELECT user_id, COUNT(session_id) as session_count
FROM QuackstagramDB.sessions s 
GROUP BY user_id
