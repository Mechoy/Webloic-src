package weblogic.management.scripting;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.management.WebLogicMBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.jmx.MBeanServerInvocationHandler;

public class WLSTTreeUtils extends WLSTUtils {
   public String getTree() {
      if (this.domainType.equals("RuntimeConfigServerDomain")) {
         return "serverConfig";
      } else if (this.domainType.equals("RuntimeRuntimeServerDomain")) {
         return "serverRuntime";
      } else if (this.domainType.equals("ConfigDomainRuntime")) {
         return "domainConfig";
      } else if (this.domainType.equals("RuntimeDomainRuntime")) {
         return "domainRuntime";
      } else if (this.domainType.equals("Domain")) {
         return "config";
      } else if (this.domainType.equals("DomainConfig")) {
         return "config";
      } else if (this.domainType.equals("DomainRuntime")) {
         return "runtime";
      } else if (this.domainType.equals("Custom_Domain")) {
         return "custom";
      } else if (this.domainType.equals("DomainCustom_Domain")) {
         return "domainCustom";
      } else if (this.domainType.equals("ConfigEdit")) {
         return "edit";
      } else if (this.domainType.equals("JSR77")) {
         return "jsr77";
      } else {
         return this.domainType.equals("JNDI") ? "jndi" : "serverConfig";
      }
   }

   String getTreeFromArgument(String attr) {
      if (attr.startsWith("config:")) {
         return "config";
      } else if (attr.startsWith("runtime:")) {
         return "runtime";
      } else if (attr.startsWith("adminConfig:")) {
         return "adminConfig";
      } else if (attr.startsWith("custom:")) {
         return "custom";
      } else if (attr.startsWith("domainCustom:")) {
         return "domainCustom";
      } else if (attr.startsWith("jndi:")) {
         return "jndi";
      } else if (attr.startsWith("serverConfig:")) {
         return "serverConfig";
      } else if (attr.startsWith("serverRuntime:")) {
         return "serverRuntime";
      } else if (attr.startsWith("domainConfig:")) {
         return "domainConfig";
      } else if (attr.startsWith("domainRuntime:")) {
         return "domainRuntime";
      } else {
         return attr.startsWith("edit:") ? "edit" : null;
      }
   }

   String removeTreeFromArgument(String attr) {
      if (!attr.startsWith("config:") && !attr.startsWith("runtime:") && !attr.startsWith("adminConfig:") && !attr.startsWith("custom:") && !attr.startsWith("domainCustom:") && !attr.startsWith("jndi:") && !attr.startsWith("serverConfig:") && !attr.startsWith("serverRuntime:") && !attr.startsWith("domainConfig:") && !attr.startsWith("domainRuntime:") && !attr.startsWith("edit:")) {
         return attr;
      } else {
         String _attr = attr.substring(attr.indexOf(":/") + 1, attr.length());
         return _attr;
      }
   }

   String getCurrentTree() {
      return this.getTree();
   }

   public String calculateTabSpace(String s) {
      String tabSpace = "";

      for(int k = 0; k < 45 - s.length(); ++k) {
         tabSpace = tabSpace + " ";
      }

      return tabSpace;
   }

   public String calculateTabSpace(String s, int i) {
      String tabSpace = "";

      for(int k = 0; k < i - s.length(); ++k) {
         tabSpace = tabSpace + " ";
      }

      return tabSpace;
   }

   boolean isMBeanExcluded(String bean) {
      return excludedMBeans.contains(bean);
   }

   boolean isAnythingInThisExcluded(String key) {
      if (key == null) {
         return false;
      } else {
         Iterator it = excludedMBeans.iterator();

         String excludedBean;
         do {
            if (!it.hasNext()) {
               return false;
            }

            excludedBean = (String)it.next();
         } while(key.indexOf(excludedBean) == -1);

         return true;
      }
   }

