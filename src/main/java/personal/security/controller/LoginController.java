package personal.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("")
    public String login() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/loginCheck")
    public String checkAccount(String id, String pwd) {
        System.out.println(id);
        System.out.println(pwd);
        if(id.equals("asdf") && pwd.equals("1234")) {
            return "success";
        } else {
            return "failed";
        }
    }
}
