package no.bouvet.sandvika.activityboard.domain;

/**
 * Enum representing the activity types we support
 */

public enum ActivityType {
    NORDIC_SKIING(Constants.NORDIC_SKIING_NAME),
    BACKCOUNTRY_SKI(Constants.BACKCOUNTRY_SKI_NAME),
    RUN(Constants.RUN_NAME),
    RIDE(Constants.RIDE_NAME),
    SWIM(Constants.SWIM_NAME),
    ROWING(Constants.ROWING_NAME),
    VIRTUAL_RIDE(Constants.VIRTUAL_RIDE_NAME),
    HIKE(Constants.HIKE_NAME),
    WALK(Constants.WALK_NAME),
    WORKOUT(Constants.WORKOUT_NAME),
    WEIGHT_TRAINING(Constants.WEIGHT_TRAINING_NAME),
    CROSSFIT(Constants.CROSSFIT_NAME),
    KAYAKING(Constants.KAYAKING_NAME),
    ROLLER_SKI(Constants.ROLLER_SKI_NAME),
    YOGA(Constants.YOGA_NAME),
    E_BIKE_RIDE(Constants.E_BIKE_RIDE_NAME),
    ICE_SKATE(Constants.ICE_SKATE_NAME),
    WINDSURF(Constants.WINDSURF_NAME);
    private String type;

    ActivityType(String type) {
        this.type = type;
    }

    /**
     * Returns an ActivityType based on the activity name provided. Returns NULL if the name is not
     * a valid activity name or if the activityName paramater is NULL.
     *
     * @return an ActivityType if a valid activityName is provided, else NULL.
     */

    public static ActivityType toActivityType(String activityName) {
        if (activityName == null) {
            return null;
        } else if (activityName.equalsIgnoreCase(Constants.NORDIC_SKIING_NAME)) {
            return ActivityType.NORDIC_SKIING;
        } else if (activityName.equalsIgnoreCase(Constants.BACKCOUNTRY_SKI_NAME)) {
            return ActivityType.NORDIC_SKIING;
        } else if (activityName.equalsIgnoreCase(Constants.RUN_NAME)) {
            return ActivityType.RUN;
        } else if (activityName.equalsIgnoreCase(Constants.RIDE_NAME)) {
            return ActivityType.RIDE;
        } else if (activityName.equalsIgnoreCase(Constants.SWIM_NAME)) {
            return ActivityType.SWIM;
        } else if (activityName.equalsIgnoreCase(Constants.ROWING_NAME)) {
            return ActivityType.ROWING;
        } else if (activityName.equalsIgnoreCase(Constants.VIRTUAL_RIDE_NAME)) {
            return ActivityType.VIRTUAL_RIDE;
        } else if (activityName.equalsIgnoreCase(Constants.HIKE_NAME)) {
            return ActivityType.HIKE;
        } else if (activityName.equalsIgnoreCase(Constants.WALK_NAME)) {
            return ActivityType.WALK;
        } else if (activityName.equalsIgnoreCase(Constants.WORKOUT_NAME)) {
            return ActivityType.WORKOUT;
        } else if (activityName.equalsIgnoreCase(Constants.WEIGHT_TRAINING_NAME)) {
            return ActivityType.WEIGHT_TRAINING;
        } else if (activityName.equalsIgnoreCase(Constants.CROSSFIT_NAME)) {
            return ActivityType.CROSSFIT;
        } else if (activityName.equalsIgnoreCase(Constants.KAYAKING_NAME)) {
            return ActivityType.KAYAKING;
        } else if (activityName.equalsIgnoreCase(Constants.ROLLER_SKI_NAME)) {
            return ActivityType.ROLLER_SKI;
        } else if (activityName.equalsIgnoreCase(Constants.YOGA_NAME)) {
            return ActivityType.YOGA;
        } else if (activityName.equalsIgnoreCase(Constants.E_BIKE_RIDE_NAME)) {
            return ActivityType.E_BIKE_RIDE;
        } else if (activityName.equalsIgnoreCase(Constants.ICE_SKATE_NAME)) {
            return ActivityType.ICE_SKATE;
        } else if (activityName.equalsIgnoreCase(Constants.WINDSURF_NAME)) {
            return ActivityType.WINDSURF;
        } else {
            return null;
        }
    }

    public String type() {
        return this.type;
    }

