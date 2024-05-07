package weblogic.wsee.reliability;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.server.ServletEndpointContext;
import javax.xml.soap.SOAPMessage;
import weblogic.i18n.logging.NonCatalogLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.messaging.saf.SAFEndpoint;
import weblogic.messaging.saf.SAFErrorAwareEndpointManager;
import weblogic.messaging.saf.SAFErrorHandler;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFResult;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.async.AsyncInvokeState;
import weblogic.wsee.async.AsyncInvokeStateObjectHandler;
import weblogic.wsee.async.AsyncPostCallContext;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.jws.container.Request;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.AccessException;
import weblogic.wsee.util.DirectInvokeUtil;
import weblogic.wsee.util.Verbose;

public class WsrmEndpointManager implements SAFErrorAwareEndpointManager {
   private static final boolean verbose = Verbose.isVerbose(WsrmEndpointManager.class);
   private static final String ENDPOINT_MANAGER_STORE = "weblogic.wsee.endpointmanager.store";
   private static final String ENDPOINT_MANAGER_IDLIST_KEY = "weblogic.wsee.endpointmanager.idlist";
   private HashMap endpoints = new HashMap();
   private Map errorHandlers = Collections.synchronizedMap(new HashMap());
   private static AuthenticatedSubject _kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static RuntimeAccess _runtimeAccess;
   private ServerStateChangeListener _stateChangeListener = new ServerStateChangeListener();
   private NonCatalogLogger _logger;

   public WsrmEndpointManager() {
      _runtimeAccess.getServerRuntime().addPropertyChangeListener(this._stateChangeListener);
      this._logger = new NonCatalogLogger("WsrmEndpointManager");
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
         if (verbose) {
            Verbose.logException(var7);
         }

         throw new JAXRPCException("Could not parse destination URL", var7);
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
      SOAPInvokeState var5 = (SOAPInvokeState)var2.getPayload();
      Map var6 = var5.getMessageContextProperties();
      String var7 = (String)var6.get("weblogic.wsee.reliabile.errorhandler");
      String var8 = (String)var6.get("weblogic.wsee.reliable.errorlistener");
      if (var7 == null && var8 == null) {
         Verbose.log((Object)("Messages not delivered for sequence " + var2.getConversationName() + ", Fault Codes are: "));
         Verbose.log((Object)var3);
      } else if (!this._stateChangeListener.isServerUp()) {
         SavedFailureNotification var9 = new SavedFailureNotification(var1, var2, var3, var4);

         try {
            this.storeFailureNotification(var9);
         } catch (Exception var11) {
            Verbose.log((Object)("Messages not delivered for sequence " + var2.getConversationName() + ", Fault Codes are: "));
            Verbose.log((Object)var3);
            throw new JAXRPCException(var11.toString(), var11);
         }
      } else {
         this.internalHandleFailure(var1, var2, var3, var4);
      }
   }

   public void internalHandleFailure(SAFErrorHandler var1, SAFRequest var2, ArrayList var3, ArrayList var4) {
      SOAPInvokeState var5 = (SOAPInvokeState)var2.getPayload();
      Map var6 = var5.getMessageContextProperties();
      String var7 = (String)var6.get("weblogic.wsee.reliabile.errorhandler");
      String var8 = (String)var6.get("weblogic.wsee.reliable.errorlistener");
      String var9 = (String)var6.get("weblogic.wsee.ws.dispatch.client.OperationName");
      String var10 = (String)var6.get("weblogic.wsee.stub.name");
      SOAPMessage var11 = var5.getSOAPMessage();
      String var12 = this.createFailureMessage(var3, var4);
      AsyncPostCallContext var13 = this.getAsyncPostCallContextForMessage(var11);
      ReliabilityErrorContextImpl var14 = new ReliabilityErrorContextImpl(var9, var10, var11, var12, var4, var13);
      if (var7 != null) {
         try {
            String var15 = (String)var6.get("weblogic.wsee.enclosing.classname");
            Class[] var16 = new Class[]{ReliabilityErrorContext.class};
            Object[] var17 = new Object[]{var14};
            String var18 = (String)var6.get("weblogic.wsee.enclosing.jws.serviceuri");
            String var19 = (String)var6.get("weblogic.wsee.conversation.ConversationId");
            Request var20 = new Request(var15, var7, var16, var17);
            DirectInvokeUtil.invoke(var18, var20, var19, (ServletEndpointContext)null);
         } catch (JAXRPCException var21) {
            throw var21;
         } catch (Throwable var22) {
            throw new JAXRPCException(var22);
         }
      }

      if (var8 != null) {
         ReliabilityErrorListener var23 = ReliabilityErrorListenerRegistry.getInstance().getListener(var8);
         if (var23 == null) {
            throw new JAXRPCException("Didn't find any registered ReliabilityErrorListener with key: " + var8);
         }

         var23.onReliabilityError(var14);
      }

   }

