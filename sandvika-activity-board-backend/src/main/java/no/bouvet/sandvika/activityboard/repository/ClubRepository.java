package no.bouvet.sandvika.activityboard.repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import no.bouvet.sandvika.activityboard.domain.Club;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "club", path = "club")
public interface ClubRepository extends CosmosRepository<Club, String> {
    List<Club> findClubsByMemberIdsContains(Integer athleteId);
}
