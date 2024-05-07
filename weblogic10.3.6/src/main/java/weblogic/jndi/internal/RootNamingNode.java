package weblogic.jndi.internal;

import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.naming.NamingException;
import weblogic.rmi.extensions.server.ServerHelper;

public final class RootNamingNode extends ServerNamingNode {
   public static final String STUB_NAME = ServerHelper.getStubClassName(ServerNamingNode.class.getName());
   private static RootNamingNode singleton;

   public static RootNamingNode getSingleton() {
      if (singleton == null) {
         Class var0 = RootNamingNode.class;
         synchronized(RootNamingNode.class) {
            if (singleton == null) {
               singleton = new RootNamingNode();
            }
         }
      }

      return singleton;
   }

   private RootNamingNode() {
      try {
         Hashtable var1 = new Hashtable();
         var1.put("weblogic.jndi.createIntermediateContexts", "true");
         var1.put("weblogic.jndi.replicateBindings", "false");
         this.bind("weblogic.rmi", new ServerNamingNode(""), var1);
      } catch (NamingException var2) {
         throw new AssertionError(var2);
      } catch (RemoteException var3) {
         throw new AssertionError(var3);
      }
   }

   static void initialize() {
   }
}
