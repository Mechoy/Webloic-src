package weblogic.servlet.ejb2jsp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.servlet.ejb2jsp.dd.BeanDescriptor;
import weblogic.servlet.ejb2jsp.dd.EJBTaglibDescriptor;

public class BeanGenerator {
   private String homeTagInterfaceName;
   private List imports = new ArrayList();
   private List methods = new ArrayList();
   private BeanDescriptor dd;
   private EJBTaglibDescriptor parentDD;

   public BeanGenerator(EJBTaglibDescriptor var1, BeanDescriptor var2) {
      this.parentDD = var1;
      this.dd = var2;
      this.addImport(var2.getRemoteType());
      this.addImport(var2.getHomeType());
      if (var2.isStatefulBean()) {
         this.calculateHomeTagInterfaceName();
      }

   }

   public BeanDescriptor getDD() {
      return this.dd;
   }

   public EJBTaglibDescriptor getParentDD() {
      return this.parentDD;
   }

   public String getHomeTagInterfaceName() {
      return this.homeTagInterfaceName;
   }

   private void calculateHomeTagInterfaceName() {
      String var1 = this.dd.getHomeType();
      int var2 = var1.lastIndexOf(46);
      if (var2 > 0) {
         var1 = var1.substring(var2 + 1);
      }

      this.homeTagInterfaceName = this.parentDD.getFileInfo().getPackage() + ".__Base_" + var1 + "_homeTag";
   }

   public void addMethodImports(Method var1) {
      this.addImport(var1.getReturnType());
      Class[] var2 = var1.getParameterTypes();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         this.addImport(var2[var3]);
      }

   }

   public String getImportString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.getImports().iterator();

      while(var2.hasNext()) {
         var1.append("import " + var2.next() + ";\n");
      }

      return var1.toString();
   }

   public String[] generateSources() throws Exception {
      ArrayList var1 = new ArrayList();
      if (this.dd.isStatefulBean()) {
         HomeInterfaceGenerator var2 = new HomeInterfaceGenerator(this.parentDD.getOpts(), this.getImportString(), this.getHomeTagInterfaceName(), this.dd.getRemoteType());
         var1.add(var2.generate()[0]);
      }

      Iterator var6 = this.methods.iterator();

      while(var6.hasNext()) {
         EJBMethodGenerator var3 = (EJBMethodGenerator)var6.next();
         String[] var4 = var3.generate();
         int var5 = var4.length;
         var1.add(var4[0]);
         if (var5 > 1) {
            var1.add(var4[1]);
         }
      }

      String[] var7 = new String[var1.size()];
      var1.toArray(var7);
      return var7;
   }

   static void p(String var0) {
      System.err.println("[beangen]: " + var0);
   }

   public void addImport(Class var1) {
      if (var1 != Void.class && var1 != Void.TYPE) {
         this.addImport(var1.getName());
      }

   }

   private void addImport(String var1) {
      if (!Utils.isPrimitive(var1) && !this.imports.contains(var1)) {
         this.imports.add(var1);
      }

   }

   public String getPackage() {
      return this.parentDD.getFileInfo().getPackage();
   }

   public List getImports() {
      return this.imports;
   }

   public void addMethod(EJBMethodGenerator var1) {
      if (!this.methods.contains(var1)) {
         this.methods.add(var1);
      }

   }

   public List getMethods() {
      return this.methods;
   }
}
