package theater;

/**
 * Represents a single performance of a play.
 */
public class Performance {

    private final String playID;
    int audience;

    /**
     * Constructs a performance.
     *
     * @param playID   the ID of the play
     * @param audience the audience size
     */
    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    public String getPlayID() {
        return playID;
    }

    public int getAudience() {
        return audience;
    }
}
