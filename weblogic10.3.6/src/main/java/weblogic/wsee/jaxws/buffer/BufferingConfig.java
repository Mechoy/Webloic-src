package weblogic.wsee.jaxws.buffer;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import weblogic.j2ee.descriptor.wl.BufferingConfigBean;
import weblogic.j2ee.descriptor.wl.BufferingQueueBean;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.management.configuration.WebServiceBufferingMBean;
import weblogic.management.configuration.WebServiceBufferingQueueMBean;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.config.PerServicePropertyAccessor;
import weblogic.wsee.jaxws.config.Property;
import weblogic.wsee.jaxws.config.PropertyAccessor;
import weblogic.wsee.jaxws.config.PropertyContainer;
import weblogic.wsee.jaxws.config.VmWidePropertyAccessor;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;

public class BufferingConfig {
   public static Service getServiceConfig(@Nullable ServerTubeAssemblerContext var0) {
      return getServiceConfig((ServerTubeAssemblerContext)var0, (Packet)null);
   }

   public static Service getServiceConfig(@Nullable ServerTubeAssemblerContext var0, @Nullable Packet var1) {
      Service var2 = new Service(var0, var1);
      return var2;
   }

   public static Service getServiceConfig(@Nullable ClientTubeAssemblerContext var0) {
      return getServiceConfig((ClientTubeAssemblerContext)var0, (Packet)null);
   }

   public static Service getServiceConfig(@Nullable ClientTubeAssemblerContext var0, @Nullable Packet var1) {
      Service var2 = new Service(var0, var1);
      return var2;
   }

   public static class Queue extends PropertyContainer implements Serializable {
      private static final long serialVersionUID = 1L;
      private transient Property<Boolean> _enabled;
      private transient Property<String> _jndiName;
      private transient Property<String> _connectionFactoryJndiName;
      private transient Property<Boolean> _transactionEnabled;

      protected List<Property> getPropertyFields() {
         ArrayList var1 = new ArrayList();
         var1.add(this._enabled);
         var1.add(this._jndiName);
         var1.add(this._connectionFactoryJndiName);
         var1.add(this._transactionEnabled);
         return var1;
      }

      public Queue(@NotNull WebServiceLogicalStoreMBean var1, @NotNull String var2, @Nullable BufferingQueueBean var3, @NotNull WebServiceBufferingQueueMBean var4) {
         this._enabled = new Property("Enabled", Boolean.class, false, new PropertyAccessor[]{new PerServicePropertyAccessor(var3), new VmWidePropertyAccessor((Class)null, var4)});
         this._jndiName = new Property(var2, String.class, (Serializable)null, new PropertyAccessor[]{new VmWidePropertyAccessor(var1)});
         this._connectionFactoryJndiName = new Property("ConnectionFactoryJndiName", String.class, (Serializable)null, new PropertyAccessor[]{new PerServicePropertyAccessor(var3), new VmWidePropertyAccessor(var4)});
         this._transactionEnabled = new Property("TransactionEnabled", Boolean.class, false, new PropertyAccessor[]{new PerServicePropertyAccessor(var3), new VmWidePropertyAccessor(var4)});
      }

      public boolean isEnabled() {
         return (Boolean)this._enabled.getValue();
      }

      public String getJndiName() {
         return (String)this._jndiName.getValue();
      }

      public String getConnectionFactoryJndiName() {
         return (String)this._connectionFactoryJndiName.getValue();
      }

      public boolean isTransactionEnabled() {
         return (Boolean)this._transactionEnabled.getValue();
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      }
   }

   public abstract static class Common extends PropertyContainer {
      protected WebServiceMBean _wsMBean = WebServiceMBeanFactory.getInstance();
      protected PersistenceConfig.Common _persistConfig;

      public Common(@Nullable ServerTubeAssemblerContext var1) {
         this._persistConfig = PersistenceConfig.getServiceConfig(var1);
      }

      public Common(@Nullable ClientTubeAssemblerContext var1) {
         this._persistConfig = PersistenceConfig.getClientConfig(var1);
      }

      public PersistenceConfig.Common getPersistenceConfig() {
         return this._persistConfig;
      }
   }

   public static class Service extends Common {
      private Queue _requestQueue;
      private Queue _responseQueue;
      private Property<Integer> _retryCount;
      private Property<String> _retryDelay;

      protected List<Property> getPropertyFields() {
         ArrayList var1 = new ArrayList();
         var1.add(this._retryCount);
         var1.add(this._retryDelay);
         return var1;
      }

      public Service(@Nullable ServerTubeAssemblerContext var1, @Nullable Packet var2) {
         super(var1);
         PortComponentBean var3 = null;
         if (var1 != null) {
            var3 = ConfigUtil.getPortComponentBeanForService(var1);
         }

         BufferingConfigBean var4 = var3 != null ? var3.getBufferingConfig() : null;
         this.commonConstructorCode(var4, var2);
      }

      public Service(@Nullable ClientTubeAssemblerContext var1, @Nullable Packet var2) {
         super(var1);
         this.commonConstructorCode((BufferingConfigBean)null, var2);
      }

      private void commonConstructorCode(@Nullable BufferingConfigBean var1, @Nullable Packet var2) {
         if (var1 != null && !var1.isCustomized()) {
            var1 = null;
         }

         WebServiceMBean var3 = WebServiceMBeanFactory.getInstance();
         WebServiceBufferingMBean var4 = var3.getWebServiceBuffering();
         WebServiceLogicalStoreMBean var5 = ConfigUtil.getLogicalStoreMBean(this._persistConfig.getLogicalStoreName());
         this._requestQueue = new Queue(var5, "RequestBufferingQueueJndiName", var1 != null ? var1.getRequestQueue() : null, var4.getWebServiceRequestBufferingQueue());
         this._responseQueue = new Queue(var5, "ResponseBufferingQueueJndiName", var1 != null ? var1.getResponseQueue() : null, var4.getWebServiceResponseBufferingQueue());
         this._retryCount = new Property("RetryCount", Integer.class, 3, new PropertyAccessor[]{new PerServicePropertyAccessor(var1), new VmWidePropertyAccessor((Class)null, var4)});
         this._retryDelay = new Property("RetryDelay", String.class, "P0DT30S", new PropertyAccessor[]{new PerServicePropertyAccessor(var1), new VmWidePropertyAccessor((Class)null, var4)});
      }

      public Queue getRequestQueue() {
         return this._requestQueue;
      }

      public Queue getResponseQueue() {
         return this._responseQueue;
      }

      public int getRetryCount() {
         return (Integer)this._retryCount.getValue();
      }

      public String getRetryDelay() {
         return (String)this._retryDelay.getValue();
      }
   }
}
