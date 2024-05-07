package weblogic.management.mbeanservers.compatibility.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.ObjectName;
import javax.transaction.Transaction;
import weblogic.management.MBeanCreationException;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class MBeanHomeImpl_1036_WLStub extends Stub implements StubInfoIntf, MBeanHome {
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
   private static Method[] m;
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md21;
   private final RemoteReference ror;
   // $FF: synthetic field
   private static Class class$weblogic$management$MBeanHome;
   private static RuntimeMethodDescriptor md20;
   private final StubInfo stubinfo;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public MBeanHomeImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
         md17 = new MethodDescriptor(m[17], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[17]), var0.getRemoteRef().getObjectID());
         md18 = new MethodDescriptor(m[18], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[18]), var0.getRemoteRef().getObjectID());
         md19 = new MethodDescriptor(m[19], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[19]), var0.getRemoteRef().getObjectID());
         md20 = new MethodDescriptor(m[20], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[20]), var0.getRemoteRef().getObjectID());
         md21 = new MethodDescriptor(m[21], class$weblogic$management$MBeanHome == null ? (class$weblogic$management$MBeanHome = class$("weblogic.management.MBeanHome")) : class$weblogic$management$MBeanHome, false, false, false, false, var0.getTimeOut(m[21]), var0.getRemoteRef().getObjectID());
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

   public final void addManagedHome(MBeanHome var1, String var2, String var3) {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         this.ror.invoke((Remote)null, md0, var5, m[0]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

   }

   public final WebLogicMBean createAdminMBean(String var1, String var2) throws MBeanCreationException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (WebLogicMBean)this.ror.invoke((Remote)null, md1, var4, m[1]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (MBeanCreationException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var17;
   }

   public final WebLogicMBean createAdminMBean(String var1, String var2, String var3) throws MBeanCreationException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var18;
      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         var18 = (WebLogicMBean)this.ror.invoke((Remote)null, md2, var5, m[2]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (MBeanCreationException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

      return var18;
   }

   public final WebLogicMBean createAdminMBean(String var1, String var2, String var3, ConfigurationMBean var4) throws MBeanCreationException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var19;
      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         var19 = (WebLogicMBean)this.ror.invoke((Remote)null, md3, var6, m[3]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (MBeanCreationException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

      return var19;
   }

   public final void deleteMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md4, var3, m[4]);
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

   public final void deleteMBean(WebLogicMBean var1) throws InstanceNotFoundException, MBeanRegistrationException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md5, var3, m[5]);
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

   public final DomainMBean getActiveDomain() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      DomainMBean var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (DomainMBean)this.ror.invoke((Remote)null, md6, var2, m[6]);
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

   public final ConfigurationMBean getAdminMBean(String var1, String var2) throws InstanceNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ConfigurationMBean var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (ConfigurationMBean)this.ror.invoke((Remote)null, md7, var4, m[7]);
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

   public final ConfigurationMBean getAdminMBean(String var1, String var2, String var3) throws InstanceNotFoundException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ConfigurationMBean var18;
      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         var18 = (ConfigurationMBean)this.ror.invoke((Remote)null, md8, var5, m[8]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

      return var18;
   }

   public final Set getAllMBeans() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Set var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (Set)this.ror.invoke((Remote)null, md9, var2, m[9]);
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

   public final Set getAllMBeans(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Set var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Set)this.ror.invoke((Remote)null, md10, var3, m[10]);
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

   public final ConfigurationMBean getConfigurationMBean(String var1, String var2) throws InstanceNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ConfigurationMBean var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (ConfigurationMBean)this.ror.invoke((Remote)null, md11, var4, m[11]);
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

   public final String getDomainName() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (String)this.ror.invoke((Remote)null, md12, var2, m[12]);
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

   public final WebLogicMBean getMBean(String var1, Class var2) throws InstanceNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (WebLogicMBean)this.ror.invoke((Remote)null, md13, var4, m[13]);
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

   public final WebLogicMBean getMBean(String var1, String var2) throws InstanceNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (WebLogicMBean)this.ror.invoke((Remote)null, md14, var4, m[14]);
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

   public final WebLogicMBean getMBean(String var1, String var2, String var3) throws InstanceNotFoundException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var18;
      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         var18 = (WebLogicMBean)this.ror.invoke((Remote)null, md15, var5, m[15]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (InstanceNotFoundException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

      return var18;
   }

   public final WebLogicMBean getMBean(String var1, String var2, String var3, String var4) throws InstanceNotFoundException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var19;
      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         var19 = (WebLogicMBean)this.ror.invoke((Remote)null, md16, var6, m[16]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (InstanceNotFoundException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

      return var19;
   }

   public final WebLogicMBean getMBean(ObjectName var1) throws InstanceNotFoundException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      WebLogicMBean var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (WebLogicMBean)this.ror.invoke((Remote)null, md17, var3, m[17]);
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

   public final RemoteMBeanServer getMBeanServer() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      RemoteMBeanServer var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (RemoteMBeanServer)this.ror.invoke((Remote)null, md18, var2, m[18]);
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

   public final Set getMBeansByType(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Set var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Set)this.ror.invoke((Remote)null, md19, var3, m[19]);
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

   public final Object getProxy(ObjectName var1) throws InstanceNotFoundException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Object)this.ror.invoke((Remote)null, md20, var3, m[20]);
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

   public final RuntimeMBean getRuntimeMBean(String var1, String var2) throws InstanceNotFoundException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      RuntimeMBean var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (RuntimeMBean)this.ror.invoke((Remote)null, md21, var4, m[21]);
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
}
