package weblogic.diagnostics.image;

import com.oracle.jrockit.jfr.client.EventSettingsBuilder;
import com.oracle.jrockit.jfr.client.FlightRecorderClient;
import com.oracle.jrockit.jfr.client.FlightRecordingClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.List;
import weblogic.diagnostics.context.DiagnosticContextManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.flightrecorder.FlightRecorderManager;
import weblogic.diagnostics.flightrecorder.event.GlobalInformationEventInfo;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.io.StreamUtils;

public class FlightRecorderSource implements ImageSource {
   private static final boolean DisableJVMEvents = Boolean.getBoolean("weblogic.diagnostics.image.DisableJVMEvents");
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticImage");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static WLDFServerDiagnosticMBean wldfConfig;
   private boolean timeoutRequested;
   private long count = 0L;
   private FlightRecorderClient flightRecorderClient;
   private FlightRecordingClient flightRecordingClient;
   private ImageManager imageManager;
   private File destinationTempFile = null;
   private GlobalInformation globalInfo = new GlobalInformation();
   private String recordingName = null;

   public FlightRecorderSource(ImageManager var1) {
      this.imageManager = var1;
      this.initialize();
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      boolean var2 = debugLogger.isDebugEnabled();
      this.timeoutRequested = false;
      if (var2) {
         debugLogger.debug("FlightRecorderSource.createDiagnosticImage()");
      }

      if (this.flightRecordingClient == null) {
         if (var2) {
            debugLogger.debug("FlightRecorderSource.createDiagnosticImage() no recording is active");
         }

      } else {
         File var3 = null;
         FlightRecordingClient var4 = null;

         try {
            try {
               this.triggerGlobalInformationEvent();
               DiagnosticContextManager.getDiagnosticContextManager().triggerThrottleInformationEvent();
               var3 = File.createTempFile("__tmp", ".jfr", new File(this.imageManager.getDestinationDirectory()));

               try {
                  var4 = this.flightRecordingClient.cloneRecordingObject(this.recordingName + "_" + this.count++, true);
               } catch (Throwable var41) {
                  RuntimeAccess var6 = ManagementService.getRuntimeAccess(kernelId);
                  if (var6 != null) {
                     ServerRuntimeMBean var7 = var6.getServerRuntime();
                     FileInputStream var8 = null;

                     try {
                        if (var7 != null && var7.isShuttingDown() && this.destinationTempFile != null && this.destinationTempFile.exists() && this.destinationTempFile.canRead()) {
                           if (var2) {
                              debugLogger.debug("Server is shutting down, trying to capture from temporary recording file");
                           }

                           var8 = new FileInputStream(this.destinationTempFile);
                           StreamUtils.writeTo(var8, var1);
                           var1.flush();
                           return;
                        }
                     } catch (IOException var39) {
                        DiagnosticsLogger.logErrorCapturingFlightRecorderImage(var39);
                     } finally {
                        if (var8 != null) {
                           var8.close();
                        }

                     }
                  }

                  DiagnosticsLogger.logErrorCapturingFlightRecorderImage(var41);
               }

               if (var4 != null) {
                  var4.copyTo(var3.getAbsolutePath());
                  if (var2) {
                     debugLogger.debug("FlightRecorderSource exists= " + var3.exists());
                  }

                  FileInputStream var5 = new FileInputStream(var3);

                  try {
                     try {
                        if (var2) {
                           debugLogger.debug("FlightRecorderSource writing from temp file " + var3 + " to stream");
                        }

                        StreamUtils.writeTo(var5, var1);
                        var1.flush();
                     } catch (IOException var37) {
                        DiagnosticsLogger.logErrorCapturingFlightRecorderImage(var37);
                     }

                     return;
                  } finally {
                     if (var5 != null) {
                        var5.close();
                     }

                  }
               }
            } catch (IOException var42) {
               DiagnosticsLogger.logErrorCapturingFlightRecorderImage(var42);
            }

         } finally {
            if (var3 != null) {
               var3.delete();
            }

            if (var4 != null) {
               var4.close();
            }

         }
      }
   }

