package weblogic.messaging.interception.internal;

import java.util.HashMap;
import java.util.Iterator;
import weblogic.messaging.interception.MIExceptionLogger;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;

class AssociationManager {
   private static HashMap associationMap = new HashMap(0);

   static synchronized Association createAssociation(InterceptionPoint var0, ProcessorWrapper var1, boolean var2, int var3) throws InterceptionServiceException {
      Association var4 = (Association)associationMap.get(var0.getInternalName());
      if (var4 != null) {
         throw new InterceptionServiceException(MIExceptionLogger.logAddAssociationAlreadyExistErrorLoggable("Association exists").getMessage());
      } else {
         var4 = new Association(var0, var1, var2, var3);
         var0.addAssociation(var4);
         var1.addAssociation(var4);
         associationMap.put(var4.getInternalName(), var4);
         return var4;
      }
   }

   static void removeAssociation(Association var0) throws InterceptionServiceException {
      String var1 = var0.getInternalName();
      ProcessorWrapper var2 = var0.getProcessorWrapper();
      var0.remove();
      var2.removeProcessorWrapperIfNotUsed();
      Class var3 = AssociationManager.class;
      synchronized(AssociationManager.class) {
         associationMap.remove(var1);
      }
   }

   static Iterator getAssociations() {
      HashMap var0 = null;
      Class var1 = AssociationManager.class;
      synchronized(AssociationManager.class) {
         var0 = (HashMap)associationMap.clone();
      }

      return var0.values().iterator();
   }

   public static int getAssociationsSize(String var0) {
      if (var0 == null) {
         return associationMap.keySet().size();
      } else {
         Iterator var1 = associationMap.keySet().iterator();
         int var2 = 0;

         while(var1.hasNext()) {
            String var3 = (String)var1.next();
            if (!var3.startsWith(var0.length() + " " + var0)) {
               ++var2;
            }
         }

         return var2;
      }
   }
}
