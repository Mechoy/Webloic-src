package weblogic.diagnostics.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import weblogic.utils.codegen.ImplementationFactory;
import weblogic.utils.codegen.RoleInfoImplementationFactory;

public class BeanInfoFactory implements RoleInfoImplementationFactory {
   private static final Map interfaceMap = new HashMap(2);
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
      return "1296097444404";
   }

   static {
      interfaceMap.put("weblogic.diagnostics.debug.DebugBean", "weblogic.diagnostics.debug.DebugBeanImplBeanInfo");
      interfaceMap.put("weblogic.diagnostics.debug.DebugScopeBean", "weblogic.diagnostics.debug.DebugScopeBeanImplBeanInfo");
      roleInfoList = new ArrayList(0);
      SINGLETON = new BeanInfoFactory();
   }
}
