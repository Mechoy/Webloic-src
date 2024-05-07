package weblogic.corba.rmic;

import java.io.Externalizable;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import weblogic.corba.utils.ClassInfo;
import weblogic.iiop.IDLUtils;
import weblogic.utils.compiler.CodeGenerationException;

public class IDLTypeValueType extends IDLType {
   private boolean opaque = false;
   private boolean custom = false;
   public static final TypeTraits TRAITS = new TypeTraits() {
      public Class getValidClass(Class var1, Class var2) {
         return IDLUtils.isConcreteValueType(var1) ? var1 : null;
      }

      public IDLType createType(Class var1, Class var2) {
         return new IDLTypeValueType(var1, var2);
      }
   };

   public IDLTypeValueType(Class var1, Class var2) {
      super(var1, var2);
      if (Externalizable.class.isAssignableFrom(var1)) {
         this.opaque = true;
      } else {
         this.custom = ClassInfo.findClassInfo(var1).getDescriptor().isCustomMarshaled();
      }

   }

   public String getIncludeDeclaration() throws CodeGenerationException {
      String var1 = new String();
      if (!IDLUtils.isException(this.getJavaClass()) || this.getEnclosingClass() != null && this.getEnclosingClass().isArray()) {
         var1 = IDLUtils.generateInclude(this.getDirectory(), this.getJavaClass());
      }

      return var1;
   }

   public String getForwardDeclaration() throws CodeGenerationException {
      return this.getForwardDeclaration(true);
   }

   public String getForwardDeclaration(boolean var1) throws CodeGenerationException {
      StringBuffer var2 = new StringBuffer();
      Class var3 = this.getJavaClass();
      if (IDLUtils.isException(var3) && (this.getEnclosingClass() == null || !this.getEnclosingClass().isArray())) {
         return IDLUtils.generateInclude(this.getDirectory(), var3);
      } else {
         if (this.isRequired()) {
            if (var1) {
               var2.append(IDLUtils.generateGuard(var3, "#ifndef"));
            }

            var2.append(IDLUtils.openModule(var3));
            var2.append("  " + this.getOpeningKeyword(true) + " " + IDLUtils.stripPackage(IDLUtils.getIDLType(this.m_class, "."))).append(";\n");
            var2.append(IDLUtils.closeModule(var3));
            if (var1) {
               var2.append(IDLUtils.generateGuard(var3, "#endif //"));
            }
         }

         return var2.toString();
      }
   }

   private String getOpeningKeyword(boolean var1) {
      return (this.opaque || this.custom) && !var1 ? "custom valuetype" : "valuetype";
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      Class var1 = this.getEnclosingClass();
      String var2 = IDLType.getOpeningDecl(this, var1, this.getInheritedClasses(), this.getOpeningKeyword(false), TRAITS);
      return var2;
   }

   public void getReferences(Hashtable var1) {
      Class var2 = this.getJavaClass();
      getAll(var2, var1, false, false);
   }

   public Hashtable getAttributes() {
      Hashtable var1 = super.getAttributes();
      if (this.opaque) {
         Enumeration var2 = var1.elements();

         while(var2.hasMoreElements()) {
            IDLField var3 = (IDLField)var2.nextElement();
            if (!var3.isPublic()) {
               var1.remove(var3.getMangledName());
            }
         }
      }

      return var1;
   }

   public Hashtable getMethods() {
      Hashtable var1 = new Hashtable();
      if (IDLOptions.getFactories()) {
         new StringBuffer();
         Constructor[] var3 = this.getJavaClass().getConstructors();
         if (var3.length <= 1) {
            Class[] var4 = var3.length == 0 ? new Class[0] : var3[0].getParameterTypes();
            IDLMethod var5 = new IDLMethod(this.getJavaClass(), this.getJavaClass().getName(), "factory create", (Class)null, var4, new Class[0]);
            var1.put(var5.getMangledName(), var5);
         } else {
            for(int var8 = 0; var8 < var3.length; ++var8) {
               Constructor var9 = var3[var8];
               String var6 = "factory " + IDLMangler.convertOverloadedName("create", var9.getParameterTypes());
               IDLMethod var7 = new IDLMethod(this.getJavaClass(), this.getJavaClass().getName(), var6, (Class)null, var9.getParameterTypes(), new Class[0]);
               var1.put(var7.getMangledName(), var7);
            }
         }
      }

      return var1;
   }
}
