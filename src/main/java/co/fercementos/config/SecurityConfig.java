package co.fercementos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de Spring Security.
 *
 * Reemplaza al filtro manual (AuthFilter) de la version con Servlets:
 * el framework se encarga del formulario de login, la validacion de
 * credenciales (delegando en UsuarioService), la sesion y el logout.
 *
 * Reglas:
 *  - /login, /registro y los recursos estaticos son publicos.
 *  - Todo lo demas requiere sesion iniciada.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/registro", "/css/**", "/img/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")            // pagina propia (prototipo Balsamiq)
                .defaultSuccessUrl("/", true)   // al ingresar, ir al menu principal
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?salir=true")
                .permitAll()
            );

        return http.build();
    }

    /** Codificador de contrasenas: BCrypt (hash con salt por usuario). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
