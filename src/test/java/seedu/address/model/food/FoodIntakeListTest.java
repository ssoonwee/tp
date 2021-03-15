package seedu.address.model.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;

public class FoodIntakeListTest {

    private final FoodIntakeList foodIntakeListTest = new FoodIntakeList(LocalDate.now());

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(),foodIntakeListTest.getFoodIntakeList());
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FoodIntakeList(null));
    }

    @Test
    public void addFoodIntake_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> foodIntakeListTest.addFoodIntake(null));
    }

    @Test
    public void deleteFoodIntake_throwsIndexOutOfRangeException() {
        assertThrows(IndexOutOfBoundsException.class, () -> foodIntakeListTest.deleteFoodIntake(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> foodIntakeListTest.deleteFoodIntake(100));
    }
}
