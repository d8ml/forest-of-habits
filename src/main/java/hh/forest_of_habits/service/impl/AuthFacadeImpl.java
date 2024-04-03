package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.service.AuthFacade;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacadeImpl implements AuthFacade {
    @Override
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
