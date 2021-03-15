package seedu.address.model.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

public class UniqueFoodListTest {

    private final UniqueFoodList uniqueFoodListTest = new UniqueFoodList();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(),uniqueFoodListTest.getFoodList());
    }

    @Test
    public void hasFoodItem() {
        Food noodles = new Food("noodles", 1,1,1);
        Food chocolate = new Food("chocolate", 2,2,2);
        assertFalse(uniqueFoodListTest.hasFoodItem(noodles));
        assertFalse(uniqueFoodListTest.hasFoodItem(chocolate));

        uniqueFoodListTest.addFoodItem(noodles);
        uniqueFoodListTest.addFoodItem(chocolate);

        assertTrue(uniqueFoodListTest.hasFoodItem(noodles));
        assertTrue(uniqueFoodListTest.hasFoodItem(chocolate));

        uniqueFoodListTest.deleteFoodItem(0);
        uniqueFoodListTest.deleteFoodItem(0);

        assertFalse(uniqueFoodListTest.hasFoodItem(noodles));
        assertFalse(uniqueFoodListTest.hasFoodItem(chocolate));
    }

    @Test
    public void getInvalidFoodItem() {
        String strawberry = "strawberry";
        assertEquals(-1, uniqueFoodListTest.getFoodItemIndex(strawberry));
    }

    @Test
    public void getValidFoodItem() {
        Food noodles = new Food("noodles", 1,1,1);
        Food chocolate = new Food("chocolate", 2,2,2);
        uniqueFoodListTest.addFoodItem(noodles);
        uniqueFoodListTest.addFoodItem(chocolate);
        assertEquals(0, uniqueFoodListTest.getFoodItemIndex("noodles"));
        assertEquals(1, uniqueFoodListTest.getFoodItemIndex("chocolate"));
    }
}
