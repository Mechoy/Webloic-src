package weblogic.cluster.singleton;

import java.io.IOException;
import java.util.Map;

public class ReplicatedLeasingBasis extends SimpleLeasingBasis {
   public static final String BASIS_NAME = "ReplicatedLeasingBasis";

   public ReplicatedLeasingBasis(String var1) throws IOException {
      super(getReplicatedMap(var1));
   }

   protected static Map getReplicatedMap(String var0) throws IOException {
      return null;
   }
}
