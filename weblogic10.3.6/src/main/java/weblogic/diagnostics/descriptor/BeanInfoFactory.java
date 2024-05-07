package weblogic.diagnostics.descriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import weblogic.utils.codegen.ImplementationFactory;
import weblogic.utils.codegen.RoleInfoImplementationFactory;

public class BeanInfoFactory implements RoleInfoImplementationFactory {
   private static final Map interfaceMap = new HashMap(14);
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
      return "1296097718676";
   }

   static {
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFBean", "weblogic.diagnostics.descriptor.WLDFBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBean", "weblogic.diagnostics.descriptor.WLDFHarvestedTypeBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFHarvesterBean", "weblogic.diagnostics.descriptor.WLDFHarvesterBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFImageNotificationBean", "weblogic.diagnostics.descriptor.WLDFImageNotificationBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFInstrumentationBean", "weblogic.diagnostics.descriptor.WLDFInstrumentationBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBean", "weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFJMSNotificationBean", "weblogic.diagnostics.descriptor.WLDFJMSNotificationBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFJMXNotificationBean", "weblogic.diagnostics.descriptor.WLDFJMXNotificationBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFNotificationBean", "weblogic.diagnostics.descriptor.WLDFNotificationBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFResourceBean", "weblogic.diagnostics.descriptor.WLDFResourceBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFSMTPNotificationBean", "weblogic.diagnostics.descriptor.WLDFSMTPNotificationBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFSNMPNotificationBean", "weblogic.diagnostics.descriptor.WLDFSNMPNotificationBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFWatchBean", "weblogic.diagnostics.descriptor.WLDFWatchBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.descriptor.WLDFWatchNotificationBean", "weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanImplBeanInfo");
      roleInfoList = new ArrayList(13);
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFHarvesterBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFImageNotificationBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFInstrumentationBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFJMSNotificationBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFJMXNotificationBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFNotificationBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFResourceBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFSMTPNotificationBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFSNMPNotificationBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFWatchBean");
      roleInfoList.add("weblogic.diagnostics.descriptor.WLDFWatchNotificationBean");
      SINGLETON = new BeanInfoFactory();
   }
}