   String printAttributes(TreeMap attrs) {
      String retString = "\n";
      Iterator iters = attrs.keySet().iterator();

      while(true) {
         String key;
         boolean isExcluded;
         String permission;
         do {
            if (!iters.hasNext()) {
               return retString;
            }

            key = (String)iters.next();
            Iterator it = excludedMBeans.iterator();
            isExcluded = false;

            while(it.hasNext()) {
               permission = (String)it.next();
               if (key.indexOf(permission) != -1) {
                  isExcluded = true;
               }
            }
         } while(isExcluded && !this.showExcluded());

         permission = (String)attrs.get(key);
         this.println(permission + "   " + key);
         retString = retString + permission + "   " + key + "   " + "\n";
      }
   }

   boolean showExcluded() {
      return this.showExcluded;
   }

   public void setShowExcluded(String bool) {
      if (this.getBoolean(bool)) {
         this.showExcluded = true;
      } else {
         this.showExcluded = false;
      }

   }

   public String printNameValuePairs(TreeMap attrs) {
      String retString = "\n";

      String name;
      String value;
      for(Iterator iters = attrs.keySet().iterator(); iters.hasNext(); retString = retString + name + "   " + value + "   " + "\n") {
         name = (String)iters.next();
         value = (String)attrs.get(name);
         this.println(name + "   " + value);
      }

      return retString;
   }

   String printAttrs(TreeSet set) {
      Iterator iter = set.iterator();

      String result;
      String s;
      for(result = ""; iter.hasNext(); result = result + s) {
         s = (String)iter.next();
         this.println(s);
      }

      return result;
   }

   boolean inNewTree() {
      return this.domainType.equals("RuntimeConfigServerDomain") || this.domainType.equals("RuntimeRuntimeServerDomain") || this.domainType.equals("ConfigDomainRuntime") || this.domainType.equals("RuntimeDomainRuntime") || this.domainType.equals("ConfigEdit") || this.domainType.equals("JNDI") || this.domainType.equals("JSR77");
   }

   ObjectName getObjectName() {
      return this.getObjectName(this.wlcmo);
   }

   Object getMBeanFromObjectName(ObjectName objName) throws Throwable {
      if (this.inNewTree()) {
         MBeanServerConnection connection = this.getMBSConnection(this.domainType);
         return MBeanServerInvocationHandler.newProxyInstance(connection, objName);
      } else {
         return this.home.getProxy(objName);
      }
   }

