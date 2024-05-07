package weblogic.servlet.ejb2jsp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import weblogic.servlet.ejb2jsp.dd.MethodParamDescriptor;

class TLDOutputter {
   static final String DTD = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n<!DOCTYPE taglib\nPUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN\" \"http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd\">\n";
   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   PrintStream ps;
   List methods;

   TLDOutputter(List var1) {
      this.ps = new PrintStream(this.baos);
      this.methods = var1;
   }

   void p(String var1) {
      this.ps.print(var1);
   }

   String generate() {
      this.p("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n<!DOCTYPE taglib\nPUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN\" \"http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd\">\n");
      this.p("<taglib>\n");
      this.p("<tlibversion>1.0</tlibversion>\n");
      this.p("<shortname>ejb2jsp generated tag library</shortname>\n");
      Iterator var1 = this.methods.iterator();

      while(var1.hasNext()) {
         this.doMethod((EJBMethodGenerator)var1.next());
      }

      this.p("</taglib>");
      this.ps.flush();
      return this.baos.toString();
   }

   void doMethod(EJBMethodGenerator var1) {
      this.p(" <tag>\n");
      this.p("  <name>" + var1.getTagName() + "</name>\n");
      this.p("  <tagclass>" + var1.generated_class_name() + "</tagclass>\n");
      MethodParamDescriptor[] var2 = var1.getDescriptor().getParams();
      String[] var3 = new String[var2.length];
      String[] var4 = new String[var2.length];
      boolean[] var5 = new boolean[var2.length];

      int var6;
      for(var6 = 0; var6 < var2.length; ++var6) {
         var3[var6] = var2[var6].getType();
         var4[var6] = var2[var6].getName();
         if (!"EXPRESSION".equalsIgnoreCase(var2[var6].getDefault()) && !"METHOD".equalsIgnoreCase(var2[var6].getDefault())) {
            var5[var6] = true;
         } else {
            var5[var6] = false;
         }
      }

      if (!var1.needExtraInfoClass()) {
         this.p("  <info>this calls " + var1.getMethodName() + "()</info>\n");
      } else {
         this.p("  <teiclass>" + var1.generated_class_name() + "TEI</teiclass> \n");
         this.p("  <info>\n");

         for(var6 = 0; var6 < var3.length; ++var6) {
            this.p("   attribute '" + var4[var6] + "' expects java type '" + var3[var6] + "'\n");
         }

         this.p("  </info>\n");
      }

      for(var6 = 0; var6 < var4.length; ++var6) {
         this.p("  <attribute>\n");
         this.p("   <name>" + var4[var6] + "</name>\n");
         this.p("   <required>" + var5[var6] + "</required>\n");
         this.p("   <rtexprvalue>true</rtexprvalue>\n");
         this.p("  </attribute>\n");
      }

      if (!"void".equalsIgnoreCase(var1.getReturnType())) {
         this.p("  <attribute>\n");
         this.p("   <name>_return</name>\n");
         this.p("   <required>false</required>\n");
         this.p("   <rtexprvalue>false</rtexprvalue>\n");
         this.p("  </attribute>\n");
      }

      this.p(" </tag>\n");
   }
}
