package ua.rd.cm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem_Pryzhkov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@Entity
@Table(name = "user_info")
@SequenceGenerator(name = "seqUserInfoGen", allocationSize = 1,
        sequenceName = "user_info_seq")
public class UserInfo {

    @Id
    @Column(name = "user_info_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUserInfoGen")
    private Long id;

    @Column(name = "short_bio", nullable = false, length = 2000)
    private String shortBio = "";

    @Column(name = "job_title", nullable = false, length = 256)
    private String jobTitle = "";

    @Column(name = "past_conference", length = 1000)
    private String pastConference;

    @Column(name = "company", nullable = false, length = 256)
    private String company = "";

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_info_contact",
            joinColumns = @JoinColumn(name = "user_info_id")
    )
    @Column(name = "link", length = 1000)
    @MapKeyJoinColumn(name = "contact_type_id",
            referencedColumnName = "contact_type_id")
    private Map<ContactType, String> contacts = new HashMap<>();

    @Column(name = "additional_info", length = 1000)
    private String additionalInfo;

}