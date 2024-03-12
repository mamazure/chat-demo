package ch.cern.chatdemo.adapters.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final SimpUserRegistry simpUserRegistry;

    @GetMapping(value = "/username", produces = "application/json")
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @GetMapping(value = "/users", produces = "application/json")
    public Set<String> currentUsers(Principal principal) {
        return simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toSet())
                .stream().filter(user -> !user.equals(principal.getName()))
                .collect(Collectors.toSet());
    }

}
