import entity.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Movie movie = new Movie("배트맨", Duration.ofMinutes(120), Money.wons(10000), null);

        movie.setMovieType(MovieType.AMOUNT_DISCOUNT);
        Screening screening = new Screening(movie, 1, LocalDateTime.now());
        ReservationAgency agency = new ReservationAgency();

        Customer minssogi = new Customer("minssogi", "1234");

        Reservation reserve = agency.reserve(screening, minssogi, 1);

        System.out.println(reserve.toString());
    }
}
