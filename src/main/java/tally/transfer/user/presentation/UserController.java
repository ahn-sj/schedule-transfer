package tally.transfer.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tally.transfer.user.application.UserCommandService;
import tally.transfer.user.presentation.dto.request.UserRequest;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/v1/users")
    public ResponseEntity<Void> createUser(
            @RequestBody UserRequest.CreateUserRequest request
    ) {
        final Long created = userCommandService.createUser(request.getName());

        return ResponseEntity.created(URI.create("/v1/users/" + created)).build();
    }
}
