package com.jlu.user.service;

import org.springframework.ui.Model;

/**
 * Created by langshiquan on 18/1/28.
 */
public interface IGithubOAuthService {
    String getAuthorizationUrl();
    Boolean checkState(String state);
    void handleCallback(String code,Model model);
}
