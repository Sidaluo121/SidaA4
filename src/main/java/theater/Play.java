package theater;

/**
 * Represents a play, including its name and type.
 */
public class Play {

    private final String name;
    private final String type;

    /**
     * Constructs a play.
     *
     * @param name the play name
     * @param type the play type (e.g., tragedy, comedy)
     */
    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
