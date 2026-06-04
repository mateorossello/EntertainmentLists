package dev.mateorossello.entertainmentlists.services.kitsu;

import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import dev.mateorossello.entertainmentlists.repositories.EntertainmentListRepository;
import dev.mateorossello.entertainmentlists.services.AbstractEntertainmentListService;
import dev.mateorossello.entertainmentlists.services.EntertainmentEntityService;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EntertainmentListServiceKitsu extends AbstractEntertainmentListService {
    // Example of a specific service for Kitsu lists

    private final EntertainmentEntityService entertainmentEntityService;

    public EntertainmentListServiceKitsu(EntertainmentListRepository entertainmentListRepository, EntertainmentEntityService entertainmentEntityService) {
        super(entertainmentListRepository);
        this.entertainmentEntityService = entertainmentEntityService;
    }

    @Override
    public Mono<EntertainmentListOutputDetailed> getListById(Long id) {
        EntertainmentList list = entertainmentListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Entertainment list not found with id: " + id));
        
        return Flux.fromIterable(list.getEntertainmentEntityIds()).flatMap(entityId -> entertainmentEntityService.getEntityById(entityId, Map.of("type", list.getType()))).collectList().map(entities -> {
            EntertainmentListOutputDetailed output = new EntertainmentListOutputDetailed(
                list.getId(),
                list.getName(),
                list.getProvider(),
                list.getType(),
                new ArrayList<>()
            );

            entities.forEach(output::addEntertainmentEntity);
            return output;
        });
    }
}
