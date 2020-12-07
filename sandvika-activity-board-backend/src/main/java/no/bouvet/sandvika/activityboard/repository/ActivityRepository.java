package no.bouvet.sandvika.activityboard.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import no.bouvet.sandvika.activityboard.domain.Activity;

@RepositoryRestResource(collectionResourceRel = "activity", path = "activity")
public interface ActivityRepository extends MongoRepository<Activity, Long>
{
    public List<Activity> findAllByBadgesIsNotNull();

    public List<Activity> findByAthleteId(int id);

    public List<Activity> findByStartDateLocalAfter(Date startDate);

    public List<Activity> findByStartDateLocalBetween(Date startDate, Date endDate);

    public List<Activity> findByStartDateLocalAfterAndType(Date startDate, String type);

    Stream<Activity> findByTypeOrderByStartDateLocalDesc(String type);

    Stream<Activity> findByTypeAndStartDateLocalBetweenOrderByStartDateLocalDesc(Date startDate, Date endDate, String type);

    List<Activity> findByStartDateLocalBetweenAndType(Date startDate, Date endDate, String type);

    List<Activity> findByStartDateLocalBetweenAndAthleteId(Date startDate, Date endDate, int athleteId);

    Stream<Activity> findAllByOrderByStartDateLocalDesc();

    Stream<Activity> findAllByPhotosIsNotNullOrderByStartDateLocalDesc();

    Stream<Activity> findByTypeAndPhotosIsNotNullOrderByStartDateLocalDesc(String type);

    Activity findOneByAthleteLastName(String lastName);

    Stream<Activity> findByStartDateLocalBetweenOrderByStartDateLocalDesc(Date startDate, Date endDate);
}
