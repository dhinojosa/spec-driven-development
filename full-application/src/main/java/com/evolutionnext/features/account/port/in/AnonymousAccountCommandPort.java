package com.evolutionnext.features.account.port.in;

import com.evolutionnext.features.account.application.command.AccountCommand;
import com.evolutionnext.features.account.application.command.AccountResult;

public interface AnonymousAccountCommandPort {
    AccountResult execute(AccountCommand command);
}
