package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark.
 */
public class Remark {
    public final String value;

    /**
     * Creates a new Remark object with the given remark parameter.
     * @param remark Actual remark
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Remark
                && value.equals(((Remark) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
