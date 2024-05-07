package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;

class MessageDrivenBeanImplGenerator implements Generator {
   private final NamingConvention nc;
   private final MessageDrivenBeanInfo mdbi;
   private final String clsName;
   private final String superClsName;

   MessageDrivenBeanImplGenerator(NamingConvention var1, MessageDrivenBeanInfo var2) {
      this.nc = var1;
      this.mdbi = var2;
      this.clsName = BCUtil.binName(this.nc.getGeneratedBeanClassName());
      this.superClsName = BCUtil.binName(this.nc.getBeanClassName());
   }

   public Generator.Output generate() {
      String var1 = BCUtil.binName(this.mdbi.getMessagingTypeInterfaceClass());
      ClassWriter var2 = new ClassWriter(0);
      var2.visit(49, 49, this.clsName, (String)null, this.superClsName, new String[]{var1});
      BCUtil.addDefInit(var2, this.superClsName);
      var2.visitEnd();
      return new ClassFileOutput(this.clsName, var2.toByteArray());
   }
}
