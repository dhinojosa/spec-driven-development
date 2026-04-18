package com.evolutionnext.features.account.domain.model;

import com.evolutionnext.features.account.arbitrary.AccountArbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.NotBlank;

import static org.assertj.core.api.Assertions.assertThat;

class AccountPropertyTest {
    @Property
    void authenticatesOnlyMatchingPassword(@ForAll("passwords") String password,
                                           @ForAll @NotBlank String wrongPassword) {
        var account = Account.register(AccountId.newId(), new UserName("casey"), new PasswordCredential(password));

        assertThat(account.authenticate(password)).isTrue();
        if (!password.equals(wrongPassword)) {
            assertThat(account.authenticate(wrongPassword)).isFalse();
        }
    }

    @net.jqwik.api.Provide
    net.jqwik.api.Arbitrary<String> passwords() {
        return AccountArbitrary.passwords();
    }
}
