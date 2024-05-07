package weblogic.connector.lifecycle;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Timer;
import java.util.Vector;
import javax.resource.ResourceException;
import javax.resource.spi.ApplicationServerInternalException;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkException;
import weblogic.application.ApplicationContextInternal;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.extensions.ExtendedBootstrapContext;
import weblogic.connector.external.AdapterListener;
import weblogic.connector.work.WorkManager;
import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextFactory;
import weblogic.diagnostics.context.DiagnosticContextHelper;
import weblogic.diagnostics.context.DiagnosticContextManager;
import weblogic.diagnostics.context.InvalidDyeException;

public class BootstrapContext implements javax.resource.spi.BootstrapContext, ExtendedBootstrapContext, Serializable {
   private static final long serialVersionUID = -6084262294645664250L;
   private String moduleName;
   private WorkManager workManager;
   private Vector listeners;
   private RAInstanceManager raIM;

   public BootstrapContext(RAInstanceManager var1, ApplicationContextInternal var2, String var3, weblogic.work.WorkManager var4) throws WorkException {
      Debug.enter(this, "( " + var1 + ", " + var2 + ", " + var3 + " )");

      try {
         this.raIM = var1;
         this.workManager = null;
         this.moduleName = var3;
         this.listeners = new Vector(10);
         if (var3 == null || var3.trim().equals("")) {
            Debug.throwAssertionError("moduleName is null or blank");
         }

         if (var4 == null) {
            Debug.throwAssertionError("Couldn't obtain WorkManager for resource adapter '" + var3 + "'");
         }

         if (Debug.isWorkEnabled()) {
            Debug.work("BootStrapContext() creating WorkManager for RA=" + var3);
         }

         this.workManager = WorkManager.create(var2.getApplicationId(), var3, var4);
      } finally {
         Debug.exit(this, "()");
      }

   }

   public Timer createTimer() {
      Debug.logTimerWarning();
      return new Timer();
   }

   public javax.resource.spi.work.WorkManager getWorkManager() {
      return this.workManager;
   }

   public XATerminator getXATerminator() {
      return weblogic.connector.inbound.XATerminator.getXATerminator();
   }

   public void setDiagnosticContextID(String var1) {
      DiagnosticContextHelper.setPayload(var1);
   }

   public String getDiagnosticContextID() {
      return DiagnosticContextHelper.getPayload();
   }

   public void setDyeBits(byte var1) throws ResourceException {
      String var2;
      if (!DiagnosticContextManager.getDiagnosticContextManager().isEnabled()) {
         Debug.println("BootstrapContext.setDyeBits failed because Diagnostic Contexts are not enabled");
         var2 = Debug.getExceptionSetDyeBitsFailedDiagCtxNotEnabled();
         throw new ResourceException(var2);
      } else if (var1 >= 0 && var1 <= 15) {
         boolean var11 = (var1 & 8) != 0;
         boolean var3 = (var1 & 4) != 0;
         boolean var4 = (var1 & 2) != 0;
         boolean var5 = (var1 & 1) != 0;
         DiagnosticContext var6 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
         if (var6 == null) {
            Debug.println("<" + this.moduleName + "> Failed to obtain Diagnostic" + "Context for BootstrapContext.setDyeBits call");
            String var7 = Debug.getExceptionFailedToGetDiagCtx(this.moduleName);
            throw new ApplicationServerInternalException(var7);
         } else {
            try {
               var6.setDye((byte)30, var11);
               var6.setDye((byte)29, var3);
               var6.setDye((byte)28, var4);
               var6.setDye((byte)27, var5);
            } catch (InvalidDyeException var10) {
               String var8 = Debug.logInvalidDye(this.moduleName, var10.toString());
               Debug.logStackTrace(var8, var10);
               String var9 = Debug.getExceptionInvalidDye(this.moduleName, var10.toString());
               throw new ApplicationServerInternalException(var9, var10);
            }
         }
      } else {
         var2 = Debug.getExceptionInvalidDyeValue(String.valueOf(var1));
         throw new ResourceException(var2);
      }
   }

   public byte getDyeBits() throws ResourceException {
      if (!DiagnosticContextManager.getDiagnosticContextManager().isEnabled()) {
         Debug.println("BootstrapContext.getDyeBits failed because Diagnostic Contexts are not enabled");
         String var7 = Debug.getExceptionGetDyeBitsFailedDiagCtxNotEnabled();
         throw new ResourceException(var7);
      } else {
         byte var1 = 0;
         DiagnosticContext var2 = DiagnosticContextFactory.findOrCreateDiagnosticContext();

         try {
            if (var2.isDyedWith((byte)30)) {
               var1 = (byte)(var1 | 8);
            }

            if (var2.isDyedWith((byte)29)) {
               var1 = (byte)(var1 | 4);
            }

            if (var2.isDyedWith((byte)28)) {
               var1 = (byte)(var1 | 2);
            }

            if (var2.isDyedWith((byte)27)) {
               var1 = (byte)(var1 | 1);
            }

            return var1;
         } catch (InvalidDyeException var6) {
            String var4 = Debug.logInvalidDye(this.moduleName, var6.toString());
            Debug.logStackTrace(var4, var6);
            String var5 = Debug.getExceptionInvalidDye(this.moduleName, var6.toString());
            throw new ApplicationServerInternalException(var5, var6);
         }
      }
   }

   public void complete() {
      ConnectorLogger.logCompleteCalled(this.moduleName, this.raIM.getVersionId());
      this.raIM.clearWaitingStartVersioningComplete();
      this.signalShutdown();
   }

   public void signalShutdown() {
      if (!this.raIM.isWaitingStartVersioningComplete()) {
         Iterator var1 = this.listeners.iterator();

         while(var1.hasNext()) {
            AdapterListener var2 = (AdapterListener)var1.next();
            var2.completed();
         }
      }

   }

   public void addListener(AdapterListener var1) {
      if (var1 != null) {
         this.listeners.add(var1);
      }

   }

   public void removeListener(AdapterListener var1) {
      if (var1 != null) {
         this.listeners.remove(var1);
      }

   }
}
