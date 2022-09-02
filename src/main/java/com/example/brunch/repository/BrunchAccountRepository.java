package com.example.brunch.repository;

import com.example.brunch.dbmodel.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrunchAccountRepository extends CrudRepository<AccountEntity, Integer> {

    AccountEntity findByUsername(String username);
    AccountEntity findByEmail(String email);

    <T> T findByUsername(String username, Class<T> type);
}
