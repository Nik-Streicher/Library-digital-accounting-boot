package cz.streicher.Project1Boot.util;

import cz.streicher.Project1Boot.models.User;
import cz.streicher.Project1Boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private  final UserService userService;


    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (userService.getUser(user.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "This name is already taken");
    }
}
