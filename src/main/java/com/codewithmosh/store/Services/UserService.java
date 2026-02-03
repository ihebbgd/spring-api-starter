package com.codewithmosh.store.Services;

import com.codewithmosh.store.dtos.ChangePasswordRequest;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.exceptions.EmailAlreadyExists;
import com.codewithmosh.store.exceptions.FalsePassword;
import com.codewithmosh.store.exceptions.UserNotFoundException;
import com.codewithmosh.store.mappres.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public Iterable<UserDto> getAllUsers(String sortBy){
        Set<String> allowedSorts = Set.of("id","name", "email");
        if(!allowedSorts.contains(sortBy)){
            sortBy = "name";
        }
        return userRepository.findAll(Sort.by(sortBy).descending()).stream().map(userMapper::userToUserDto).toList();
    }
    public UserDto getUserById(Long id){
        var user = userRepository.findById(id).orElse(null);
        if(user==null){
            throw new UserNotFoundException();
        }
        return userMapper.userToUserDto(user);
    }


    public UserDto createUser(RegisterUserRequest request){
        var user = userMapper.toEntity(request);
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExists();
        }
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request){
        var user = userRepository.findById(id).orElse(null);
        if (user==null){
            throw new UserNotFoundException();
        }
        userMapper.updateUserFromRequest(request, user);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
    public void deleteuser(Long id){
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            throw new UserNotFoundException();
        }
        userRepository.delete(user);
    }
    public void changePassword(Long id, ChangePasswordRequest request){
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            throw new UserNotFoundException();
        }
        if(!user.getPassword().equals(request.getOldPassword())){
            throw new FalsePassword();
        }
        userMapper.updateUserPassword(request, user);
        userRepository.save(user);
    }





}
