package weblogic.corba.rmic;

import java.util.Hashtable;
import weblogic.iiop.IDLUtils;
import weblogic.utils.compiler.CodeGenerationException;

public class IDLTypeNonConformant extends IDLType {
   public static final String OPENING_DECLARATION = "abstract valuetype";
   public static final TypeTraits TRAITS = new TypeTraits() {
      public Class getValidClass(Class var1, Class var2) {
         return IDLUtils.getNonConformantType(var1);
      }

      public IDLType createType(Class var1, Class var2) {
         return new IDLTypeNonConformant(var1, var2);
      }
   };

   public IDLTypeNonConformant(Class var1, Class var2) {
      super(var1, var2);
   }

   public String getIncludeDeclaration() throws CodeGenerationException {
      return IDLUtils.generateInclude(this.getDirectory(), this.getJavaClass());
   }

   public String getForwardDeclaration() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer("");
      if (!IDLOptions.getNoValueTypes()) {
         Class var2 = this.getJavaClass();
         var1.append(IDLUtils.generateGuard(var2, "#ifndef"));
         var1.append(IDLUtils.openModule(var2));
         var1.append("  abstract valuetype " + IDLUtils.stripPackage(IDLUtils.getIDLType(var2, "."))).append(";\n");
         var1.append(IDLUtils.closeModule(var2));
         var1.append(IDLUtils.generateGuard(var2, "#endif //"));
      }

      return var1.toString();
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      Class var1 = this.getEnclosingClass();
      String var2 = IDLType.getOpeningDecl(this, var1, this.getInheritedClasses(), "abstract valuetype", TRAITS);
      return var2;
   }

   public Hashtable getMethods() {
      return new Hashtable();
   }

   public Hashtable getAttributes() {
      return new Hashtable();
   }

   public void getReferences(Hashtable var1) {
      Class var2 = this.getJavaClass();
      getAll(var2, var1, false, false);
   }
}
