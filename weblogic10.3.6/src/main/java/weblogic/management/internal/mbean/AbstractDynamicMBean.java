package weblogic.management.internal.mbean;

import java.security.AccessController;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.management.Admin;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public abstract class AbstractDynamicMBean extends AbstractDescriptorBean implements ConfigurationMBeanCustomized, DynamicMBean, MBeanRegistration, NotificationBroadcaster {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public AbstractDynamicMBean() {
   }

   public AbstractDynamicMBean(DescriptorBean var1, int var2) {
      super(var1, var2);
   }

   public void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
   }

   public MBeanNotificationInfo[] getNotificationInfo() {
      throw new UnsupportedOperationException("AbstractDynamicMBean.getNotificationInfo()");
   }

   public void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
   }

   public MBeanInfo getMBeanInfo() {
      throw new UnsupportedOperationException("AbstractDynamicMBean.getMBeanInfo()");
   }

   public Object getAttribute(String var1) throws AttributeNotFoundException, MBeanException, ReflectionException {
      return this.getValue(var1);
   }

   public AttributeList getAttributes(String[] var1) {
      AttributeList var2 = new AttributeList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var10000 = var1[var3];
         var2.add(new Attribute(var1[var3], this.getValue(var1[var3])));
      }

      return var2;
   }

   public Object invoke(String var1, Object[] var2, String[] var3) throws MBeanException, ReflectionException {
      throw new UnsupportedOperationException("AbstractDynamicMBean.invoke()");
   }

   public void setAttribute(Attribute var1) throws AttributeNotFoundException, InvalidAttributeValueException, ReflectionException {
      this.putValue(var1.getName(), var1.getValue());
   }

   public AttributeList setAttributes(AttributeList var1) {
      throw new UnsupportedOperationException("AbstractDynamicMBean.setAttributes()");
   }

   public ObjectName preRegister(MBeanServer var1, ObjectName var2) throws Exception {
      return null;
   }

   public void postDeregister() {
   }

   public void postRegister(Boolean var1) {
   }

   public void preDeregister() throws Exception {
   }

   public void touch() throws ConfigurationException {
      throw new UnsupportedOperationException("AbstractDynamicMBean.()");
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      throw new UnsupportedOperationException("AbstractDynamicMBean.()");
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      throw new UnsupportedOperationException("AbstractDynamicMBean.()");
   }

   public ConfigurationMBean getMbean() {
      return (ConfigurationMBean)this;
   }

   public void putValue(String var1, Object var2) {
      throw new AssertionError("No such field " + var1);
   }

   public void putValueNotify(String var1, Object var2) {
      this.putValue(var1, var2);
   }

   public Object getValue(String var1) {
      throw new AssertionError("No such field " + var1);
   }

   public boolean isConfig() {
      return true;
   }

   public boolean isAdmin() {
      return false;
   }

   public boolean isEdit() {
      Descriptor var1 = this.getDescriptor();
      if (var1 == null) {
         return false;
      } else {
         DescriptorBean var2 = var1.getRootBean();
         return var2 != null && var2 instanceof DomainMBean ? var2.isEditable() : false;
      }
   }

   public boolean isRuntime() {
      Descriptor var1 = this.getDescriptor();
      if (var1 == null) {
         return false;
      } else {
         DescriptorBean var2 = var1.getRootBean();
         if (var2 != null && var2 instanceof DomainMBean) {
            DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
            return var3 == var2;
         } else {
            return false;
         }
      }
   }

   public String getType() {
      String var1 = this.getClass().getName();
      int var2 = var1.lastIndexOf(".");
      var1 = var1.substring(var2 + 1);
      var2 = var1.lastIndexOf("MBeanImpl");
      if (var2 >= 0) {
         var1 = var1.substring(0, var2);
      }

      return var1;
   }

   public MBeanHome getMBeanHome() {
      return Admin.getInstance().getMBeanHome();
   }

   public MBeanHome getAdminMBeanHome() {
      return Admin.getInstance().getAdminMBeanHome();
   }

   public void markAttributeModified(String var1) {
   }

   public void sendNotification(Notification var1) {
   }

   protected abstract static class Helper extends AbstractDescriptorBeanHelper {
      protected Helper(AbstractDynamicMBean var1) {
         super(var1);
      }
   }
}
