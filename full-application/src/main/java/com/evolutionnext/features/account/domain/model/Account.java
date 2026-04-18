package com.evolutionnext.features.account.domain.model;

import com.evolutionnext.features.account.domain.events.AccountEvent;
import com.evolutionnext.features.account.domain.events.AccountLoggedIn;
import com.evolutionnext.features.account.domain.events.AccountRegistered;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Account {
    private final AccountId accountId;
    private final UserName userName;
    private final PasswordCredential passwordCredential;
    private final List<AccountEvent> accountEvents;

    private Account(AccountId accountId,
                    UserName userName,
                    PasswordCredential passwordCredential,
                    List<AccountEvent> accountEvents) {
        this.accountId = Objects.requireNonNull(accountId, "accountId");
        this.userName = Objects.requireNonNull(userName, "userName");
        this.passwordCredential = Objects.requireNonNull(passwordCredential, "passwordCredential");
        this.accountEvents = new ArrayList<>(accountEvents);
    }

    public static Account register(AccountId accountId, UserName userName, PasswordCredential passwordCredential) {
        var events = new ArrayList<AccountEvent>();
        events.add(new AccountRegistered(accountId, userName, Instant.now()));
        return new Account(accountId, userName, passwordCredential, events);
    }

    public static Account restore(AccountId accountId, UserName userName, PasswordCredential passwordCredential) {
        return new Account(accountId, userName, passwordCredential, List.of());
    }

    public boolean authenticate(String password) {
        var authenticated = passwordCredential.matches(password);
        if (authenticated) {
            accountEvents.add(new AccountLoggedIn(accountId, userName, Instant.now()));
        }
        return authenticated;
    }

    public AccountId accountId() {
        return accountId;
    }

    public UserName userName() {
        return userName;
    }

    public PasswordCredential passwordCredential() {
        return passwordCredential;
    }

    public List<AccountEvent> accountEvents() {
        return List.copyOf(accountEvents);
    }
}
