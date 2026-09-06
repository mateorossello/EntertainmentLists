package dev.mateorossello.entertainmentlists.services;

import dev.mateorossello.entertainmentlists.dtos.EntertainmentListInput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import dev.mateorossello.entertainmentlists.models.User;
import dev.mateorossello.entertainmentlists.repositories.EntertainmentListRepository;
import dev.mateorossello.entertainmentlists.repositories.UserRepository;
import dev.mateorossello.entertainmentlists.services.registry.EntertainmentEntityServiceRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EntertainmentListService {
    private final EntertainmentListRepository entertainmentListRepository;
    private final UserRepository userRepository;
    private final EntertainmentEntityServiceRegistry entityServiceRegistry;

    public EntertainmentListService(EntertainmentListRepository entertainmentListRepository, UserRepository userRepository, EntertainmentEntityServiceRegistry entityServiceRegistry) {
        this.entertainmentListRepository = entertainmentListRepository;
        this.userRepository = userRepository;
        this.entityServiceRegistry = entityServiceRegistry;
    }

    public List<EntertainmentListOutput> getAllListsForUser(String username) {
        List<EntertainmentList> lists = entertainmentListRepository.findByUserUsername(username);
        return lists.stream().map(this::mapToOutput).collect(Collectors.toList());
    }

    public Mono<EntertainmentListOutputDetailed> getListById(Long id) {
        EntertainmentList list = entertainmentListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Entertainment list not found for ID " + id));
        
        List<Long> entityIds = list.getEntertainmentEntityIds();

        if (entityIds == null || entityIds.isEmpty()) {
            return Mono.just(new EntertainmentListOutputDetailed(
                list.getId(),
                list.getProvider(),
                list.getType(),
                list.getName(),
                new ArrayList<>()
            ));
        }

        return Flux.fromIterable(entityIds)
            .flatMap(entityId -> entityServiceRegistry.getService(list.getProvider()).getEntityById(entityId, Map.of("type", list.getType())))
            .collectList()
            .map(entities -> new EntertainmentListOutputDetailed(
                list.getId(),
                list.getProvider(),
                list.getType(),
                list.getName(),
                entities
            ));
    }

    public EntertainmentList createList(EntertainmentListInput input, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
                
        EntertainmentList newList = new EntertainmentList();
        newList.setName(input.name());
        newList.setEntertainmentEntityIds(input.entertainmentEntityIds());
        newList.setProvider(input.provider());
        newList.setType(input.type());
        newList.setUser(user);
        
        return entertainmentListRepository.save(newList);
    }

    public EntertainmentList updateList(Long id, EntertainmentListInput input, String username) {
        EntertainmentList list = findByIdAndUsernameOrThrow(id, username);
        
        if (!list.getProvider().equals(input.provider())) {
            throw new IllegalArgumentException("Entertainment list provider does not match");
        }

        if (list.getType() != null && !list.getType().equals(input.type())) {
            throw new IllegalArgumentException("Entertainment list type does not match");
        }

        list.setName(input.name());
        list.setEntertainmentEntityIds(input.entertainmentEntityIds());

        return entertainmentListRepository.save(list);
    }

    public void deleteList(Long id, String username) {
        EntertainmentList list = findByIdAndUsernameOrThrow(id, username);
        entertainmentListRepository.delete(list);
    }

    public void addEntityToList(Long listId, Long entityId, String username) {
        EntertainmentList list = findByIdAndUsernameOrThrow(listId, username);
        
        List<Long> entityIds = list.getEntertainmentEntityIds();

        if (entityIds != null && !entityIds.contains(entityId)) {
            entityIds.add(entityId);
            entertainmentListRepository.save(list);
        } else if (entityIds == null) {
            list.setEntertainmentEntityIds(new ArrayList<>(List.of(entityId)));
            entertainmentListRepository.save(list);
        }
    }

    public void removeEntityFromList(Long listId, Long entityId, String username) {
        EntertainmentList list = findByIdAndUsernameOrThrow(listId, username);
        
        List<Long> entityIds = list.getEntertainmentEntityIds();

        if (entityIds != null && entityIds.contains(entityId)) {
            entityIds.remove(entityId);
            entertainmentListRepository.save(list);
        }
    }

    private EntertainmentList findByIdAndUsernameOrThrow(Long id, String username) {
        return entertainmentListRepository.findByIdAndUserUsername(id, username).orElseThrow(() -> new IllegalArgumentException("Entertainment list not found for ID " + id));
    }

    private EntertainmentListOutput mapToOutput(EntertainmentList list) {
        return new EntertainmentListOutput(list.getId(), list.getName(), list.getProvider(), list.getType());
    }
}
