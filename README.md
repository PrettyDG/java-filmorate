# java-filmorate

Template repository for Filmorate project.

![photo_2025-01-06_20-53-18](https://github.com/user-attachments/assets/871f6ce8-3e5f-4dde-a350-533e5cef97d9)

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
