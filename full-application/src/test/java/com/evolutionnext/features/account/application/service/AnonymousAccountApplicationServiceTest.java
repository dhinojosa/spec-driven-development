package com.evolutionnext.features.account.application.service;

import com.evolutionnext.features.account.application.command.AccountCommand;
import com.evolutionnext.features.account.application.command.AccountResult;
import com.evolutionnext.features.account.application.query.AccountQueryResult;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

class AnonymousAccountApplicationServiceTest {
    @Property
    void registeredAccountCanBeFoundThroughQueryPort(@ForAll("userNames") String userName,
                                                     @ForAll("passwords") String password) {
        var repository = new InMemoryAccountRepository();
        var commandService = new AnonymousAccountCommandApplicationService(repository);
        var queryService = new AnonymousAccountQueryApplicationService(repository);

        var result = commandService.execute(new AccountCommand.RegisterAccount(AccountId.newId(), userName, password));

        assertThat(result).isInstanceOf(AccountResult.AccountRegistered.class);
        assertThat(queryService.findByUserName(userName)).isInstanceOf(AccountQueryResult.AccountFound.class);
    }

    @Property
    void loginCommandIsObservableThroughResultAndQueryPort(@ForAll("userNames") String userName,
                                                           @ForAll("passwords") String password) {
        var repository = new InMemoryAccountRepository();
        var commandService = new AnonymousAccountCommandApplicationService(repository);
        var queryService = new AnonymousAccountQueryApplicationService(repository);

        commandService.execute(new AccountCommand.RegisterAccount(AccountId.newId(), userName, password));

        assertThat(commandService.execute(new AccountCommand.LogIn(userName, password)))
            .isInstanceOf(AccountResult.LogInSucceeded.class);
        assertThat(queryService.findByUserName(userName)).isInstanceOf(AccountQueryResult.AccountFound.class);
    }

    @net.jqwik.api.Provide
    net.jqwik.api.Arbitrary<String> userNames() {
        return com.evolutionnext.features.account.arbitrary.AccountArbitrary.userNames();
    }

    @net.jqwik.api.Provide
    net.jqwik.api.Arbitrary<String> passwords() {
        return com.evolutionnext.features.account.arbitrary.AccountArbitrary.passwords();
    }
}
