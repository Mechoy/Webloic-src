package weblogic.t3.srvr;

import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.version;
import weblogic.common.AdminServicesDef;
import weblogic.common.LogServicesDef;
import weblogic.common.NameServicesDef;
import weblogic.common.T3Client;
import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.common.internal.LogOutputStream;
import weblogic.io.common.IOServicesDef;
import weblogic.io.common.internal.IOServicesServerImpl;
import weblogic.jdbc.common.JdbcServicesDef;
import weblogic.jndi.Environment;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.provider.ManagementService;
import weblogic.platform.VM;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifecycleException;
import weblogic.time.common.Schedulable;
import weblogic.time.common.ScheduledTriggerDef;
import weblogic.time.common.Scheduler;
import weblogic.time.common.TimeServicesDef;
import weblogic.time.common.TimeTriggerException;
import weblogic.time.common.Trigger;
import weblogic.time.common.Triggerable;
import weblogic.time.common.internal.TimeEventGenerator;
import weblogic.time.server.ScheduledTrigger;
import weblogic.utils.AssertionError;

public class T3ServerServices implements T3ServicesDef, AdminServicesDef, JdbcServicesDef, TimeServicesDef, NameServicesDef {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private IOServicesDef ioSvc;
   LogOutputStream los = new LogOutputStream("T3Services");

   public AdminServicesDef admin() {
      return this;
   }

   /** @deprecated */
   public JdbcServicesDef jdbc() {
      return null;
   }

   public LogServicesDef log() {
      return this.los;
   }

   public NameServicesDef name() {
      return this;
   }

   public IOServicesDef io() {
      if (this.ioSvc != null) {
         return this.ioSvc;
      } else {
         try {
            return this.ioSvc = new IOServicesServerImpl(this);
         } catch (T3Exception var2) {
            throw new AssertionError("Failed to instantiate IOServicesImpl: " + var2);
         }
      }
   }

   public TimeServicesDef time() {
      return this;
   }

   public String ping(byte[] var1) {
      return "OK";
   }

   public String shut(int var1) throws ServerLifecycleException {
      return this.shut((String)null, var1);
   }

   public String shut() throws ServerLifecycleException {
      return this.shut((String)null, -1);
   }

   public String shut(String var1, int var2) throws ServerLifecycleException {
      if (var2 >= 0) {
         T3Srvr.getT3Srvr().setShutdownWaitSecs(var2);
      }

      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().shutdown();
      T3SrvrTextTextFormatter var3 = new T3SrvrTextTextFormatter();
      return var3.getServerShutdownSuccessfully(ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getName());
   }

   public String cancelShut() {
      return T3Srvr.getT3Srvr().cancelShutdown();
   }

   public String version() {
      return version.getVersions();
   }

   public String lockServer(String var1) {
      return T3Srvr.getT3Srvr().getLockoutManager().lockServer(var1);
   }

   public String unlockServer() {
      return T3Srvr.getT3Srvr().getLockoutManager().unlockServer();
   }

   public void enableWatchDog(int var1) {
      T3SrvrLogger.logEnableWatchDogNotPermitted();
   }

   public void disableWatchDog() {
      T3SrvrLogger.logDisableWatchDogNotPermitted();
   }

   public void threadDump() {
      VM.getVM().threadDump();
   }

   public ScheduledTriggerDef getScheduledTrigger(Schedulable var1, Triggerable var2) {
      ScheduledTrigger var3 = new ScheduledTrigger(var1, var2, TimeEventGenerator.getOne());
      return var3;
   }

   public ScheduledTriggerDef getScheduledTrigger(Scheduler var1, Trigger var2) throws TimeTriggerException {
      var1.private_initialize(this);
      if (var2.theObject() == null && var2.className().equals(var1.className())) {
         var2.private_set_instance((Triggerable)var1.theObject());
      }

      var2.private_initialize(this);
      return this.getScheduledTrigger((Schedulable)var1.theObject(), (Triggerable)var2.theObject());
   }

   public long currentTimeMillis() throws T3Exception {
      return System.currentTimeMillis();
   }

   public int getRoundTripDelayMillis() throws T3Exception {
      return 0;
   }

   public int getLocalClockOffsetMillis() throws T3Exception {
      return 0;
   }

   /** @deprecated */
   public void resetPool(String var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   public void shrinkPool(String var1) {
      throw new UnsupportedOperationException();
   }

   public Context getInitialContext() throws NamingException {
      return this.getInitialContext(new Hashtable());
   }

   public Context getInitialContext(Hashtable var1) throws NamingException {
      return (new Environment(var1)).getInitialContext();
   }

   /** @deprecated */
   public void private_setT3Client(T3Client var1) {
   }
}
