package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.request.UserUpdateRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-16T11:03:46+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setUsername( request.getUsername() );
        user.setPassword( request.getPassword() );
        user.setDob( request.getDob() );
        user.setEmail( request.getEmail() );
        user.setPhoneNumber( request.getPhoneNumber() );
        user.setAddress( request.getAddress() );

        return user;
    }

    @Override
    public UserResponse toUserRessponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( user.getId() );
        userResponse.setFirstName( user.getFirstName() );
        userResponse.setLastName( user.getLastName() );
        userResponse.setUsername( user.getUsername() );
        userResponse.setPassword( user.getPassword() );
        userResponse.setDob( user.getDob() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setPhoneNumber( user.getPhoneNumber() );
        userResponse.setAddress( user.getAddress() );

        return userResponse;
    }

    @Override
    public void updateUser(UserUpdateRequest request, User user) {
        if ( request == null ) {
            return;
        }

        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setUsername( request.getUsername() );
        user.setPassword( request.getPassword() );
        user.setDob( request.getDob() );
        user.setEmail( request.getEmail() );
        user.setPhoneNumber( request.getPhoneNumber() );
        user.setAddress( request.getAddress() );
    }
}
