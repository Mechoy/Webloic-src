package weblogic.wsee.reliability2.saf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.messaging.saf.SAFEndpoint;
import weblogic.messaging.saf.SAFErrorAwareEndpointManager;
import weblogic.messaging.saf.SAFErrorHandler;
import weblogic.messaging.saf.SAFRequest;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WsrmSAFEndpointManager implements SAFErrorAwareEndpointManager {
   private static final Logger LOGGER = Logger.getLogger(WsrmSAFEndpointManager.class.getName());
   private HashMap endpoints = new HashMap();
   private Map errorHandlers = Collections.synchronizedMap(new HashMap());
   private static RuntimeAccess _runtimeAccess;

   public WsrmSAFEndpointManager() {
      ServerStateChangeListener var1 = new ServerStateChangeListener();
      _runtimeAccess.getServerRuntime().addPropertyChangeListener(var1);
   }

   public synchronized void addEndpoint(String var1, SAFEndpoint var2) {
      this.endpoints.put(var1, var2);
   }

   public synchronized void removeEndpoint(String var1) {
      this.endpoints.remove(var1);
   }

   public SAFEndpoint getEndpoint(String var1) {
      String var3;
      try {
         var3 = (new URI(var1)).getPath();
      } catch (URISyntaxException var7) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, var7.toString(), var7);
         }

         throw new RuntimeException("Could not parse destination URL", var7);
      }

      Object var2;
      synchronized(this) {
         var2 = (SAFEndpoint)this.endpoints.get(var3);
      }

      if (var2 == null) {
         String var4;
         if (!var3.endsWith("/")) {
            var4 = var3 + "/";
            var2 = (SAFEndpoint)this.endpoints.get(var4);
         } else if (var3.endsWith("/")) {
            var4 = var3.substring(0, var3.length() - 1);
            var2 = (SAFEndpoint)this.endpoints.get(var4);
         }

         if (var2 == null) {
            var2 = new WsrmSAFEndpoint(var3);
            this.addEndpoint(var3, (SAFEndpoint)var2);
         }
      }

      return (SAFEndpoint)var2;
   }

   public void handleFailure(SAFErrorHandler var1, SAFRequest var2, ArrayList var3) {
      this.handleFailure(var1, var2, var3, new ArrayList());
   }

   public void handleFailure(SAFErrorHandler var1, SAFRequest var2, ArrayList var3, ArrayList var4) {
   }

   public SAFErrorHandler createErrorHandlerInstance() {
      return null;
   }

   public SAFErrorHandler getErrorHandler(String var1) {
      return (SAFErrorHandler)this.errorHandlers.get(var1);
   }

   static {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      _runtimeAccess = ManagementService.getRuntimeAccess(var0);
   }

   private class ServerStateChangeListener implements PropertyChangeListener {
      boolean _serverUp;

      public ServerStateChangeListener() {
         this.interpretState(WsrmSAFEndpointManager._runtimeAccess.getServerRuntime().getState());
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if ("State".equals(var1.getPropertyName())) {
            this.interpretState((String)var1.getNewValue());
         }

      }

      private void interpretState(String var1) {
         if ("RUNNING".equals(var1)) {
            this.setServerUp();
         } else {
            this.setServerDown();
         }

      }

      private void setServerDown() {
         this._serverUp = false;
      }

      private void setServerUp() {
         if (!this._serverUp) {
            this._serverUp = true;
         }

         this._serverUp = true;
      }

      public boolean isServerUp() {
         return this._serverUp;
      }
   }
}
