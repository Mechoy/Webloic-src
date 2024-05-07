package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import weblogic.application.CustomModuleFactory;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.internal.flow.CustomModuleHelper;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.management.DeploymentException;
import weblogic.utils.compiler.ToolFailureException;

public class CustomModuleManager {
   private final EARModule parentModule;
   private EARModule[] scopedModules;
   private final CompilerCtx ctx;
   private final Map<String, Object> descriptors = new Hashtable();
   private WeblogicExtensionBean extDD;
   private String extDescriptorUri;
   private WeblogicExtensionBean earExtDD = null;

   public CustomModuleManager(CompilerCtx var1, EARModule var2, String var3) throws ToolFailureException {
      this.ctx = var1;
      this.parentModule = var2;
      this.extDescriptorUri = var3;
      this.scopedModules = this.createScopedCustomModules();
   }

   public String[] getDescriptorUris() {
      return (String[])((String[])this.descriptors.keySet().toArray(new String[0]));
   }

   public DescriptorBean getRootBean(String var1) {
      return (DescriptorBean)this.descriptors.get(var1);
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      try {
         for(int var2 = 0; var2 < this.scopedModules.length; ++var2) {
            EARModule var3 = this.scopedModules[var2];
            var3.initModuleClassLoader(var1, this.parentModule.getModuleClassLoader());
            var3.setOutputDir(this.parentModule.getOutputDir());
            var3.merge(var1);
            String[] var4 = var3.getDescriptorUris();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               this.descriptors.put(var4[var5], var3.getRootBean(var4[var5]));
            }
         }

      } catch (ToolFailureException var6) {
         if (this.extDD == null && this.earExtDD != null) {
            this.scopedModules = new EARModule[0];
         } else {
            throw var6;
         }
      }
   }

   public void write(CompilerCtx var1) throws ToolFailureException {
      for(int var2 = 0; var2 < this.scopedModules.length; ++var2) {
         this.scopedModules[var2].write(var1);
      }

      File var5;
      if (this.extDD != null) {
         try {
            var5 = new File(this.parentModule.getOutputDir(), this.extDescriptorUri);
            DescriptorUtils.writeDescriptor(new EditableDescriptorManager(), (DescriptorBean)this.extDD, var5);
         } catch (IOException var4) {
            throw new ToolFailureException("Unable to write out " + this.extDescriptorUri, var4);
         }
      } else if (this.earExtDD != null && this.scopedModules.length > 0) {
         try {
            var5 = new File(this.parentModule.getOutputDir(), this.extDescriptorUri);
            DescriptorUtils.writeDescriptor(new EditableDescriptorManager(), (DescriptorBean)this.earExtDD, var5);
         } catch (IOException var3) {
            throw new ToolFailureException("Unable to write out " + this.extDescriptorUri, var3);
         }
      }

   }

   public void writeDescriptors(CompilerCtx var1) throws ToolFailureException {
      for(int var2 = 0; var2 < this.scopedModules.length; ++var2) {
         this.scopedModules[var2].writeDescriptors(var1);
      }

   }

   public void cleanup() {
      for(int var1 = 0; var1 < this.scopedModules.length; ++var1) {
         this.scopedModules[var1].cleanup();
      }

   }

   private EARModule[] createScopedCustomModules() throws ToolFailureException {
      ExtensionDescriptorParser var1 = new ExtensionDescriptorParser(this.parentModule.getVirtualJarFile(), this.ctx.getConfigDir(), this.ctx.getPlanBean(), this.parentModule.getURI(), this.extDescriptorUri);

      try {
         var1.mergeWlExtensionDescriptorsFromLibraries(this.ctx.getApplicationContext());
         this.extDD = var1.getWlExtensionDescriptor();
         this.earExtDD = this.ctx.getWLExtensionDD();
         Map var2;
         CustomModuleBean[] var3;
         ArrayList var4;
         int var5;
         CustomModuleFactory var6;
         EARModule var7;
         if (this.extDD != null) {
            var2 = CustomModuleHelper.initFactories(this.extDD, this.parentModule.getModuleClassLoader(), this.parentModule.getURI(), this.parentModule.getURI());
            if (var2 == null) {
               return new EARModule[0];
            } else {
               var3 = this.extDD.getCustomModules();
               var4 = new ArrayList();

               for(var5 = 0; var5 < var3.length; ++var5) {
                  var6 = (CustomModuleFactory)var2.get(var3[var5].getProviderName());
                  var7 = var6.createToolsModule(var3[var5]);
                  if (var7 != null) {
                     var4.add(var7);
                  }
               }

               this.descriptors.put(this.extDescriptorUri, this.extDD);
               return (EARModule[])((EARModule[])var4.toArray(new EARModule[0]));
            }
         } else if (this.earExtDD != null) {
            var2 = null;

            try {
               var2 = CustomModuleHelper.initFactories(this.earExtDD, this.parentModule.getModuleClassLoader(), this.parentModule.getURI(), this.parentModule.getURI());
            } catch (DeploymentException var8) {
               return new EARModule[0];
            }

            if (var2 == null) {
               return new EARModule[0];
            } else {
               var3 = this.earExtDD.getCustomModules();
               var4 = new ArrayList();

               for(var5 = 0; var5 < var3.length; ++var5) {
                  var6 = (CustomModuleFactory)var2.get(var3[var5].getProviderName());
                  var7 = var6.createToolsModule(var3[var5]);
                  if (var7 != null) {
                     var4.add(var7);
                  }
               }

               return (EARModule[])((EARModule[])var4.toArray(new EARModule[0]));
            }
         } else {
            return new EARModule[0];
         }
      } catch (XMLStreamException var9) {
         throw new ToolFailureException("Problem loading or merging weblogic-extension.xml for parent module", var9);
      } catch (IOException var10) {
         throw new ToolFailureException("Problem loading or merging weblogic-extension.xml for parent module", var10);
      } catch (DeploymentException var11) {
         throw new ToolFailureException("Unable to create custom module factories", var11);
      }
   }
}
