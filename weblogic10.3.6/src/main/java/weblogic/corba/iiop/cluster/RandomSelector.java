package weblogic.corba.iiop.cluster;

public class RandomSelector implements Selector {
   public static final RandomSelector SELECTOR = new RandomSelector();
   public static final String ALGORITHM = "random";

   public int select(int var1, int var2) {
      double var3 = Math.random() * (double)var2 + 0.5;
      return (int)Math.round(var3) - 1;
   }
}
