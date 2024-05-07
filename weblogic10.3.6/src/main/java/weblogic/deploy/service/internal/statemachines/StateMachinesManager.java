package weblogic.deploy.service.internal.statemachines;

import java.util.ArrayList;
import weblogic.deploy.service.internal.adminserver.AdminRequestImpl;
import weblogic.deploy.service.internal.statemachines.adminserver.AdminServerState;
import weblogic.deploy.service.internal.statemachines.targetserver.TargetServerState;
import weblogic.deploy.service.internal.targetserver.TargetRequestStatus;

public final class StateMachinesManager {
   public static final int SENDING_PREPARE = 0;
   public static final int AWAITING_PREPARE_RESPONSES = 1;
   public static final int SENDING_COMMIT = 2;
   public static final int AWAITING_COMMIT_RESPONSES = 3;
   public static final int SENDING_CANCEL = 4;
   public static final int AWAITING_CANCEL_RESPONSES = 5;
   public static final int RECEIVED_PREPARE = 0;
   public static final int AWAITING_CONTEXT_UPDATE_COMPLETION = 1;
   public static final int AWAITING_PREPARE_COMPLETION = 2;
   public static final int AWAITING_COMMIT = 3;
   public static final int AWAITING_COMMIT_COMPLETION = 4;
   public static final int AWAITING_GET_DEPLOYMENTS_RESPONSE = 5;
   public static final int AWAITING_CANCEL = 6;
   public static final int AWAITING_CANCEL_COMPLETION = 7;
   private static final String[] adminServerStates = new String[]{"weblogic.deploy.service.internal.statemachines.adminserver.SendingPrepare", "weblogic.deploy.service.internal.statemachines.adminserver.AwaitingPrepareResponses", "weblogic.deploy.service.internal.statemachines.adminserver.SendingCommit", "weblogic.deploy.service.internal.statemachines.adminserver.AwaitingCommitResponses", "weblogic.deploy.service.internal.statemachines.adminserver.SendingCancel", "weblogic.deploy.service.internal.statemachines.adminserver.AwaitingCancelResponses"};
   private static final String[] targetServerStates = new String[]{"weblogic.deploy.service.internal.statemachines.targetserver.ReceivedPrepare", "weblogic.deploy.service.internal.statemachines.targetserver.AwaitingContextUpdateCompletion", "weblogic.deploy.service.internal.statemachines.targetserver.AwaitingPrepareCompletion", "weblogic.deploy.service.internal.statemachines.targetserver.AwaitingCommit", "weblogic.deploy.service.internal.statemachines.targetserver.AwaitingCommitCompletion", "weblogic.deploy.service.internal.statemachines.targetserver.AwaitingGetDeploymentsResponse", "weblogic.deploy.service.internal.statemachines.targetserver.AwaitingCancel", "weblogic.deploy.service.internal.statemachines.targetserver.AwaitingCancelCompletion"};
   private static StateMachinesManager singleton = null;

   private StateMachinesManager() {
   }

   public static StateMachinesManager getStateMachinesManager() {
      if (singleton == null) {
         Class var0 = StateMachinesManager.class;
         synchronized(StateMachinesManager.class) {
            singleton = new StateMachinesManager();
         }
      }

      return singleton;
   }

   public static ArrayList createAdminServerStates(AdminRequestImpl var0) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < adminServerStates.length; ++var2) {
         Class var3 = Class.forName(adminServerStates[var2]);
         AdminServerState var4 = (AdminServerState)var3.newInstance();
         var4.initialize(var0);
         var1.add(var4);
      }

      return var1;
   }

   public static ArrayList createTargetServerStates(TargetRequestStatus var0) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < targetServerStates.length; ++var2) {
         Class var3 = Class.forName(targetServerStates[var2]);
         TargetServerState var4 = (TargetServerState)var3.newInstance();
         var4.setDeploymentStatus(var0);
         var1.add(var4);
      }

      return var1;
   }
}
