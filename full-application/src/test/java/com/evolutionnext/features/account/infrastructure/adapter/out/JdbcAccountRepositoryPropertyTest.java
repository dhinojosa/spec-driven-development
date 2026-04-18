package com.evolutionnext.features.account.infrastructure.adapter.out;

import com.evolutionnext.features.account.arbitrary.AccountArbitrary;
import com.evolutionnext.features.account.domain.model.Account;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.testcontainers.Container;
import net.jqwik.testcontainers.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
class JdbcAccountRepositoryPropertyTest {
    @Container(restartPerTry = false)
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15.2");

    @Property(tries = 3)
    void everySavedAccountCanBeFoundById(@ForAll("accounts") Account account) {
        var repository = new JdbcAccountRepository(POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword());

        repository.save(account);

        assertThat(repository.findById(account.accountId()))
            .map(Account::userName)
            .contains(account.userName());
    }

    @Provide
    net.jqwik.api.Arbitrary<Account> accounts() {
        return AccountArbitrary.accounts();
    }
}
