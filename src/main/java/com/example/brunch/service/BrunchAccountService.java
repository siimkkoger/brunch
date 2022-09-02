package com.example.brunch.service;

import com.example.brunch.dbmodel.AccountEntity;
import com.example.brunch.dbmodel.RoleEntity;
import com.example.brunch.form.RegisterForm;
import com.example.brunch.repository.BrunchAccountRepository;
import com.example.brunch.repository.RoleRepository;
import com.example.brunch.security.roles.BrunchRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BrunchAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrunchAccountService.class);

    private final BrunchAccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public BrunchAccountService(BrunchAccountRepository accountRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public AccountEntity create(final RegisterForm registerForm) {
        if (accountRepository.findByUsername(registerForm.username()) != null) {
            LOGGER.debug("Account with the given username already exists: {}", registerForm.username());
        }
        if (accountRepository.findByEmail(registerForm.email()) != null) {
            LOGGER.debug("Account with the given email already exists: {}", registerForm.email());
        }
        try {
            AccountEntity accountEntity = registerFormToAccount(registerForm);
            RoleEntity roleEntity = roleRepository.getByCode(BrunchRole.ROLE_USER);
            accountEntity.addRole(roleEntity);
            return accountRepository.save(accountEntity);
        } catch (Exception e) {
            LOGGER.debug("Failed to map register form to database entity: {}", registerForm.username());
            return null;
        }
    }

    private AccountEntity registerFormToAccount(final RegisterForm registerForm) {
        final AccountEntity account = new AccountEntity();
        account.setEnabled(true);
        account.setUsername(registerForm.username());
        account.setEmail(registerForm.email());
        account.setPassword(bCryptPasswordEncoder.encode(registerForm.password()));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        return account;
    }
}
