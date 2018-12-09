package bank.web;

import bank.domain.Account;
import bank.domain.User;
import bank.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class DoTransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;
    @Autowired
    AccountServiceImpl accountService;

    @RequestMapping(value = "/doTransaction", method = RequestMethod.GET)
    public String tr(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        List<Account> accounts = accountService.getAvailabeAccounts(user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("name", user.getFirstName() + " " + user.getLastname());
        return "doTransaction";
    }

    @RequestMapping(value = "/doTransaction", method = RequestMethod.POST)
    public String doTransaction(@RequestParam(value = "action", required = true) String action,
                                @RequestParam(value = "fromAccount", required = false) String fromAcc,
                                @RequestParam(value = "toAccount", required = false) String toAcc,
                                @RequestParam(value = "amount", required = false) String amount,
                                Model model) {


        if (action.equals("again")) {
            return "redirect:doTransaction";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        List errors;
        if (toAcc.isEmpty() || amount.isEmpty()) {
            errors = Arrays.asList("all fields must be filled");
            model.addAttribute("errors", errors);
            return "doTransaction";
        }

        DoTransactionResponse response = transactionService.doTransaction(new DoTransactionRequest(fromAcc, toAcc, amount, user.getId()));
        if (response.isSuccess()) {
            model.addAttribute("success", true);
        } else {
            errors = response.getErrors();
            model.addAttribute("errors", errors);
        }
        return "doTransaction";
    }

}