   ObjectName getObjectName(Object cmo) {
      if (cmo == null) {
         cmo = this.wlcmo;
      }

      try {
         String s;
         if (this.domainType.equals("Custom_Domain")) {
            s = (String)this.prompts.peek();
            return new ObjectName(s);
         }

         if (this.domainType.equals("DomainCustom_Domain")) {
            s = (String)this.prompts.peek();
            return new ObjectName(s);
         }
      } catch (MalformedObjectNameException var3) {
         return null;
      }

      if (cmo instanceof ObjectName) {
         return (ObjectName)cmo;
      } else {
         if (Proxy.isProxyClass(cmo.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(cmo);
            if (handler instanceof MBeanServerInvocationHandler) {
               return ((MBeanServerInvocationHandler)handler)._getObjectName();
            }

            if (cmo instanceof WebLogicMBean) {
               return ((WebLogicMBean)cmo).getObjectName();
            }

            if (cmo instanceof StandardInterface) {
               return ((StandardInterface)cmo).wls_getObjectName();
            }
         } else {
            if (cmo instanceof WebLogicMBean) {
               return ((WebLogicMBean)cmo).getObjectName();
            }

            if (cmo instanceof StandardInterface) {
               return ((StandardInterface)cmo).wls_getObjectName();
            }
         }

         return null;
      }
   }

   ObjectName[] getObjectNames(Object cmo) {
      if (cmo instanceof ObjectName[]) {
         return (ObjectName[])((ObjectName[])cmo);
      } else {
         Object[] cmos = (Object[])((Object[])cmo);
         ObjectName[] ons = new ObjectName[cmos.length];

         for(int i = 0; i < cmos.length; ++i) {
            if (Proxy.isProxyClass(cmos[i].getClass())) {
               InvocationHandler handler = Proxy.getInvocationHandler(cmos[i]);
               if (handler instanceof MBeanServerInvocationHandler) {
                  ons[i] = ((MBeanServerInvocationHandler)handler)._getObjectName();
               } else if (cmos[i] instanceof WebLogicMBean) {
                  ons[i] = ((WebLogicMBean)cmo).getObjectName();
               } else if (cmos[i] instanceof StandardInterface) {
                  ons[i] = ((StandardInterface)cmos[i]).wls_getObjectName();
               }
            } else if (cmos[i] instanceof WebLogicMBean) {
               ons[i] = ((WebLogicMBean)cmos[i]).getObjectName();
            } else if (cmos[i] instanceof StandardInterface) {
               ons[i] = ((StandardInterface)cmos[i]).wls_getObjectName();
            }
         }

         return ons;
      }
   }

   MBeanInfo getMBeanInfo(Object cmo) throws ScriptException {
      if (cmo == null) {
         cmo = this.wlcmo;
      }

      try {
         if (Proxy.isProxyClass(cmo.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(cmo);
            if (handler instanceof MBeanServerInvocationHandler) {
               return this.getMBSConnection(this.domainType).getMBeanInfo(((MBeanServerInvocationHandler)handler)._getObjectName());
            }
         }

         if (cmo instanceof WebLogicMBean) {
            return this.getMBSConnection(this.domainType).getMBeanInfo(((WebLogicMBean)cmo).getObjectName());
         }

         if (cmo instanceof StandardInterface) {
            return this.getMBSConnection(this.domainType).getMBeanInfo(((StandardInterface)cmo).wls_getObjectName());
         }
      } catch (Throwable var3) {
         this.throwWLSTException("Error getting the MBeanInfo for MBean", var3);
      }

      return null;
   }

   WebLogicMBean getCurrentRootMBean() {
      if (this.domainType.equals("RuntimeConfigServerDomain")) {
         return this.runtimeDomainMBean;
      } else if (this.domainType.equals("RuntimeRuntimeServerDomain")) {
         return this.runtimeServerRuntimeMBean;
      } else if (this.domainType.equals("ConfigDomainRuntime")) {
         return this.configDomainRuntimeDRMBean;
      } else if (this.domainType.equals("RuntimeDomainRuntime")) {
         return this.runtimeDomainRuntimeDRMBean;
      } else if (this.domainType.equals("Domain")) {
         return this.compatDomainMBean;
      } else if (this.domainType.equals("DomainConfig")) {
         return this.compatDomainMBean;
      } else if (this.domainType.equals("DomainRuntime")) {
         return this.compatDomainRuntimeMBean;
      } else if (this.domainType.equals("ConfigEdit")) {
         return this.editDomainMBean;
      } else {
         return this.domainType.equals("JSR77") ? null : null;
      }
   }

   WebLogicMBean getRootMBean(String domainType) {
      if (domainType.equals("RuntimeConfigServerDomain")) {
         return this.runtimeDomainMBean;
      } else if (domainType.equals("RuntimeRuntimeServerDomain")) {
         return this.runtimeServerRuntimeMBean;
      } else if (domainType.equals("ConfigDomainRuntime")) {
         return this.configDomainRuntimeDRMBean;
      } else if (domainType.equals("RuntimeDomainRuntime")) {
         return this.runtimeDomainRuntimeDRMBean;
      } else if (domainType.equals("Domain")) {
         return this.compatDomainMBean;
      } else if (domainType.equals("DomainConfig")) {
         return this.compatDomainMBean;
      } else if (domainType.equals("ConfigEdit")) {
         return this.editDomainMBean;
      } else if (domainType.equals("JSR77")) {
         return null;
      } else if (domainType.equals("DomainRuntime")) {
         return (WebLogicMBean)(this.isAdminServer ? this.compatDomainRuntimeMBean : this.compatServerRuntimeMBean);
      } else {
         return null;
      }
   }

   MBeanServerConnection getMBSConnection(String domainType) {
      if (domainType == null) {
         domainType = this.domainType;
      }

      if (domainType.equals("RuntimeConfigServerDomain")) {
         return this.runtimeMSC;
      } else if (domainType.equals("RuntimeRuntimeServerDomain")) {
         return this.runtimeMSC;
      } else if (domainType.equals("ConfigDomainRuntime")) {
         return this.domainRTMSC;
      } else if (domainType.equals("RuntimeDomainRuntime")) {
         return this.domainRTMSC;
      } else if (domainType.equals("Domain")) {
         return this.compatMBS;
      } else if (domainType.equals("DomainConfig")) {
         return this.compatMBS;
      } else if (domainType.equals("DomainRuntime")) {
         return this.compatMBS;
      } else if (domainType.equals("Custom_Domain")) {
         return this.runtimeMSC;
      } else if (domainType.equals("DomainCustom_Domain")) {
         return this.domainRTMSC;
      } else if (domainType.equals("ConfigEdit")) {
         return this.editMSC;
      } else {
         return domainType.equals("JSR77") ? this.jsr77MSC : null;
      }
   }

   String getMBeanServerNameFromTree(String treeName) {
      if (treeName.equals("RuntimeConfigServerDomain")) {
         return "RuntimeMBeanServer";
      } else if (treeName.equals("RuntimeRuntimeServerDomain")) {
         return "RuntimeMBeanServer";
      } else if (treeName.equals("ConfigDomainRuntime")) {
         return "DomainRuntimeMBeanServer";
      } else if (treeName.equals("RuntimeDomainRuntime")) {
         return "DomainRuntimeMBeanServer";
      } else if (treeName.equals("Domain")) {
         return "DeprecatedMBeanServer";
      } else if (treeName.equals("DomainConfig")) {
         return "DeprecatedMBeanServer";
      } else if (treeName.equals("DomainRuntime")) {
         return "DeprecatedMBeanServer";
      } else if (treeName.equals("Custom_Domain")) {
         return "RuntimeMBeanServer";
      } else if (treeName.equals("DomainCustom_Domain")) {
         return "DomainRuntimeMBeanServer";
      } else if (treeName.equals("ConfigEdit")) {
         return "EditMBeanServer";
      } else {
         return treeName.equals("JSR77") ? "JSR77MBeanServer" : null;
      }
   }

   TreeSet getCustomMBeans(String objectNamePattern) throws ScriptException {
      TreeSet customBeans;
      if (this.customMBeanDomainObjNameMap.size() > 0) {
         customBeans = new TreeSet();
         Iterator ittt = this.customMBeanDomainObjNameMap.keySet().iterator();

         while(ittt.hasNext()) {
            customBeans.add(ittt.next());
         }

         return customBeans;
      } else {
         customBeans = new TreeSet();

         try {
            ObjectName pattern;
            if (objectNamePattern != null) {
               pattern = new ObjectName(objectNamePattern);
            } else {
               pattern = new ObjectName("*:*");
            }

            MBeanServerConnection _mbsc;
            Set _s;
            Iterator __i;
            ObjectName objname;
            if (this.isCompatabilityServerEnabled) {
               _mbsc = this.getMBSConnection("Domain");
               _s = _mbsc.queryNames(pattern, (QueryExp)null);
               __i = _s.iterator();

               while(__i.hasNext()) {
                  objname = (ObjectName)__i.next();
                  if (this.isCustomMBean(objname, _mbsc)) {
                     this.addCustomToMap(objname);
                  }
               }
            }

            _mbsc = this.getMBSConnection("Custom_Domain");
            _s = _mbsc.queryNames(pattern, (QueryExp)null);
            __i = _s.iterator();

            while(__i.hasNext()) {
               objname = (ObjectName)__i.next();
               if (this.isCustomMBean(objname, _mbsc)) {
                  this.addCustomToMap(objname);
               }
            }
         } catch (Throwable var8) {
            this.throwWLSTException("Error getting the Custom MBeans", var8);
         }

         return customBeans;
      }
   }

   TreeSet getDomainCustomMBeans(String objectNamePattern) throws ScriptException {
      TreeSet domainCustomBeans;
      if (this.domainCustomMBeanDomainObjNameMap.size() > 0) {
         domainCustomBeans = new TreeSet();
         Iterator ittt = this.domainCustomMBeanDomainObjNameMap.keySet().iterator();

         while(ittt.hasNext()) {
            domainCustomBeans.add(ittt.next());
         }

         return domainCustomBeans;
      } else {
         domainCustomBeans = new TreeSet();

         try {
            ObjectName pattern;
            if (objectNamePattern != null) {
               pattern = new ObjectName(objectNamePattern);
            } else {
               pattern = new ObjectName("*:*");
            }

            MBeanServerConnection _mbsc = this.getMBSConnection("DomainCustom_Domain");
            Set _s = _mbsc.queryNames(pattern, (QueryExp)null);
            Iterator __i = _s.iterator();

            while(__i.hasNext()) {
               ObjectName objname = (ObjectName)__i.next();
               if (this.isCustomMBean(objname, _mbsc)) {
                  this.addDomainCustomToMap(objname);
               }
            }
         } catch (Throwable var8) {
            this.throwWLSTException("Error getting the Domain Custom MBeans. ", var8);
         }

         return domainCustomBeans;
      }
   }

   private boolean isCustomMBean(ObjectName on, MBeanServerConnection mbsc) throws Throwable {
      try {
         MBeanInfo mbeanInfo = mbsc.getMBeanInfo(on);
         if (!(mbeanInfo instanceof ModelMBeanInfo)) {
            return true;
         } else {
            ModelMBeanInfo modelInfo = (ModelMBeanInfo)mbeanInfo;
            Boolean custom = (Boolean)modelInfo.getMBeanDescriptor().getFieldValue("com.bea.custom");
            if (custom != null && custom) {
               return true;
            } else {
               String clzName = (String)modelInfo.getMBeanDescriptor().getFieldValue("interfaceclassname");
               if (clzName != null) {
                  try {
                     this.mbeanTypeService.getMBeanInfo(clzName);
                     return false;
                  } catch (Exception var8) {
                     return true;
                  }
               } else {
                  return true;
               }
            }
         }
      } catch (Throwable var9) {
         return false;
      }
   }

   private void addCustomToMap(ObjectName oname) {
      Iterator it = this.customMBeanDomainObjNameMap.keySet().iterator();
      List onames = new ArrayList();
      if (this.customMBeanDomainObjNameMap.containsKey(oname.getDomain())) {
         onames = (List)this.customMBeanDomainObjNameMap.get(oname.getDomain());
         ((List)onames).add(oname.toString());
      } else {
         ((List)onames).add(oname.toString());
      }

      this.customMBeanDomainObjNameMap.put(oname.getDomain(), onames);
   }

   private void addDomainCustomToMap(ObjectName oname) {
      List onames = new ArrayList();
      if (this.domainCustomMBeanDomainObjNameMap.containsKey(oname.getDomain())) {
         onames = (List)this.domainCustomMBeanDomainObjNameMap.get(oname.getDomain());
         ((List)onames).add(oname.toString());
      } else {
         ((List)onames).add(oname.toString());
      }

      this.domainCustomMBeanDomainObjNameMap.put(oname.getDomain(), onames);
   }

   boolean inConfigRT() {
      return this.domainType.equals("RuntimeConfigServerDomain") || this.domainType.equals("RuntimeRuntimeServerDomain");
   }

   boolean inDomainRT() {
      return this.domainType.equals("ConfigDomainRuntime") || this.domainType.equals("RuntimeDomainRuntime");
   }
}
