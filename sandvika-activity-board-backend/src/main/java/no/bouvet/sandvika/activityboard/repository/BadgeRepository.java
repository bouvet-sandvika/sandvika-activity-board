package no.bouvet.sandvika.activityboard.repository;

import no.bouvet.sandvika.activityboard.domain.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RepositoryRestResource(collectionResourceRel = "badge", path = "badge")
public interface BadgeRepository extends MongoRepository<Badge, String> {
    Badge findByName(String name);

//    Set<Badge> findBadgeByActivityTypeIn(List<String> activityTypes);
    Set<Badge> findBadgeByActivityTypeInAndStartDateBeforeAndEndDateAfter(List<String> activityType, Date startDate, Date endDate);

    void deleteByName(String name);

}