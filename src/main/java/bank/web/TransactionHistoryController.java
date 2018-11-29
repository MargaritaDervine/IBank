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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TransactionHistoryController {

    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;


    @RequestMapping(value ="/transactionHistory", method=RequestMethod.GET)
    public String transactionHistory(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        Account acct = accountService.getAvailabeAccounts(user).get(0);
        List <Transaction> transactions = transactionService.getTransactionsByAccount(acct);
        model.addAttribute("transactions", transactions);
        return "transactionHistory";
    }

    @RequestMapping(value="/transactionHistory", method=RequestMethod.POST)
    public String locateFromTransactionWindow(@RequestParam(value="action", required=true) String action) {

        if (action.equals("doTransaction")) {
            return "redirect:doTransaction";
        }

        if (action.equals("userPage")) {
            return "redirect:userpage";
        }
        return "transactionHistory";
    }

}
