package cz.streicher.Project1Boot.controllers;


import cz.streicher.Project1Boot.models.User;
import cz.streicher.Project1Boot.services.UserService;
import cz.streicher.Project1Boot.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/people")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;


    @Autowired
    public UserController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.index());
        return "/users/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable int id, Model model) {
        model.addAttribute("user", userService.get(id));
        model.addAttribute("books", userService.getUserBooks(id));
        return "/users/get";
    }

    @RequestMapping("/new")
    public String add(Model model) {
        model.addAttribute("emptyUser", new User());
        return "/users/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("emptyUser") @Valid User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "/users/create";
        userService.add(user);
        return "redirect:/people";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.get(id));
        return "/users/edit";
    }


    @PatchMapping("/{id}")
    public String change(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {

      //  userValidator.validate(user, bindingResult); Todo


        if (bindingResult.hasErrors())
            return "/users/edit";
        userService.edit(id, user);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/people";
    }
}
