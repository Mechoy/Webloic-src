package weblogic.wsee.config;

import weblogic.management.configuration.WebServicePhysicalStoreMBean;
import weblogic.wsee.jaxws.framework.ConfigUtil;

public class WebServicePhysicalStoreMBeanImpl extends DummyConfigurationMBeanImpl implements WebServicePhysicalStoreMBean {
   private String _storeType = "FILE";
   private String _location = ConfigUtil.getStandaloneClientStoreDir().getPath();
   private String _synchronousWritePolicy = "CACHE_FLUSH";

   public WebServicePhysicalStoreMBeanImpl(String var1) {
      super(var1);
   }

   public String getStoreType() {
      return this._storeType;
   }

   public void setStoreType(String var1) {
      this._storeType = var1;
   }

   public String getLocation() {
      return this._location;
   }

   public void setLocation(String var1) {
      this._location = var1;
   }

   public String getSynchronousWritePolicy() {
      return this._synchronousWritePolicy;
   }

   public void setSynchronousWritePolicy(String var1) {
      this._synchronousWritePolicy = var1;
   }
}
