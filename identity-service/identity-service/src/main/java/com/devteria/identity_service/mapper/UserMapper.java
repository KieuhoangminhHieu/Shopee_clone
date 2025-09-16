package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.request.UserUpdateRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")

public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserRessponse(User user);

    void updateUser(UserUpdateRequest request, @MappingTarget User user);
}
