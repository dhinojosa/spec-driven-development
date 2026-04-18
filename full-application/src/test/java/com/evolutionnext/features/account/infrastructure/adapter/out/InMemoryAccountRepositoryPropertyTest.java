package com.evolutionnext.features.account.infrastructure.adapter.out;

import com.evolutionnext.features.account.arbitrary.AccountArbitrary;
import com.evolutionnext.features.account.domain.model.Account;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryAccountRepositoryPropertyTest {
    @Property
    void everySavedAccountCanBeFoundById(@ForAll("accounts") Account account) {
        var repository = new InMemoryAccountRepository();

        repository.save(account);

        assertThat(repository.findById(account.accountId())).contains(account);
    }

    @Provide
    net.jqwik.api.Arbitrary<Account> accounts() {
        return AccountArbitrary.accounts();
    }
}
