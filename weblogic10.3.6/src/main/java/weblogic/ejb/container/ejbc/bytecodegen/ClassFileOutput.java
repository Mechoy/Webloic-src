package weblogic.ejb.container.ejbc.bytecodegen;

import java.io.File;

class ClassFileOutput implements Generator.Output {
   private final String name;
   private final byte[] bytes;

   ClassFileOutput(String var1, byte[] var2) {
      this.name = var1.replace('/', File.separatorChar) + ".class";
      this.bytes = var2;
   }

   public String relativeFilePath() {
      return this.name;
   }

   public byte[] bytes() {
      return this.bytes;
   }
}
