package weblogic.application.internal;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationContext;
import weblogic.application.Deployment;
import weblogic.application.DeploymentWrapper;
import weblogic.application.utils.EarUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.container.DeploymentContext;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.utils.StringUtils;

public class DeploymentStateChecker implements Deployment, DeploymentWrapper {
   public static final int STATE_NEW = 0;
   public static final int STATE_PREPARED = 1;
   public static final int STATE_ADMIN = 2;
   public static final int STATE_ACTIVE = 3;
   public static final int STATE_PENDING_UPDATE = 4;
   protected final Deployment delegate;
   private final List callbacks = new ArrayList();
   private int state = 0;
   private int preUpdateState;
   private int pendingUpdates = 0;

   public DeploymentStateChecker(Deployment var1) {
      this.delegate = var1;
   }

   public int getState() {
      return this.state;
   }

   public Deployment getDeployment() {
      return this.delegate;
   }

   public static String state2String(int var0) {
      switch (var0) {
         case 0:
            return "STATE_NEW";
         case 1:
            return "STATE_PREPARED";
         case 2:
            return "STATE_ADMIN";
         case 3:
            return "STATE_ACTIVE";
         case 4:
            return "STATE_PENDING_UPDATE";
         default:
            throw new AssertionError("unexpected state: " + var0);
      }
   }

   private void throwAssertion(String var1) {
      DeploymentAssertionError var2 = null;
      if (Debug.isDeploymentDebugEnabled()) {
         var2 = new DeploymentAssertionError("\n\n\n*********   YOU HAVE ENCOUNTERED A DEPLOYMENT BUG *********\n\n" + var1 + "\n\n\n", this.callbacks);
         var2.printStackTrace();
      } else {
         var2 = new DeploymentAssertionError(var1, this.callbacks);
      }

      Loggable var3 = DeployerRuntimeLogger.logInternalErrorLoggable(var2);
      var3.log();
   }

   private void assertState(int var1, int var2) {
      if (this.state != var1 && this.state != var2) {
         this.throwAssertion("Unexpected current state for application " + this.getApplicationContext().getApplicationId() + " " + state2String(this.state) + ".  We expected us to be in " + state2String(var1) + " or " + state2String(var2));
      }

   }

   private void illegal(int var1, int var2) {
      this.throwAssertion("Unexpected transition: current state for application " + this.getApplicationContext().getApplicationId() + " : " + state2String(var1) + " attempt to transition to " + state2String(var2));
   }

   private void up(int var1) {
      if (var1 - this.state == 1) {
         if (EarUtils.isDebugOn()) {
            EarUtils.debug("transitioned from " + state2String(this.state) + " to " + state2String(var1) + " " + this.delegate.getApplicationContext().getApplicationId());
         }

         this.state = var1;
      } else {
         this.illegal(this.state, var1);
      }

   }

   private void down(int var1, boolean var2) {
      if (this.state - var1 == 1) {
         if (EarUtils.isDebugOn()) {
            EarUtils.debug("transition from " + state2String(this.state) + " to " + state2String(var1) + " " + this.delegate.getApplicationContext().getApplicationId());
         }

         this.state = var1;
      } else {
         if (EarUtils.isDebugOn()) {
            EarUtils.debug("Illegal state transition: " + state2String(this.state) + " -> " + state2String(var1));
         }

         if (!var2 || var1 != 2 || this.state != 2) {
            this.illegal(this.state, var1);
         }
      }

   }

   private void save(String var1) {
      this.callbacks.add(new Exception(var1));
   }

   public void prepare(DeploymentContext var1) throws DeploymentException {
      this.save("prepare");
      this.delegate.prepare(var1);
      this.up(1);
   }

   public void activate(DeploymentContext var1) throws DeploymentException {
      this.save("activate");
      this.delegate.activate(var1);
      this.up(2);
   }

   public void adminToProduction(DeploymentContext var1) throws DeploymentException {
      this.save("adminToProduction");
      this.delegate.adminToProduction(var1);
      this.up(3);
   }

