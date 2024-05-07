package weblogic.jms.backend.udd;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeliveryFailureParamsBean;
import weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBean;
import weblogic.j2ee.descriptor.wl.MessageLoggingParamsBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.ThresholdParamsBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedDestinationBean;

public class SyntheticDDBean implements UniformDistributedDestinationBean, DescriptorBean {
   ArrayList members = new ArrayList();
   LinkedList listeners = new LinkedList();
   UDDEntity udd;

   SyntheticDDBean(UDDEntity var1) {
      this.udd = var1;
   }

   private int findMember(String var1) {
      if (this.members == null) {
         return -1;
      } else {
         for(int var2 = 0; var2 < this.members.size(); ++var2) {
            SyntheticMemberBean var3 = (SyntheticMemberBean)this.members.get(var2);
            if (var3.jmsServerName.equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   SyntheticMemberBean findMemberBean(String var1) {
      int var2 = this.findMember(var1);
      return var2 == -1 ? null : (SyntheticMemberBean)this.members.get(var2);
   }

   void removeMember(String var1) {
      int var2 = this.findMember(var1);
      if (var2 != -1) {
         this.members.remove(var2);
      }
   }

   SyntheticMemberBean addMember(String var1) {
      int var2 = this.findMember(var1);
      if (var2 != -1) {
         throw new AssertionError("Same server added twice?");
      } else {
         SyntheticMemberBean var3 = new SyntheticMemberBean(this.udd, var1);
         this.members.add(var3);
         return var3;
      }
   }

   public String getName() {
      return this.udd.getEntityName();
   }

   public void setName(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getNotes() {
      return this.udd.getUDestBean().getNotes();
   }

   public void setNotes(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getJNDIName() {
      return this.udd.getUDestBean().getJNDIName();
   }

   public void setJNDIName(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getLocalJNDIName() {
      return this.udd.getUDestBean().getLocalJNDIName();
   }

   public void setLocalJNDIName(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getSubDeploymentName() {
      return this.udd.getUDestBean().getSubDeploymentName();
   }

   public void setSubDeploymentName(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getLoadBalancingPolicy() {
      return this.udd.getUDestBean().getLoadBalancingPolicy();
   }

   public void setLoadBalancingPolicy(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public String getUnitOfOrderRouting() {
      return this.udd.getUDestBean().getUnitOfOrderRouting();
   }

   public void setUnitOfOrderRouting(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public void addBeanUpdateListener(BeanUpdateListener var1) {
      this.listeners.add(var1);
   }

   public Descriptor getDescriptor() {
      return null;
   }

   public DescriptorBean getParentBean() {
      return ((DescriptorBean)this.udd.getUDestBean()).getParentBean();
   }

   public boolean isEditable() {
      return false;
   }

   public boolean isSet(String var1) {
      return false;
   }

   public void removeBeanUpdateListener(BeanUpdateListener var1) {
      this.listeners.remove(var1);
   }

   public void unSet(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public DescriptorBean createChildCopy(String var1, DescriptorBean var2) {
      throw new UnsupportedOperationException();
   }

   public DescriptorBean createChildCopyIncludingObsolete(String var1, DescriptorBean var2) {
      throw new UnsupportedOperationException();
   }

   public String getSAFExportPolicy() {
      return null;
   }

   public void setSAFExportPolicy(String var1) {
   }

   public boolean isDefaultTargetingEnabled() {
      return this.udd.getUDestBean().isDefaultTargetingEnabled();
   }

   public void setDefautTargetingEnabled(boolean var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public void addDestinationKey(String var1) {
      this.udd.getUDestBean().addDestinationKey(var1);
   }

   public String getAttachSender() {
      return this.udd.getUDestBean().getAttachSender();
   }

   public DeliveryFailureParamsBean getDeliveryFailureParams() {
      return this.udd.getUDestBean().getDeliveryFailureParams();
   }

   public DeliveryParamsOverridesBean getDeliveryParamsOverrides() {
      return this.udd.getUDestBean().getDeliveryParamsOverrides();
   }

   public String[] getDestinationKeys() {
      return this.udd.getUDestBean().getDestinationKeys();
   }

   public int getIncompleteWorkExpirationTime() {
      return this.udd.getUDestBean().getIncompleteWorkExpirationTime();
   }

   public String getJMSCreateDestinationIdentifier() {
      return this.udd.getUDestBean().getJMSCreateDestinationIdentifier();
   }

   public int getMaximumMessageSize() {
      return this.udd.getUDestBean().getMaximumMessageSize();
   }

   public MessageLoggingParamsBean getMessageLoggingParams() {
      return this.udd.getUDestBean().getMessageLoggingParams();
   }

   public int getMessagingPerformancePreference() {
      return this.udd.getUDestBean().getMessagingPerformancePreference();
   }

   public QuotaBean getQuota() {
      return this.udd.getUDestBean().getQuota();
   }

   public TemplateBean getTemplate() {
      return this.udd.getUDestBean().getTemplate();
   }

   public ThresholdParamsBean getThresholds() {
      return this.udd.getUDestBean().getThresholds();
   }

   public String getUnitOfWorkHandlingPolicy() {
      return this.udd.getUDestBean().getUnitOfWorkHandlingPolicy();
   }

   public boolean isConsumptionPausedAtStartup() {
      return this.udd.getUDestBean().isConsumptionPausedAtStartup();
   }

   public boolean isDefaultUnitOfOrder() {
      return this.udd.getUDestBean().isDefaultUnitOfOrder();
   }

   public boolean isInsertionPausedAtStartup() {
      return this.udd.getUDestBean().isInsertionPausedAtStartup();
   }

   public boolean isProductionPausedAtStartup() {
      return this.udd.getUDestBean().isProductionPausedAtStartup();
   }

   public void removeDestinationKey(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setAttachSender(String var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setConsumptionPausedAtStartup(boolean var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setDefaultUnitOfOrder(boolean var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setDestinationKeys(String[] var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setIncompleteWorkExpirationTime(int var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setInsertionPausedAtStartup(boolean var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setJMSCreateDestinationIdentifier(String var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setMaximumMessageSize(int var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setMessagingPerformancePreference(int var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setProductionPausedAtStartup(boolean var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setQuota(QuotaBean var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setTemplate(TemplateBean var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setUnitOfWorkHandlingPolicy(String var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setDefaultTargetingEnabled(boolean var1) throws IllegalArgumentException {
      throw new AssertionError("Cannot write to fake bean");
   }
}
