package com.example.brunch.controller;

import com.example.brunch.dbmodel.AccountEntity;
import com.example.brunch.form.RegisterForm;
import com.example.brunch.security.exceptions.BrunchException;
import com.example.brunch.service.BrunchAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    private final BrunchAccountService accountService;

    @Autowired
    public RegisterController(BrunchAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/register")
    boolean register(@RequestBody @NotNull final RegisterForm registerForm) {
        final AccountEntity account = accountService.create(registerForm);
        if (account != null) {
            LOGGER.info("Successfully created a new account: {}", account.getUsername());
            return true;
        }
        return false;
    }

    @PostMapping(value = "/test/brunchexception")
    boolean testBrunchError() {
        throw new BrunchException(BrunchException.Type.ENTITY_NOT_FOUND, "Works!");
    }

    @PostMapping(value = "/test/exception")
    boolean testGeneralError() {
        throw new RuntimeException("General runtime exception.");
    }

}
