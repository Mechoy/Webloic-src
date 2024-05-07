package weblogic.ejb.container.persistence.spi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.ejbc.EjbCodeGenerator;
import weblogic.ejb.container.ejbc.codegen.MethodSignature;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.PersistenceType;
import weblogic.utils.AssertionError;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerator;

public abstract class CMPCodeGenerator extends EjbCodeGenerator {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   protected CMPBeanDescriptor bd = null;
   private PersistenceType associatedType;
   protected Hashtable finderMethods;

   public CMPCodeGenerator() {
   }

   public CMPCodeGenerator(Getopt2 var1) {
      super(var1);
   }

   public void setAssociatedType(PersistenceType var1) {
      this.associatedType = var1;
   }

   public void setCMPBeanDescriptor(CMPBeanDescriptor var1) {
      this.bd = var1;
   }

   protected void addOutputs(Vector var1, BeanInfo var2, NamingConvention var3) throws EJBCException {
      if (!(var2 instanceof EntityBeanInfo)) {
         throw new AssertionError("Can only generate container managed persistence code for EntityBeans.");
      } else {
         EntityBeanInfo var4 = (EntityBeanInfo)var2;
         Output var5;
         if (var4.hasRemoteClientView() || var4.hasLocalClientView()) {
            var5 = new Output(this);
            var5.setBeanInfo(var2);
            var5.setNamingConvention(var3);
            var5.setPackage(var3.getBeanPackageName());
            var5.setCMPBeanDescriptor(this.bd);
            var5.setPersistenceType(this.associatedType);
            List var6 = this.typeSpecificTemplates();
            Iterator var7 = var6.iterator();
            boolean var8 = true;

            while(var7.hasNext()) {
               String var9 = (String)var7.next();
               if (var8) {
                  var5.setTemplate(var9);
                  var8 = false;
               } else {
                  var5.addExtraTemplate(var9);
               }
            }

            var5.setOutputFile(var3.getSimpleCmpBeanClassName(this.ejbStoreType()) + ".java");
            var1.addElement(var5);
         }

         var5 = new Output(this);
         var5.setBeanInfo(var2);
         var5.setNamingConvention(var3);
         var5.setPackage(var3.getBeanPackageName());
         var5.setCMPBeanDescriptor(this.bd);
         var5.setPersistenceType(this.associatedType);
         var5.setTemplate("weblogic/ejb/container/ejbc/ejbBeanIntf.j");
         var5.setOutputFile(var3.getSimpleGeneratedBeanInterfaceName() + ".java");
         var1.addElement(var5);
      }
   }

   protected void prepare(CodeGenerator.Output var1) throws EJBCException, ClassNotFoundException {
      super.prepare(var1);
      this.finderMethods = new Hashtable();
      Method[] var2 = null;
      if (this.hasLocalClientView) {
         var2 = this.localFindMethods;
      } else {
         var2 = this.findMethods;
      }

      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            MethodSignature var4 = new MethodSignature(var2[var3]);
            var4.setAbstract(false);
            this.finderMethods.put(var4.asNameAndParamTypesKey(), var4);
         }
      }

   }

   private String ejbStoreType() {
      return this.associatedType.getIdentifier();
   }

   protected abstract List typeSpecificTemplates();

   public List listGenerate() throws Exception {
      String[] var1 = this.generate();
      ArrayList var2 = new ArrayList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.set(var3, var1[var3]);
      }

      return var2;
   }

   public String cmpBeanClassName() {
      return this.nc.getSimpleCmpBeanClassName(this.ejbStoreType());
   }

   public static void addToHashtable(Hashtable var0, Hashtable var1) {
      if (var0 != null) {
         Enumeration var2 = var0.keys();
         if (var2 != null) {
            while(var2.hasMoreElements()) {
               Object var3 = var2.nextElement();
               var1.put(var3, var0.get(var3));
            }

         }
      }
   }

   public static class Output extends EjbCodeGenerator.Output {
      private PersistenceType associatedType = null;
      private CMPCodeGenerator owningGenerator = null;
      private CMPBeanDescriptor beanDesc = null;

      public Output(CMPCodeGenerator var1) {
         this.owningGenerator = var1;
      }

      public InputStream getTemplateStream(String var1) throws IOException {
         return this.getClass().getResourceAsStream("/" + var1);
      }

      public void setPersistenceType(PersistenceType var1) {
         this.associatedType = var1;
      }

      public CMPBeanDescriptor getCMPBeanDescriptor() {
         return this.beanDesc;
      }

      public void setCMPBeanDescriptor(CMPBeanDescriptor var1) {
         this.beanDesc = var1;
      }
   }
}
