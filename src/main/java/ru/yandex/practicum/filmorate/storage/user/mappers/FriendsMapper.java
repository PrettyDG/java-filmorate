package ru.yandex.practicum.filmorate.storage.user.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FriendsMapper implements RowMapper<Friends> {

    @Override
    public Friends mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friends friends = new Friends();
        friends.setMainUserId(rs.getLong("userID"));
        friends.setFriendId(rs.getLong("friendID"));
        friends.setStatusId(rs.getLong("friendshipStatusID"));

        return friends;
    }
}
