package seedu.address.model.food;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FoodIntakeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        String date = "6 May 2019";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        assertThrows(NullPointerException.class, () -> new FoodIntake(LocalDate.parse(date, formatter), null));
        assertThrows(NullPointerException.class, () -> new FoodIntake(null, null));
        assertThrows(NullPointerException.class, () -> new FoodIntake(null, new Food("Shrooms", 1,1,1)));
    }
}
