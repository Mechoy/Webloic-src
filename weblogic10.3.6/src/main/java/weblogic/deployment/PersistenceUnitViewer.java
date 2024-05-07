package weblogic.deployment;

import java.io.File;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

public abstract class PersistenceUnitViewer extends AbstractPersistenceUnitRegistry {
   private static boolean disablePUViewer = Boolean.getBoolean("weblogic.deployment.disablePersistenceViewer");

   public PersistenceUnitViewer(GenericClassLoader var1, String var2, File var3, DeploymentPlanBean var4) {
      super(var1, var2, var3, var4);
   }

   public abstract void loadDescriptors() throws ToolFailureException;

   public PersistenceUnitInfoImpl getPersistenceUnit(String var1) throws IllegalArgumentException {
      throw new AssertionError("This class is for descriptor viewing only");
   }

   protected void throwLoadException(Exception var1) throws ToolFailureException {
      throw new ToolFailureException("Unable to load persistence descriptor", var1);
   }

   public static class EntryViewer extends PersistenceUnitViewer {
      private final VirtualJarFile vjf;

      public EntryViewer(VirtualJarFile var1, String var2, File var3, DeploymentPlanBean var4) {
         super((GenericClassLoader)null, var2, var3, var4);
         this.vjf = var1;
      }

      public void loadDescriptors() throws ToolFailureException {
         if (!PersistenceUnitViewer.disablePUViewer) {
            try {
               this.loadPersistenceDescriptor(this.vjf, false, (File)null);
            } catch (Exception var2) {
               this.throwLoadException(var2);
            }

         }
      }
   }

   public static class ResourceViewer extends PersistenceUnitViewer {
      public ResourceViewer(GenericClassLoader var1, String var2, File var3, DeploymentPlanBean var4) {
         super(var1, var2, var3, var4);
      }

      public void loadDescriptors() throws ToolFailureException {
         if (!PersistenceUnitViewer.disablePUViewer) {
            try {
               this.loadPersistenceDescriptors(false);
            } catch (Exception var2) {
               this.throwLoadException(var2);
            }

         }
      }
   }
}
