package no.bouvet.sandvika.activityboard.repository;

import no.bouvet.sandvika.activityboard.domain.Club;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "club", path = "club")
public interface ClubRepository extends MongoRepository<Club, String> {
    List<Club> findClubsByMemberIdsContains(Integer athleteId);
}
