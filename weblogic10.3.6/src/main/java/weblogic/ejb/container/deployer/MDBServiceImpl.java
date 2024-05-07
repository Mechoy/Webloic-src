package weblogic.ejb.container.deployer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.internal.MDConnectionManager;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceFailureException;

public final class MDBServiceImpl extends ActivatedService implements MDBService {
   private HashSet deployedMDBs = null;
   private static Set mdbStarters = new HashSet();
   private static boolean started = false;
   private static final int MDB_SUSPEND = 1;
   private static final int MDB_SIGNAL_THREAD_EXIT = 2;
   private static final int MDB_SHUTDOWN = 3;
   private static final int MDB_RESUME = 4;
   private boolean shutdown;

   private void MDBServiceImpl() {
   }

   public synchronized void haltService() throws ServiceFailureException {
      if (!this.shutdown) {
         if (this.deployedMDBs != null) {
            EJBLogger.logMDBsBeingSuspended();
            this.iterateOnMDBs(2);
            this.iterateOnMDBs(1);
            EJBLogger.logMDBsDoneSuspending();
         }

         this.shutdown = true;
      }
   }

   public synchronized void stopService() throws ServiceFailureException {
      this.haltService();
   }

   public synchronized boolean startService() throws ServiceFailureException {
      this.iterateOnMDBs(4);
      synchronized(mdbStarters) {
         Iterator var2 = mdbStarters.iterator();

         while(var2.hasNext()) {
            EJBDeployer var3 = (EJBDeployer)var2.next();

            try {
               ApplicationVersionUtils.setCurrentAdminMode(true);

               try {
                  var3.deployMessageDrivenBeansUsingModuleCL();
               } catch (RuntimeException var11) {
                  throw var11;
               } catch (Exception var12) {
                  EJBLogger.logErrorStartingMDB(var12);
               }
            } finally {
               ApplicationVersionUtils.setCurrentAdminMode(false);
            }
         }

         mdbStarters.clear();
         started = true;
      }

      this.shutdown = false;
      return started;
   }

   private void iterateOnMDBs(int var1) throws ServiceFailureException {
      if (this.deployedMDBs != null) {
         Iterator var2 = this.deployedMDBs.iterator();

         while(var2.hasNext()) {
            MDConnectionManager var3 = (MDConnectionManager)var2.next();
            if (var3 != null) {
               switch (var1) {
                  case 1:
                     var3.suspend(true);
                     break;
                  case 2:
                     var3.signalBackgroundThreads();
                     break;
                  case 3:
                     var3.shutdown();
                     break;
                  case 4:
                     var3.resume(true);
               }
            }
         }
      }

   }

   public synchronized void addDeployedMDB(MDConnectionManager var1) {
      if (this.deployedMDBs == null) {
         this.deployedMDBs = new HashSet();
      }

      this.deployedMDBs.add(var1);
   }

   public synchronized void removeDeployedMDB(MDConnectionManager var1) {
      if (this.deployedMDBs != null) {
         this.deployedMDBs.remove(var1);
      }

   }

   public boolean addMDBStarter(EJBDeployer var1) {
      synchronized(mdbStarters) {
         if (!started) {
            mdbStarters.add(var1);
            return true;
         } else {
            return false;
         }
      }
   }
}
