package dev.mateorossello.entertainmentlists.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entertainment_lists")
public class EntertainmentList {
    // Base class for entertainment lists
    // Contains common fields for all entertainment lists
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = true)
    private String type;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "entertainment_list_entities", joinColumns = @JoinColumn(name = "entertainment_list_id"))
    @Column(name = "entertainment_entity_id")
    private List<Long> entertainmentEntityIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
