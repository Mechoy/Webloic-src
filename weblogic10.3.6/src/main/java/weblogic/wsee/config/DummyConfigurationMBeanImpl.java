package weblogic.wsee.config;

import java.beans.PropertyChangeListener;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
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

public class DummyConfigurationMBeanImpl implements ConfigurationMBean {
   private String _name;

   protected DummyConfigurationMBeanImpl() {
      this("");
   }

   protected DummyConfigurationMBeanImpl(String var1) {
      this._name = var1;
   }

   public String getName() {
      return this._name;
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
   }

   public String getType() {
      return null;
   }

   public WebLogicObjectName getObjectName() {
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

   public WebLogicMBean getParent() {
      return null;
   }

   public void setParent(WebLogicMBean var1) throws ConfigurationException {
   }

   public boolean isRegistered() {
      return false;
   }

   public String getNotes() {
      return null;
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
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

   public String getComments() {
      return null;
   }

   public void setComments(String var1) {
   }

   public void touch() throws ConfigurationException {
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
   }

   public boolean isSet(String var1) {
      return false;
   }

   public void unSet(String var1) {
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

   public void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
   }

   public void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
   }

   public MBeanNotificationInfo[] getNotificationInfo() {
      return new MBeanNotificationInfo[0];
   }

   public Descriptor getDescriptor() {
      return null;
   }

   public DescriptorBean getParentBean() {
      return null;
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
      return false;
   }

   public DescriptorBean createChildCopy(String var1, DescriptorBean var2) throws IllegalArgumentException {
      return null;
   }

   public DescriptorBean createChildCopyIncludingObsolete(String var1, DescriptorBean var2) throws IllegalArgumentException {
      return null;
   }
}
