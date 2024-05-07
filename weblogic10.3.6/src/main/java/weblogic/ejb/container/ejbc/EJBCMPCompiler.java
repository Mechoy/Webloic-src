package weblogic.ejb.container.ejbc;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import weblogic.ejb.container.interfaces.CMPCompiler;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.PersistenceType;
import weblogic.ejb.container.persistence.spi.CMPCodeGenerator;
import weblogic.ejb.container.persistence.spi.CMPDeployer;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.ICompilerFactory;

public final class EJBCMPCompiler implements CMPCompiler {
   private Getopt2 opts;
   private File outputDir;
   private CMPDeployer cmpDeployer = null;
   private CMPCodeGenerator cmpGenerator = null;
   private ICompilerFactory compilerFactory;

   EJBCMPCompiler(File var1, Getopt2 var2, ICompilerFactory var3) {
      this.outputDir = var1;
      this.opts = var2;
      this.compilerFactory = var3;
   }

   public List generatePersistenceSources(EntityBeanInfo var1) throws Exception {
      assert var1 != null;

      PersistenceType var2 = var1.getCMPInfo().getPersistenceType();
      this.cmpDeployer = var1.getCMPInfo().getDeployer();

      assert this.cmpDeployer != null;

      var2.setOptions(this.opts);
      this.cmpGenerator = var2.getCodeGenerator();
      this.cmpGenerator.setCompilerFactory(this.compilerFactory);
      this.cmpDeployer.preCodeGeneration(this.cmpGenerator);
      Object var3 = null;
      if (this.cmpGenerator != null) {
         this.cmpGenerator.setAssociatedType(var2);
         this.cmpGenerator.setRootDirectoryName(this.outputDir.getAbsolutePath());
         this.cmpGenerator.setTargetDirectory(this.outputDir.getAbsolutePath());
         this.cmpGenerator.generate(var1);
         var3 = this.cmpGenerator.getGeneratedOutputs();
      }

      if (var3 == null) {
         var3 = new LinkedList();
      }

      return (List)var3;
   }

   public void postCompilation() throws Exception {
      this.cmpDeployer.postCodeGeneration(this.cmpGenerator);
   }
}
