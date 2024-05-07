package weblogic.application.compiler;

import java.io.IOException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.module.JMSParser;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;

public class JMSModule extends WLSModule {
   private EditableDescriptorManager edm;

   public JMSModule(String var1, String var2) {
      super(var1, var2);
      if (this.edm == null) {
         this.edm = new EditableDescriptorManager();
      }
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      try {
         JMSBean var2 = null;
         if (var1.getEar() != null) {
            var2 = JMSParser.createJMSDescriptor(this.edm, this.getCL(this.getURI()), var1.getConfigDir(), var1.getPlanBean(), var1.getEar().getURI(), this.getURI());
         } else {
            var2 = JMSParser.createJMSDescriptor(this.edm, this.getCL(this.getURI()), var1.getConfigDir(), var1.getPlanBean(), this.getURI(), this.getURI());
         }

         if (var2 != null) {
            this.addRootBean(this.getURI(), (DescriptorBean)var2);
         }

      } catch (IOException var3) {
         throw new ToolFailureException("Unable to parse JMS descriptor", var3);
      } catch (XMLStreamException var4) {
         throw new ToolFailureException("Unable to parse JMS descriptor", var4);
      }
   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      try {
         JMSBean var3 = JMSParser.createJMSDescriptor(this.getOutputFileName());
         this.getModuleValidationInfo().setJMSBean(var3);
         if (var2.isVerbose() && var3 != null) {
            DescriptorUtils.writeAsXML((DescriptorBean)var3);
         }

      } catch (Exception var7) {
         String var4 = null;
         Throwable var5 = var7.getCause();
         if (var5 != null) {
            var4 = var5.getMessage();
         } else {
            var4 = var7.getMessage();
         }

         Exception var6 = new Exception("Failed to compile JMS module " + this.getOutputFileName() + ": " + var4);
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.getOutputFileName(), var6.toString()).getMessage(), var6);
      }
   }

   public ModuleType getModuleType() {
      return WebLogicModuleType.JMS;
   }
}
