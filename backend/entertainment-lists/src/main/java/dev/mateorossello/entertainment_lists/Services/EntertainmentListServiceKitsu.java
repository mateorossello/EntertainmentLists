package dev.mateorossello.entertainment_lists.Services;

import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListInput;
import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainment_lists.Models.EntertainmentList;
import dev.mateorossello.entertainment_lists.Models.EntertainmentListKitsu;
import dev.mateorossello.entertainment_lists.Repositories.EntertainmentListRepository;
import java.util.Map;
import org.springframework.stereotype.Service;

//
// EXAMPLE SERVICE. CAN BE USED AS A TEMPLATE FOR OTHER SERVICES OR DELETE IF NOT NEEDED
//

@Service
public class EntertainmentListServiceKitsu implements EntertainmentListService {
    // Example of a specific service for Kitsu lists
    private final EntertainmentListRepository entertainmentListRepository;
    private final EntertainmentEntityService entertainmentEntityService;

    public EntertainmentListServiceKitsu(EntertainmentListRepository entertainmentListRepository, EntertainmentEntityService entertainmentEntityService) {
        this.entertainmentListRepository = entertainmentListRepository;
        this.entertainmentEntityService = entertainmentEntityService;
    }

    @Override
    public EntertainmentListRepository getEntertainmentListRepository() {
        return entertainmentListRepository;
    }

    @Override
    public EntertainmentListOutputDetailed getListById(Long id) {
        EntertainmentListKitsu list = (EntertainmentListKitsu) entertainmentListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entertainment list not found with id: " + id));
        
        return mapToOutputListDetailed(list);
    }

    private EntertainmentListOutputDetailed mapToOutputListDetailed(EntertainmentListKitsu list) {
        EntertainmentListOutputDetailed output = new EntertainmentListOutputDetailed();
        output.setId(list.getId());
        output.setName(list.getName());
        for (Long entityId : list.getEntertainmentEntityIds()) {
            output.addEntertainmentEntity(entertainmentEntityService.getEntityById(entityId, Map.of("type", list.getType())));
        }
        
        return output;
    }

    @Override
    public EntertainmentList createList(EntertainmentListInput input, Map<String, String> parameters) {
        EntertainmentListKitsu newList = new EntertainmentListKitsu();
        newList.setName(input.getName());
        newList.setType(parameters.get("type"));
        newList.setEntertainmentEntityIds(input.getEntertainmentEntityIds());

        return entertainmentListRepository.save(newList);
    }
}
