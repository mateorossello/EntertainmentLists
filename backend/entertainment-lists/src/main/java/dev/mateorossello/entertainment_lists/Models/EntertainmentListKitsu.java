package dev.mateorossello.entertainment_lists.Models;

import jakarta.persistence.*;
import lombok.*;

//
// EXAMPLE CLASS. CAN BE USED AS A TEMPLATE FOR OTHER ENTITIES OR DELETE IF NOT NEEDED
//

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entertainment_list_kitsu")
public class EntertainmentListKitsu extends EntertainmentList {
    // Example of a specific entertainment list for Kitsu entities
    private String type; // Type of list (anime or manga)
}
