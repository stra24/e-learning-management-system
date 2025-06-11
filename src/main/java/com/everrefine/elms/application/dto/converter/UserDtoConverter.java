package com.everrefine.elms.application.dto.converter;

import com.everrefine.elms.application.dto.UserDto;
import com.everrefine.elms.domain.model.user.User;

public class UserDtoConverter {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmailAddress().getValue(),
                user.getRealName().getValue(),
                user.getUserName().getValue(),
                (user.getThumbnailUrl() != null)
                        ? user.getThumbnailUrl().getValue()
                        : null,
                user.getUserRole().getRoleName(),
                user.getCreatedAt()
        );
    }
}
