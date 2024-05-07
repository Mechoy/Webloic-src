package weblogic.corba.rmic;

import java.util.Hashtable;
import weblogic.iiop.IDLUtils;
import weblogic.utils.compiler.CodeGenerationException;

public class IDLTypeEx extends IDLType {
   String m_exName = null;
   String m_exceptionName = null;
   Class m_class = null;
   String m_guard = null;
   IDLTypeValueType m_enclosed;
   public static final TypeTraits TRAITS = new TypeTraits() {
      public Class getValidClass(Class var1, Class var2) {
         Class var3 = null;
         if (IDLUtils.isValid(var1) && IDLUtils.isException(var1)) {
            var3 = var1;
         }

         return var3;
      }

      public IDLType createType(Class var1, Class var2) {
         IDLTypeValueType var3 = new IDLTypeValueType(var1, var2);
         IDLType.m_usedTypes.put(var3.getFileName(), var3);
         return new IDLTypeEx(var1, var2, var3);
      }
   };
   private static final String IDL_EXT = ".idl";

   public IDLTypeEx(Class var1, Class var2, IDLTypeValueType var3) {
      super(var1, var2);
      this.m_class = this.getJavaClass();
      this.m_exceptionName = IDLUtils.stripPackage(IDLUtils.getIDLType(this.m_class), "::");
      this.m_exName = IDLUtils.exceptionToEx(this.m_exceptionName);
      this.m_guard = IDLUtils.exceptionToEx(IDLUtils.getIDLType(this.m_class, "_"));
      this.m_enclosed = var3;
   }

   public String getIncludeDeclaration() throws CodeGenerationException {
      return "";
   }

   public String getForwardDeclaration() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      String var2 = IDLUtils.javaTypeToPath((String)null, this.m_class);
      var2 = var2.substring(0, var2.length() - ".idl".length());
      var2 = IDLUtils.exceptionToEx(var2) + ".idl";
      var1.append("#include \"" + var2 + "\"\n");
      return var1.toString();
   }

   public String beforeMainDeclaration() {
      StringBuffer var1 = new StringBuffer();
      if (!IDLOptions.getNoValueTypes()) {
         var1.append(IDLUtils.generateGuard(this.m_class, "#ifndef"));
         var1.append(IDLUtils.openModule(this.m_class));
         var1.append("  valuetype ").append(this.m_exceptionName).append(";\n");
         var1.append(IDLUtils.closeModule(this.m_class));
         var1.append(IDLUtils.generateGuard(this.m_class, "#endif //"));
      }

      return var1.toString();
   }

   public IDLType getEnclosed() {
      return this.m_enclosed;
   }

   public String afterMainDeclaration() {
      String var1 = new String();
      if (!IDLOptions.getNoValueTypes()) {
         var1 = IDLUtils.generateInclude((String)null, this.m_class);
      }

      return var1;
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      StringBuffer var1 = (new StringBuffer("exception ")).append(this.m_exName).append(" ");
      return var1.toString();
   }

   public Hashtable getMethods() {
      return new Hashtable();
   }

   public Hashtable getAttributes() {
      return new Hashtable();
   }

   public Hashtable getExtraLines() {
      Hashtable var1 = new Hashtable();
      String var2 = "value";
      String var3 = IDLOptions.getNoValueTypes() ? "::CORBA::WStringValue" : this.m_exceptionName;
      IDLExtraLine var4 = new IDLExtraLine(var2, var3 + " " + var2 + ";\n");
      var1.put(var4.getMangledName(), var4);
      return var1;
   }

   public String getGuardName(String var1) {
      String var2 = IDLUtils.generateGuard(this.m_guard, var1);
      return var2;
   }

   public void getReferences(Hashtable var1) {
   }

   public String getFileName() {
      String var1 = IDLUtils.javaTypeToPath(this.getDirectory(), this.getJavaClass());
      var1 = var1.substring(0, var1.length() - ".idl".length());
      return IDLUtils.exceptionToEx(var1) + ".idl";
   }

   public String getPragmaID() {
      return "";
   }

   public boolean canHaveSubtype(IDLType var1) {
      return false;
   }
}
