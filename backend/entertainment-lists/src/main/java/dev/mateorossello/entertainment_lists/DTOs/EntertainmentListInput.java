package dev.mateorossello.entertainment_lists.DTOs;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntertainmentListInput {
    private String name;
    private List<Long> entertainmentEntityIds;
}
