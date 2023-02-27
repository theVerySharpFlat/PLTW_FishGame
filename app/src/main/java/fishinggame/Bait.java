package fishinggame;

/*
 * Activity 4.9.3
 */
public class Bait extends LakeObject {
    @Override
    public int getCost() {
        return 10;
    }

    @Override
    public String say() {
        return "You got Bait";
    }
}
