package weblogic.management.mbeanservers.compatibility.internal;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;
import javax.transaction.Transaction;
import weblogic.corba.rmi.Stub;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class RemoteMBeanServerImpl_IIOP_WLStub extends Stub implements StubInfoIntf, RemoteMBeanServer {
   private static RuntimeMethodDescriptor md19;
   private static RuntimeMethodDescriptor md18;
   private static RuntimeMethodDescriptor md17;
   private static RuntimeMethodDescriptor md16;
   private static RuntimeMethodDescriptor md15;
   private static RuntimeMethodDescriptor md14;
   private static RuntimeMethodDescriptor md13;
   private static RuntimeMethodDescriptor md12;
   private static RuntimeMethodDescriptor md11;
   private static RuntimeMethodDescriptor md10;
   private static RuntimeMethodDescriptor md31;
   private static RuntimeMethodDescriptor md30;
   private static RuntimeMethodDescriptor md35;
   private static Method[] m;
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md34;
   private static RuntimeMethodDescriptor md29;
   private static RuntimeMethodDescriptor md28;
   private static RuntimeMethodDescriptor md27;
   private static RuntimeMethodDescriptor md26;
   private static RuntimeMethodDescriptor md25;
   private static RuntimeMethodDescriptor md24;
   private static RuntimeMethodDescriptor md23;
   private static RuntimeMethodDescriptor md22;
   private static RuntimeMethodDescriptor md21;
   private final RemoteReference ror;
   // $FF: synthetic field
   private static Class class$weblogic$management$RemoteMBeanServer;
   private static RuntimeMethodDescriptor md20;
   private static RuntimeMethodDescriptor md37;
   private static RuntimeMethodDescriptor md36;
   private final StubInfo stubinfo;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md33;
   private static RuntimeMethodDescriptor md32;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public RemoteMBeanServerImpl_IIOP_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
         md17 = new MethodDescriptor(m[17], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[17]), var0.getRemoteRef().getObjectID());
         md18 = new MethodDescriptor(m[18], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[18]), var0.getRemoteRef().getObjectID());
         md19 = new MethodDescriptor(m[19], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[19]), var0.getRemoteRef().getObjectID());
         md20 = new MethodDescriptor(m[20], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[20]), var0.getRemoteRef().getObjectID());
         md21 = new MethodDescriptor(m[21], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[21]), var0.getRemoteRef().getObjectID());
         md22 = new MethodDescriptor(m[22], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[22]), var0.getRemoteRef().getObjectID());
         md23 = new MethodDescriptor(m[23], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[23]), var0.getRemoteRef().getObjectID());
         md24 = new MethodDescriptor(m[24], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[24]), var0.getRemoteRef().getObjectID());
         md25 = new MethodDescriptor(m[25], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[25]), var0.getRemoteRef().getObjectID());
         md26 = new MethodDescriptor(m[26], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[26]), var0.getRemoteRef().getObjectID());
         md27 = new MethodDescriptor(m[27], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[27]), var0.getRemoteRef().getObjectID());
         md28 = new MethodDescriptor(m[28], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[28]), var0.getRemoteRef().getObjectID());
         md29 = new MethodDescriptor(m[29], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[29]), var0.getRemoteRef().getObjectID());
         md30 = new MethodDescriptor(m[30], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[30]), var0.getRemoteRef().getObjectID());
         md31 = new MethodDescriptor(m[31], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[31]), var0.getRemoteRef().getObjectID());
         md32 = new MethodDescriptor(m[32], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[32]), var0.getRemoteRef().getObjectID());
         md33 = new MethodDescriptor(m[33], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[33]), var0.getRemoteRef().getObjectID());
         md34 = new MethodDescriptor(m[34], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[34]), var0.getRemoteRef().getObjectID());
         md35 = new MethodDescriptor(m[35], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[35]), var0.getRemoteRef().getObjectID());
         md36 = new MethodDescriptor(m[36], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[36]), var0.getRemoteRef().getObjectID());
         md37 = new MethodDescriptor(m[37], class$weblogic$management$RemoteMBeanServer == null ? (class$weblogic$management$RemoteMBeanServer = class$("weblogic.management.RemoteMBeanServer")) : class$weblogic$management$RemoteMBeanServer, false, false, false, false, var0.getTimeOut(m[37]), var0.getRemoteRef().getObjectID());
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

   public final void addNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         this.ror.invoke((Remote)null, md0, var6, m[0]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void addNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         this.ror.invoke((Remote)null, md1, var6, m[1]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInstance var25;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var25 = (ObjectInstance)this.ror.invoke((Remote)null, md2, var4, m[2]);
      } catch (Error var16) {
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (ReflectionException var18) {
         throw var18;
      } catch (InstanceAlreadyExistsException var19) {
         throw var19;
      } catch (MBeanRegistrationException var20) {
         throw var20;
      } catch (MBeanException var21) {
         throw var21;
      } catch (NotCompliantMBeanException var22) {
         throw var22;
      } catch (Throwable var23) {
         throw new RemoteRuntimeException("Unexpected Exception", var23);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var25;
   }

   public final ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInstance var28;
      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         var28 = (ObjectInstance)this.ror.invoke((Remote)null, md3, var5, m[3]);
      } catch (Error var18) {
         throw var18;
      } catch (RuntimeException var19) {
         throw var19;
      } catch (ReflectionException var20) {
         throw var20;
      } catch (InstanceAlreadyExistsException var21) {
         throw var21;
      } catch (MBeanRegistrationException var22) {
         throw var22;
      } catch (MBeanException var23) {
         throw var23;
      } catch (NotCompliantMBeanException var24) {
         throw var24;
      } catch (InstanceNotFoundException var25) {
         throw var25;
      } catch (Throwable var26) {
         throw new RemoteRuntimeException("Unexpected Exception", var26);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

      return var28;
   }

   public final ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException {
      Transaction var6 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInstance var30;
      try {
         Object[] var7 = new Object[]{var1, var2, var3, var4, var5};
         var30 = (ObjectInstance)this.ror.invoke((Remote)null, md4, var7, m[4]);
      } catch (Error var20) {
         throw var20;
      } catch (RuntimeException var21) {
         throw var21;
      } catch (ReflectionException var22) {
         throw var22;
      } catch (InstanceAlreadyExistsException var23) {
         throw var23;
      } catch (MBeanRegistrationException var24) {
         throw var24;
      } catch (MBeanException var25) {
         throw var25;
      } catch (NotCompliantMBeanException var26) {
         throw var26;
      } catch (InstanceNotFoundException var27) {
         throw var27;
      } catch (Throwable var28) {
         throw new RemoteRuntimeException("Unexpected Exception", var28);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var6);
      }

      return var30;
   }

   public final ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInstance var27;
      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         var27 = (ObjectInstance)this.ror.invoke((Remote)null, md5, var6, m[5]);
      } catch (Error var18) {
         throw var18;
      } catch (RuntimeException var19) {
         throw var19;
      } catch (ReflectionException var20) {
         throw var20;
      } catch (InstanceAlreadyExistsException var21) {
         throw var21;
      } catch (MBeanRegistrationException var22) {
         throw var22;
      } catch (MBeanException var23) {
         throw var23;
      } catch (NotCompliantMBeanException var24) {
         throw var24;
      } catch (Throwable var25) {
         throw new RemoteRuntimeException("Unexpected Exception", var25);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

      return var27;
   }

   public final ObjectInputStream deserialize(String var1, ObjectName var2, byte[] var3) throws InstanceNotFoundException, OperationsException, ReflectionException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInputStream var22;
      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         var22 = (ObjectInputStream)this.ror.invoke((Remote)null, md6, var5, m[6]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (InstanceNotFoundException var17) {
         throw var17;
      } catch (OperationsException var18) {
         throw var18;
      } catch (ReflectionException var19) {
         throw var19;
      } catch (Throwable var20) {
         throw new RemoteRuntimeException("Unexpected Exception", var20);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

      return var22;
   }

   public final ObjectInputStream deserialize(String var1, byte[] var2) throws OperationsException, ReflectionException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInputStream var19;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var19 = (ObjectInputStream)this.ror.invoke((Remote)null, md7, var4, m[7]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (OperationsException var15) {
         throw var15;
      } catch (ReflectionException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var19;
   }

   public final ObjectInputStream deserialize(ObjectName var1, byte[] var2) throws InstanceNotFoundException, OperationsException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInputStream var19;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var19 = (ObjectInputStream)this.ror.invoke((Remote)null, md8, var4, m[8]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (OperationsException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var19;
   }

   public final Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var23;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var23 = (Object)this.ror.invoke((Remote)null, md9, var4, m[9]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (MBeanException var17) {
         throw var17;
      } catch (AttributeNotFoundException var18) {
         throw var18;
      } catch (InstanceNotFoundException var19) {
         throw var19;
      } catch (ReflectionException var20) {
         throw var20;
      } catch (Throwable var21) {
         throw new RemoteRuntimeException("Unexpected Exception", var21);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var23;
   }

   public final AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      AttributeList var19;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var19 = (AttributeList)this.ror.invoke((Remote)null, md10, var4, m[10]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (ReflectionException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var19;
   }

   public final ClassLoader getClassLoader(ObjectName var1) throws InstanceNotFoundException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ClassLoader var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (ClassLoader)this.ror.invoke((Remote)null, md11, var3, m[11]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (InstanceNotFoundException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final ClassLoader getClassLoaderFor(ObjectName var1) throws InstanceNotFoundException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ClassLoader var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (ClassLoader)this.ror.invoke((Remote)null, md12, var3, m[12]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (InstanceNotFoundException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final ClassLoaderRepository getClassLoaderRepository() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ClassLoaderRepository var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (ClassLoaderRepository)this.ror.invoke((Remote)null, md13, var2, m[13]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final String getDefaultDomain() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (String)this.ror.invoke((Remote)null, md14, var2, m[14]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final String[] getDomains() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String[] var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (String[])this.ror.invoke((Remote)null, md15, var2, m[15]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final Integer getMBeanCount() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Integer var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (Integer)this.ror.invoke((Remote)null, md16, var2, m[16]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final MBeanHome getMBeanHome() throws RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      MBeanHome var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (MBeanHome)this.ror.invoke((Remote)null, md17, var2, m[17]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (RemoteException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final MBeanInfo getMBeanInfo(ObjectName var1) throws InstanceNotFoundException, IntrospectionException, ReflectionException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      MBeanInfo var20;
      try {
         Object[] var3 = new Object[]{var1};
         var20 = (MBeanInfo)this.ror.invoke((Remote)null, md18, var3, m[18]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (IntrospectionException var16) {
         throw var16;
      } catch (ReflectionException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var20;
   }

   public final ObjectInstance getObjectInstance(ObjectName var1) throws InstanceNotFoundException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInstance var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (ObjectInstance)this.ror.invoke((Remote)null, md19, var3, m[19]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (InstanceNotFoundException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final String getServerName() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (String)this.ror.invoke((Remote)null, md20, var2, m[20]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final Object instantiate(String var1) throws ReflectionException, MBeanException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var18;
      try {
         Object[] var3 = new Object[]{var1};
         var18 = (Object)this.ror.invoke((Remote)null, md21, var3, m[21]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (ReflectionException var14) {
         throw var14;
      } catch (MBeanException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var18;
   }

   public final Object instantiate(String var1, ObjectName var2) throws ReflectionException, MBeanException, InstanceNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var21;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var21 = (Object)this.ror.invoke((Remote)null, md22, var4, m[22]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (ReflectionException var16) {
         throw var16;
      } catch (MBeanException var17) {
         throw var17;
      } catch (InstanceNotFoundException var18) {
         throw var18;
      } catch (Throwable var19) {
         throw new RemoteRuntimeException("Unexpected Exception", var19);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var21;
   }

   public final Object instantiate(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, MBeanException, InstanceNotFoundException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var23;
      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         var23 = (Object)this.ror.invoke((Remote)null, md23, var6, m[23]);
      } catch (Error var16) {
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (ReflectionException var18) {
         throw var18;
      } catch (MBeanException var19) {
         throw var19;
      } catch (InstanceNotFoundException var20) {
         throw var20;
      } catch (Throwable var21) {
         throw new RemoteRuntimeException("Unexpected Exception", var21);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

      return var23;
   }

   public final Object instantiate(String var1, Object[] var2, String[] var3) throws ReflectionException, MBeanException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var20;
      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         var20 = (Object)this.ror.invoke((Remote)null, md24, var5, m[24]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (ReflectionException var16) {
         throw var16;
      } catch (MBeanException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

      return var20;
   }

   public final Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var23;
      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         var23 = (Object)this.ror.invoke((Remote)null, md25, var6, m[25]);
      } catch (Error var16) {
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (InstanceNotFoundException var18) {
         throw var18;
      } catch (MBeanException var19) {
         throw var19;
      } catch (ReflectionException var20) {
         throw var20;
      } catch (Throwable var21) {
         throw new RemoteRuntimeException("Unexpected Exception", var21);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

      return var23;
   }

   public final boolean isInstanceOf(ObjectName var1, String var2) throws InstanceNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (Boolean)this.ror.invoke((Remote)null, md26, var4, m[26]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (InstanceNotFoundException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var17;
   }

   public final boolean isRegistered(ObjectName var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md27, var3, m[27]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final Set queryMBeans(ObjectName var1, QueryExp var2) {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Set var15;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var15 = (Set)this.ror.invoke((Remote)null, md28, var4, m[28]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var15;
   }

   public final Set queryNames(ObjectName var1, QueryExp var2) {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Set var15;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var15 = (Set)this.ror.invoke((Remote)null, md29, var4, m[29]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var15;
   }

   public final ObjectInstance registerMBean(Object var1, ObjectName var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ObjectInstance var21;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var21 = (ObjectInstance)this.ror.invoke((Remote)null, md30, var4, m[30]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (InstanceAlreadyExistsException var16) {
         throw var16;
      } catch (MBeanRegistrationException var17) {
         throw var17;
      } catch (NotCompliantMBeanException var18) {
         throw var18;
      } catch (Throwable var19) {
         throw new RemoteRuntimeException("Unexpected Exception", var19);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var21;
   }

   public final void removeNotificationListener(ObjectName var1, NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md31, var4, m[31]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (InstanceNotFoundException var14) {
         throw var14;
      } catch (ListenerNotFoundException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void removeNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         this.ror.invoke((Remote)null, md32, var6, m[32]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (InstanceNotFoundException var16) {
         throw var16;
      } catch (ListenerNotFoundException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void removeNotificationListener(ObjectName var1, ObjectName var2) throws InstanceNotFoundException, ListenerNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md33, var4, m[33]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (InstanceNotFoundException var14) {
         throw var14;
      } catch (ListenerNotFoundException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void removeNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         this.ror.invoke((Remote)null, md34, var6, m[34]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (InstanceNotFoundException var16) {
         throw var16;
      } catch (ListenerNotFoundException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md35, var4, m[35]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (InstanceNotFoundException var17) {
         throw var17;
      } catch (AttributeNotFoundException var18) {
         throw var18;
      } catch (InvalidAttributeValueException var19) {
         throw var19;
      } catch (MBeanException var20) {
         throw var20;
      } catch (ReflectionException var21) {
         throw var21;
      } catch (Throwable var22) {
         throw new RemoteRuntimeException("Unexpected Exception", var22);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      AttributeList var19;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var19 = (AttributeList)this.ror.invoke((Remote)null, md36, var4, m[36]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (ReflectionException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var19;
   }

   public final void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md37, var3, m[37]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (InstanceNotFoundException var13) {
         throw var13;
      } catch (MBeanRegistrationException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }
}
