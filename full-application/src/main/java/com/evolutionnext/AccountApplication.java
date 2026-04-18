package com.evolutionnext;

import com.evolutionnext.features.account.application.service.AccountApplicationService;
import com.evolutionnext.features.account.infrastructure.adapter.in.AccountHttpHandler;
import com.evolutionnext.features.account.port.out.AccountRepository;
import com.evolutionnext.http.HealthHandler;
import com.evolutionnext.http.ResourceLoader;
import com.evolutionnext.http.StaticAssetHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class AccountApplication {
    public HttpServer start(int port, AccountRepository accountRepository) {
        try {
            var server = HttpServer.create(new InetSocketAddress(port), 0);
            var accountService = new AccountApplicationService(accountRepository);
            var resourceLoader = new ResourceLoader();
            server.createContext("/health", new HealthHandler());
            server.createContext("/assets", new StaticAssetHandler(resourceLoader));
            server.createContext("/account", new AccountHttpHandler(accountService, accountService, resourceLoader));
            server.start();
            return server;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to start application", exception);
        }
    }
}
