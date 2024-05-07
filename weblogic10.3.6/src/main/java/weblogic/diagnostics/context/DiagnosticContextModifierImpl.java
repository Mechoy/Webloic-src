package weblogic.diagnostics.context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.workarea.utils.WorkContextInputAdapter;
import weblogic.workarea.utils.WorkContextOutputAdapter;

class DiagnosticContextModifierImpl implements DiagnosticContextModifier {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static DiagnosticContextModifier SINGLETON = null;

   private DiagnosticContextModifierImpl() {
   }

   static synchronized DiagnosticContextModifier getInstance() {
      if (!SecurityServiceManager.isKernelIdentity(SecurityServiceManager.getCurrentSubject(KERNEL_ID))) {
         return null;
      } else {
         if (SINGLETON == null) {
            SINGLETON = new DiagnosticContextModifierImpl();
         }

         return SINGLETON;
      }
   }

   public byte[] getContext() throws IOException {
      DiagnosticContext var1 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      if (var1 == null) {
         return null;
      } else {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ObjectOutputStream var3 = new ObjectOutputStream(var2);
         WorkContextOutputAdapter var4 = new WorkContextOutputAdapter(var3);
         var1.writeContext(var4);
         var3.flush();
         return var2.toByteArray();
      }
   }

   public void setContext(byte[] var1) throws IOException {
      if (var1 == null) {
         DiagnosticContextFactory.setDiagnosticContext((DiagnosticContext)null);
      } else {
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
         ObjectInputStream var3 = new ObjectInputStream(var2);
         WorkContextInputAdapter var4 = new WorkContextInputAdapter(var3);
         DiagnosticContextImpl var5 = new DiagnosticContextImpl();
         var5.readContext(var4);
         DiagnosticContextFactory.setDiagnosticContext(var5);
      }
   }

   public void setContextId(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         DiagnosticContextImpl var2 = new DiagnosticContextImpl();
         var2.setContextId(var1);
         DiagnosticContextFactory.setDiagnosticContext(var2);
      }
   }
}
