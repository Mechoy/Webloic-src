package weblogic.wsee.monitoring;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.t3.srvr.ServerRuntime;

public abstract class WseeRuntimeMBeanDelegate<M extends RuntimeMBean, D extends WseeBaseRuntimeData> extends RuntimeMBeanDelegate {
   private static final Logger LOGGER = Logger.getLogger(WseeRuntimeMBeanDelegate.class.getName());
   public static final String RELIABLE_SECURE_PROFILE_ENABLED_PROPERTY = "weblogic.wsee.ReliableSecureProfileEnabled";
   private static boolean isReliableSecureProfileEnabled;
   private boolean _isProxy;
   private WseeRuntimeMBeanDelegate<M, D> _proxy;
   private WseeRuntimeMBeanDelegate<M, D> _master;
   private D _data;
   private String _registeredName;

   public static final boolean isReliableSecureProfileEnabled() {
      return isReliableSecureProfileEnabled;
   }

   protected abstract WseeRuntimeMBeanDelegate<M, D> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException;

   public WseeRuntimeMBeanDelegate<M, D> createProxy(String var1, RuntimeMBean var2) throws ManagementException {
      if (this.isProxy()) {
         throw new ManagementException("Cannot create a proxy for MBean " + this.getName() + " because it already is a proxy");
      } else {
         this._proxy = this.internalCreateProxy(var1, var2);
         this._proxy._isProxy = true;
         this._proxy.setData(this._data);
         return this._proxy;
      }
   }

   protected WseeRuntimeMBeanDelegate(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate<M, D> var3, boolean var4) throws ManagementException {
      super(var1, var2, var4);
      this._isProxy = var3 != null;
      if (var2 instanceof WseeRuntimeMBeanDelegate && this._isProxy != ((WseeRuntimeMBeanDelegate)var2).isProxy()) {
         throw new ManagementException("Attempt to create a WSEE MBean where part of the hierarchy isn't doesn't match proxy/non-proxy " + this.getClass().getSimpleName() + " name=" + var1 + " isProxy=" + this._isProxy + " parent isProxy=" + ((WseeRuntimeMBeanDelegate)var2).isProxy());
      } else {
         this._master = var3;
      }
   }

   public boolean isProxy() {
      return this._isProxy;
   }

   public WseeRuntimeMBeanDelegate getProxy() {
      return this.isProxy() ? null : this._proxy;
   }

   public WseeRuntimeMBeanDelegate getMaster() {
      return this.isProxy() ? this._master : null;
   }

   public D getData() {
      return this._data;
   }

   public void setData(D var1) {
      this._data = var1;
   }

   public void register() throws ManagementException {
      if (!this.isRegistered()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Registering: " + this.toString());
         }

         this._registeredName = this.getQualifiedName();
         super.register();
         if (this._proxy != null) {
            this._proxy.register();
         }

      }
   }

   public void unregister() throws ManagementException {
      if (this.isRegistered()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Un-Registering: " + this.toString());
         }

         if (this._proxy != null) {
            this._proxy.unregister();
            this._proxy = null;
         }

         super.unregister();
      }
   }

   public String getQualifiedName() {
      if (this._registeredName != null) {
         return this._registeredName;
      } else {
         StringBuffer var1 = new StringBuffer();

         for(Object var2 = this; var2 != null; var2 = ((WebLogicMBean)var2).getParent()) {
            var1.insert(0, "]");
            var1.insert(0, var2.getClass().getSimpleName());
            var1.insert(0, "[");
            var1.insert(0, ((WebLogicMBean)var2).getName());
            if (var2 instanceof WseeRuntimeMBeanDelegate && ((WseeRuntimeMBeanDelegate)var2).isProxy()) {
               var1.insert(0, "Proxy:");
            }

            var1.insert(0, "/");
         }

         return var1.toString();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString());
      var1.append(" - ");
      var1.append(this.getQualifiedName());
      return var1.toString();
   }

   public int hashCode() {
      return this.getQualifiedName().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof WseeRuntimeMBeanDelegate)) {
         return false;
      } else {
         WseeRuntimeMBeanDelegate var2 = (WseeRuntimeMBeanDelegate)var1;
         return var2.getQualifiedName().equals(this.getQualifiedName());
      }
   }

   public void setParent(WebLogicMBean var1) {
      ServerRuntime.theOne().removeChild(this);
      this.parent = (RuntimeMBeanDelegate)var1;
   }

   static {
      try {
         String var0 = System.getProperty("weblogic.wsee.ReliableSecureProfileEnabled");
         isReliableSecureProfileEnabled = var0 == null ? true : var0.equalsIgnoreCase("true");
      } catch (Exception var1) {
         isReliableSecureProfileEnabled = false;
         var1.printStackTrace();
      }

   }
}
