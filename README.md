# java-filmorate

Template repository for Filmorate project.

![2024-12-14_13-51-45](https://github.com/user-attachments/assets/7a0fdcef-7d41-45f5-a435-ffbacb959a15)

# Примеры запросов

Получение всех фильмов:  
SELECT *
FROM film

Получение ТОП-100 наиболее популярных фильмов:  
SELECT f.film_id  
f.name,  
COUNT(fl_userID) AS like_count  
FROM film AS f   
LEFT JOIN filmLikedByUsers AS fl ON f.film_id = fl.film_id  
GROUP BY f.film_id, f.name  
ORDER BY like_count DESC  
LIMIT 100;  
