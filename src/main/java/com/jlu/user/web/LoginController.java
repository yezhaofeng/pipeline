package com.jlu.user.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.jlu.common.web.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jlu.common.permission.annotations.PermissionPass;
import com.jlu.common.permission.exception.ForbiddenException;
import com.jlu.common.web.ResponseBean;
import com.jlu.github.service.IGithubDataService;
import com.jlu.user.bean.UserBean;
import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IGithubOAuthService;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by langshiquan on 18/1/28.
 */
@ApiIgnore
@PermissionPass
@Controller
@RequestMapping("/github")
public class LoginController extends AbstractController{

    @Autowired
    private IGithubDataService githubDataService;

    @Autowired
    private IGithubOAuthService githubOAuthService;

    // 重定向到github的授权页面
    @RequestMapping(value = "/authorization", method = RequestMethod.GET)
    public ModelAndView authorization() {
        return new ModelAndView(new RedirectView(githubOAuthService.getAuthorizationUrl()));
    }

    // github回调pipeline
    @RequestMapping(value = "/login/callback", method = RequestMethod.GET)
    public String callback(Model model, HttpServletRequest request, String code, String state) {
        // avoid CSRF
        if (!githubOAuthService.checkState(state)) {
            throw new ForbiddenException("鉴权失败");
        }
        HttpSession session = request.getSession();
        Boolean isExits = githubOAuthService.handleCallback(code, model, session);
        if (isExits) {
            return "redirect:/";
        } else {
            return "register";
        }
    }

    // 根据用户注册信息初始化用户
    @RequestMapping(value = "/initUser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean initUser(@Valid UserBean userBean, BindingResult result,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        checkBindingResult(result);
        if (!githubOAuthService.checkRegisterToken(userBean.getRegisterToken())) {
            throw new ForbiddenException("鉴权失败");
        }

        GithubUser githubUser = githubDataService.initUser(userBean);
        request.getSession().setAttribute(GithubUser.CURRENT_USER_NAME, githubUser);
        response.sendRedirect("/");
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "/exit", method = RequestMethod.GET)
    public String exitLogin(HttpServletResponse response, HttpServletRequest request) {
        request.getSession().removeAttribute(GithubUser.CURRENT_USER_NAME);
        return "login";
    }

}
