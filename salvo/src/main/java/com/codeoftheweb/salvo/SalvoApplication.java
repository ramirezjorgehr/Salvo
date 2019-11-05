package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Models.*;
import com.codeoftheweb.salvo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ScoreRepository scoreRepository, SalvoRepository salvoRepository, ShipRepository shipRepository, GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository) {
        return (args) -> {


            Player bauer = new Player("j.bauer@ctu.gov");
            Player obrian = new Player("c.obrian@ctu.gov");
            Player almeida = new Player("t.almeida@ctu.gov");
            Player kim = new Player("kim_bauer@gmail.com");

            bauer.setPassword(passwordEncoder().encode("123"));
            obrian.setPassword(passwordEncoder().encode("1234"));
            almeida.setPassword(passwordEncoder().encode("549"));
            kim.setPassword(passwordEncoder().encode("235"));


            playerRepository.save(bauer);
            playerRepository.save(obrian);
            playerRepository.save(almeida);
            playerRepository.save(kim);


//            Game game1 = new Game();
//            Game game2 = new Game();
//            Game game3 = new Game();
//            Game game4 = new Game();
//            Game game5 = new Game();
//            Game game6 = new Game();
//            Game game7 = new Game();
//            Game game8 = new Game();
//
//            gameRepository.save(game1);
//            gameRepository.save(game2);
//            gameRepository.save(game3);
//            gameRepository.save(game4);
//            gameRepository.save(game5);
//            gameRepository.save(game6);
//            gameRepository.save(game7);
//            gameRepository.save(game8);
//
//
//            GamePlayer gamePlayer1 = new GamePlayer(new Date(), game1, bauer);
//            GamePlayer gamePlayer2 = new GamePlayer(new Date(), game1, obrian);
//            GamePlayer gamePlayer3 = new GamePlayer(new Date(), game2, bauer);
//            GamePlayer gamePlayer4 = new GamePlayer(new Date(), game2, obrian);
//            GamePlayer gamePlayer5 = new GamePlayer(new Date(), game3, obrian);
//            GamePlayer gamePlayer6 = new GamePlayer(new Date(), game3, almeida);
//            GamePlayer gamePlayer7 = new GamePlayer(new Date(), game4, obrian);
//            GamePlayer gamePlayer8 = new GamePlayer(new Date(), game4, bauer);
//            GamePlayer gamePlayer9 = new GamePlayer(new Date(), game5, almeida);
//            GamePlayer gamePlayer10 = new GamePlayer(new Date(), game5, bauer);
//            GamePlayer gamePlayer11 = new GamePlayer(new Date(), game6, kim);
//            GamePlayer gamePlayer12 = new GamePlayer(new Date(), game7, almeida);
//            GamePlayer gamePlayer13 = new GamePlayer(new Date(), game8, kim);
//            GamePlayer gamePlayer14 = new GamePlayer(new Date(), game8, almeida);
//
//            gamePlayerRepository.save(gamePlayer1);
//            gamePlayerRepository.save(gamePlayer2);
//            gamePlayerRepository.save(gamePlayer3);
//            gamePlayerRepository.save(gamePlayer4);
//            gamePlayerRepository.save(gamePlayer5);
//            gamePlayerRepository.save(gamePlayer6);
//            gamePlayerRepository.save(gamePlayer7);
//            gamePlayerRepository.save(gamePlayer8);
//            gamePlayerRepository.save(gamePlayer9);
//            gamePlayerRepository.save(gamePlayer10);
//            gamePlayerRepository.save(gamePlayer11);
//            gamePlayerRepository.save(gamePlayer12);
//            gamePlayerRepository.save(gamePlayer13);
//            gamePlayerRepository.save(gamePlayer14);
//
//
//            String sunmarine = "Submarine";
//            String destroyer = "Destroyer";
//            String patrolBoat = "Patrolboat";
//            String carrier = "Carrier";
//            String battleship = "Battleship";
//
//            Ship ship1 = new Ship(destroyer, Arrays.asList("D8", "E8", "F8"), gamePlayer1);
//            Ship ship2 = new Ship(sunmarine, Arrays.asList("F2", "F3", "F4"), gamePlayer1);
//            Ship ship3 = new Ship(patrolBoat, Arrays.asList("B2", "C2"), gamePlayer1);
//            Ship ship27 = new Ship(carrier, Arrays.asList("I4", "I5", "I6", "I7", "I8"), gamePlayer1);
//            Ship ship28 = new Ship(battleship, Arrays.asList("B6", "B7", "B8", "B9"), gamePlayer1);
//
//            Ship ship4 = new Ship(destroyer, Arrays.asList("B5", "C5", "D5"), gamePlayer2);
//            Ship ship5 = new Ship(patrolBoat, Arrays.asList("F1", "F2"), gamePlayer2);
//
//            Ship ship6 = new Ship(destroyer, Arrays.asList("B5", "C5", "C5"), gamePlayer3);
//            Ship ship7 = new Ship(patrolBoat, Arrays.asList("C6", "C7"), gamePlayer3);
//            Ship ship8 = new Ship(sunmarine, Arrays.asList("A2", "A3", "A4"), gamePlayer4);
//            Ship ship9 = new Ship(patrolBoat, Arrays.asList("G6", "H6"), gamePlayer4);
//
//            Ship ship10 = new Ship(destroyer, Arrays.asList("B5", "C5", "C5"), gamePlayer5);
//            Ship ship11 = new Ship(patrolBoat, Arrays.asList("C6", "C7"), gamePlayer5);
//            Ship ship12 = new Ship(sunmarine, Arrays.asList("A2", "A3", "A4"), gamePlayer6);
//            Ship ship13 = new Ship(patrolBoat, Arrays.asList("G6", "H6"), gamePlayer6);
//
//            Ship ship14 = new Ship(destroyer, Arrays.asList("B5", "C5", "C5"), gamePlayer7);
//            Ship ship15 = new Ship(patrolBoat, Arrays.asList("C6", "C7"), gamePlayer7);
//            Ship ship16 = new Ship(sunmarine, Arrays.asList("A2", "A3", "A4"), gamePlayer8);
//            Ship ship17 = new Ship(patrolBoat, Arrays.asList("G6", "H6"), gamePlayer8);
//
//            Ship ship18 = new Ship(patrolBoat, Arrays.asList("C6", "C7"), gamePlayer9);
//            Ship ship19 = new Ship(sunmarine, Arrays.asList("A2", "A3", "A4"), gamePlayer10);
//            Ship ship20 = new Ship(patrolBoat, Arrays.asList("G6", "H6"), gamePlayer10);
//
//            Ship ship21 = new Ship(destroyer, Arrays.asList("B5", "C5", "C5"), gamePlayer11);
//            Ship ship22 = new Ship(patrolBoat, Arrays.asList("C6", "C7"), gamePlayer11);
//
//            Ship ship23 = new Ship(destroyer, Arrays.asList("B5", "C5", "C5"), gamePlayer13);
//            Ship ship24 = new Ship(patrolBoat, Arrays.asList("C6", "C7"), gamePlayer13);
//            Ship ship25 = new Ship(sunmarine, Arrays.asList("A2", "A3", "A4"), gamePlayer14);
//            Ship ship26 = new Ship(patrolBoat, Arrays.asList("G6", "H6"), gamePlayer14);
//
//
//            shipRepository.save(ship1);
//            shipRepository.save(ship2);
//            shipRepository.save(ship3);
//            shipRepository.save(ship4);
//            shipRepository.save(ship5);
//            shipRepository.save(ship6);
//            shipRepository.save(ship7);
//            shipRepository.save(ship8);
//            shipRepository.save(ship9);
//            shipRepository.save(ship10);
//            shipRepository.save(ship11);
//            shipRepository.save(ship12);
//            shipRepository.save(ship13);
//            shipRepository.save(ship14);
//            shipRepository.save(ship15);
//            shipRepository.save(ship16);
//            shipRepository.save(ship17);
//            shipRepository.save(ship18);
//            shipRepository.save(ship19);
//            shipRepository.save(ship20);
//            shipRepository.save(ship21);
//            shipRepository.save(ship22);
//            shipRepository.save(ship23);
//            shipRepository.save(ship24);
//            shipRepository.save(ship25);
//            shipRepository.save(ship26);
//            shipRepository.save(ship27);
//            shipRepository.save(ship28);
//
//
//            Salvo salvo1 = new Salvo(gamePlayer1, Arrays.asList("B5", "C5", "F1"), 1);
//            Salvo salvo2 = new Salvo(gamePlayer2, Arrays.asList("B4", "B5", "B6"), 1);
//            Salvo salvo3 = new Salvo(gamePlayer1, Arrays.asList("F2", "D5"), 2);
//            Salvo salvo4 = new Salvo(gamePlayer2, Arrays.asList("E1", "H3", "A2"), 2);
//
//            Salvo salvo5 = new Salvo(gamePlayer3, Arrays.asList("A2", "A4", "G6"), 1);
//            Salvo salvo6 = new Salvo(gamePlayer4, Arrays.asList("B5", "D5", "C7"), 1);
//            Salvo salvo7 = new Salvo(gamePlayer3, Arrays.asList("A3", "H6"), 2);
//            Salvo salvo8 = new Salvo(gamePlayer4, Arrays.asList("C5", "C6"), 2);
//
//            Salvo salvo9 = new Salvo(gamePlayer5, Arrays.asList("G6", "H6", "A4"), 1);
//            Salvo salvo10 = new Salvo(gamePlayer6, Arrays.asList("H1", "H2", "H3"), 1);
//            Salvo salvo11 = new Salvo(gamePlayer5, Arrays.asList("A2", "A3", "A4"), 2);
//            Salvo salvo12 = new Salvo(gamePlayer6, Arrays.asList("E1", "F2", "G3"), 2);
//
//            Salvo salvo13 = new Salvo(gamePlayer7, Arrays.asList("A3", "A4", "F7"), 1);
//            Salvo salvo14 = new Salvo(gamePlayer8, Arrays.asList("B5", "C6", "H1"), 1);
//            Salvo salvo15 = new Salvo(gamePlayer7, Arrays.asList("A2", "G6", "H6"), 2);
//            Salvo salvo16 = new Salvo(gamePlayer8, Arrays.asList("C5", "C7", "D5"), 2);
//
//            Salvo salvo17 = new Salvo(gamePlayer9, Arrays.asList("A1", "A2", "A3"), 1);
//            Salvo salvo18 = new Salvo(gamePlayer10, Arrays.asList("B5", "B6", "C7"), 1);
//            Salvo salvo19 = new Salvo(gamePlayer9, Arrays.asList("G6", "G7", "G8"), 2);
//            Salvo salvo20 = new Salvo(gamePlayer10, Arrays.asList("C6", "D6", "E6"), 2);
//            Salvo salvo21 = new Salvo(gamePlayer9, Arrays.asList("H1", "H8"), 3);
//
//
//            salvoRepository.save(salvo1);
//            salvoRepository.save(salvo2);
//            salvoRepository.save(salvo3);
//            salvoRepository.save(salvo4);
//            salvoRepository.save(salvo5);
//            salvoRepository.save(salvo6);
//            salvoRepository.save(salvo7);
//            salvoRepository.save(salvo8);
//            salvoRepository.save(salvo9);
//            salvoRepository.save(salvo10);
//            salvoRepository.save(salvo11);
//            salvoRepository.save(salvo12);
//            salvoRepository.save(salvo13);
//            salvoRepository.save(salvo14);
//            salvoRepository.save(salvo15);
//            salvoRepository.save(salvo16);
//            salvoRepository.save(salvo17);
//            salvoRepository.save(salvo18);
//            salvoRepository.save(salvo19);
//            salvoRepository.save(salvo20);
//            salvoRepository.save(salvo21);

//            Score score1 = new Score(game1, bauer, 1.0, LocalDateTime.now());
//            Score score2 = new Score(game1, obrian, 0.0, LocalDateTime.now());
//            Score score3 = new Score(game2, bauer, 0.5, LocalDateTime.now());
//            Score score4 = new Score(game2, obrian, 0.5, LocalDateTime.now());
//            Score score5 = new Score(game3, obrian, 1.0, LocalDateTime.now());
//            Score score6 = new Score(game3, almeida, 0.0, LocalDateTime.now());
//            Score score7 = new Score(game4, obrian, 0.5, LocalDateTime.now());
//            Score score8 = new Score(game4, bauer, 0.5, LocalDateTime.now());
//
//            scoreRepository.save(score1);
//            scoreRepository.save(score2);
//            scoreRepository.save(score3);
//            scoreRepository.save(score4);
//            scoreRepository.save(score5);
//            scoreRepository.save(score6);
//            scoreRepository.save(score7);
//            scoreRepository.save(score8);
        };


    }

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputEmail -> {
            Player player = playerRepository.findByUserName(inputEmail);
            if (player != null) {
                return new User(player.getUserName(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputEmail);
            }
        });
    }

}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/players").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/**").hasAuthority("USER")
                .antMatchers("/api/game_view/*").permitAll();
        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");


        // turn off checking for CSRF tokens
        http.csrf().disable();
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }
}




