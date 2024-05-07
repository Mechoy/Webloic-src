package weblogic.cluster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ClusterDropoutListener implements ClusterMembersChangeListener {
   private static final boolean DEBUG = true;
   private static HashMap dropouts = new HashMap();
   private static ClusterDropoutListener theClusterDropoutListener;

   public static ClusterDropoutListener theOne() {
      return theClusterDropoutListener;
   }

   static void initialize() {
      if (theClusterDropoutListener == null) {
         theClusterDropoutListener = new ClusterDropoutListener();
      }
   }

   private ClusterDropoutListener() {
      MemberManager.theOne().addClusterMembersListener(this);
   }

   public HashMap getDropoutCounts() {
      HashMap var1 = new HashMap(dropouts.size());
      Set var2 = dropouts.keySet();
      if (var2 == null) {
         return var1;
      } else {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            DropoutCounter var5 = (DropoutCounter)dropouts.get(var4);
            var1.put(var4, new Integer(var5.getCount()));
         }

         return var1;
      }
   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      if (var1.getAction() == 1) {
         DropoutCounter var2 = (DropoutCounter)dropouts.get(var1.getClusterMemberInfo().serverName());
         if (var2 == null) {
            var2 = new DropoutCounter();
         }

         var2.increment();
         dropouts.put(var1.getClusterMemberInfo().serverName(), var2);
      }

   }

   private void debug(Object var1) {
      System.out.println("<ClusterDropoutListener> " + var1.toString());
   }

   private static class DropoutCounter {
      private int dropouts;

      private DropoutCounter() {
         this.dropouts = 0;
      }

      public void increment() {
         ++this.dropouts;
      }

      public int getCount() {
         return this.dropouts;
      }

      public String toString() {
         return "Number of dropouts: " + this.dropouts;
      }

      // $FF: synthetic method
      DropoutCounter(Object var1) {
         this();
      }
   }
}
