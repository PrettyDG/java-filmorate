package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friends {
    private Long mainUserId;
    private Long friendId;
    private Long statusId;
}
