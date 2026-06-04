package dev.mateorossello.entertainmentlists.services;

import dev.mateorossello.entertainmentlists.dtos.EntertainmentListInput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutput;
import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import dev.mateorossello.entertainmentlists.repositories.EntertainmentListRepository;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEntertainmentListService implements EntertainmentListService {
    // Base service class for entertainment lists
    // Each specific service for different types of entertainment entities will extend this class
    
    protected final EntertainmentListRepository entertainmentListRepository;

    protected AbstractEntertainmentListService(EntertainmentListRepository entertainmentListRepository) {
        this.entertainmentListRepository = entertainmentListRepository;
    }
    
    @Override
    public List<EntertainmentListOutput> getAllLists() {
        List<EntertainmentList> lists = entertainmentListRepository.findAll();
        return mapToOutputList(lists);
    }

    @Override
    public EntertainmentList createList(EntertainmentListInput input) {
        EntertainmentList newList = new EntertainmentList();
        newList.setName(input.name());
        newList.setEntertainmentEntityIds(input.entertainmentEntityIds());
        newList.setProvider(input.provider());
        newList.setType(input.type());
        return entertainmentListRepository.save(newList);
    }

    @Override
    public EntertainmentList updateList(Long id, EntertainmentListInput input) {
        EntertainmentList existingList = entertainmentListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Entertainment list not found with id: " + id));
        
        if (!existingList.getProvider().equals(input.provider())) {
            throw new IllegalArgumentException("Entertainment list provider does not match");
        }

        if (existingList.getType() != null && !existingList.getType().equals(input.type())) {
            throw new IllegalArgumentException("Entertainment list type does not match");
        }

        existingList.setName(input.name());
        existingList.setEntertainmentEntityIds(input.entertainmentEntityIds());

        return entertainmentListRepository.save(existingList);
    }

    @Override
    public void deleteList(Long id) {
        entertainmentListRepository.deleteById(id);
    }

    private EntertainmentListOutput mapToOutput(EntertainmentList list) {
        return new EntertainmentListOutput(list.getId(), list.getName(), list.getProvider(), list.getType());
    }

    private List<EntertainmentListOutput> mapToOutputList(List<EntertainmentList> lists) {
        return lists.stream().map(this::mapToOutput).collect(Collectors.toList());
    }
}
