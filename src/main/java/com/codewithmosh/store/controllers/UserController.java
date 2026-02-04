package com.codewithmosh.store.controllers;


import com.codewithmosh.store.Services.UserService;
import com.codewithmosh.store.dtos.ChangePasswordRequest;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;




@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false,defaultValue = "",name= "sorts") String sortBy){
        return userService.getAllUsers(sortBy);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder builder) {
        var user = userService.createUser(request);
        var location = builder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(user);
    }

@PutMapping("/{id}")
public ResponseEntity<UserDto> updateUser(@PathVariable(name="id") Long id, @RequestBody UpdateUserRequest request){
        var user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable(name="id") Long id){
        userService.deleteuser(id);
        return ResponseEntity.noContent().build();

}

@PostMapping("/{id}/change-password")
public ResponseEntity<Void> changePassword(@PathVariable(name="id") Long id, @RequestBody ChangePasswordRequest request){
    userService.changePassword(id, request);
    return ResponseEntity.noContent().build();
    }


}
