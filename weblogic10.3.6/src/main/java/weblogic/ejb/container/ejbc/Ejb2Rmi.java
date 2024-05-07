package weblogic.ejb.container.ejbc;

import java.util.Vector;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.utils.Getopt2;

public final class Ejb2Rmi extends EjbCodeGenerator {
   public Ejb2Rmi(Getopt2 var1) {
      super(var1);
   }

   protected void addOutputs(Vector var1, BeanInfo var2, NamingConvention var3) throws EJBCException {
      this.interpretBeanInfo(var2);
      EjbCodeGenerator.Output var4;
      EjbCodeGenerator.Output var5;
      if (this.hasRemoteClientView && this.hasDeclaredRemoteHome) {
         var4 = new EjbCodeGenerator.Output();
         var4.setBeanInfo(var2);
         var4.setNamingConvention(var3);
         var4.setTemplate("ejbHomeImpl.j");
         var4.setPackage(var3.getBeanPackageName());
         var4.setOutputFile(var3.getSimpleHomeClassName() + ".java");
         var1.addElement(var4);
         var5 = new EjbCodeGenerator.Output();
         var5.setBeanInfo(var2);
         var5.setNamingConvention(var3);
         var5.setTemplate("ejbEOImpl.j");
         var5.setPackage(var3.getBeanPackageName());
         var5.setOutputFile(var3.getSimpleEJBObjectClassName() + ".java");
         var1.addElement(var5);
      }

      if (this.hasLocalClientView && this.hasDeclaredLocalHome) {
         var4 = new EjbCodeGenerator.Output();
         var4.setBeanInfo(var2);
         var4.setNamingConvention(var3);
         var4.setTemplate("ejbLocalHomeImpl.j");
         var4.setPackage(var3.getBeanPackageName());
         var4.setOutputFile(var3.getSimpleLocalHomeClassName() + ".java");
         var1.addElement(var4);
         var5 = new EjbCodeGenerator.Output();
         var5.setBeanInfo(var2);
         var5.setNamingConvention(var3);
         var5.setTemplate("ejbELOImpl.j");
         var5.setPackage(var3.getBeanPackageName());
         var5.setOutputFile(var3.getSimpleEJBLocalObjectClassName() + ".java");
         var1.addElement(var5);
      }

      if (((EntityBeanInfo)var2).getIsBeanManagedPersistence()) {
         var4 = new EjbCodeGenerator.Output();
         var4.setBeanInfo(var2);
         var4.setNamingConvention(var3);
         var4.setTemplate("ejbBeanImpl.j");
         var4.setPackage(var3.getBeanPackageName());
         var4.setOutputFile(var3.getSimpleGeneratedBeanClassName() + ".java");
         var1.addElement(var4);
         var5 = new EjbCodeGenerator.Output();
         var5.setBeanInfo(var2);
         var5.setNamingConvention(var3);
         var5.setTemplate("ejbBeanIntf.j");
         var5.setPackage(var3.getBeanPackageName());
         var5.setOutputFile(var3.getSimpleGeneratedBeanInterfaceName() + ".java");
         var1.addElement(var5);
      }

   }
}
