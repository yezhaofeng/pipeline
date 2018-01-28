package com.jlu.user.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.user.model.GithubUser;

/**
 * Created by langshiquan on 18/1/28.
 */
@RestController
@RequestMapping("/pipeline/user")
public class UserController {

    @RequestMapping("/current")
    public GithubUser currentUser() {
        // TODO
        return null;
    }
}
