package no.bouvet.sandvika.activityboard.repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

import no.bouvet.sandvika.activityboard.domain.Athlete;

@RepositoryRestResource(collectionResourceRel = "athlete", path = "athlete")
public interface AthleteRepository extends CosmosRepository<Athlete, Integer>
{
    Athlete findById(int id);

    void deleteById(int id);

    Athlete findOneByLastNameAndFirstName(String lastName, String firstName);

    Athlete findOneByToken(String token);

    List<Athlete> findAllByBadgesIsNotNull();

    List<Athlete> findAllByTokenIsNotNull();
}
