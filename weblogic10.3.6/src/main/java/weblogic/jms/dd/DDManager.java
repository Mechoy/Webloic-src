package weblogic.jms.dd;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.NewDestinationListener;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.frontend.FEDDHandler;

public final class DDManager implements NewDestinationListener {
   private static HashMap deferredMemberTable = new HashMap();
   private static HashMap member2DDHandler = new HashMap();
   private static Hashtable name2DDHandler = new Hashtable();

   private DDManager() {
   }

   public static DDHandler activateOrUpdate(DDHandler var0) {
      synchronized(name2DDHandler) {
         DDHandler var2 = (DDHandler)name2DDHandler.get(var0.getName());
         if (var2 != null) {
            var2.update(var0);
            return var2;
         } else {
            name2DDHandler.put(var0.getName(), var0);
            var0.activate();
            return var0;
         }
      }
   }

   public static void remoteUpdate(DDHandler var0, List var1) {
      DDHandler var2 = activateOrUpdate(var0);
      if (!var2.isLocal()) {
         var2.updateMembers(var1);
      } else if (var2.isRemoteUpdatePending()) {
         var2.updateMembers(var1);
         var2.setRemoteUpdatePending(false);
      }

   }

   public static void remoteDeactivate(String var0) {
      synchronized(name2DDHandler) {
         DDHandler var2 = (DDHandler)name2DDHandler.get(var0);
         if (var2 != null) {
            if (!var2.isLocal()) {
               var2.deactivate();
            }
         }
      }
   }

   public static void deactivate(DDHandler var0) {
      name2DDHandler.remove(var0.getName());
   }

   private static void deferMember(DDMember var0) {
      deferredMemberTable.put(var0.getName(), var0);
   }

   public static DDMember removeDeferredMember(String var0) {
      return (DDMember)deferredMemberTable.remove(var0);
   }

   public static void memberUpdate(DDMember var0) {
      DDHandler var1 = findDDHandlerByMemberName(var0.getName());
      if (var1 == null) {
         deferMember(var0);
      } else {
         var1.memberUpdate(var0);
      }

   }

   public static void addMemberStatusListener(String var0, MemberStatusListener var1) {
      DDHandler var2 = findDDHandlerByMemberName(var0);

      assert var2 != null;

      var2.addMemberStatusListener(var0, var1);
   }

   public static DDHandler findDDHandlerByMemberName(String var0) {
      synchronized(member2DDHandler) {
         return (DDHandler)member2DDHandler.get(var0);
      }
   }

   public static void addDDHandlerMember(String var0, DDHandler var1) {
      synchronized(member2DDHandler) {
         member2DDHandler.put(var0, var1);
      }
   }

   public static DDHandler removeDDHandlerMember(String var0) {
      synchronized(member2DDHandler) {
         return (DDHandler)member2DDHandler.remove(var0);
      }
   }

   public static boolean isMember(String var0, String var1) {
      DDHandler var2 = findDDHandlerByMemberName(var1);
      return var2 == null ? false : var2.getName().equals(var0);
   }

   public static boolean isDD(String var0) {
      return findDDHandlerByDDName(var0) != null;
   }

   public static DDHandler findDDHandlerByDDName(String var0) {
      return (DDHandler)name2DDHandler.get(var0);
   }

   public static FEDDHandler findFEDDHandlerByDDName(String var0) {
      DDHandler var1 = findDDHandlerByDDName(var0);
      return var1 == null ? null : var1.getFEDDHandler();
   }

   public static DistributedDestinationImpl findDDImplByDDName(String var0) {
      DDHandler var1 = findDDHandlerByDDName(var0);
      return var1 == null ? null : var1.getDDImpl();
   }

   public static boolean handlerHasSecurityModeByMemberName(String var0, int var1) {
      DDHandler var2 = findDDHandlerByMemberName(var0);
      return var2 == null ? false : var2.memberHasSecurityMode(var1);
   }

   public static DistributedDestinationImpl findDDImplByMemberName(String var0) {
      DDHandler var1 = findDDHandlerByMemberName(var0);
      return var1 == null ? null : var1.getDDIByMemberName(var0);
   }

   public static String debugKeys() {
      String var1 = "\n values are ";
      Set var0;
      synchronized(member2DDHandler) {
         var0 = ((HashMap)member2DDHandler.clone()).keySet();
         Iterator var3 = ((HashMap)member2DDHandler.clone()).values().iterator();
         if (var3.hasNext()) {
            while(var3.hasNext()) {
               DDHandler var4 = (DDHandler)var3.next();
               var1 = var1 + "\n(" + var4 + " keys: " + var4.debugKeys() + ")";
            }
         } else {
            var1 = "\n no values";
         }
      }

      return var0 == null ? "DDManager keys are null" : "DDManager Member Name keys are: " + var0.toString() + var1;
   }

   public static DDMember findDDMemberByMemberName(String var0) {
      DDHandler var1 = findDDHandlerByMemberName(var0);
      return var1 == null ? null : var1.findMemberByName(var0);
   }

   public static Iterator getAllDDHandlers() {
      return name2DDHandler.values().iterator();
   }

   public void newDestination(BEDestinationImpl var1) {
      DDHandler var2 = findDDHandlerByMemberName(var1.getName());
      if (var2 != null) {
         var2.newDestination(var1);
      }

   }

   static {
      BEDestinationImpl.addNewDestinationListener(new DDManager());
   }
}
