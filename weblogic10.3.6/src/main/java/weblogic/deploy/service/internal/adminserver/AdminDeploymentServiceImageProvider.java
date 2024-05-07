package weblogic.deploy.service.internal.adminserver;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.application.utils.XMLWriter;
import weblogic.deploy.internal.diagnostics.ImageProvider;
import weblogic.deploy.service.FailureDescription;

public class AdminDeploymentServiceImageProvider extends ImageProvider {
   public void writeDiagnosticImage(XMLWriter var1) {
      if (isAdminServer) {
         AdminRequestManager var2 = AdminRequestManager.getInstance();
         Iterator var3 = var2.getRequests().iterator();

         while(var3.hasNext() && !this.timedOut) {
            AdminRequestImpl var4 = (AdminRequestImpl)((Map.Entry)var3.next()).getValue();
            AdminRequestStatus var5 = var4.getStatus();
            var1.addElement("admin-deployment-service-request");
            var1.addElement("id", "" + var5.getId());
            var1.addElement("current-state", var5.getCurrentState().toString());
            this.writeCollection(var1, var5.getTargetedServers(), "targets");
            this.writePrepareDeliveryFailed(var1, var5);
            this.writeRespondPrepareTargets(var1, var5);
            this.writeRestartTargets(var1, var5);
            this.writeCommittedTargets(var1, var5);
            this.writeCommitDeliveryFailed(var1, var5);
            this.writeRespondCommitTargets(var1, var5);
            this.writeCommitFailures(var1, var5);
            this.writeCancelTargets(var1, var5);
            this.writeRespondCancelTargets(var1, var5);
            this.writeCancelFailures(var1, var5);
            this.writePrepareFailure(var1, var5);
            if (var5.timedOut()) {
               var1.addElement("timed-out", "true");
            }

            if (var5.isCancelledByUser()) {
               var1.addElement("cancelled-by", "user / administrator");
            }

            if (var5.isCancelledByClusterConstraints()) {
               var1.addElement("cancelled-by", "cluster-constraints-enabled");
            }

            var1.closeElement();
            var1.flush();
         }

      }
   }

   private void writePrepareDeliveryFailed(XMLWriter var1, AdminRequestStatus var2) {
      Map var3 = var2.getPrepareDeliveryFailureTargets();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.keySet().iterator(), "prepare-failed-to-be-delivered-to");
      }

   }

   private void writeRespondPrepareTargets(XMLWriter var1, AdminRequestStatus var2) {
      Set var3 = var2.getTargetsToRespondToPrepare();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.iterator(), "targets-to-respond-to-prepare");
      }

   }

   private void writeRestartTargets(XMLWriter var1, AdminRequestStatus var2) {
      Set var3 = var2.getTargetsToBeRestarted();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.iterator(), "targets-to-be-restarted");
      }

   }

   private void writeCommittedTargets(XMLWriter var1, AdminRequestStatus var2) {
      Set var3 = var2.getTargetsToBeCommited();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.iterator(), "targets-that-are-to-be-commited");
      }

   }

   private void writeCommitDeliveryFailed(XMLWriter var1, AdminRequestStatus var2) {
      Map var3 = var2.getCommitDeliveryFailureTargets();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.keySet().iterator(), "commit-failed-to-be-delivered-to");
      }

   }

   private void writeRespondCommitTargets(XMLWriter var1, AdminRequestStatus var2) {
      Set var3 = var2.getTargetsToRespondToCommit();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.iterator(), "targets-to-respond-to-commit");
      }

   }

   private void writeCommitFailures(XMLWriter var1, AdminRequestStatus var2) {
      Set var3 = var2.getTargetsToBeCanceled();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.iterator(), "targets-to-be-canceled");
      }

   }

   private void writeCancelTargets(XMLWriter var1, AdminRequestStatus var2) {
      Map var3 = var2.getCancelDeliveryFailureTargets();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.keySet().iterator(), "cancel-failed-to-be-delivered-to");
      }

   }

   private void writeRespondCancelTargets(XMLWriter var1, AdminRequestStatus var2) {
      Set var3 = var2.getTargetsToRespondToCancel();
      if (!var3.isEmpty()) {
         this.writeCollection(var1, var3.iterator(), "targets-to-respond-to-cancel");
      }

   }

   private void writeCancelFailures(XMLWriter var1, AdminRequestStatus var2) {
      Set var3 = var2.getCancelFailureSet();
      if (!var3.isEmpty()) {
         Iterator var4 = var3.iterator();
         StringBuffer var5 = new StringBuffer();

         while(var4.hasNext()) {
            FailureDescription var6 = (FailureDescription)var4.next();
            var5.append(var6.getServer());
            if (var4.hasNext()) {
               var5.append(", ");
            }
         }

         var1.addElement("targets-cancel-failed-on", var5.toString());
      }
   }

   private void writePrepareFailure(XMLWriter var1, AdminRequestStatus var2) {
      Throwable var3 = var2.getPrepareFailure();
      if (var3 != null) {
         var1.addElement("prepare-failure");
         var1.addElement("message", var3.toString());
         var1.addElement("target", var2.getPrepareFailureSource());
      }

   }
}
