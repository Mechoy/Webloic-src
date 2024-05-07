package weblogic.application.internal.flow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.application.internal.AppDDHolder;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.deploy.container.DeploymentContext;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.jars.VirtualJarFile;

public final class DescriptorParsingFlow extends BaseFlow {
   private static DebugCategory debugger = Debug.getCategory("weblogic.application.DebugDescriptorParsing");

   public DescriptorParsingFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      this.parseDDs();
      if (this.appCtx.getWLApplicationDD() != null) {
         this.appCtx.addUpdateListener(new WLAppUpdateListener(this.appCtx));
      } else {
         this.appCtx.addUpdateListener(new NullListener());
      }

   }

   public void start(String[] var1) throws DeploymentException {
      this.parseDDs();
   }

   private String getApplicationNameForPlan(String var1, String var2, DeploymentPlanBean var3) {
      if (var3 != null && var3.findModuleOverride(var1) != null) {
         ModuleOverrideBean var4 = var3.findModuleOverride(var2);
         if (var4 == null) {
            return var1;
         }

         boolean var5 = false;
         ModuleDescriptorBean[] var6 = var4.getModuleDescriptors();
         if (var6 != null) {
            for(int var7 = 0; var7 < var6.length && !var5; ++var7) {
               if (var6[var7].getVariableAssignments() != null && var6[var7].getVariableAssignments().length > 0) {
                  var5 = true;
               }
            }
         }

         if (!var5) {
            return var1;
         }
      }

      return var2;
   }

   private ApplicationDescriptor createAppDescriptor() throws ModuleException {
      if (debugger.isEnabled()) {
         Debug.say("Parsing application level descriptors");
      }

      VirtualJarFile var1 = null;
      AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
      DeploymentPlanBean var3 = var2.getDeploymentPlanDescriptor();
      String var4 = var2.getAltDescriptorPath();
      String var5 = var2.getAltWLSDescriptorPath();
      File var6 = null;
      File var7 = null;
      if (var4 != null) {
         var6 = new File(var4);
      }

      if (var5 != null) {
         var7 = new File(var5);
      }

      ApplicationDescriptor var10;
      try {
         if (debugger.isEnabled()) {
            Debug.say("Making a choice between app name & file name for plan overrides: " + this.appCtx.getApplicationName() + ", " + this.appCtx.getApplicationFileName());
         }

         var1 = this.appCtx.getApplicationFileManager().getVirtualJarFile();
         String var8 = this.getApplicationNameForPlan(this.appCtx.getApplicationName(), this.appCtx.getApplicationFileName(), var3);
         if (debugger.isEnabled()) {
            Debug.say("Plan override choice made: " + var8);
         }

         ApplicationDescriptor var9 = new ApplicationDescriptor(var6, var7, var1, EarUtils.getConfigDir(this.appCtx), var3, var8);
         if (debugger.isEnabled()) {
            this.say("Parsed descriptor", var9);
         }

         var10 = var9;
      } catch (IOException var14) {
         throw new ModuleException(var14);
      } finally {
         IOUtils.forceClose(var1);
      }

      return var10;
   }

   public void validateRedeploy(DeploymentContext var1) throws DeploymentException {
      AppDDHolder var2 = null;
      var2 = this.parseDDs(this.createAppDescriptor());
      this.appCtx.setProposedPartialRedeployDDs(var2);
   }

   private void parseDDs() throws DeploymentException {
      ApplicationDescriptor var1 = this.createAppDescriptor();

      try {
         this.appCtx.setApplicationDescriptor(var1);
      } catch (IOException var3) {
         throw new DeploymentException(var3);
      } catch (XMLStreamException var4) {
         throw new DeploymentException(var4);
      }
   }

   private AppDDHolder parseDDs(ApplicationDescriptor var1) throws DeploymentException {
      try {
         return new AppDDHolder(var1.getApplicationDescriptor(), var1.getWeblogicApplicationDescriptor(), var1.getWeblogicExtensionDescriptor());
      } catch (IOException var3) {
         throw new DeploymentException(var3);
      } catch (XMLStreamException var4) {
         throw new DeploymentException(var4);
      }
   }

   private void say(String var1, ApplicationDescriptor var2) {
      try {
         DescriptorBean var3 = (DescriptorBean)var2.getApplicationDescriptor();
         DescriptorBean var4 = (DescriptorBean)var2.getWeblogicApplicationDescriptor();
         DescriptorBean var5 = (DescriptorBean)var2.getWeblogicExtensionDescriptor();
         ByteArrayOutputStream var6;
         if (var3 != null) {
            var6 = new ByteArrayOutputStream();
            (new DescriptorManager()).writeDescriptorAsXML(var3.getDescriptor(), var6);
            Debug.say(var1 + "\n" + var6.toString());
            var6.close();
         }

         if (var4 != null) {
            var6 = new ByteArrayOutputStream();
            (new DescriptorManager()).writeDescriptorAsXML(var4.getDescriptor(), var6);
            Debug.say(var1 + "\n" + var6.toString());
            var6.close();
         }

         if (var5 != null) {
            var6 = new ByteArrayOutputStream();
            (new DescriptorManager()).writeDescriptorAsXML(var5.getDescriptor(), var6);
            Debug.say(var1 + "\n" + var6.toString());
            var6.close();
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      } catch (XMLStreamException var8) {
         var8.printStackTrace();
      }

   }

   private static class NullListener implements UpdateListener {
      private NullListener() {
      }

      public boolean acceptURI(String var1) {
         return "META-INF/weblogic-application.xml".equals(var1);
      }

      public void prepareUpdate(String var1) {
      }

      public void activateUpdate(String var1) {
      }

      public void rollbackUpdate(String var1) {
      }

      // $FF: synthetic method
      NullListener(Object var1) {
         this();
      }
   }

   private class WLAppUpdateListener implements UpdateListener {
      private static final boolean DEBUG = false;
      private final ApplicationContextInternal appCtx;
      private Descriptor proposedDescriptor;
      private Descriptor currentDescriptor;

      private WLAppUpdateListener(ApplicationContextInternal var2) {
         this.appCtx = var2;
         WeblogicApplicationBean var3 = var2.getWLApplicationDD();
         this.currentDescriptor = ((DescriptorBean)var3).getDescriptor();
      }

      private WeblogicApplicationBean parseNewWLDD() throws ModuleException {
         ApplicationDescriptor var1 = DescriptorParsingFlow.this.createAppDescriptor();

         try {
            return var1.getWeblogicApplicationDescriptor();
         } catch (IOException var3) {
            throw new ModuleException(var3);
         } catch (XMLStreamException var4) {
            throw new ModuleException(var4);
         }
      }

      public boolean acceptURI(String var1) {
         return "META-INF/weblogic-application.xml".equals(var1);
      }

      public void prepareUpdate(String var1) throws ModuleException {
         WeblogicApplicationBean var2 = this.parseNewWLDD();
         this.proposedDescriptor = ((DescriptorBean)var2).getDescriptor();

         try {
            this.currentDescriptor.prepareUpdate(this.proposedDescriptor);
         } catch (DescriptorUpdateRejectedException var4) {
            throw new ModuleException(var4);
         }
      }

      public void activateUpdate(String var1) throws ModuleException {
         try {
            this.currentDescriptor.activateUpdate();
         } catch (DescriptorUpdateFailedException var6) {
            throw new ModuleException(var6);
         } finally {
            this.proposedDescriptor = null;
         }

      }

      public void rollbackUpdate(String var1) {
         try {
            this.currentDescriptor.rollbackUpdate();
         } finally {
            this.proposedDescriptor = null;
         }

      }

      // $FF: synthetic method
      WLAppUpdateListener(ApplicationContextInternal var2, Object var3) {
         this(var2);
      }
   }
}
