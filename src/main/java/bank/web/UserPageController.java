package bank.web;

import bank.domain.Account;
import bank.domain.User;
import bank.services.AccountServiceImpl;
import bank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserPageController {
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    UserService userService;

    @RequestMapping(value ="/userpage", method = RequestMethod.GET)
    public String accounts(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        List<Account> accounts = accountService.getAvailabeAccounts(user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("name", user.getFirstName() + " " + user.getLastname());
        return "userPage";
    }
}
