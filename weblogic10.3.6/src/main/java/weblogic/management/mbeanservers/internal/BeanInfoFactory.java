package weblogic.management.mbeanservers.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import weblogic.utils.codegen.ImplementationFactory;
import weblogic.utils.codegen.RoleInfoImplementationFactory;

public class BeanInfoFactory implements RoleInfoImplementationFactory {
   private static final Map interfaceMap = new HashMap(11);
   private static final ArrayList roleInfoList;
   private static final BeanInfoFactory SINGLETON;

   public static final ImplementationFactory getInstance() {
      return SINGLETON;
   }

   public String getImplementationClassName(String var1) {
      return (String)interfaceMap.get(var1);
   }

   public String[] getInterfaces() {
      Set var1 = interfaceMap.keySet();
      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   public String[] getInterfacesWithRoleInfo() {
      return (String[])((String[])roleInfoList.toArray(new String[roleInfoList.size()]));
   }

   public String getRoleInfoImplementationFactoryTimestamp() {
      return "1311999072276";
   }

   static {
      interfaceMap.put("weblogic.management.mbeanservers.MBeanTypeService", "weblogic.management.mbeanservers.internal.MBeanTypeServiceImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.Service", "weblogic.management.mbeanservers.internal.ServiceImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean", "weblogic.management.mbeanservers.domainruntime.internal.DomainRuntimeServiceMBeanImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.domainruntime.MBeanServerConnectionManagerMBean", "weblogic.management.mbeanservers.domainruntime.internal.MBeanServerConnectionManagerBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.edit.ActivationTaskMBean", "weblogic.management.mbeanservers.edit.internal.ActivationTaskMBeanImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.edit.Change", "weblogic.management.mbeanservers.edit.internal.ChangeImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.edit.ConfigurationManagerMBean", "weblogic.management.mbeanservers.edit.internal.ConfigurationManagerMBeanImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.edit.EditServiceMBean", "weblogic.management.mbeanservers.edit.internal.EditServiceMBeanImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.edit.RecordingManagerMBean", "weblogic.management.mbeanservers.edit.internal.RecordingManagerMBeanImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.edit.ServerStatus", "weblogic.management.mbeanservers.edit.internal.ServerStatusImplBeanInfo");
      interfaceMap.put("weblogic.management.mbeanservers.runtime.RuntimeServiceMBean", "weblogic.management.mbeanservers.runtime.internal.RuntimeServiceMBeanImplBeanInfo");
      roleInfoList = new ArrayList(6);
      roleInfoList.add("weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
      roleInfoList.add("weblogic.management.mbeanservers.edit.ActivationTaskMBean");
      roleInfoList.add("weblogic.management.mbeanservers.edit.ConfigurationManagerMBean");
      roleInfoList.add("weblogic.management.mbeanservers.edit.EditServiceMBean");
      roleInfoList.add("weblogic.management.mbeanservers.edit.RecordingManagerMBean");
      roleInfoList.add("weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
      SINGLETON = new BeanInfoFactory();
   }
}
