package it.uniroma3.siwcdf.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;






import static it.uniroma3.siwcdf.spring.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siwcdf.spring.model.Credentials.DEFAULT_ROLE;
import static it.uniroma3.siwcdf.spring.model.Credentials.NOTAPPROVED_ROLE;


@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    DataSource datasource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // authorization paragraph: qui definiamo chi può accedere a cosa
                .authorizeRequests()
                	// chiunque puo registrarsi o tentare un login
	                .antMatchers(HttpMethod.GET, "/login", "/register", "/needsapproval", "/login-error").permitAll()
	                .antMatchers(HttpMethod.POST, "/login", "/register", "/needsapproval").permitAll()
	                // chiunque puo visualizzare file js css e immagini in ogni folder
	                .antMatchers("/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpg").permitAll()
	                // permessi di debug
	                .antMatchers(HttpMethod.GET, "/index").hasAnyAuthority(ADMIN_ROLE, DEFAULT_ROLE, NOTAPPROVED_ROLE)
	                //.antMatchers("/", "/index", "/admin/managestudents").permitAll()
	                // gli utenti registrati, loggati, ma non approvati, non possono visualizzare nulla fuorche la pagina informativa
	                //.antMatchers("/index").hasAnyAuthority(NOTAPPROVED_ROLE)
	                // solo gli utenti autenticati con ruolo ADMIN possono accedere a risorse con path /admin/**
	                .antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
	                .antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
	                .antMatchers(HttpMethod.POST, "/admin/confirm").hasAnyAuthority(ADMIN_ROLE)
	                // tutti gli utenti autenticati possono accedere alle pagine rimanenti 
	                .anyRequest().authenticated()

                // login paragraph: qui definiamo come è gestita l'autenticazione
                .and().formLogin()
	                .loginPage("/login")
	                // se il login ha successo, si viene rediretti alla index
	                .defaultSuccessUrl("/index")
	                .failureUrl("/login-error")

                .and().logout()
                	.logoutUrl("/logout")
                	.deleteCookies("JSESSIONID")
	                // in caso di successo, l'utente perde tutti i poteri di visibilita, deve rifare il login
	                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                .logoutSuccessUrl("/login")        
	                .invalidateHttpSession(true)
	                .clearAuthentication(true).permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                //use the autowired datasource to access the saved credentials
                .dataSource(this.datasource)
                //retrieve username and role
                .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
                //retrieve username, password and a boolean flag specifying whether the user is enabled or not (always enabled in our case)
                .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}