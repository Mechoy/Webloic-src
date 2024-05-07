package weblogic.messaging.interception.internal;

import java.util.HashMap;
import weblogic.messaging.interception.MIExceptionLogger;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.AssociationListener;
import weblogic.messaging.interception.interfaces.InterceptionPointNameDescriptor;

class InterceptionPointTypeWrapper {
   private HashMap interceptionPointMap = new HashMap(0);
   private String name = null;
   private InterceptionPointNameDescriptor[] ipnd = null;
   private AssociationListener listener = null;

   InterceptionPointTypeWrapper(String var1, InterceptionPointNameDescriptor[] var2, AssociationListener var3) {
      this.name = var1;
      this.ipnd = var2;
      this.listener = var3;
   }

   String getName() {
      return this.name;
   }

   InterceptionPointNameDescriptor[] getIPND() {
      return this.ipnd;
   }

   AssociationListener getAssociationListener() {
      return this.listener;
   }

   void validate(String[] var1) throws InterceptionServiceException {
      if (this.ipnd.length != var1.length) {
         throw new InterceptionServiceException(MIExceptionLogger.logInvalidInterceptionPointNameLoggable("Invalid InterceptionPointName - different length").getMessage());
      } else {
         for(int var2 = 0; var2 < this.ipnd.length; ++var2) {
            if (!this.ipnd[var2].isValid(var1[var2])) {
               throw new InterceptionServiceException(MIExceptionLogger.logInvalidInterceptionPointNameLoggable("Invalid InterceptionPointName - invalid name").getMessage());
            }
         }

      }
   }

   synchronized InterceptionPoint findOrCreateInterceptionPoint(String[] var1) {
      String var2 = InterceptionPoint.createInternalName(this.name, var1);
      InterceptionPoint var3 = (InterceptionPoint)this.interceptionPointMap.get(var2);
      if (var3 == null) {
         var3 = new InterceptionPoint(this.name, var1, var2, this);
         this.interceptionPointMap.put(var2, var3);
      }

      return var3;
   }

   synchronized InterceptionPoint findInterceptionPoint(String[] var1) {
      String var2 = InterceptionPoint.createInternalName(this.name, var1);
      return (InterceptionPoint)this.interceptionPointMap.get(var2);
   }

   synchronized void removeIP(String var1) {
      this.interceptionPointMap.remove(var1);
   }

   synchronized int getIPsSize() {
      return this.interceptionPointMap.keySet().size();
   }
}
