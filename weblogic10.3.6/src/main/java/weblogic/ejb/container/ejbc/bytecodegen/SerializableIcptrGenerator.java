package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;

class SerializableIcptrGenerator implements Generator {
   private final String clsName;
   private final String superClsName;

   SerializableIcptrGenerator(String var1, String var2) {
      this.clsName = BCUtil.binName(var1);
      this.superClsName = BCUtil.binName(var2);
   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(0);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, new String[]{"java/io/Serializable"});
      BCUtil.addDefInit(var1, this.superClsName);
      BCUtil.addSerializationMethods(var1);
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }
}
