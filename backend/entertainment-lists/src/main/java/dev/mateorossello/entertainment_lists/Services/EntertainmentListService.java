package dev.mateorossello.entertainment_lists.Services;

import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListInput;
import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListOutput;
import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainment_lists.Models.EntertainmentList;
import dev.mateorossello.entertainment_lists.Repositories.EntertainmentListRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface EntertainmentListService {
    // Base service interface for entertainment lists
    // Each specific service for different types of entertainment entities will implement this interface
    EntertainmentListRepository getEntertainmentListRepository();

    default List<EntertainmentListOutput> getAllLists() {
        List<EntertainmentList> lists = getEntertainmentListRepository().findAll();
        
        return mapToOutputList(lists);
    }

    // This method should be defined in subclasses
    // to provide the specific implementation
    // about the information to show in the detailed output
    public abstract EntertainmentListOutputDetailed getListById(Long id);

    // This method should be defined in subclasses
    // to provide the specific implementation
    // about how to create a new list
    public abstract EntertainmentList createList(EntertainmentListInput input, Map<String, String> parameters);

    default EntertainmentList updateList(Long id, EntertainmentListInput input) {
        EntertainmentList updatedList = new EntertainmentList();
        updatedList.setId(id);
        updatedList.setName(input.getName());
        updatedList.setEntertainmentEntityIds(input.getEntertainmentEntityIds());

        return getEntertainmentListRepository().save(updatedList);
    }

    default void deleteList(Long id) {
        getEntertainmentListRepository().deleteById(id);
    }

    private EntertainmentListOutput mapToOutput(EntertainmentList list) {
        return new EntertainmentListOutput(list.getId(), list.getName());
    }

    private List<EntertainmentListOutput> mapToOutputList(List<EntertainmentList> lists) {
        return lists.stream()
                .map(this::mapToOutput)
                .collect(Collectors.toList());
    }
}
