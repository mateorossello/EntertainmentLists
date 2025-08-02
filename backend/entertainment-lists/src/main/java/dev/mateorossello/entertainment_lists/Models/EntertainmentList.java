package dev.mateorossello.entertainment_lists.Models;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "entertainment_lists")
public class EntertainmentList {
    // Base class for entertainment lists
    // Contains common fields for all entertainment lists
    // Each specific type of entertainment list will extend this class
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "entertainment_list_entities", joinColumns = @JoinColumn(name = "entertainment_list_id"))
    @Column(name = "entertainment_entity_id")
    private List<Long> entertainmentEntityIds;
}
