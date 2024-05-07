package weblogic.jms.saf;

import java.util.HashMap;
import java.util.List;
import javax.jms.JMSException;
import weblogic.application.ModuleException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.SAFDestinationBean;
import weblogic.j2ee.descriptor.wl.SAFErrorHandlingBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSMessageExpirationHelper;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.utils.BeanListenerCustomizer;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.RedirectionListener;
import weblogic.messaging.kernel.SendOptions;

public class ErrorHandler implements JMSModuleManagedEntity, BeanListenerCustomizer {
   public static final int DISCARD = 1;
   public static final int DISCARD_AND_LOG = 2;
   public static final int REDIRECT = 3;
   public static final int ALWAYS_FORWARD = 4;
   private SAFErrorHandlingBean ehBean;
   private SAFDestinationBean safErrorDestinationBean;
   private short type;
   private String name;
   private String policy;
   private String logFormat;
   private int policyAsInt = -1;
   private String fullyQualifiedName;
   private static final HashMap safErrorHandlingBeanSignatures = new HashMap();
   private GenericBeanListener safErrorHandlingBeanListener;
   private HashMap importedDestinations;
   private String safErrorDestinationNamePrefix;

   public ErrorHandler() {
   }

   public ErrorHandler(SAFErrorHandlingBean var1, String var2, List var3, String var4) throws ModuleException {
      this.ehBean = var1;
      this.name = var1.getName();
      this.fullyQualifiedName = var2;
      this.type = 2;
      this.initialize(var4);
   }

   private void initialize(String var1) throws ModuleException {
      this.policy = this.ehBean.getPolicy();
      this.policyAsInt = this.getSafErrorHandlingPolicyAsInt();
      this.logFormat = this.ehBean.getLogFormat();
      this.safErrorDestinationBean = this.ehBean.getSAFErrorDestination();
      if (this.safErrorDestinationBean != null) {
         String var2 = ((SAFImportedDestinationsBean)((DescriptorBean)this.safErrorDestinationBean).getParentBean()).getName();
         this.safErrorDestinationNamePrefix = JMSBeanHelper.getDecoratedName(var1, var2);
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("ErrorHandling " + this.fullyQualifiedName + ": Policy = " + this.policy);
      }

      this.safErrorHandlingBeanListener = JMSSAFManager.initializeGenericBeanListener((DescriptorBean)this.ehBean, this, this, safErrorHandlingBeanSignatures, (HashMap)null);
      JMSSAFManager.manager.addErrorHandler(this.fullyQualifiedName, this);
      this.importedDestinations = new HashMap();
   }

   public short getType() {
      return this.type;
   }

   public String getFullyQualifiedName() {
      return this.fullyQualifiedName;
   }

   public boolean isAlwaysForward() {
      return this.policyAsInt == 4;
   }

   synchronized void removeImportedDestination(String var1) {
      this.importedDestinations.remove(var1);
   }

   public void prepare() {
   }

   public void activate(JMSBean var1) throws ModuleException {
      this.ehBean = var1.lookupSAFErrorHandling(this.getEntityName());
      this.safErrorHandlingBeanListener = JMSSAFManager.initializeGenericBeanListener((DescriptorBean)this.ehBean, this, this, safErrorHandlingBeanSignatures, (HashMap)null);
      if (this.safErrorHandlingBeanListener != null) {
         this.safErrorHandlingBeanListener.open();
      }

   }

   public void deactivate() {
      if (this.safErrorHandlingBeanListener != null) {
         this.safErrorHandlingBeanListener.close();
      }

   }

   public void unprepare() {
   }

   public void destroy() {
      JMSSAFManager.manager.removeErrorHandler(this.fullyQualifiedName);
   }

   public void remove() {
   }

   public String getEntityName() {
      return this.ehBean.getName();
   }

   public void setTargets(List var1, DomainMBean var2) {
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) {
   }

   public void activateChangeOfTargets() {
   }

   public void rollbackChangeOfTargets() {
   }

   public void activateFinished() {
   }

   public String toString() {
      String var1 = "Policy=" + this.getPolicy();
      if (this.getType() == 3) {
         var1 = var1 + ", ErrorDestination" + this.safErrorDestinationBean.getName();
      }

      return var1;
   }

   public String getName() {
      return this.name;
   }

