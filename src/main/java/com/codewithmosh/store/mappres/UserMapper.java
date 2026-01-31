package com.codewithmosh.store.mappres;
import com.codewithmosh.store.dtos.ChangePasswordRequest;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
    User toEntity(RegisterUserRequest request);
    User updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);
    @Mapping(target = "password", source = "newPassword")
    User updateUserPassword(ChangePasswordRequest request, @MappingTarget User user);
}
