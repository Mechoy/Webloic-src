package weblogic.ejb.container.swap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.NoSuchEJBException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.SessionEJBContextImpl;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.AssertionError;
import weblogic.utils.FileUtils;
import weblogic.utils.collections.NumericValueHashtable;

public final class DiskSwap implements EJBSwap, TimerListener {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   protected static final DebugLogger debugLogger;
   private File dir;
   private long idleTimeoutMS;
   private long sessionTimeoutMS;
   private EJBReplacer replacer;
   private BeanManager beanManager;
   private PassivationUtils passivationUtils;
   private Timer timer;
   private NumericValueHashtable fileToOIDTable = new NumericValueHashtable();

   public DiskSwap(File var1, long var2, long var4) {
      this.dir = var1;
      if (debugLogger.isDebugEnabled()) {
         debug("idleTimeoutMS-" + var2);
         debug("sessionTimeoutMS-" + var4);
      }

      this.idleTimeoutMS = var2;
      this.sessionTimeoutMS = var4;
      if (this.sessionTimeoutMS < this.idleTimeoutMS) {
         this.sessionTimeoutMS = var2;
      }

      if (!this.dir.exists()) {
         boolean var6 = this.dir.mkdirs();
         if (!var6) {
            throw new RuntimeException("Failed to create Stateful Session Persistence Directory: " + this.dir.getAbsolutePath());
         }
      } else {
         FileUtils.remove(this.dir, FileUtils.STAR);
      }

   }

   public void timerExpired(Timer var1) {
      File[] var2 = this.dir.listFiles();
      long var3 = System.currentTimeMillis();
      if (var2 != null) {
         for(int var5 = 0; var5 < var2.length; ++var5) {
            File var6 = var2[var5];
            if (Math.abs(var6.lastModified() - var3) > this.sessionTimeoutMS) {
               if (debugLogger.isDebugEnabled()) {
                  debug("timerExpired: removing file from swap-" + var6.getName());
               }

               var6.delete();
               int var7 = (int)this.fileToOIDTable.remove(var6);
               if (var7 != 0) {
                  try {
                     ServerHelper.unexportObject(var7);
                  } catch (NoSuchObjectException var9) {
                  }
               }
            }
         }
      }

   }

   private String keyToFileName(Object var1) {
      return var1.toString() + ".db";
   }

   public void setup(BeanInfo var1, BeanManager var2, ClassLoader var3) {
      this.replacer = new EJBReplacer();
      this.passivationUtils = new PassivationUtils(var3);
      this.beanManager = var2;
      this.startIdleTimeout(this.idleTimeoutMS);
   }

   private void startIdleTimeout(long var1) {
      if (this.idleTimeoutMS > 0L) {
         TimerManagerFactory var3 = TimerManagerFactory.getTimerManagerFactory();
         TimerManager var4 = var3.getDefaultTimerManager();
         this.timer = var4.scheduleAtFixedRate(this, this.idleTimeoutMS, var1);
      }

   }

   private void stopIdleTimeout() {
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }

   }

   public void cancelTrigger() {
      this.stopIdleTimeout();
      if (this.dir.exists()) {
         FileUtils.remove(this.dir);
      }

   }

   public void remove(Object var1) {
      File var2 = new File(this.dir, this.keyToFileName(var1));
      if (debugLogger.isDebugEnabled()) {
         debug("removing file from swap-" + var2.getName());
      }

      var2.delete();
      this.fileToOIDTable.remove(var2);
   }

   public EnterpriseBean read(Object var1) throws InternalException {
      String var2 = this.keyToFileName(var1);
      File var3 = new File(this.dir, var2);
      FileInputStream var4 = null;
      if (debugLogger.isDebugEnabled()) {
         debug("reading file from swap-" + var3.getName());
      }

      EnterpriseBean var17;
      try {
         var4 = new FileInputStream(var3);
         EnterpriseBean var5 = this.passivationUtils.read(this.beanManager, var4, var1);
         var17 = var5;
      } catch (IOException var15) {
         NoSuchEJBException var6 = new NoSuchEJBException("Bean has been deleted.");
         if (debugLogger.isDebugEnabled()) {
            debug("key not found in swap-" + var1);
            EJBLogger.logStackTraceAndMessage(var15.getMessage(), var15);
         }

         EJBRuntimeUtils.throwInternalException("Error during read.", var6);
         throw new AssertionError("Should not reach.");
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var14) {
         }

         var3.delete();
         this.fileToOIDTable.remove(var3);
      }

      return var17;
   }

   public void write(Object var1, Object var2) throws InternalException {
      String var3 = this.keyToFileName(var1);
      FileOutputStream var4 = null;
      if (debugLogger.isDebugEnabled()) {
         debug("writing file to swap-" + var3);
      }

      try {
         File var5 = new File(this.dir, var3);
         var4 = new FileOutputStream(var5);
         this.passivationUtils.write(this.beanManager, var4, var1, var2);
         this.addToFileToOidTable(var5, var2);
      } catch (FileNotFoundException var14) {
         EJBRuntimeUtils.throwInternalException("Error in write.", var14);
         throw new AssertionError("Should not reach.");
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var13) {
         }

      }

   }

   private void addToFileToOidTable(File var1, Object var2) {
      boolean var3 = false;

      try {
         WLEnterpriseBean var4 = (WLEnterpriseBean)var2;
         int var5 = var4.__WL_getMethodState();
         var4.__WL_setMethodState(128);
         EJBObject var6 = null;

         label68: {
            try {
               var6 = ((SessionEJBContextImpl)var4.__WL_getEJBContext()).getEJBObject();
               break label68;
            } catch (IllegalStateException var13) {
            } finally {
               var4.__WL_setMethodState(var5);
            }

            return;
         }

         if (var6 != null && ((StatefulSessionManager)this.beanManager).isInMemoryReplication() && !(var6 instanceof Activatable)) {
            int var16 = ServerHelper.getObjectId(var6);
            this.fileToOIDTable.put(var1, (long)var16);
         }
      } catch (NoSuchObjectException var15) {
      }

   }

   public void updateClassLoader(ClassLoader var1) {
      this.passivationUtils.updateClassLoader(var1);
   }

   public void updateIdleTimeoutMS(long var1) {
      long var3 = 0L;
      if (this.timer != null) {
         long var5 = this.timer.getTimeout();
         this.stopIdleTimeout();
         var3 = var5 - System.currentTimeMillis();
         if (var3 < 0L) {
            var3 = 0L;
         }

         if (var1 < var3) {
            var3 = var1;
         }
      } else {
         var3 = var1;
      }

      this.idleTimeoutMS = var1;
      if (this.sessionTimeoutMS < this.idleTimeoutMS) {
         this.sessionTimeoutMS = this.idleTimeoutMS;
      }

      this.startIdleTimeout(var3);
   }

   private static void debug(String var0) {
      debugLogger.debug("[DiskSwap] " + var0);
   }

   static {
      debugLogger = EJBDebugService.swappingLogger;
   }
}
