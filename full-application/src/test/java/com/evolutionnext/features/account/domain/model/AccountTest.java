package com.evolutionnext.features.account.domain.model;

import com.evolutionnext.features.account.domain.events.AccountLoggedIn;
import com.evolutionnext.features.account.domain.events.AccountRegistered;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {
    @Test
    void registersAccountWithDomainEvent() {
        var account = Account.register(AccountId.newId(),
            new UserName("casey"),
            new PasswordCredential("correct-horse-battery-staple"));

        assertThat(account.userName().value()).isEqualTo("casey");
        assertThat(account.accountEvents()).hasAtLeastOneElementOfType(AccountRegistered.class);
    }

    @Test
    void rejectsShortPassword() {
        assertThatThrownBy(() -> new PasswordCredential("short"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password must be at least 8 characters");
    }

    @Test
    void recordsSuccessfulLoginEvent() {
        var account = Account.register(AccountId.newId(),
            new UserName("casey"),
            new PasswordCredential("correct-horse-battery-staple"));

        assertThat(account.authenticate("correct-horse-battery-staple")).isTrue();
        assertThat(account.accountEvents()).hasAtLeastOneElementOfType(AccountLoggedIn.class);
    }
}
