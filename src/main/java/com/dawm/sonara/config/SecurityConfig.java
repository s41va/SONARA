package com.dawm.sonara.config;

import com.dawm.sonara.handler.OAuth2LoginSuccessHandler;
import com.dawm.sonara.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Inyectamos la URL de tu frontend de producción desde el .env
    @Value("${CORS_ALLOWED_ORIGINS}")
    private String corsAllowedOrigins;

    @Order(1)
    @Bean
    public SecurityFilterChain swaggerChair(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/login", "/logout")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.logoutUrl("/logout"))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de seguridad
        http
                .cors(withDefaults()) // Busca automáticamente el bean corsConfigurationSource() de abajo
                // 1) API REST: Desactivamos CSRF
                .csrf(csrf -> csrf.disable())

                // 2) IMPORTANTE PARA OAUTH2: Sesión temporal para el login de Google
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // 3) Desactivamos formLogin tradicional en la API
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 4) Manejo de errores API: 401 / 403
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Forbidden\"}");
                        })
                )
                // 5) Autorización por rutas
                .authorizeHttpRequests(auth -> auth
                        // Permitir las rutas nativas de OAuth2/Google
                        .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/**").permitAll()

                        // Endpoints públicos
                        .requestMatchers("/api/usuarios").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/api/localidad/**").permitAll()

                        // Rutas restringidas por roles
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/profile/perfil").hasAnyRole("ADMIN", "MANAGER", "USER")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/manager/**").hasRole("MANAGER")

                        // Lo demás requiere token válido
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        // Ejecuta tu handler dinámico al iniciar sesión con éxito
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para configurar CORS usando las variables de tu .env de forma segura
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Añade el dominio del frontend (ej: https://acassar.alixarblue.team)
        configuration.setAllowedOrigins(List.of(corsAllowedOrigins));

        // Métodos HTTP permitidos para la API
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Cabeceras permitidas
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));

        // Permitir el envío de credenciales (Authorization headers, etc.)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Entrando en el método passwordEncoder");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Saliendo del método passwordEncoder");
        return encoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}