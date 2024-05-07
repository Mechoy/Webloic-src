package weblogic.deploy.service.internal.targetserver;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TimeAuditorManager {
   private static final boolean AUDITOR_ENABLED = Boolean.getBoolean("weblogic.deployment.TimeAuditorEnabled");
   public static final int DEPLOYMENT_CONTEXT = 0;
   public static final int PREPARE = 1;
   public static final int COMMIT = 2;
   public static final int CANCEL = 3;
   private Map auditors = Collections.synchronizedMap(new HashMap());

   public static TimeAuditorManager getInstance() {
      return TimeAuditorManager.Maker.SINGLETON;
   }

   public void startAuditor(long var1) {
      if (AUDITOR_ENABLED) {
         this.auditors.put(var1, new RequestAuditor(var1));
      }

   }

   public void printAuditor(long var1, OutputStream var3) {
      this.printAuditor(var1, new PrintWriter(var3, true));
   }

   public void printAuditor(long var1, PrintStream var3) {
      RequestAuditor var4 = (RequestAuditor)this.auditors.get(var1);
      if (var4 != null) {
         var3.println("<" + new Date() + "> " + var4);
      }

   }

   public void printAuditor(long var1, PrintWriter var3) {
      RequestAuditor var4 = (RequestAuditor)this.auditors.get(var1);
      if (var4 != null) {
         var3.println("<" + new Date() + "> " + var4);
         var3.flush();
      }

   }

   public String getAuditorAsString(long var1) {
      RequestAuditor var3 = (RequestAuditor)this.auditors.get(var1);
      return var3 != null ? var3.toString() : null;
   }

   public Object endAuditor(long var1) {
      return this.auditors.remove(var1);
   }

   public void startTransition(long var1, int var3) {
      RequestAuditor var4 = (RequestAuditor)this.auditors.get(var1);
      if (var4 != null) {
         var4.startTransition(var3);
      }

   }

   public void endTransition(long var1, int var3) {
      RequestAuditor var4 = (RequestAuditor)this.auditors.get(var1);
      if (var4 != null) {
         var4.endTransition(var3);
      }

   }

   public void startDeploymentTransition(long var1, String var3, int var4) {
      RequestAuditor var5 = (RequestAuditor)this.auditors.get(var1);
      if (var5 != null) {
         var5.startDeploymentTransition(var3, var4);
      }

   }

   public void endDeploymentTransition(long var1, String var3, int var4) {
      RequestAuditor var5 = (RequestAuditor)this.auditors.get(var1);
      if (var5 != null) {
         var5.endDeploymentTransition(var3, var4);
      }

   }

   private static String transitionToString(int var0) {
      switch (var0) {
         case 0:
            return "DEPLOYMENT_CONTEXT";
         case 1:
            return "PREPARE";
         case 2:
            return "COMMIT";
         case 3:
            return "CANCEL";
         default:
            throw new IllegalArgumentException("Transition type '" + var0 + "' is invalid");
      }
   }

   private class RequestAuditor {
      private final List inspectors = Arrays.asList(TimeAuditorManager.this.new TargetTransitionInspector(0), TimeAuditorManager.this.new TargetTransitionInspector(1), TimeAuditorManager.this.new TargetTransitionInspector(2), TimeAuditorManager.this.new TargetTransitionInspector(3));
      private final long requestId;

      RequestAuditor(long var2) {
         this.requestId = var2;
      }

      void startTransition(int var1) {
         TargetTransitionInspector var2 = this.getInspector(var1);
         var2.setBeginTime(System.currentTimeMillis());
      }

      void endTransition(int var1) {
         TargetTransitionInspector var2 = this.getInspector(var1);
         var2.setEndTime(System.currentTimeMillis());
      }

      void startDeploymentTransition(String var1, int var2) {
         TargetTransitionInspector var3 = this.getInspector(var2);
         var3.startDeploymentTransition(var1);
      }

      void endDeploymentTransition(String var1, int var2) {
         TargetTransitionInspector var3 = this.getInspector(var2);
         var3.endDeploymentTransition(var1);
      }

      private TargetTransitionInspector getInspector(int var1) {
         Iterator var2 = this.inspectors.iterator();

         TargetTransitionInspector var3;
         do {
            if (!var2.hasNext()) {
               throw new IllegalArgumentException("Transition type '" + var1 + "' is invalid");
            }

            var3 = (TargetTransitionInspector)var2.next();
         } while(var3.getTransitionType() != var1);

         return var3;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Target - Time calculations for requestId '").append(this.requestId);
         var1.append("' are [").append("\n");
         Iterator var2 = this.inspectors.iterator();

         while(var2.hasNext()) {
            var1.append("\t").append(var2.next()).append("\n");
         }

         var1.append("]").append("\n");
         return var1.toString();
      }
   }

   private class TargetTransitionInspector extends TransitionInspector {
      private Map deploymentTransitions = new HashMap();

      TargetTransitionInspector(int var2) {
         super(var2);
      }

      void startDeploymentTransition(String var1) {
         Object var2 = (TransitionInspector)this.deploymentTransitions.get(var1);
         if (var2 == null) {
            var2 = TimeAuditorManager.this.new DeploymentTransitionInspector(var1, this.getTransitionType());
            this.deploymentTransitions.put(var1, var2);
         }

         ((TransitionInspector)var2).setBeginTime(System.currentTimeMillis());
      }

      void endDeploymentTransition(String var1) {
         TransitionInspector var2 = (TransitionInspector)this.deploymentTransitions.get(var1);
         if (var2 != null) {
            var2.setEndTime(System.currentTimeMillis());
         }

      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString()).append(" - {").append("\n");
         Iterator var2 = this.deploymentTransitions.values().iterator();

         while(var2.hasNext()) {
            var1.append(var2.next());
         }

         var1.append("\t").append("}");
         return var1.toString();
      }
   }

   private class DeploymentTransitionInspector extends TransitionInspector {
      private final String deploymentType;

      DeploymentTransitionInspector(String var2, int var3) {
         super(var3);
         this.deploymentType = var2;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("\t\t").append(this.deploymentType).append("=");
         var1.append(this.timeSpent()).append("\n");
         return var1.toString();
      }
   }

   private class TransitionInspector {
      private final int transition;
      private long beginTime = 0L;
      private long endTime = 0L;

      TransitionInspector(int var2) {
         this.transition = var2;
      }

      int getTransitionType() {
         return this.transition;
      }

      long getBeginTime() {
         return this.beginTime;
      }

      long getEndTime() {
         return this.endTime;
      }

      void setBeginTime(long var1) {
         this.beginTime = var1;
      }

      void setEndTime(long var1) {
         this.endTime = var1;
      }

      long timeSpent() {
         return this.endTime - this.beginTime;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Transition '").append(TimeAuditorManager.transitionToString(this.transition));
         var1.append("'").append(" took : ").append(this.timeSpent());
         return var1.toString();
      }
   }

   private static class Maker {
      private static TimeAuditorManager SINGLETON = new TimeAuditorManager();
   }
}
