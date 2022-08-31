package com.example.brunch.repository;

import com.example.brunch.dbmodel.BrunchAccount;
import com.example.brunch.projections.BrunchAccountProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrunchAccountRepository extends CrudRepository<BrunchAccount, Integer> {

    BrunchAccount findByUsername(String email);

    BrunchAccountProjection findProjectionByUsername(String email);
}