    public double distanceCoefficient() {
        switch (this) {
            case NORDIC_SKIING:
                return Constants.NORDIC_SKIIING_COEFFISIENT_KM;
            case BACKCOUNTRY_SKI:
                return Constants.BACKCOUNTRY_SKI_COEFFISIENT_KM;
            case RIDE:
                return Constants.RIDE_COEFFISIENT_KM;
            case RUN:
                return Constants.RUN_COEFFISIENT_KM;
            case SWIM:
                return Constants.SWIM_COEFFISIENT_KM;
            case ROWING:
                return Constants.ROWING_COEFFISIENT_KM;
            case VIRTUAL_RIDE:
                return Constants.VIRTUAL_RIDE_COEFFISIENT_KM;
            case HIKE:
                return Constants.HIKE_COEFFISIENT_KM;
            case WALK:
                return Constants.WALK_COEFFISIENT_KM;
            case WORKOUT:
                return Constants.WORKOUT_COEFFISIENT_KM;
            case WEIGHT_TRAINING:
                return Constants.WEIGHT_TRAINING_COEFFISIENT_KM;
            case CROSSFIT:
                return Constants.CROSSFIT_COEFFISIENT_KM;
            case KAYAKING:
                return Constants.KAYAKING_COEFFISIENT_KM;
            case ROLLER_SKI:
                return Constants.ROLLER_SKI_COEFFISIENT_KM;
            case YOGA:
                return Constants.YOGA_COEFFISIENT_KM;
            case E_BIKE_RIDE:
                return Constants.E_BIKE_RIDE_COEFFISIENT_KM;
            case ICE_SKATE:
                return Constants.ICE_SKATE_COEFFISIENT_KM;
            case WINDSURF:
                return Constants.WINDSURF_COEFFISIENT_KM;
            default:
                return 1;
        }
    }

    public double durationCoefficient() {
        switch (this) {
            case NORDIC_SKIING:
                return Constants.NORDIC_SKIIING_COEFFISIENT_MIN;
            case BACKCOUNTRY_SKI:
                return Constants.BACKCOUNTRY_SKI_COEFFISIENT_MIN;
            case RIDE:
                return Constants.RIDE_COEFFISIENT_MIN;
            case RUN:
                return Constants.RUN_COEFFISIENT_MIN;
            case SWIM:
                return Constants.SWIM_COEFFISIENT_MIN;
            case ROWING:
                return Constants.ROWING_COEFFISIENT_MIN;
            case VIRTUAL_RIDE:
                return Constants.VIRTUAL_RIDE_COEFFISIENT_MIN;
            case HIKE:
                return Constants.HIKE_COEFFISIENT_MIN;
            case WALK:
                return Constants.WALK_COEFFISIENT_MIN;
            case WORKOUT:
                return Constants.WORKOUT_COEFFISIENT_MIN;
            case WEIGHT_TRAINING:
                return Constants.WEIGHT_TRAINING_COEFFISIENT_MIN;
            case CROSSFIT:
                return Constants.CROSSFIT_COEFFISIENT_MIN;
            case KAYAKING:
                return Constants.KAYAKING_COEFFISIENT_MIN;
            case ROLLER_SKI:
                return Constants.ROLLER_SKI_COEFFISIENT_MIN;
            case YOGA:
                return Constants.YOGA_COEFFISIENT_MIN;
            case E_BIKE_RIDE:
                return Constants.E_BIKE_RIDE_COEFFISIENT_MIN;
            case ICE_SKATE:
                return Constants.ICE_SKATE_COEFFISIENT_MIN;
            case WINDSURF:
                return Constants.WINDSURF_COEFFISIENT_MIN;
            default:
                return 1;
        }
    }

    public double elevationCoefficient() {
        switch (this) {
            case NORDIC_SKIING:
                return Constants.NORDIC_SKIIING_COEFFISIENT_ELE;
            case BACKCOUNTRY_SKI:
                return Constants.BACKCOUNTRY_SKI_COEFFISIENT_ELE;
            case RIDE:
                return Constants.RIDE_COEFFISIENT_ELE;
            case RUN:
                return Constants.RUN_COEFFISIENT_ELE;
            case SWIM:
                return Constants.SWIM_COEFFISIENT_ELE;
            case ROWING:
                return Constants.ROWING_COEFFISIENT_ELE;
            case VIRTUAL_RIDE:
                return Constants.VIRTUAL_RIDE_COEFFISIENT_ELE;
            case HIKE:
                return Constants.HIKE_COEFFISIENT_ELE;
            case WALK:
                return Constants.WALK_COEFFISIENT_ELE;
            case WORKOUT:
                return Constants.WORKOUT_COEFFISIENT_ELE;
            case WEIGHT_TRAINING:
                return Constants.WEIGHT_TRAINING_COEFFISIENT_ELE;
            case CROSSFIT:
                return Constants.CROSSFIT_COEFFISIENT_ELE;
            case KAYAKING:
                return Constants.KAYAKING_COEFFISIENT_ELE;
            case ROLLER_SKI:
                return Constants.ROLLER_SKI_COEFFISIENT_ELE;
            case YOGA:
                return Constants.YOGA_COEFFISIENT_ELE;
            case E_BIKE_RIDE:
                return Constants.E_BIKE_RIDE_COEFFISIENT_ELE;
            case ICE_SKATE:
                return Constants.ICE_SKATE_COEFFISIENT_ELE;
            case WINDSURF:
                return Constants.WINDSURF_COEFFISIENT_ELE;
            default:
                return 1;
        }
    }

