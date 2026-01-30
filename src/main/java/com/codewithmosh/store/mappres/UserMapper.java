package com.codewithmosh.store.mappres;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.dtos.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
}
