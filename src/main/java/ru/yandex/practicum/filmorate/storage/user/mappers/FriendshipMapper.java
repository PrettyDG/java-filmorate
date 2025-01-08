package ru.yandex.practicum.filmorate.storage.user.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class FriendshipMapper implements RowMapper<FriendshipStatus> {

    @Override
    public FriendshipStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setStatusId(rs.getLong("friendshipStatusID"));
        friendshipStatus.setStatus(rs.getString("friendshipStatus"));

        return friendshipStatus;
    }
}
