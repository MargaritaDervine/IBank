package bank.web;

import bank.AppError;
import bank.domain.Account;
import bank.domain.User;
import bank.domain.ValidationField;
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
    public String transactions(Model model) {
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
        model.addAttribute("name", user.getFirstName() + " " + user.getLastname());
        String error;
        String toAccError;
        String amountError;
        if (toAcc.isEmpty() || amount.isEmpty()) {
            error = "all fields must be filled";
            model.addAttribute("error", error);
            return "doTransaction";
        }

        DoTransactionResponse response = transactionService.doTransaction(new DoTransactionRequest(fromAcc, toAcc,
                amount, user.getId()));
        if (response.isSuccess()) {
            model.addAttribute("success", true);
        } else {
            toAccError = checkError(response, ValidationField.ToAccount);
            model.addAttribute("toAccError", toAccError);

            amountError = checkError(response, ValidationField.Amount);
            model.addAttribute("amountError", amountError);
        }
        return "doTransaction";
    }

    String checkError(DoTransactionResponse response, ValidationField field) {
        for (AppError e : response.getErrors()) {
            if (e.getField().equals(field)) {
                return e.getDescription();
            }
        }
        return null;
    }

}