   public void timeoutImageCreation() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("FlightRecorderSource.timeoutImageCreation()");
      }

      this.timeoutRequested = true;
   }

   private void initialize() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("FlightRecorderSource.initialize()");
      }

      this.flightRecorderClient = FlightRecorderManager.getFlightRecorderClient();
      if (this.flightRecorderClient == null) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("FlightRecorderSource.initialize() no FlightRecorderClient found");
         }

         this.determineRecordingName();
         this.triggerGlobalInformationEvent();
      } else {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("FlightRecorderSource.initialize() start recording");
         }

         this.determineRecordingName();
         this.startRecording();
         this.triggerGlobalInformationEvent();
      }
   }

   private void startRecording() {
      try {
         this.flightRecordingClient = this.flightRecorderClient.createRecordingObject(this.recordingName);
         List var1 = this.flightRecorderClient.getEventSettings(this.flightRecordingClient.getObjectName());
         EventSettingsBuilder var2 = new EventSettingsBuilder();
         var2.createSetting("http://www.oracle.com/jrockit/jvm/*", false, false, Long.MAX_VALUE, 0L);
         List var3 = var2.createSettings(this.flightRecorderClient);
         if ("Off".equals(this.getDiagnosticVolume()) || "Low".equals(this.getDiagnosticVolume()) && FlightRecorderManager.areJVMEventsExpensive()) {
            this.flightRecorderClient.updateEventSettings(this.flightRecordingClient.getObjectName(), var3);
         }

         this.destinationTempFile = File.createTempFile("__tmp", ".jfr", new File(this.imageManager.getDestinationDirectory()));
         this.destinationTempFile.deleteOnExit();
         this.flightRecordingClient.setDestination(this.destinationTempFile.getAbsolutePath());
         FlightRecorderManager.setImageRecordingClient(this.flightRecordingClient, "WLDF ", DisableJVMEvents ? var3 : var1, var3);
         this.flightRecordingClient.start();
         FlightRecorderManager.debugRecorderDetails();
      } catch (Exception var4) {
         DiagnosticsLogger.logErrorInitializingFlightRecording(var4);
      }

   }

   private String getDiagnosticVolume() {
      return wldfConfig.getWLDFDiagnosticVolume();
   }

   private void determineRecordingName() {
      StringBuffer var1 = new StringBuffer();
      var1.append("WLDFDiagnosticImageRecording");
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      if (var2 != null) {
         this.globalInfo.setDomainName(var2.getDomainName());
         this.globalInfo.setServerName(var2.getServerName());
         ServerRuntimeMBean var3 = var2.getServerRuntime();
         if (var3 != null) {
            this.globalInfo.setMachineName(var3.getCurrentMachine());
         }

         wldfConfig = var2.getServer().getServerDiagnosticConfig();
      }

      var1.append("_");
      var1.append(this.globalInfo.getDomainName());
      var1.append("_");
      var1.append(this.globalInfo.getServerName());
      var1.append("_");
      var1.append(this.globalInfo.getMachineName());
      this.recordingName = var1.toString();
   }

   private GlobalInformation triggerGlobalInformationEvent() {
      this.globalInfo.setDiagnosticVolume(this.getDiagnosticVolume());
      return this.globalInfo;
   }

   private static class GlobalInformation implements GlobalInformationEventInfo {
      private String domainName;
      private String serverName;
      private String machineName;
      private String diagnosticVolume;

      private GlobalInformation() {
      }

      public String getDomainName() {
         return this.domainName;
      }

      public void setDomainName(String var1) {
         this.domainName = var1;
      }

      public String getServerName() {
         return this.serverName;
      }

      public void setServerName(String var1) {
         this.serverName = var1;
      }

      public String getMachineName() {
         return this.machineName;
      }

      public void setMachineName(String var1) {
         this.machineName = var1;
      }

      public String getDiagnosticVolume() {
         return this.diagnosticVolume;
      }

      public void setDiagnosticVolume(String var1) {
         this.diagnosticVolume = var1;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("domainName=");
         var1.append(this.domainName);
         var1.append(", serverName=");
         var1.append(this.serverName);
         var1.append(", machineName=");
         var1.append(this.machineName);
         var1.append(", diagnosticVolume=");
         var1.append(this.diagnosticVolume);
         return var1.toString();
      }

      // $FF: synthetic method
      GlobalInformation(Object var1) {
         this();
      }
   }
}
