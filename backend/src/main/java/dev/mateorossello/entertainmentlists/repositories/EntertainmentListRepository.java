package dev.mateorossello.entertainmentlists.repositories;

import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntertainmentListRepository extends JpaRepository<EntertainmentList, Long> {
    List<EntertainmentList> findByUserUsername(String username);
    
    Optional<EntertainmentList> findByIdAndUserUsername(Long id, String username);
}
