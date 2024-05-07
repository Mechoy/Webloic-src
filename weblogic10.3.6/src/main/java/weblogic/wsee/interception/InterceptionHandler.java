package weblogic.wsee.interception;

import java.security.AccessController;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.interception.MessageInterceptionService;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.AssociationListener;
import weblogic.messaging.interception.interfaces.InterceptionPointHandle;
import weblogic.messaging.interception.interfaces.InterceptionPointNameDescriptor;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.message.WlMessageContext;

public final class InterceptionHandler extends GenericHandler {
   private InterceptionPointHandle[] IPHandles = new InterceptionPointHandle[2];
   private static final String[] IPStrings = new String[]{"Request", "Response"};
   private static final int REQUEST_IP = 0;
   private static final int RESPONSE_IP = 1;
   private static boolean registeredType = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void init(HandlerInfo var1) {
      super.init(var1);
      if (!registeredType) {
         registeredType = true;

         try {
            this.registerWithInterceptionService();
         } catch (InterceptionServiceException var3) {
            throw new JAXRPCException("Registering with interception service", var3);
         }
      }

   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   private final void registerWithInterceptionService() throws InterceptionServiceException {
      InterceptionPointNameDescriptor[] var1 = new InterceptionPointNameDescriptor[]{new WSInterceptionPointNameDescriptor("server name"), new WSInterceptionPointNameDescriptor("URI"), new WSInterceptionPointNameDescriptor("location")};
      MessageInterceptionService.getSingleton().registerInterceptionPointNameDescription("Web Services", var1, (AssociationListener)null);
   }

   private boolean interceptionPoint(int var1, MessageContext var2) throws JAXRPCException {
      WlMessageContext var3 = WlMessageContext.narrow(var2);
      InterceptionPointHandle var4 = this.IPHandles[var1];
      if (var4 == null) {
         if (ManagementService.getRuntimeAccess(kernelId) == null) {
            return true;
         }

         String[] var5 = new String[]{ManagementService.getRuntimeAccess(kernelId).getServerName(), var3.getDispatcher().getConnection().getTransport().getServiceURI(), IPStrings[var1]};

         try {
            var4 = MessageInterceptionService.getSingleton().registerInterceptionPoint("Web Services", var5);
         } catch (InterceptionServiceException var8) {
            throw new JAXRPCException("Failed to registerInterceptionPoint" + var8, var8);
         }

         this.IPHandles[var1] = var4;
      }

      try {
         return var4.hasAssociation() ? var4.process(var2) : true;
      } catch (Exception var7) {
         throw new JAXRPCException("Processor threw exception", var7);
      }
   }

   public boolean handleRequest(MessageContext var1) throws JAXRPCException {
      return this.interceptionPoint(0, var1);
   }

   public boolean handleResponse(MessageContext var1) throws JAXRPCException {
      return this.interceptionPoint(1, var1);
   }

   public boolean handleFault(MessageContext var1) throws JAXRPCException {
      return true;
   }

   public void destroy() {
      for(int var1 = 0; var1 < 2; ++var1) {
         if (this.IPHandles[var1] != null) {
            try {
               MessageInterceptionService.getSingleton().unRegisterInterceptionPoint(this.IPHandles[var1]);
            } catch (InterceptionServiceException var3) {
               throw new AssertionError("unRegister failed: " + var3);
            }
         }
      }

   }

   private class WSInterceptionPointNameDescriptor extends InterceptionPointNameDescriptor {
      private String title;

      public WSInterceptionPointNameDescriptor(String var2) {
         this.title = var2;
      }

      public String getTitle() {
         return this.title;
      }

      public int getTotalNumberOfUniqueValue() {
         return 100;
      }

      public boolean isValid(String var1) {
         return true;
      }
   }
}
