package org.booking.user_service.service;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import lombok.RequiredArgsConstructor;
import org.booking.user_service.exception.UserAlreadyTakenException;
import org.booking.user_service.model.User;
import org.booking.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void createUser(User user) {
        boolean emailAlreadyExists = repository.existsByEmail(user.getEmail());
        if (emailAlreadyExists)
            throw new UserAlreadyTakenException();

        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);
        Hash hash = Password.hash(user.getPassword())
                .addPepper("shared-secret")
                .with(bcrypt);
        user.setPassword(hash.getResult());
        repository.save(user);
    }

}
