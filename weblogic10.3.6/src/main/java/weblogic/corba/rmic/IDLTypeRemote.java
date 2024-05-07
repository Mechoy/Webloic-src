package weblogic.corba.rmic;

import java.util.Hashtable;
import weblogic.iiop.IDLUtils;
import weblogic.utils.compiler.CodeGenerationException;

public class IDLTypeRemote extends IDLType {
   public static final TypeTraits TRAITS = new TypeTraits() {
      public Class getValidClass(Class var1, Class var2) {
         Class var3 = null;
         if (IDLUtils.isARemote(var1)) {
            var3 = var1;
         }

         return var3;
      }

      public IDLType createType(Class var1, Class var2) {
         return new IDLTypeRemote(var1, var2);
      }
   };
   public static final String OPENING_DECLARATION = "interface";

   public String getOpeningDeclarationString() {
      return "interface";
   }

   public IDLTypeRemote(Class var1, Class var2) {
      super(var1, var2);
   }

   public String getIncludeDeclaration() throws CodeGenerationException {
      return IDLUtils.generateInclude(this.getDirectory(), this.getJavaClass());
   }

   public String getForwardDeclaration() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Class var2 = this.getJavaClass();
      var1.append(IDLUtils.openModule(var2));
      var1.append(this.getOpeningDeclarationString() + " " + IDLUtils.stripPackage(IDLUtils.getIDLType(this.m_class, "."))).append(";\n");
      var1.append(IDLUtils.closeModule(var2));
      return var1.toString();
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      Class var1 = this.getEnclosingClass();
      return IDLType.getOpeningDecl(this, var1, this.getInheritedClasses(), "interface", TRAITS);
   }

   public void getReferences(Hashtable var1) {
      Class var2 = this.getJavaClass();
      getAll(var2, var1, false);
   }
}
