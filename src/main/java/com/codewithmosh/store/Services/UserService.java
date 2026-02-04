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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;



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
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new FalsePassword();
        }
        userMapper.updateUserPassword(request, user);
        userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String email)  {

        var user= userRepository.findByEmail(email);
        if(user==null){
            throw new UserNotFoundException();
        }
        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }





}
