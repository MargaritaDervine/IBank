package bank.services;

import bank.database.UserRepository;
import bank.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class UserService {
    @Autowired
    UserRepository repository;

    public User getUser(String username) {
        return repository.findByUsername(username);
    }

    public User getUser(Long id) {
        return repository.getOne(id);
    }

}
