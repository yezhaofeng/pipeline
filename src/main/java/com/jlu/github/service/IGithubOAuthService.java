package com.jlu.github.service;

/**
 * Created by langshiquan on 18/1/28.
 */
public interface IGithubOAuthService {
    String getAuthorizationUrl();
    Boolean checkState(String state);

    void handleCallback(String code);
}
