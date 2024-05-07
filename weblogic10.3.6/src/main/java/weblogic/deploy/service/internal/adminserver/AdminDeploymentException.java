package weblogic.deploy.service.internal.adminserver;

import java.net.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.deploy.service.DeploymentException;
import weblogic.deploy.service.FailureDescription;

public class AdminDeploymentException extends Exception implements DeploymentException {
   private List failureDescriptions = Collections.synchronizedList(new ArrayList());

   public AdminDeploymentException() {
      this.failureDescriptions = new ArrayList();
   }

   public FailureDescription[] getFailures() {
      FailureDescription[] var1 = new FailureDescription[0];
      return (FailureDescription[])((FailureDescription[])this.failureDescriptions.toArray(var1));
   }

   public void addFailureDescription(FailureDescription var1) {
      String var2 = var1.getServer();
      if (var2 != null && !this.hasFailureFor(var2)) {
         this.failureDescriptions.add(var1);
      }

   }

   public void addFailureDescriptions(Set var1) {
      if (var1 != null && var1.size() > 0) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.addFailureDescription((FailureDescription)var2.next());
         }

      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("AdminDeploymentException due to underlying exceptions : ");
      if (!this.failureDescriptions.isEmpty()) {
         Iterator var2 = this.failureDescriptions.iterator();
         int var3 = 1;

         while(true) {
            Throwable var6;
            label33:
            do {
               while(var2.hasNext()) {
                  var1.append(" Failure ");
                  var1.append(var3);
                  ++var3;
                  var1.append(": reason: ");
                  FailureDescription var4 = (FailureDescription)var2.next();
                  Exception var5 = var4.getReason();
                  if (var5 instanceof RemoteException) {
                     var6 = ((RemoteException)var5).detail;
                     continue label33;
                  }

                  var1.append(var4.toString());
               }

               return var1.toString();
            } while(!(var6 instanceof ConnectException) && !(var6 instanceof java.rmi.ConnectException) && !(var6 instanceof ConnectIOException) && !(var6 instanceof UnknownHostException));

            var1.append("ConnectException : " + var6.toString());
         }
      } else {
         return var1.toString();
      }
   }

   private boolean hasFailureFor(String var1) {
      Iterator var2 = this.failureDescriptions.iterator();

      String var4;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         FailureDescription var3 = (FailureDescription)var2.next();
         var4 = var3.getServer();
      } while(var4 == null || !var4.equals(var1));

      return true;
   }
}
