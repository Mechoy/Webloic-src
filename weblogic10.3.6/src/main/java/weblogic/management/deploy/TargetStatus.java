package weblogic.management.deploy;

import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

/** @deprecated */
public class TargetStatus implements Serializable {
   private static final long serialVersionUID = -8426317304673733688L;
   private int state = 0;
   private int type;
   private ArrayList messages = new ArrayList();
   private String targetName;
   private transient boolean targetListEmpty = false;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final int STATE_INIT = 0;
   public static final int STATE_IN_PROGRESS = 1;
   public static final int STATE_FAILED = 2;
   public static final int STATE_SUCCESS = 3;
   /** @deprecated */
   public static final int STATE_UNAVAILABLE = 4;
   public static final int TYPE_UNKNOWN = 0;
   public static final int TYPE_SERVER = 1;
   public static final int TYPE_CLUSTER = 2;
   public static final int TYPE_JMS_SERVER = 3;
   public static final int TYPE_VIRTUAL_HOST = 4;
   public static final int TYPE_SAF_AGENT = 5;

   public TargetStatus(String var1) {
      this.targetName = var1;
      this.setTargetType();
   }

   private TargetStatus() {
   }

   public int getState() {
      return this.state;
   }

   public synchronized void setState(int var1) {
      boolean var2 = false;
      switch (this.state) {
         case 0:
            if (var1 != 1) {
               var2 = true;
            }
            break;
         case 1:
         case 3:
            if (var1 != 2 && var1 != 3 && var1 != 4) {
               var2 = true;
            }
            break;
         case 2:
            var2 = true;
            break;
         case 4:
            if (var1 != 2) {
               return;
            }
      }

      if (!var2) {
         this.state = var1;
      }
   }

   public Exception[] getMessages() {
      return (Exception[])((Exception[])this.messages.toArray(new Exception[0]));
   }

   public synchronized void addMessage(Exception var1) {
      this.messages.add(var1);
   }

   public String getTarget() {
      return this.targetName;
   }

   public int getType() {
      return this.type;
   }

   public int getTargetType() {
      return this.getType();
   }

   private void setTargetType() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (var1.lookupServer(this.getTarget()) != null) {
         this.type = 1;
      } else if (var1.lookupCluster(this.getTarget()) != null) {
         this.type = 2;
      } else if (var1.lookupJMSServer(this.getTarget()) != null) {
         this.type = 3;
      } else if (var1.lookupSAFAgent(this.getTarget()) != null) {
         this.type = 5;
      } else if (var1.lookupVirtualHost(this.getTarget()) != null) {
         this.type = 4;
      } else {
         this.type = 0;
      }

   }

   protected void setTargetListEmpty(boolean var1) {
      this.targetListEmpty = var1;
   }

   protected boolean isTargetListEmpty() {
      return this.targetListEmpty;
   }

   final synchronized TargetStatus copy() {
      TargetStatus var1 = new TargetStatus();
      var1.messages = (ArrayList)this.messages.clone();
      var1.state = this.state;
      var1.type = this.type;
      var1.targetName = this.targetName;
      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("(");
      var1.append("  Name: ");
      var1.append(this.getTarget());
      var1.append('\n');
      var1.append("  Type: ");
      var1.append(this.getType());
      var1.append('\n');
      var1.append("  State: ");
      var1.append(this.getState());
      var1.append('\n');
      var1.append("  Exceptions:\n");
      Exception[] var2 = this.getMessages();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.append("  ");
            var1.append(var2[var3].getMessage());
            var1.append('\n');
         }
      }

      var1.append(" )");
      var1.append('\n');
      return var1.toString();
   }
}
