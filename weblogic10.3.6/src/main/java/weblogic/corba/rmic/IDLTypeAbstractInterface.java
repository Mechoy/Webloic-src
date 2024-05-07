package weblogic.corba.rmic;

import weblogic.iiop.IDLUtils;
import weblogic.utils.compiler.CodeGenerationException;

public class IDLTypeAbstractInterface extends IDLTypeRemote {
   public static final String OPENING_DECLARATION = "abstract interface";
   public static final TypeTraits TRAITS = new TypeTraits() {
      public Class getValidClass(Class var1, Class var2) {
         return IDLUtils.isAbstractInterface(var1) ? var1 : null;
      }

      public IDLType createType(Class var1, Class var2) {
         return new IDLTypeAbstractInterface(var1, var2);
      }
   };

   public String getOpeningDeclarationString() {
      return "abstract interface";
   }

   public IDLTypeAbstractInterface(Class var1, Class var2) {
      super(var1, var2);
   }

   public String getIncludeDeclaration() throws CodeGenerationException {
      return !IDLOptions.getNoAbstract() ? super.getIncludeDeclaration() : "// abstract interface " + this.getClassName() + " omitted.\n";
   }

   public String getForwardDeclaration() throws CodeGenerationException {
      return !IDLOptions.getNoAbstract() ? super.getForwardDeclaration() : "// abstract interface " + this.getClassName() + " omitted.\n";
   }

   public String getInheritKeyword(IDLType var1) {
      return IDLTypeValueType.class.isAssignableFrom(var1.getClass()) ? "supports" : super.getInheritKeyword(var1);
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      String var1 = "";
      Class var2 = this.getEnclosingClass();
      var1 = IDLType.getOpeningDecl(this, var2, this.getInheritedClasses(), "abstract interface", TRAITS);
      return var1;
   }
}
