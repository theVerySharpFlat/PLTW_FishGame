package fishinggame;

/**
 * Activity 4.9.3
 */
public class Hook extends LakeObject {
  private int strength;

  public Hook() {
    super();
    strength = 55;
  }

  @Override
  public int getCost() {
    return 15;
  }

  @Override
  public String say() {
    return "You have a hook!";
  }

  /*---------- accessor ----------*/
  public int getStrength() {
    return strength; // This return will be updated in the next activity
    // return strength;
  }

}