   public void gracefulProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      this.save("gracefulProductionToAdmin");
      this.down(2, true);
      this.delegate.gracefulProductionToAdmin(var1);
   }

   public void forceProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      this.save("forceProductionToAdmin");
      if (this.state != 2) {
         this.down(2, true);
      }

      this.delegate.forceProductionToAdmin(var1);
   }

   public void deactivate(DeploymentContext var1) throws DeploymentException {
      this.save("deactivate");
      this.down(1, false);
      this.delegate.deactivate(var1);
   }

   public void unprepare(DeploymentContext var1) throws DeploymentException {
      this.save("unprepare");
      this.down(0, false);
      this.delegate.unprepare(var1);
   }

   public void remove(DeploymentContext var1) throws DeploymentException {
      this.save("remove");
      if (this.state != 0) {
         this.illegal(this.state, 0);
      }

      this.delegate.remove(var1);
   }

   public void prepareUpdate(DeploymentContext var1) throws DeploymentException {
      this.save("prepareUpdate uris: " + this.getUrisAsString(var1));
      this.delegate.prepareUpdate(var1);
      if (this.state != 2 && this.state != 3) {
         if (this.state == 4) {
            ++this.pendingUpdates;
         } else {
            this.illegal(this.state, 4);
         }
      } else {
         this.preUpdateState = this.state;
         this.pendingUpdates = 1;
         this.state = 4;
      }

   }

   public void rollbackUpdate(DeploymentContext var1) {
      String[] var2 = null;
      if (var1 != null) {
         var2 = var1.getUpdatedResourceURIs();
      }

      this.save("rollbackUpdate uris: " + this.getUrisAsString(var1));
      if (this.state != 4) {
         this.illegal(this.state, 3);
      }

      --this.pendingUpdates;
      if (this.pendingUpdates == 0) {
         this.state = this.preUpdateState;
      }

      this.delegate.rollbackUpdate(var1);
   }

   public void activateUpdate(DeploymentContext var1) throws DeploymentException {
      this.save("activateUpdate uris: " + this.getUrisAsString(var1));
      if (this.state != 4) {
         this.illegal(this.state, 3);
      }

      --this.pendingUpdates;
      if (this.pendingUpdates == 0) {
         this.state = this.preUpdateState;
      }

      this.delegate.activateUpdate(var1);
   }

   public void stop(DeploymentContext var1) throws DeploymentException {
      this.save("stop");
      this.assertState(2, 3);
      this.delegate.stop(var1);
   }

   public void start(DeploymentContext var1) throws DeploymentException {
      this.save("start");
      this.assertState(2, 3);
      this.delegate.start(var1);
   }

   public ApplicationContext getApplicationContext() {
      return this.delegate.getApplicationContext();
   }

   private String getUrisAsString(DeploymentContext var1) {
      String[] var2 = null;
      if (var1 != null) {
         var2 = var1.getUpdatedResourceURIs();
      }

      return var2 == null ? "" : StringUtils.join(var2, ",");
   }

   private static class DeploymentAssertionError extends AssertionError {
      private final List callbacks;

      DeploymentAssertionError(String var1, List var2) {
         super(var1);
         this.callbacks = var2;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         StringWriter var2 = new StringWriter();
         PrintWriter var3 = new PrintWriter(var2);
         this.printCallbacks(var3);
         var3.flush();
         var1.append(var2.toString());
         return var1.toString();
      }

      public void printStackTrace(PrintStream var1) {
         super.printStackTrace(var1);
         this.printStackTrace(new PrintWriter(new OutputStreamWriter(var1)));
      }

      public void printStackTrace(PrintWriter var1) {
         super.printStackTrace(var1);
         this.printCallbacks(var1);
      }

      private void printCallbacks(PrintWriter var1) {
         var1.println("\n\nDumping " + this.callbacks.size() + " callbacks");
         var1.println("----------------------   BEGIN CALLBACK DUMP -------");
         Iterator var2 = this.callbacks.iterator();

         while(var2.hasNext()) {
            var1.println("\n");
            ((Exception)var2.next()).printStackTrace(var1);
            var1.println("\n");
         }

         var1.println("----------------------   END CALLBACK DUMP -------");
      }
   }
}
