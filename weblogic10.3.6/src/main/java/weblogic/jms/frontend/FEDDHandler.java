package weblogic.jms.frontend;

import java.security.AccessController;
import java.util.Iterator;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSFailover;
import weblogic.jms.common.JMSLoadBalancer;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDMember;
import weblogic.jms.dd.DDStatusListener;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class FEDDHandler implements DDStatusListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   DDHandler ddHandler;
   JMSLoadBalancer loadBalancer = null;
   String boundJNDIName = null;

   public FEDDHandler(DDHandler var1) {
      this.ddHandler = var1;
      var1.addStatusListener(this, 255);
   }

   public void statusChangeNotification(DDHandler var1, int var2) {
      if ((var2 & 16) != 0) {
         this.stop();
      } else {
         this.refresh();
      }

   }

   private boolean areAnyMembersUp() {
      Iterator var1 = this.ddHandler.memberCloneIterator();

      DDMember var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (DDMember)var1.next();
      } while(!var2.isUp());

      return true;
   }

   private synchronized void stop() {
      this.unbindFromJNDI();
      this.loadBalancer = null;
   }

   private synchronized void refresh() {
      boolean var1 = this.areAnyMembersUp();
      if (!var1) {
         this.stop();
      } else {
         if (this.loadBalancer == null) {
            this.loadBalancer = new JMSLoadBalancer(this.ddHandler);
            this.bindIntoJNDI();
         } else {
            String var2 = JMSServerUtilities.transformJNDIName(this.ddHandler.getJNDIName(), this.ddHandler.getApplicationName());
            if (this.boundJNDIName != null && !this.boundJNDIName.equals(var2)) {
               this.unbindFromJNDI();
            }

            if (this.boundJNDIName == null && var2 != null) {
               this.bindIntoJNDI();
            }

            this.loadBalancer.refresh();
         }

      }
   }

   private void bindIntoJNDI() {
      String var1 = this.ddHandler.getJNDIName();
      var1 = JMSServerUtilities.transformJNDIName(var1, this.ddHandler.getApplicationName());
      if (var1 != null) {
         this.boundJNDIName = var1;

         try {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Binding " + this.boundJNDIName + " into JNDI for " + this.ddHandler.getName());
            }

            PrivilegedActionUtilities.bindAsSU(JMSService.getContext(false), this.boundJNDIName, this.ddHandler.getDDImpl(), kernelId);
         } catch (NamingException var3) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Bind failure", var3);
            }

            if (!(var3 instanceof NameAlreadyBoundException)) {
               this.boundJNDIName = null;
               throw new AssertionError(var3);
            }

            JMSLogger.logNameConflictBindingGlobalJNDIName(this.boundJNDIName, this.ddHandler.getName(), this.ddHandler.getEARModuleName());
            this.boundJNDIName = null;
         }

      }
   }

   private void unbindFromJNDI() {
      if (this.boundJNDIName != null) {
         try {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Unbinding " + this.boundJNDIName + " into JNDI for " + this.ddHandler.getName());
            }

            PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(false), this.boundJNDIName, kernelId);
         } catch (NamingException var2) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Unbind failure", var2);
            }
         }

         this.boundJNDIName = null;
      }
   }

   public int getLoadBalancingPolicy() {
      return this.ddHandler.getLoadBalancingPolicyAsInt();
   }

   public DestinationImpl producerLoadBalance(boolean var1, FESession var2) {
      JMSLoadBalancer var3;
      synchronized(this) {
         if (this.loadBalancer == null) {
            return null;
         }

         var3 = this.loadBalancer;
      }

      return var3.producerLoadBalance(var1, var2);
   }

   public DestinationImpl consumerLoadBalance(FESession var1) {
      JMSLoadBalancer var2;
      synchronized(this) {
         if (this.loadBalancer == null) {
            return null;
         }

         var2 = this.loadBalancer;
      }

      return var2.consumerLoadBalance(var1);
   }

   public DestinationImpl connectionConsumerLoadBalance() {
      JMSLoadBalancer var1;
      synchronized(this) {
         if (this.loadBalancer == null) {
            return null;
         }

         var1 = this.loadBalancer;
      }

      return var1.connectionConsumerLoadBalance();
   }

   public JMSFailover getProducerFailover(DistributedDestinationImpl var1, Throwable var2, boolean var3, FESession var4) {
      JMSLoadBalancer var5;
      synchronized(this) {
         if (this.loadBalancer == null) {
            return null;
         }

         var5 = this.loadBalancer;
      }

      return var5.getProducerFailover(var1, var2, var3, var4);
   }

   public DDMember findDDMemberByMemberName(String var1) {
      return this.ddHandler == null ? null : this.ddHandler.findMemberByName(var1);
   }

   public String getUnitOfOrderRouting() {
      return this.ddHandler.getUnitOfOrderRouting();
   }

   public DDHandler getDDHandler() {
      return this.ddHandler;
   }

   public String getName() {
      return this.ddHandler.getName();
   }

   public boolean isDDPartitionedDistributedTopic() {
      return this.loadBalancer == null ? false : this.loadBalancer.isPartitionedDistributedTopic();
   }

   public String toString() {
      return "FEDDHandler: " + this.ddHandler.getName() + ", hash: " + this.hashCode();
   }
}
