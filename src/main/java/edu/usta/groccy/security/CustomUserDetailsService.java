package edu.usta.groccy.security;

import edu.usta.groccy.entity.User;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo"));

        return new CustomUserDetails(user);
    }
}