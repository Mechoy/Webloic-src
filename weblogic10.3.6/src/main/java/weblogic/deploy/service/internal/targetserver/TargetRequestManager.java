package weblogic.deploy.service.internal.targetserver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.service.internal.RequestManager;

public class TargetRequestManager extends RequestManager {
   private final Map requestsMap;
   private final Set pendingCancels;

   private TargetRequestManager() {
      super("TargetRequestManager");
      this.requestsMap = new HashMap();
      this.pendingCancels = new HashSet();
   }

   public static TargetRequestManager getInstance() {
      return TargetRequestManager.Maker.SINGLETON;
   }

   public final synchronized void addToRequestTable(TargetRequestImpl var1) {
      if (this.isDebugEnabled()) {
         this.debug("adding request '" + var1.getId() + "' to target request table");
      }

      this.requestsMap.put(new Long(var1.getId()), var1);
   }

   public final synchronized Set getRequests() {
      return this.requestsMap.entrySet();
   }

   public final synchronized TargetRequestImpl getRequest(long var1) {
      return (TargetRequestImpl)this.requestsMap.get(new Long(var1));
   }

   public final synchronized void removeRequest(long var1) {
      if (this.isDebugEnabled()) {
         this.debug("removing request '" + var1 + "' from target request table");
      }

      this.requestsMap.remove(new Long(var1));
   }

   public final synchronized void addToPendingCancels(long var1) {
      if (this.isDebugEnabled()) {
         this.debug("adding request '" + var1 + "' to target pending cancel set");
      }

      this.pendingCancels.add(new Long(var1));
   }

   public final synchronized void removePendingCancelFor(long var1) {
      if (this.isDebugEnabled()) {
         this.debug("removing request '" + var1 + "' from target " + "pending cancel set");
      }

      this.pendingCancels.remove(new Long(var1));
   }

   public final synchronized boolean hasAPendingCancelFor(long var1) {
      return this.pendingCancels.contains(new Long(var1));
   }

   public final synchronized boolean handlingRequests() {
      return this.requestsMap.size() > 0;
   }

   // $FF: synthetic method
   TargetRequestManager(Object var1) {
      this();
   }

   static class Maker {
      static final TargetRequestManager SINGLETON = new TargetRequestManager();
   }
}
