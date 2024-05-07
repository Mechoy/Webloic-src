package weblogic.iiop;

import java.util.Iterator;

public class printior {
   public static final void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         System.out.println("weblogic.iiop.printior <ior>");
      } else {
         IOR var1 = IOR.destringify(var0[0]);
         prettyPrintIOR(var1);
         if (var1.getProfile().isClusterable()) {
            ClusterComponent var2 = (ClusterComponent)var1.getProfile().getComponent(1111834883);
            System.out.println("Cluster replicas: " + var2.getIORs().size());
            Iterator var3 = var2.getIORs().iterator();
            int var4 = 0;

            while(var3.hasNext()) {
               System.out.println("Replica " + var4++);
               var1 = (IOR)var3.next();
               prettyPrintIOR(var1);
            }
         }
      }

   }

   private static final void prettyPrintIOR(IOR var0) {
      System.out.println("IOR for type: " + var0.getTypeId());
      System.out.println("IOP Profile:");
      System.out.println("\tversion:\t1." + var0.getProfile().getMinorVersion());
      System.out.println("\thost:\t" + var0.getProfile().getHost());
      System.out.println("\tport:\t" + var0.getProfile().getPort());
      System.out.println("\tSSL host:\t" + var0.getProfile().getSecureHost());
      System.out.println("\tSSL port:\t" + var0.getProfile().getSecurePort());
      System.out.println("\tCSIv2 support:\t" + var0.getProfile().useSAS());
      System.out.println("\ttransactional:\t" + var0.getProfile().isTransactional());
      System.out.println("\tclusterable:\t" + var0.getProfile().isClusterable());
      System.out.println("\tkey:\t" + var0.getProfile().getObjectKey().toString());
   }
}
