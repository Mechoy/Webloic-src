package weblogic.iiop;

import java.io.IOException;
import java.rmi.Remote;
import java.util.HashMap;
import weblogic.corba.cos.codebase.CodeBaseImpl;
import weblogic.corba.cos.codebase.RunTimeImpl;
import weblogic.corba.cos.naming.RootNamingContextImpl;
import weblogic.corba.cos.transactions.RecoveryCoordinatorImpl;
import weblogic.corba.cos.transactions.RecoveryFactory;
import weblogic.corba.cos.transactions.ResourceFactory;
import weblogic.corba.cos.transactions.ResourceImpl;
import weblogic.corba.cos.transactions.TransactionFactoryImpl;
import weblogic.corba.idl.IDLHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.HeartbeatHelperImpl;
import weblogic.rmi.internal.InitialReferenceConstants;

public final class InitialReferences implements InitialReferenceConstants {
   public static final String COS_NAMING_SERVICE = "NameService";
   public static final String COS_NAMING_PATH = "weblogic.cosnaming.NameService";
   public static final String COS_TRANSACTION_FACTORY_SERVICE = "TransactionFactory";
   public static final String COS_CODEBASE_SERVICE = "CodeBase";
   public static final String HEARTBEAT_HELPER_SERVICE = "HeartbeatHelper";
   static final HashMap refMap = new HashMap();
   static final HashMap refObjectMap = new HashMap();
   public static final int MAX_INITIAL_REF_LENGTH = 128;
   private static final Object NULL_OBJECT = new Object();
   private static final String[] services = new String[]{"NameService", "CodeBase", "TransactionFactory", "HeartbeatHelper"};

   public static void initializeServerInitialReferences() throws IOException {
      IDLHelper.exportObject(RootNamingContextImpl.getRootNamingContext(), "weblogic.cosnaming.NameService");
      IDLHelper.exportObject(TransactionFactoryImpl.getTransactionFactory());
      ServerHelper.exportObject(ResourceImpl.class, ResourceFactory.getActivator());
      ServerHelper.exportObject(RecoveryCoordinatorImpl.class, RecoveryFactory.getActivator());
      refMap.put("NameService", RootNamingContextImpl.getRootNamingContext().getIOR());
      refObjectMap.put("NameService", RootNamingContextImpl.getRootNamingContext());
      refMap.put("TransactionFactory", (IOR)IIOPReplacer.getReplacer().replaceObject(TransactionFactoryImpl.getTransactionFactory()));
      refObjectMap.put("TransactionFactory", TransactionFactoryImpl.getTransactionFactory());
      String var0 = "RMI:weblogic.rmi.extensions.server.HeartbeatHelper:0000000000000000";
      IOR var1 = new IOR(var0, new ObjectKey(var0, 21));
      refMap.put("HeartbeatHelper", var1);
      refObjectMap.put("HeartbeatHelper", HeartbeatHelperImpl.getHeartbeatHelper());
   }

   public static void initializeClientInitialReferences() throws IOException {
      IDLHelper.exportObject(RunTimeImpl.getRunTime());
      IDLHelper.exportObject(CodeBaseImpl.getCodeBase());
      refMap.put("CodeBase", (IOR)IIOPReplacer.getReplacer().replaceObject(CodeBaseImpl.getCodeBase()));
      refObjectMap.put("CodeBase", CodeBaseImpl.getCodeBase());
   }

   public static IOR getInitialReference(String var0) {
      Object var1 = refMap.get(var0);
      if (var1 == null && var0.startsWith("weblogic.")) {
         try {
            Class var2 = Class.forName(var0);
            Remote var3 = ServerHelper.getLocalInitialReference(var2);
            var1 = IIOPReplacer.getIIOPReplacer().replaceRemote(var3);
            refMap.put(var0, var1);
         } catch (Exception var4) {
            refMap.put(var0, NULL_OBJECT);
         }
      } else if (var1 == NULL_OBJECT) {
         return null;
      }

      return (IOR)var1;
   }

   public static String[] getServiceList() {
      return services;
   }

   public static org.omg.CORBA.Object getInitialReferenceObject(String var0) {
      return (org.omg.CORBA.Object)refObjectMap.get(var0);
   }
}
