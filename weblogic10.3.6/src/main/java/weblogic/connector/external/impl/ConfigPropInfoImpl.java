package weblogic.connector.external.impl;

import weblogic.connector.external.ConfigPropInfo;
import weblogic.j2ee.descriptor.ConfigPropertyBean;

public class ConfigPropInfoImpl implements ConfigPropInfo {
   ConfigPropertyBean configPropertyBean;
   String value = null;

   public ConfigPropInfoImpl(ConfigPropertyBean var1, String var2) {
      this.configPropertyBean = var1;
      this.value = var2;
   }

   public String getDescription() {
      return this.configPropertyBean.getDescriptions() != null && this.configPropertyBean.getDescriptions().length > 0 ? this.configPropertyBean.getDescriptions()[0] : null;
   }

   public String[] getDescriptions() {
      return this.configPropertyBean.getDescriptions();
   }

   public String getName() {
      return this.configPropertyBean.getConfigPropertyName();
   }

   public String getType() {
      return this.configPropertyBean.getConfigPropertyType();
   }

   public String getValue() {
      return this.value;
   }

   public ConfigPropertyBean getConfigPropertyBean() {
      return this.configPropertyBean;
   }
}
