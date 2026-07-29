package dev.mateorossello.entertainmentlists.services;

import dev.mateorossello.entertainmentlists.dtos.EntertainmentListInput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import java.util.List;
import reactor.core.publisher.Mono;

public interface EntertainmentListService {
    // Base service interface for entertainment lists

    Mono<EntertainmentListOutputDetailed> getListById(Long id);

    List<EntertainmentListOutput> getAllListsForUser(String username);

    EntertainmentList createList(EntertainmentListInput input, String username);

    EntertainmentList updateList(Long id, EntertainmentListInput input, String username);

    void deleteList(Long id, String username);

    void addEntityToList(Long listId, Long entityId, String username);

    void removeEntityFromList(Long listId, Long entityId, String username);
}
