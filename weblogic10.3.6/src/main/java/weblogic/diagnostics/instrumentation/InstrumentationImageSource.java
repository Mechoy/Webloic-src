package weblogic.diagnostics.instrumentation;

import java.io.OutputStream;
import java.security.AccessController;
import java.util.Iterator;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.accessor.ColumnInfo;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.accessor.DiagnosticAccessRuntime;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.diagnostics.image.descriptor.ColumnDataBean;
import weblogic.diagnostics.image.descriptor.InstrumentationEventBean;
import weblogic.diagnostics.image.descriptor.InstrumentationImageSourceBean;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.WLDFDataAccessRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class InstrumentationImageSource implements ImageSource {
   private boolean timeoutRequested;
   private String[] columnNames;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      DescriptorManager var2 = new DescriptorManager();
      Descriptor var3 = var2.createDescriptorRoot(InstrumentationImageSourceBean.class);
      InstrumentationImageSourceBean var4 = (InstrumentationImageSourceBean)var3.getRootBean();
      this.writeRecentEvents(var4);

      try {
         var2.writeDescriptorBeanAsXML((DescriptorBean)var4, var1);
      } catch (Exception var6) {
         throw new ImageSourceCreationException(var6);
      }
   }

   public void timeoutImageCreation() {
      this.timeoutRequested = true;
   }

   private String[] getColumnNames(WLDFDataAccessRuntimeMBean var1) throws Exception {
      if (this.columnNames == null) {
         ColumnInfo[] var2 = var1.getColumns();
         this.columnNames = new String[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.columnNames[var3] = var2[var3].getColumnName();
         }
      }

      return this.columnNames;
   }

   private void writeRecentEvents(InstrumentationImageSourceBean var1) {
      try {
         this.timeoutRequested = false;
         ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
         WLDFServerDiagnosticMBean var3 = var2.getServerDiagnosticConfig();
         long var4 = var3.getEventsImageCaptureInterval();
         if (var4 > 0L) {
            DiagnosticAccessRuntime var6 = DiagnosticAccessRuntime.getInstance();
            WLDFDataAccessRuntimeMBean var7 = var6.lookupWLDFDataAccessRuntime("EventsDataArchive");
            long var8 = System.currentTimeMillis();
            long var10 = var8 - var4;
            String[] var12 = this.getColumnNames(var7);
            Iterator var13 = var7.retrieveDataRecords(var10, var8, (String)null);

            while(var13.hasNext() && !this.timeoutRequested) {
               DataRecord var14 = (DataRecord)var13.next();
               InstrumentationEventBean var15 = var1.createInstrumentationEvent();

               for(int var16 = 0; var16 < var12.length; ++var16) {
                  String var17 = var12[var16];
                  ColumnDataBean var18 = var15.createColumnData();
                  var18.setName(var17);
                  Object var19 = var14.get(var16);
                  if (var19 != null) {
                     var18.setValue(var19.toString());
                  }
               }
            }
         }
      } catch (Exception var20) {
         UnexpectedExceptionHandler.handle("Error in InstrumentationImageSource.", var20);
      }

   }
}
