package com.example.e_commerce.technology.model.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchRequest {
    String username;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
}
