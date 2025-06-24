package tally.transfer.account.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tally.transfer.account.application.AccountCommandService;
import tally.transfer.account.presentation.dto.request.AccountRequest;

import java.net.URI;
import java.util.Objects;

@RestController
public class AccountController {

    private final AccountCommandService accountCommandService;

    public AccountController(final AccountCommandService accountCommandService) {
        this.accountCommandService = Objects.requireNonNull(accountCommandService);
    }

    @PostMapping("/v1/accounts")
    public ResponseEntity<Void> createAccount(
            @RequestBody AccountRequest.CreateAccountRequest request
    ) {
        final Long accountId = accountCommandService.createAccount(
                request.getUserId(),
                request.getAccountType()
        );
        return ResponseEntity.created(URI.create("/v1/accounts/" + accountId)).build();
    }
}
