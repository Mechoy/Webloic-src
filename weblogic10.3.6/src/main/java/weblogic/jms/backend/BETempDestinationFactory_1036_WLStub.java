package weblogic.jms.backend;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.transaction.Transaction;
import weblogic.jms.common.JMSID;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class BETempDestinationFactory_1036_WLStub extends Stub implements StubInfoIntf, BETempDestinationFactoryRemote {
   private static Method[] m;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   // $FF: synthetic field
   private static Class class$weblogic$jms$backend$BETempDestinationFactoryRemote;
   private static boolean initialized;

   public BETempDestinationFactory_1036_WLStub(StubInfo var1) {
      super(var1);
      this.stubinfo = var1;
      this.ror = this.stubinfo.getRemoteRef();
      ensureInitialized(this.stubinfo);
   }

   public StubInfo getStubInfo() {
      return this.stubinfo;
   }

   private static synchronized void ensureInitialized(StubInfo var0) {
      if (!initialized) {
         m = Utilities.getRemoteRMIMethods(var0.getInterfaces());
         md0 = new MethodDescriptor(m[0], class$weblogic$jms$backend$BETempDestinationFactoryRemote == null ? (class$weblogic$jms$backend$BETempDestinationFactoryRemote = class$("weblogic.jms.backend.BETempDestinationFactoryRemote")) : class$weblogic$jms$backend$BETempDestinationFactoryRemote, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         initialized = true;
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public final Destination createTempDestination(DispatcherId var1, JMSID var2, boolean var3, int var4, long var5, String var7) throws RemoteException, JMSException {
      Transaction var8 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Destination var24;
      try {
         Object[] var9 = new Object[]{var1, var2, new Boolean(var3), new Integer(var4), new Long(var5), var7};
         var24 = (Destination)this.ror.invoke((Remote)null, md0, var9, m[0]);
      } catch (Error var18) {
         throw var18;
      } catch (RuntimeException var19) {
         throw var19;
      } catch (RemoteException var20) {
         throw var20;
      } catch (JMSException var21) {
         throw var21;
      } catch (Throwable var22) {
         throw new RemoteRuntimeException("Unexpected Exception", var22);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var8);
      }

      return var24;
   }
}
