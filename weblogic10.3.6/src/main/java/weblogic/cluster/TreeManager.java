package weblogic.cluster;

import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.utils.AssertionError;

public final class TreeManager {
   private static TreeManager theTreeManager = null;
   private Map nodes;
   private Context initialCtx;
   private ConflictHandler conHandler;
   private long ageThreshold;

   static TreeManager theOne() {
      return theTreeManager;
   }

   static void initialize(long var0) {
      theTreeManager = new TreeManager(var0);
   }

   private TreeManager(long var1) {
      try {
         Environment var3 = new Environment();
         var3.setReplicateBindings(false);
         var3.setCreateIntermediateContexts(true);
         this.initialCtx = var3.getInitialContext();
      } catch (NamingException var4) {
         throw new AssertionError("Failed to create initial context", var4);
      }

      this.conHandler = new BasicConflictHandler();
      this.nodes = new HashMap();
      this.ageThreshold = var1;
   }

   void install(ServiceOffer var1, boolean var2) {
      NodeInfo var3 = this.find(var1.name());
      var3.install(var1, var2);
      this.done(var3);
   }

   void retract(ServiceOffer var1, boolean var2) {
      NodeInfo var3 = this.find(var1.name());
      var3.retract(var1, var2);
      this.doneRemove(var3, var1.name());
   }

   void update(ServiceOffer var1, boolean var2) {
      NodeInfo var3 = this.find(var1.name());
      var3.update(var1, var2);
      this.done(var3);
   }

   private synchronized NodeInfo find(String var1) {
      NodeInfo var2 = (NodeInfo)this.nodes.get(var1);
      if (var2 == null) {
         var2 = new NodeInfo(this.initialCtx, this.conHandler, var1, this.ageThreshold);
         this.nodes.put(var1, var2);
         var2.numUnprocessedRequests = 1;
      } else {
         ++var2.numUnprocessedRequests;
      }

      return var2;
   }

   private synchronized void done(NodeInfo var1) {
      --var1.numUnprocessedRequests;
   }

   private synchronized void doneRemove(NodeInfo var1, String var2) {
      --var1.numUnprocessedRequests;
      if (var1.numUnprocessedRequests == 0 && var1.isEmpty()) {
         this.nodes.remove(var2);
      }

   }
}
