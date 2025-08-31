package dev.mateorossello.entertainment_lists.Repositories;

import dev.mateorossello.entertainment_lists.Models.EntertainmentList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntertainmentListRepository extends JpaRepository<EntertainmentList, Long> {
    List<EntertainmentList> findByName(String name);
}
