package weblogic.management.provider.custom;

import java.beans.PropertyChangeListener;
import java.security.AccessController;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConfigurationMBeanBase implements ConfigurationMBean {
   private String name;
   private String type;
   private ConfigurationMBean parent;
   private WebLogicObjectName objectName;
   private String notes;
   private String comments;
   private boolean registered;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void unregister() {
      if (this.registered) {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         var1.unregisterCustomBean(this.objectName);
         this.registered = false;
      }
   }

   public ConfigurationMBeanBase(String var1, String var2, ConfigurationMBean var3) {
      this.name = var1;
      this.type = var2;
      this.parent = var3;
      RuntimeAccess var4 = ManagementService.getRuntimeAccess(kernelId);

      try {
         if (var3 != null) {
            this.objectName = new WebLogicObjectName(var1, var2, var4.getDomainName(), var3.getObjectName());
         } else {
            this.objectName = new WebLogicObjectName(var1, var2, var4.getDomainName());
         }
      } catch (MalformedObjectNameException var6) {
         throw new RuntimeException(var6);
      }

      var4.registerCustomBean(this.objectName, this);
      this.registered = true;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      this.name = var1;
   }

   public String getType() {
      return this.type;
   }

   public WebLogicObjectName getObjectName() {
      return this.objectName;
   }

   public DescriptorBean getParentBean() {
      return this.parent;
   }

   public WebLogicMBean getParent() {
      return this.parent;
   }

   public void setParent(WebLogicMBean var1) throws ConfigurationException {
      this.parent = (ConfigurationMBean)var1;
   }

   public boolean isRegistered() {
      return this.registered;
   }

   public String getComments() {
      return this.comments;
   }

   public void setComments(String var1) {
      this.comments = var1;
   }

   public String getNotes() {
      return this.notes;
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.notes = var1;
   }

   public Descriptor getDescriptor() {
      return null;
   }

   public boolean isSet(String var1) throws IllegalArgumentException {
      return false;
   }

   public void unSet(String var1) throws IllegalArgumentException {
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
   }

   public void addBeanUpdateListener(BeanUpdateListener var1) {
   }

   public void removeBeanUpdateListener(BeanUpdateListener var1) {
   }

   public boolean isEditable() {
      return true;
   }

   public DescriptorBean createChildCopy(String var1, DescriptorBean var2) {
      return null;
   }

   public DescriptorBean createChildCopyIncludingObsolete(String var1, DescriptorBean var2) {
      return null;
   }

   public Object getAttribute(String var1) throws AttributeNotFoundException, MBeanException, ReflectionException {
      return null;
   }

   public void setAttribute(Attribute var1) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
   }

   public AttributeList getAttributes(String[] var1) {
      return null;
   }

   public AttributeList setAttributes(AttributeList var1) {
      return null;
   }

   public Object invoke(String var1, Object[] var2, String[] var3) throws MBeanException, ReflectionException {
      return null;
   }

   public MBeanInfo getMBeanInfo() {
      return null;
   }

   public boolean isCachingDisabled() {
      return false;
   }

   public boolean isPersistenceEnabled() {
      return false;
   }

   public void setPersistenceEnabled(boolean var1) {
   }

   public boolean isDefaultedMBean() {
      return false;
   }

   public void setDefaultedMBean(boolean var1) {
   }

   public ObjectName preRegister(MBeanServer var1, ObjectName var2) throws Exception {
      return null;
   }

   public void postRegister(Boolean var1) {
   }

   public void preDeregister() throws Exception {
   }

   public void postDeregister() {
   }

   public void touch() throws ConfigurationException {
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
   }

   public void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
   }

   public void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
   }

   public MBeanNotificationInfo[] getNotificationInfo() {
      return new MBeanNotificationInfo[0];
   }
}
