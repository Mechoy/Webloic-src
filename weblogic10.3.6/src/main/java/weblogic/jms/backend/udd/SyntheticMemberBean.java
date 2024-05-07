package weblogic.jms.backend.udd;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.jms.backend.MemberBeanWithTargeting;
import weblogic.jms.extensions.JMSModuleHelper;

public class SyntheticMemberBean implements MemberBeanWithTargeting, DescriptorBean {
   LinkedList listeners = new LinkedList();
   String jmsServerName;
   UDDEntity udd;

   public SyntheticMemberBean(UDDEntity var1, String var2) {
      this.udd = var1;
      this.jmsServerName = var2;
   }

   public String getServerName() {
      return this.jmsServerName;
   }

   public String getMigratableTargetName() {
      return null;
   }

   public String getClusterName() {
      return this.udd.getUDestBean().getSubDeploymentName();
   }

   public int getWeight() {
      return 1;
   }

   public void setWeight(int var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Cannot set weight on a uniform distributed destination");
   }

   public String getName() {
      return JMSModuleHelper.uddMakeName(this.jmsServerName, this.udd.getUDestBean().getName());
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

   public String getPhysicalDestinationName() {
      return this.getName();
   }

   public void setPhysicalDestinationName(String var1) {
      this.setName(var1);
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
      return this.udd.getDDBean();
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
}
