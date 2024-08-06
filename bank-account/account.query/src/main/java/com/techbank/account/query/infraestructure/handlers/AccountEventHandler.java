package com.techbank.account.query.infraestructure.handlers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountEventHandler implements EventHandler {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        BankAccount account = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();

        accountRepository.save(account);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        Optional<BankAccount> optionalBankAccount  = accountRepository.findById(event.getId());
        if (optionalBankAccount.isEmpty()) {
            return;
        }
        BankAccount account = optionalBankAccount.get();
        double currentBalance = account.getBalance();
        double latestBalance = currentBalance + event.getAmount();
        account.setBalance(latestBalance);
        accountRepository.save(account);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        Optional<BankAccount> optionalBankAccount  = accountRepository.findById(event.getId());
        if (optionalBankAccount.isEmpty()) {
            return;
        }
        BankAccount account = optionalBankAccount.get();
        double currentBalance = account.getBalance();
        double latestBalance = currentBalance - event.getAmount();
        account.setBalance(latestBalance);
        accountRepository.save(account);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }
}
