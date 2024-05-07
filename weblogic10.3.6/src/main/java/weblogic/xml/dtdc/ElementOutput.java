package weblogic.xml.dtdc;

import com.ibm.xml.parser.DTD;
import com.ibm.xml.parser.ElementDecl;
import weblogic.utils.compiler.CodeGenerator;

public class ElementOutput extends CodeGenerator.Output {
   private ElementDecl elementDecl;
   private DTD dtd;

   ElementOutput(String var1, String var2, String var3, ElementDecl var4, DTD var5) {
      super(var1, var3, var2);
      this.elementDecl = var4;
      this.dtd = var5;
   }

   public ElementDecl getElementDecl() {
      return this.elementDecl;
   }

   public DTD getDTD() {
      return this.dtd;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof ElementOutput) {
         ElementOutput var2 = (ElementOutput)var1;
         return var2.getOutputFile().equals(this.getOutputFile()) && var2.getPackage().equals(this.getPackage());
      } else {
         return false;
      }
   }
}
