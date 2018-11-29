package bank.web;

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
@Controller
public class DoTransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/doTransaction", method = RequestMethod.GET)
    public String tr(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        model.addAttribute("name", user.getFirstName() + " " + user.getLastname());
        return "doTransaction";
    }
    @RequestMapping(value="/doTransaction", method=RequestMethod.POST)
    public String doTransaction(@RequestParam(value="action", required=true) String action,
                                @RequestParam(value="fromAccount", required=true) String fromAcc,
                                @RequestParam(value="toAccount", required=true) String toAcc,
                                @RequestParam(value="amount", required=true) String amount,
                                Model model) {

        if (action.equals("transactionHistory")) {

            return "redirect:transactionHistory";
        }
        if (action.equals("userPage")) {
            return "redirect:userpage";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        double amountDouble = Double.parseDouble(amount);
        DoTransactionResponse response = transactionService.doTransaction(new DoTransactionRequest(fromAcc, toAcc, amountDouble, user.getId()));
        if(response.isSuccess()){
            model.addAttribute("transactionID", response.getTransactionId());
        }
        return "doTransaction";
    }

}
