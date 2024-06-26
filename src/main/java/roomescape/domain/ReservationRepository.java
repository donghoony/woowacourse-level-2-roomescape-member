package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> findById(long id);

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void deleteById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsBy(LocalDate date, long timeId, long themeId);
}
