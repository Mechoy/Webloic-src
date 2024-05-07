package weblogic.wsee.mc.api;

import com.sun.xml.ws.api.FeatureConstructor;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Binding;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.WseeMCLogger;

public class McFeature extends WebServiceFeature implements ServiceCreationInterceptor {
   private static final Logger LOGGER = Logger.getLogger(McFeature.class.getName());
   private static final String ID = "MakeConnection Feature";
   private static final String DEFAULT_EXPIRES_INTERVAL = "P1D";
   private static final String DEFAULT_MESSAGE_INTERVAL = "P0DT5S";
   private WSBinding _binding;
   private String _messageInterval;
   private String _expiresInterval;
   private boolean _exponentialBackoff;
   private boolean _useMcWithSyncInvoke;
   private boolean _nonPersistent;

   @FeatureConstructor
   public McFeature() {
      super.enabled = true;
      this._nonPersistent = !KernelStatus.isServer();
      this._messageInterval = "P0DT5S";
      this._expiresInterval = "P1D";
      this._useMcWithSyncInvoke = true;
      this._exponentialBackoff = false;
   }

   public McFeature(boolean var1) {
      super.enabled = true;
      this._nonPersistent = !KernelStatus.isServer();
      this._messageInterval = "P0DT5S";
      this._expiresInterval = "P1D";
      this._exponentialBackoff = false;
      this._useMcWithSyncInvoke = var1;
   }

   public String getID() {
      return "MakeConnection Feature";
   }

   public void setUseMcWithSyncInvoke(boolean var1) {
      this._useMcWithSyncInvoke = var1;
   }

   public boolean isUseMcWithSyncInvoke() {
      return this._useMcWithSyncInvoke;
   }

   public void setInterval(String var1) {
      this._messageInterval = var1;
   }

   public String getInterval() {
      return this._messageInterval;
   }

   public void setExpires(String var1) {
      this._expiresInterval = var1;
   }

   public String getExpires() {
      return this._expiresInterval;
   }

   public boolean isNonPersistent() {
      return this._nonPersistent;
   }

   public void setNonPersistent(boolean var1) {
      this._nonPersistent = var1;
   }

   public void setExponentialBackoff(boolean var1) {
      this._exponentialBackoff = var1;
   }

   public boolean isUseExponentialBackoff() {
      return this._exponentialBackoff;
   }

   public void setBinding(WSBinding var1) {
      this._binding = var1;
   }

   public void postCreateDispatch(WSBindingProvider var1) {
      if (this._binding != null) {
         if (this._binding.getFeature(AsyncClientTransportFeature.class) != null) {
            throw new WebServiceException(WseeMCLogger.logCannotUseAsyncClientTransportLoggable().getMessage());
         }
      } else {
         Binding var2 = var1.getBinding();
         if (var2 instanceof WSBinding) {
            if (((WSBinding)var2).getFeature(AsyncClientTransportFeature.class) != null) {
               throw new WebServiceException(WseeMCLogger.logCannotUseAsyncClientTransportLoggable().getMessage());
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Unable to check feature compatability as binding is not a WS Binding");
         }
      }

   }

   public void postCreateProxy(WSBindingProvider var1, Class<?> var2) {
      this.postCreateDispatch(var1);
   }

   public static String getMcFeatureIDValue() {
      return "MakeConnection Feature";
   }
}