   private int getSafErrorHandlingPolicyAsInt() {
      byte var1 = 1;
      if (this.policy != null) {
         if ("Discard".equals(this.policy)) {
            var1 = 1;
         } else if ("Log".equals(this.policy)) {
            var1 = 2;
         } else if ("Redirect".equals(this.policy)) {
            var1 = 3;
         } else if ("Always-Forward".equals(this.policy)) {
            var1 = 4;
         }
      }

      return var1;
   }

   public String getPolicy() {
      return this.policy;
   }

   public int getPolicyAsInt() {
      return this.policyAsInt;
   }

   public synchronized void setPolicy(String var1) {
      this.policy = var1;
      this.policyAsInt = this.getSafErrorHandlingPolicyAsInt();
   }

   public String getLogFormat() {
      return this.logFormat;
   }

   public void setLogFormat(String var1) {
      this.logFormat = var1;
   }

   public SAFDestinationBean getSAFErrorDestination() {
      return this.safErrorDestinationBean;
   }

   public void setSAFErrorDestination(SAFDestinationBean var1) {
      this.safErrorDestinationBean = var1;
   }

   void handleFailure(RedirectionListener.Info var1, String var2, MessageImpl var3) throws KernelException, JMSException {
      switch (this.getPolicyAsInt()) {
         case 1:
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("Handle failure: policy = " + this.getPolicyAsInt());
            }

            return;
         case 2:
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("Handle failure: policy = " + this.getPolicy() + " log policy = " + this.getLogFormat());
            }

            StringBuffer var4 = new StringBuffer(256);
            List var5 = JMSMessageExpirationHelper.extractJMSHeaderAndProperty(this.getLogFormat(), var4);
            List var6 = JMSMessageExpirationHelper.convertStringToLinkedList(var4.toString());
            JMSMessageExpirationHelper.logExpiredSAFMessage(var3, var5, var6);
            return;
         case 3:
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("Handle failure: policy = " + this.getPolicy() + " Error destination = " + this.safErrorDestinationBean.getName());
            }

            BEDestinationImpl var7 = this.findDestination(var2, this.safErrorDestinationBean.getName());
            Destination var8 = var7.getKernelDestination();
            if (var1 != null) {
               var1.setRedirectDestination(var8);
               var1.setSendOptions(this.createSendOptions(var3, var7));
            } else {
               this.redirect(var7, var3);
            }

            return;
         default:
      }
   }

   private SendOptions createSendOptions(MessageImpl var1, BEDestinationImpl var2) throws JMSException {
      this.overrideMessageProperties(var1);
      return var2.createSendOptions(0L, var2.findOrCreateKernelSequence(var1), var1);
   }

   private BEDestinationImpl findDestination(String var1, String var2) {
      String var3 = JMSBeanHelper.getDecoratedName(this.safErrorDestinationNamePrefix, var2);
      BackEnd var4 = JMSService.getJMSService().getBEDeployer().findBackEnd(var1);
      var3 = var4.getFullSAFDestinationName(var3);
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Finding kernel destination " + var2 + " in " + var1 + " fully qualified error destination name = " + var3);
      }

      BEDestinationImpl var5 = var4.findDestination(var3);
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Found kernel destination " + var2 + " in " + var1 + " backend = " + var4 + " dest = " + var5);
      }

      return var5;
   }

   private void overrideMessageProperties(MessageImpl var1) {
      var1.setDeliveryTime(0L);
      var1._setJMSRedeliveryLimit(-1);
      var1._setJMSExpiration(0L);
      var1.setSAFSequenceName((String)null);
      var1.setSAFSeqNumber(0L);
   }

   private void redirect(BEDestinationImpl var1, MessageImpl var2) throws KernelException, JMSException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Redirecting message: " + var2.getJMSMessageID() + " to " + var1.getName());
      }

      Destination var3 = var1.getKernelDestination();
      KernelRequest var4 = var3.send(var2, this.createSendOptions(var2, var1));
      if (var4 != null) {
         var4.getResult();
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Successfully redirected " + var2.getJMSMessageID() + " to destination " + var3.getName());
      }

   }

   static {
      safErrorHandlingBeanSignatures.put("Policy", String.class);
      safErrorHandlingBeanSignatures.put("LogFormat", String.class);
      safErrorHandlingBeanSignatures.put("SAFErrorDestination", SAFDestinationBean.class);
   }
}
