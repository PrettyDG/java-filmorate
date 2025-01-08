package ru.yandex.practicum.filmorate.storage.user.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .build();


        if (rs.getString("name") != null) {
            user.setName(rs.getString("name"));
        } else {
            user.setName(user.getLogin());
        }


        return user;
    }
}
