package dev.mateorossello.entertainmentlists.repositories;

import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntertainmentListRepository extends JpaRepository<EntertainmentList, Long> {

}
