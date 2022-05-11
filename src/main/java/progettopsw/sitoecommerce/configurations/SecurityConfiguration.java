package progettopsw.sitoecommerce.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@EnableGlobalMethodSecurty(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigureAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, ..antPatterns: "/**").permitAll()
                .antMatchers( ..antPatterns: "/check/simple").permitAll()
                .antMatchers( ..antPatterns: "/users/**").permitAll()
                .antMatchers( ..antPatterns: "/products/**").permitAll()
                .antMatchers( ..antPatterns: "/purchases/**").permitAll()
                .anyRequests().authenticate().and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(new JwtAuthenticationConverter());
    }//configure

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration() configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        source.registerCorsConfiguration(path: "/**",configuration);
        return new CorsFilter(source);
    }

}//SecurityConfiguration
