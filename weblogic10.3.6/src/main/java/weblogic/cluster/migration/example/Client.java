package weblogic.cluster.migration.example;

import javax.naming.Context;
import weblogic.jndi.Environment;
import weblogic.rmi.internal.StubInfoIntf;

public final class Client {
   public static void main(String[] var0) throws Exception {
      if (var0.length >= 2) {
         System.out.println("Contacting: " + var0[0]);
         Context var1 = null;
         Environment var2 = new Environment();
         var2.setProviderUrl(var0[0]);
         MigratableVariable var3 = null;
         var1 = var2.getInitialContext();
         System.out.println("Lookup of: " + var0[1]);
         Object var4 = var1.lookup(var0[1]);
         StubInfoIntf var5 = (StubInfoIntf)var4;
         System.out.println(var5.getStubInfo().getRemoteRef());
         var3 = (MigratableVariable)var4;
         System.out.println("looping forever, hit control-C to stop");

         while(true) {
            while(true) {
               try {
                  System.out.println(var3.whereAmI());
                  Thread.currentThread();
                  Thread.sleep(1000L);
               } catch (Exception var7) {
                  var7.printStackTrace();
               }
            }
         }
      }

      System.out.println("Usage: java ...Client t3://host:port jndi-name");
   }
}