   private AsyncPostCallContext getAsyncPostCallContextForMessage(SOAPMessage var1) {
      String var2;
      try {
         var2 = var1.getSOAPPart().getEnvelope().getNamespaceURI();
      } catch (Exception var12) {
         throw new JAXRPCException(var12.toString(), var12);
      }

      boolean var3 = "http://www.w3.org/2003/05/soap-envelope".equals(var2);
      SoapMessageContext var4 = new SoapMessageContext(var3);
      var4.setMessage(var1);
      MessageIdHeader var5 = (MessageIdHeader)var4.getHeaders().getHeader(MessageIdHeader.TYPE);
      if (var5 == null) {
         return null;
      } else {
         String var6 = var5.getMessageId();
         WsStorage var7 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());

         AsyncInvokeState var8;
         try {
            var8 = (AsyncInvokeState)var7.persistentGet(var6);
            if (var8 == null) {
               return null;
            }
         } catch (PersistentStoreException var13) {
            if (verbose) {
               Verbose.logException(var13);
            }

            throw new JAXRPCException(var13);
         }

         synchronized(var8) {
            var8 = (AsyncInvokeState)var7.get(var6);
            return var8 == null ? null : var8.getAsyncPostCallContext();
         }
      }
   }

   private synchronized void storeFailureNotification(SavedFailureNotification var1) throws PersistentStoreException {
      if (var1 != null && var1.getSAFRequest() != null) {
         Verbose.log((Object)("Storing/deferring failure notification in WsrmEndpointManager for message: " + var1.getSAFRequest().getMessageId() + " and conversation: " + var1.getSAFRequest().getConversationName()));
         WsStorage var2 = WsStorageFactory.getStorage("weblogic.wsee.endpointmanager.store");
         Object var3 = (Set)var2.persistentGet("weblogic.wsee.endpointmanager.idlist");
         if (var3 == null) {
            var3 = new HashSet();
         }

         String var4 = var1.getSAFRequest().getMessageId();
         ((Set)var3).add(var4);
         var2.persistentPut("weblogic.wsee.endpointmanager.idlist", var3);
         var2.persistentPut(var4, var1);
      }
   }

   private synchronized void handleServerUp() {
      if (verbose) {
         Verbose.log((Object)"Server is now up, delivering any stored/deferred failure notification in WsrmEndpointManager");
      }

      WsStorage var1 = WsStorageFactory.getStorage("weblogic.wsee.endpointmanager.store");

      try {
         Set var2 = (Set)var1.persistentGet("weblogic.wsee.endpointmanager.idlist");
         if (var2 != null) {
            Verbose.log((Object)("Delivering " + var2.size() + " stored/deferred failure notifications in WsrmEndpointManager"));
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               SavedFailureNotification var5 = (SavedFailureNotification)var1.persistentGet(var4);
               if (var5 != null) {
                  Verbose.log((Object)("Delivering stored/deferred failure notification in WsrmEndpointManager for message: " + var5.getSAFRequest().getMessageId() + " and conversation: " + var5.getSAFRequest().getConversationName()));

                  try {
                     this.internalHandleFailure(var5.getErrorHandler(), var5.getSAFRequest(), var5.getFaultCodes(), var5.getErrors());
                  } finally {
                     try {
                        var1.persistentRemove(var4);
                     } catch (Exception var27) {
                        this._logger.error(var27.toString(), var27);
                     }

                  }
               } else {
                  Verbose.log((Object)("[ERROR] - Didn't find stored/deferred failure notification in WsrmEndpointManager for message: " + var4));
               }
            }

            return;
         }
      } catch (Exception var29) {
         this._logger.error("Could not process stored/deferred failure notifications", var29);
         return;
      } finally {
         try {
            var1.persistentRemove("weblogic.wsee.endpointmanager.idlist");
         } catch (Exception var26) {
            this._logger.error(var26.toString(), var26);
         }

      }

   }

   private String createFailureMessage(ArrayList var1, ArrayList var2) {
      StringBuffer var3 = new StringBuffer();
      if (verbose) {
         Verbose.log((Object)"Handle failure called");
      }

      int var4;
      for(var4 = 0; var4 < var1.size(); ++var4) {
         int var5 = (Integer)var1.get(var4);
         if (verbose) {
            Verbose.log((Object)("Got fault " + var5 + " " + SAFResult.description[var5]));
         }

         var3.append(SAFResult.description[var5]);
         var3.append("\n");
      }

      for(var4 = 0; var4 < var2.size(); ++var4) {
         Throwable var6 = (Throwable)var2.get(var4);
         var6 = this.unwrapError(var6);
         if (verbose) {
            Verbose.log((Object)("Got exception from transport: " + var6.toString()));
         }

         var3.append(var6.toString());
         var3.append("\n");
      }

      return var3.toString();
   }

   private Throwable unwrapError(Throwable var1) {
      Throwable var2 = var1;

      for(Throwable var3 = var1; var3 != null; var3 = var3.getCause()) {
         if (var3 instanceof WsrmPermanentTransportException || var3 instanceof AccessException) {
            var2 = var3;
            break;
         }
      }

      return var2;
   }

   public SAFErrorHandler createErrorHandlerInstance() {
      if (verbose) {
         Verbose.log((Object)"createErrorHandlerInstance is not supported for WsrmEndpointManager");
      }

      return null;
   }

   public SAFErrorHandler getErrorHandler(String var1) {
      return (SAFErrorHandler)this.errorHandlers.get(var1);
   }

   static {
      _runtimeAccess = ManagementService.getRuntimeAccess(_kernelId);
   }

   private static class SavedFailureNotification implements Serializable {
      private static final long serialVersionUID = 1L;
      public SAFErrorHandler eh;
      public SAFRequest request;
      public ArrayList faultCodes;
      public transient ArrayList errors;

      public SavedFailureNotification(SAFErrorHandler var1, SAFRequest var2, ArrayList var3, ArrayList var4) {
         this.eh = var1;
         this.request = var2;
         this.faultCodes = var3;
         this.errors = var4;
      }

      public SAFErrorHandler getErrorHandler() {
         return this.eh;
      }

      public SAFRequest getSAFRequest() {
         return this.request;
      }

      public ArrayList getFaultCodes() {
         return this.faultCodes;
      }

      public ArrayList getErrors() {
         return this.errors;
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         if (this.errors != null) {
            ArrayList var2 = new ArrayList(this.errors);
            var1.writeInt(var2.size());
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Throwable var4 = (Throwable)var3.next();

               try {
                  var1.writeObject(var4);
               } catch (Exception var7) {
                  if (WsrmEndpointManager.verbose) {
                     Verbose.say("Error encountered writing SAF endpoint error to persistent storage. A serializable copy will be stored instead");
                     Verbose.logException(var7);
                  }

                  Exception var6 = new Exception(var4.toString());
                  var1.writeObject(var6);
               }
            }
         }

      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         int var2 = var1.readInt();
         this.errors = new ArrayList();

         for(int var3 = 0; var3 < var2; ++var3) {
            Throwable var4 = (Throwable)var1.readObject();
            this.errors.add(var4);
         }

      }
   }

   private class ServerStateChangeListener implements PropertyChangeListener {
      boolean _serverUp;

      public ServerStateChangeListener() {
         this.interpretState(WsrmEndpointManager._runtimeAccess.getServerRuntime().getState());
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
            WsrmEndpointManager.this.handleServerUp();
         }

         this._serverUp = true;
      }

      public boolean isServerUp() {
         return this._serverUp;
      }
   }
}
