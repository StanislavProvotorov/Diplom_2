package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String email;
    private String password;
    private String name;

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}