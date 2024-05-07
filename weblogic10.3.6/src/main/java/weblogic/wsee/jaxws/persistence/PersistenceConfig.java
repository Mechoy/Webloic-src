package weblogic.wsee.jaxws.persistence;

import com.sun.istack.Nullable;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.client.Stub;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import weblogic.j2ee.descriptor.wl.PersistenceConfigBean;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.wsee.WseePersistLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.config.PerClientPropertyAccessor;
import weblogic.wsee.jaxws.config.PerServicePropertyAccessor;
import weblogic.wsee.jaxws.config.Property;
import weblogic.wsee.jaxws.config.PropertyAccessor;
import weblogic.wsee.jaxws.config.PropertyContainer;
import weblogic.wsee.jaxws.config.VmWidePropertyAccessor;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.jaxws.spi.WLSServiceDelegate;
import weblogic.wsee.ws.WsPort;

public class PersistenceConfig {
   public static Client getClientConfig(@Nullable PortInfoBean var0) {
      Client var1 = new Client(var0);
      return var1;
   }

   public static Client getClientConfig(@Nullable ClientTubeAssemblerContext var0) {
      Client var1 = new Client(var0);
      return var1;
   }

   public static Client getClientConfig(@Nullable Stub var0) {
      Client var1 = new Client((WLSServiceDelegate)var0.getService(), var0.getPortName().getLocalPart());
      return var1;
   }

   public static Service getServiceConfig(@Nullable ServerTubeAssemblerContext var0) {
      Service var1 = new Service(var0);
      return var1;
   }

   public static Service getServiceConfig(@Nullable WsPort var0) {
      Service var1 = new Service(var0);
      return var1;
   }

   public abstract static class Common extends PropertyContainer {
      protected Property<String> _logicalStoreName;

      protected PersistenceConfigBean getPersistenceConfigBeanFromWsPort(WsPort var1) {
         PersistenceConfigBean var2 = null;
         if (var1 != null) {
            PortComponentBean var3 = var1.getPortComponent();
            var2 = var3 != null ? var3.getPersistenceConfig() : null;
         }

         return var2;
      }

      public WebServiceLogicalStoreMBean getLogicalStoreMBean() {
         WebServiceMBean var1 = WebServiceMBeanFactory.getInstance();
         WebServiceLogicalStoreMBean[] var2 = var1.getWebServicePersistence().getWebServiceLogicalStores();
         String var3 = this.getLogicalStoreName();
         WebServiceLogicalStoreMBean[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            WebServiceLogicalStoreMBean var7 = var4[var6];
            if (var7.getName().equals(var3)) {
               return var7;
            }
         }

         throw new IllegalArgumentException(WseePersistLogger.logLogicalStoreNotFoundLoggable(var3).getMessage());
      }

      public String getLogicalStoreName() {
         return (String)this._logicalStoreName.getValue();
      }

      protected List<Property> getPropertyFields() {
         ArrayList var1 = new ArrayList();
         var1.add(this._logicalStoreName);
         return var1;
      }
   }

   public static class Service extends Common {
      public Service(@Nullable ServerTubeAssemblerContext var1) {
         PersistenceConfigBean var2 = null;
         if (var1 != null) {
            PortComponentBean var3 = ConfigUtil.getPortComponentBeanForService(var1);
            var2 = var3 != null ? var3.getPersistenceConfig() : null;
         }

         this.init(var2);
      }

      public Service(@Nullable WsPort var1) {
         PersistenceConfigBean var2 = this.getPersistenceConfigBeanFromWsPort(var1);
         this.init(var2);
      }

      protected void init(@Nullable PersistenceConfigBean var1) {
         if (var1 != null && !var1.isCustomized()) {
            var1 = null;
         }

         WebServiceMBean var2 = WebServiceMBeanFactory.getInstance();
         this._logicalStoreName = new Property("DefaultLogicalStoreName", String.class, "WseeStore", new PropertyAccessor[]{new PerServicePropertyAccessor(var1), new VmWidePropertyAccessor(var2.getWebServicePersistence())});
      }
   }

   public static class Client extends Common {
      public static final String LOGICAL_STORE_NAME_PROP = "weblogic.wsee.persistence.DefaultLogicalStoreName";

      public Client(@Nullable ClientTubeAssemblerContext var1) {
         Map var2 = null;
         if (var1 != null) {
            var2 = ConfigUtil.getServiceRefPropsForClient(var1);
         }

         this.init(var2);
      }

      public Client(@Nullable WLSServiceDelegate var1, String var2) {
         Map var3 = ConfigUtil.getServiceRefPropsForClient(var1, var2);
         this.init(var3);
      }

      public Client(@Nullable PortInfoBean var1) {
         Map var2 = null;
         if (var1 != null) {
            var2 = ConfigUtil.getServiceRefProps(var1);
         }

         this.init(var2);
      }

      protected void init(@Nullable Map<String, Object> var1) {
         WebServiceMBean var2 = WebServiceMBeanFactory.getInstance();
         this._logicalStoreName = new Property("DefaultLogicalStoreName", String.class, "WseeStore", new PropertyAccessor[]{new PerClientPropertyAccessor("weblogic.wsee.persistence.DefaultLogicalStoreName", var1), new VmWidePropertyAccessor(var2.getWebServicePersistence())});
      }
   }
}
