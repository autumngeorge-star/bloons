package com.hongbao.bloons.entities;

/**
 * Tracks normalized path progress along waypoints and segments for game entities.
 */
public class PathProgress implements Cloneable {

    private int waypointIndex;
    private float segmentRatio;
    private float accumulatedDistance;

    public PathProgress() {
        this.waypointIndex = 0;
        this.segmentRatio = 0.0f;
        this.accumulatedDistance = 0.0f;
    }

    public PathProgress(int waypointIndex, float segmentRatio, float accumulatedDistance) {
        this.waypointIndex = waypointIndex;
        this.segmentRatio = segmentRatio;
        this.accumulatedDistance = accumulatedDistance;
    }

    public PathProgress(PathProgress other) {
        if (other != null) {
            this.waypointIndex = other.waypointIndex;
            this.segmentRatio = other.segmentRatio;
            this.accumulatedDistance = other.accumulatedDistance;
        } else {
            this.waypointIndex = 0;
            this.segmentRatio = 0.0f;
            this.accumulatedDistance = 0.0f;
        }
    }

    public int getWaypointIndex() {
        return waypointIndex;
    }

    public void setWaypointIndex(int waypointIndex) {
        this.waypointIndex = waypointIndex;
    }

    public float getSegmentRatio() {
        return segmentRatio;
    }

    public void setSegmentRatio(float segmentRatio) {
        this.segmentRatio = segmentRatio;
    }

    public float getAccumulatedDistance() {
        return accumulatedDistance;
    }

    public void setAccumulatedDistance(float accumulatedDistance) {
        this.accumulatedDistance = accumulatedDistance;
    }

    /**
     * Updates path progress based on movement displacement along a path segment of given length.
     * Ignores updates when displacement is less than or equal to zero (zero-velocity updates).
     *
     * @param displacement distance moved along segment
     * @param segmentLength length of current path segment
     */
    public void updateProgress(float displacement, float segmentLength) {
        if (displacement <= 0) {
            return; // Ignore zero-velocity or negative displacement updates
        }
        this.accumulatedDistance += displacement;
        if (segmentLength > 0) {
            float addedRatio = displacement / segmentLength;
            this.segmentRatio += addedRatio;
            while (this.segmentRatio >= 1.0f) {
                this.segmentRatio -= 1.0f;
                this.waypointIndex++;
            }
        }
    }

    /**
     * Returns normalized progress scalar combining waypoint index and segment ratio.
     *
     * @return normalized path progress (waypointIndex + segmentRatio)
     */
    public float getNormalizedProgress() {
        return waypointIndex + segmentRatio;
    }

    @Override
    public PathProgress clone() {
        return new PathProgress(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathProgress that = (PathProgress) o;

        if (waypointIndex != that.waypointIndex) return false;
        if (Float.compare(that.segmentRatio, segmentRatio) != 0) return false;
        return Float.compare(that.accumulatedDistance, accumulatedDistance) == 0;
    }

    @Override
    public int hashCode() {
        int result = waypointIndex;
        result = 31 * result + (segmentRatio != +0.0f ? Float.floatToIntBits(segmentRatio) : 0);
        result = 31 * result + (accumulatedDistance != +0.0f ? Float.floatToIntBits(accumulatedDistance) : 0);
        return result;
    }
}
