package upm.etsit.monolito_backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors()
            .and()
            .csrf().disable()
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/login",
                        "/api/v1/users/register",
                       // "/api/v1/users/register_admin",
                        "/logout",
                        "/api/v1/products/page",
                        "/api/v1/productcategories"//getCategoriesAll

                    ).permitAll()
                    ///users/general_user.jpg PERMISOS ADMIN
                    .anyRequest().authenticated()
            }
            .formLogin { login ->
                login
                    .loginProcessingUrl("/login")
                    .successHandler { _, response, _ ->
                        response.status = 200
                    }
                    .failureHandler { _, response, _ ->
                        response.status = 401
                        response.contentType = "application/json"
                        response.writer.write("{\"error\": \"Credenciales inválidas\"}")
                    }
            }
            .logout { logout ->
                logout
                    .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                    .logoutSuccessHandler { _, response, _ -> response.status = 200 }
            }
            .exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.status = 401
                    response.contentType = "application/json"
                    response.writer.write("{\"error\": \"No autorizado\"}")
                }
            }

        return http.build()
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}


@Configuration
class CorsGlobalConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf("http://localhost:4200", "http://157.90.27.9:4200") 
            allowedHeaders = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return CorsFilter(source)
    }
}