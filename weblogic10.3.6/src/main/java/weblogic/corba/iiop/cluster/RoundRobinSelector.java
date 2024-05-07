package weblogic.corba.iiop.cluster;

public class RoundRobinSelector implements Selector {
   public static final RoundRobinSelector SELECTOR = new RoundRobinSelector();
   public static final String ALGORITHM = "round-robin";

   public int select(int var1, int var2) {
      return (var1 + 1) % var2;
   }
}
