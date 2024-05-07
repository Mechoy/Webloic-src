package weblogic.deploy.api.spi.status;

import java.util.HashSet;
import java.util.Iterator;
import javax.enterprise.deploy.shared.ActionType;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.shared.StateType;
import weblogic.management.ManagementException;
import weblogic.utils.ErrorCollectionException;

public class DeploymentStatusImpl implements WebLogicDeploymentStatus {
   private StateType state;
   private CommandType command;
   private ActionType action;
   private boolean completed;
   private boolean failed;
   private String message;
   private Throwable error;

   DeploymentStatusImpl() {
      this.state = StateType.RUNNING;
      this.command = null;
      this.action = null;
      this.completed = false;
      this.failed = false;
      this.message = null;
      this.error = null;
   }

   DeploymentStatusImpl(StateType var1, CommandType var2, ActionType var3, String var4) {
      this.state = StateType.RUNNING;
      this.command = null;
      this.action = null;
      this.completed = false;
      this.failed = false;
      this.message = null;
      this.error = null;
      this.setState(var1);
      this.setCommand(var2);
      this.setAction(var3);
      this.setMessage(var4);
   }

   DeploymentStatusImpl(StateType var1, CommandType var2, ActionType var3, String var4, Throwable var5) {
      this(var1, var2, var3, var4);
      this.setError(var5);
   }

   public StateType getState() {
      return this.state;
   }

   public CommandType getCommand() {
      return this.command;
   }

   public ActionType getAction() {
      return this.action;
   }

   public String getMessage() {
      return this.message;
   }

   public boolean isCompleted() {
      if (!this.completed) {
         this.completed = this.state == StateType.COMPLETED;
      }

      return this.completed;
   }

   public boolean isFailed() {
      if (!this.failed) {
         this.failed = this.state == StateType.FAILED;
      }

      return this.failed;
   }

   public boolean isRunning() {
      return !this.isCompleted() && !this.isFailed() && this.state != StateType.RELEASED;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(59);
      var1.append("State Type : " + this.state);
      var1.append("; Command Type : " + this.command);
      var1.append("; Action Type  : " + this.action);
      var1.append("; Completed  : " + this.completed);
      var1.append("; Failed : " + this.failed);
      var1.append("; Message : " + this.message);
      var1.append("; Exception : " + this.error);
      return var1.toString();
   }

   public void setState(StateType var1) {
      this.state = var1;
   }

   public void setCommand(CommandType var1) {
      this.command = var1;
   }

   public void setAction(ActionType var1) {
      this.action = var1;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public void setCompleted(boolean var1) {
      this.completed = var1;
   }

   public void setFailed(boolean var1) {
      this.failed = var1;
   }

   public void setError(Throwable var1) {
      this.error = var1;
   }

   public Iterator getRootException() {
      HashSet var1 = new HashSet();
      if (this.error != null) {
         Throwable var2 = ManagementException.unWrapExceptions(this.error);
         if (var2 instanceof ErrorCollectionException) {
            return ((ErrorCollectionException)var2).getErrors();
         } else {
            var1.add(var2);
            return var1.iterator();
         }
      } else {
         return var1.iterator();
      }
   }
}