    private static class Constants {
        public static final String NORDIC_SKIING_NAME = "nordicski";
        public static final String BACKCOUNTRY_SKI_NAME = "backcountryski";
        public static final String RUN_NAME = "run";
        public static final String RIDE_NAME = "ride";
        public static final String SWIM_NAME = "swim";
        public static final String ROWING_NAME = "rowing";
        public static final String WALK_NAME = "walk";
        public static final String HIKE_NAME = "hike";
        public static final String VIRTUAL_RIDE_NAME = "virtualride";
        public static final String WORKOUT_NAME = "workout";
        public static final String WEIGHT_TRAINING_NAME = "weighttraining";
        public static final String CROSSFIT_NAME = "crossfit";
        public static final String KAYAKING_NAME = "kayaking";
        public static final String ROLLER_SKI_NAME = "rollerski";
        public static final String YOGA_NAME = "yoga";
        public static final String E_BIKE_RIDE_NAME = "ebikeride";
        public static final String ICE_SKATE_NAME = "iceskate";
        public static final String WINDSURF_NAME = "windsurf";

        public static final double ROWING_COEFFISIENT_KM = 1;
        public static final double KAYAKING_COEFFISIENT_KM = 1;
        public static final double NORDIC_SKIIING_COEFFISIENT_KM = 0.6;
        public static final double BACKCOUNTRY_SKI_COEFFISIENT_KM = 0.6;
        public static final double RIDE_COEFFISIENT_KM = 0.4;
        public static final double VIRTUAL_RIDE_COEFFISIENT_KM = RIDE_COEFFISIENT_KM;
        public static final double RUN_COEFFISIENT_KM = 1;
        public static final double SWIM_COEFFISIENT_KM = 3.5;
        public static final double HIKE_COEFFISIENT_KM = 1;
        public static final double WALK_COEFFISIENT_KM = 1;
        public static final double WORKOUT_COEFFISIENT_KM = 0;
        public static final double WEIGHT_TRAINING_COEFFISIENT_KM = 0;
        public static final double CROSSFIT_COEFFISIENT_KM = 0;
        public static final double ROLLER_SKI_COEFFISIENT_KM = 0.5;
        public static final double YOGA_COEFFISIENT_KM = 0;
        public static final double E_BIKE_RIDE_COEFFISIENT_KM = 0.1;
        public static final double ICE_SKATE_COEFFISIENT_KM = 0.3;
        public static final double WINDSURF_COEFFISIENT_KM = 0.3;

        public static final double ROWING_COEFFISIENT_MIN = 1;
        public static final double KAYAKING_COEFFISIENT_MIN = 1;
        public static final double NORDIC_SKIIING_COEFFISIENT_MIN = 1;
        public static final double BACKCOUNTRY_SKI_COEFFISIENT_MIN = 0.3;
        public static final double RIDE_COEFFISIENT_MIN = 0.5;
        public static final double VIRTUAL_RIDE_COEFFISIENT_MIN = RIDE_COEFFISIENT_MIN;
        public static final double RUN_COEFFISIENT_MIN = 1;
        public static final double SWIM_COEFFISIENT_MIN = 1;
        public static final double HIKE_COEFFISIENT_MIN = 0.2;
        public static final double WALK_COEFFISIENT_MIN = 0.2;
        public static final double WORKOUT_COEFFISIENT_MIN = 1;
        public static final double WEIGHT_TRAINING_COEFFISIENT_MIN = 1.3;
        public static final double CROSSFIT_COEFFISIENT_MIN = 1.3;
        public static final double ROLLER_SKI_COEFFISIENT_MIN = 1;
        public static final double YOGA_COEFFISIENT_MIN = 1;
        public static final double E_BIKE_RIDE_COEFFISIENT_MIN = 0.3;
        public static final double ICE_SKATE_COEFFISIENT_MIN = 0.3;
        public static final double WINDSURF_COEFFISIENT_MIN = 0.3;

        public static final double ROWING_COEFFISIENT_ELE = 1;
        public static final double KAYAKING_COEFFISIENT_ELE = 1;
        public static final double NORDIC_SKIIING_COEFFISIENT_ELE = 1;
        public static final double BACKCOUNTRY_SKI_COEFFISIENT_ELE = 0.3;
        public static final double RIDE_COEFFISIENT_ELE = 0.5;
        public static final double VIRTUAL_RIDE_COEFFISIENT_ELE = RIDE_COEFFISIENT_ELE;
        public static final double RUN_COEFFISIENT_ELE = 1;
        public static final double SWIM_COEFFISIENT_ELE = 1;
        public static final double HIKE_COEFFISIENT_ELE = 0.2;
        public static final double WALK_COEFFISIENT_ELE = 0.2;
        public static final double WORKOUT_COEFFISIENT_ELE = 1;
        public static final double WEIGHT_TRAINING_COEFFISIENT_ELE = 0;
        public static final double CROSSFIT_COEFFISIENT_ELE = 0;
        public static final double ROLLER_SKI_COEFFISIENT_ELE = 1;
        public static final double YOGA_COEFFISIENT_ELE = 1;
        public static final double E_BIKE_RIDE_COEFFISIENT_ELE = 0.3;
        public static final double ICE_SKATE_COEFFISIENT_ELE = 0.3;
        public static final double WINDSURF_COEFFISIENT_ELE = 0;

    }
}
