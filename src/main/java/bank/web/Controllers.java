package bank.web;

import bank.domain.Account;
import bank.domain.Transaction;
import bank.domain.User;
import bank.services.AccountServiceImpl;
import bank.services.TransactionService;
import bank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class Controllers {

    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;

    @GetMapping("/userpage")
    public String accounts(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        List <Account> accounts = accountService.getAvailabeAccounts(user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("name", user.getFirstName() + " " + user.getLastname());
        return "userPage";
    }

    @GetMapping("/transactionHistory")
    public String transactionHistory(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        String acct = accountService.getAvailabeAccounts(user).get(0).getNumber();
        List <Transaction> transactions = transactionService.getTransactionsByAccount(acct);
        model.addAttribute("transactions", transactions);
        return "transactionHistory";
    }

    @GetMapping("/doTransaction")
    public String doTransaction(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        List <Account> accounts = accountService.getAvailabeAccounts(user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("name", user.getFirstName() + " " + user.getLastname());
        return "doTransaction";
    }
}
