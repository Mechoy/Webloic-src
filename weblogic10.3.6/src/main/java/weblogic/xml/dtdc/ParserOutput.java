package weblogic.xml.dtdc;

import com.ibm.xml.parser.DTD;
import weblogic.utils.compiler.CodeGenerator;

public class ParserOutput extends CodeGenerator.Output {
   private DTD dtd;
   private String dtdname;

   ParserOutput(String var1, String var2, String var3, DTD var4, String var5) {
      super(var1, var3, var2);
      this.dtd = var4;
      this.dtdname = var5;
   }

   public DTD getDTD() {
      return this.dtd;
   }

   public String getDTDName() {
      return this.dtdname;
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
