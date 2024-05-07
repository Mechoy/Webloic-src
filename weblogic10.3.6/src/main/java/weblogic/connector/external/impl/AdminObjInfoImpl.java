package weblogic.connector.external.impl;

import java.util.Hashtable;
import weblogic.connector.external.AdminObjInfo;
import weblogic.j2ee.descriptor.AdminObjectBean;
import weblogic.j2ee.descriptor.ConfigPropertyBean;
import weblogic.j2ee.descriptor.wl.AdminObjectGroupBean;
import weblogic.j2ee.descriptor.wl.AdminObjectInstanceBean;
import weblogic.j2ee.descriptor.wl.AdminObjectsBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;

public class AdminObjInfoImpl implements AdminObjInfo {
   AdminObjectBean adminObjBean = null;
   AdminObjectInstanceBean adminInstanceBean = null;
   WeblogicConnectorBean wlConnBean = null;
   AdminObjectsBean adminObjsBean = null;
   AdminObjectGroupBean adminGroupBean = null;

   public AdminObjInfoImpl(AdminObjectBean var1, AdminObjectInstanceBean var2, WeblogicConnectorBean var3, AdminObjectGroupBean var4) {
      this.adminObjBean = var1;
      this.adminInstanceBean = var2;
      this.wlConnBean = var3;
      this.adminObjsBean = var3.getAdminObjects();
      this.adminGroupBean = var4;
   }

   public String getInterface() {
      return this.adminObjBean.getAdminObjectInterface();
   }

   public String getAdminObjClass() {
      return this.adminObjBean.getAdminObjectClass();
   }

   public Hashtable getConfigProps() {
      Hashtable var1 = new Hashtable();
      ConfigPropertyBean[] var2 = this.adminObjBean.getConfigProperties();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         ConfigPropInfoImpl var3 = new ConfigPropInfoImpl(var2[var4], var2[var4].getConfigPropertyValue());
         var3 = DDLayerUtils.getConfigPropInfoWithOverrides(var3, this.adminObjsBean.getDefaultProperties());
         var3 = DDLayerUtils.getConfigPropInfoWithOverrides(var3, this.adminGroupBean.getDefaultProperties());
         var3 = DDLayerUtils.getConfigPropInfoWithOverrides(var3, this.adminInstanceBean.getProperties());
         if (var3.getName() != null) {
            var1.put(var3.getName(), var3);
         }
      }

      return var1;
   }

   public String getJndiName() {
      return this.adminInstanceBean.getJNDIName();
   }

   public String getResourceLink() {
      return this.adminInstanceBean.getResourceLink();
   }

   public String getKey() {
      String var1 = null;
      String var2 = this.getJndiName();
      if (var2 != null && var2.length() != 0) {
         var1 = var2;
      } else {
         var1 = this.getResourceLink();
      }

      return var1;
   }
}
