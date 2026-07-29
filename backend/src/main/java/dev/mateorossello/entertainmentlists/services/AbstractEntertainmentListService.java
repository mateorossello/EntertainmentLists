package dev.mateorossello.entertainmentlists.services;

import dev.mateorossello.entertainmentlists.dtos.EntertainmentListInput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutput;
import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import dev.mateorossello.entertainmentlists.models.User;
import dev.mateorossello.entertainmentlists.repositories.EntertainmentListRepository;
import dev.mateorossello.entertainmentlists.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEntertainmentListService implements EntertainmentListService {
    // Base service class for entertainment lists
    // Each specific service for different types of entertainment entities will extend this class
    
    protected final EntertainmentListRepository entertainmentListRepository;
    protected final UserRepository userRepository;

    protected AbstractEntertainmentListService(EntertainmentListRepository entertainmentListRepository, UserRepository userRepository) {
        this.entertainmentListRepository = entertainmentListRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public List<EntertainmentListOutput> getAllListsForUser(String username) {
        List<EntertainmentList> lists = entertainmentListRepository.findByUserUsername(username);
        return mapToOutputList(lists);
    }

    @Override
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

    @Override
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

    @Override
    public void deleteList(Long id, String username) {
        EntertainmentList list = findByIdAndUsernameOrThrow(id, username);
        entertainmentListRepository.delete(list);
    }

    @Override
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

    @Override
    public void removeEntityFromList(Long listId, Long entityId, String username) {
        EntertainmentList list = findByIdAndUsernameOrThrow(listId, username);
        
        List<Long> entityIds = list.getEntertainmentEntityIds();

        if (entityIds != null && entityIds.contains(entityId)) {
            entityIds.remove(entityId);
            entertainmentListRepository.save(list);
        }
    }

    private EntertainmentList findByIdAndUsernameOrThrow(Long id, String username) {
        return entertainmentListRepository.findByIdAndUserUsername(id, username).orElseThrow(() -> new IllegalArgumentException("Entertainment list not found or unauthorized for ID " + id));
    }

    private EntertainmentListOutput mapToOutput(EntertainmentList list) {
        return new EntertainmentListOutput(list.getId(), list.getName(), list.getProvider(), list.getType());
    }

    private List<EntertainmentListOutput> mapToOutputList(List<EntertainmentList> lists) {
        return lists.stream().map(this::mapToOutput).collect(Collectors.toList());
    }
}
