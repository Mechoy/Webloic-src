package weblogic.corba.rmic;

import java.io.Externalizable;
import java.io.Serializable;
import java.rmi.Remote;
import java.util.Hashtable;
import weblogic.iiop.IDLUtils;
import weblogic.utils.Debug;
import weblogic.utils.compiler.CodeGenerationException;

public class IDLTypeSpecial extends IDLType {
   String m_exName = null;
   String m_exceptionName = null;
   Class m_class = null;
   String m_guard = null;
   static final Class JAVA_LANG_OBJECT = Object.class;
   static final String JAVA_LANG_OBJECT_CONTENT = "  typedef any _Object;\n";
   static final Class CORBA_OBJECT = org.omg.CORBA.Object.class;
   static final Class JAVA_RMI_REMOTE = Remote.class;
   static final String JAVA_RMI_REMOTE_CONTENT = "  typedef Object Remote;\n";
   static final Class JAVA_IO_SERIALIZABLE = Serializable.class;
   static final String JAVA_IO_SERIALIZABLE_CONTENT = "  typedef any Serializable;\n";
   static final Class JAVA_IO_EXTERNALIZABLE = Externalizable.class;
   static final String JAVA_IO_EXTERNALIZABLE_CONTENT = "typedef any Externalizable;\n";
   static final Class JAVA_LANG_CLASS = Class.class;
   static final String JAVA_LANG_CLASS_CONTENT = " valuetype ClassDesc {\n  private ::CORBA::WStringValue codebase;\n  private ::CORBA::WStringValue repid;\n  #pragma ID ClassDesc \"RMI:javax.rmi.CORBA.ClassDesc:2BABDA04587ADCCC:CFBF02CF5294176B\"\n };\n";
   static final String JAVA_LANG_CLASS_CONTENT_STRUCT = "struct ClassDesc {\n  ::CORBA::WStringValue codebase;\n  ::CORBA::WStringValue repid;\n};\n";
   static String[] SPECIAL_FILES_CONTENT = new String[]{"  typedef any _Object;\n", "  typedef any Serializable;\n", "typedef any Externalizable;\n", "  typedef Object Remote;\n", "", " valuetype ClassDesc {\n  private ::CORBA::WStringValue codebase;\n  private ::CORBA::WStringValue repid;\n  #pragma ID ClassDesc \"RMI:javax.rmi.CORBA.ClassDesc:2BABDA04587ADCCC:CFBF02CF5294176B\"\n };\n", "struct ClassDesc {\n  ::CORBA::WStringValue codebase;\n  ::CORBA::WStringValue repid;\n};\n"};
   static Class[] SPECIAL_FILES;
   public static final TypeTraits TRAITS;
   int m_index = -1;

   public IDLTypeSpecial(Class var1, Class var2) {
      super(var1, var2);

      for(int var3 = 0; var3 < SPECIAL_FILES.length; ++var3) {
         if (SPECIAL_FILES[var3].equals(var1)) {
            if (Class.class.equals(var1) && IDLOptions.getNoValueTypes()) {
               this.m_index = var3 + 1;
            } else {
               this.m_index = var3;
            }
            break;
         }
      }

      Debug.assertion(-1 != this.m_index);
   }

   public String getIncludeDeclaration() throws CodeGenerationException {
      return "";
   }

   public String getForwardDeclaration() throws CodeGenerationException {
      return SPECIAL_FILES_CONTENT[this.m_index].length() == 0 ? "" : IDLUtils.generateInclude((String)null, this.getJavaClass());
   }

   public String beforeMainDeclaration() {
      return "";
   }

   public String afterMainDeclaration() {
      return "";
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      return "";
   }

   public String getOpenBrace() {
      return SPECIAL_FILES_CONTENT[this.m_index];
   }

   public String getCloseBrace() {
      return "";
   }

   public Hashtable getMethods() {
      return new Hashtable();
   }

   public Hashtable getAttributes() {
      return new Hashtable();
   }

   public void getReferences(Hashtable var1) {
   }

   public String getPragmaID() {
      return "";
   }

   public boolean canHaveSubtype(IDLType var1) {
      return false;
   }

   static {
      SPECIAL_FILES = new Class[]{JAVA_LANG_OBJECT, JAVA_IO_SERIALIZABLE, JAVA_IO_EXTERNALIZABLE, JAVA_RMI_REMOTE, CORBA_OBJECT, JAVA_LANG_CLASS};
      TRAITS = new TypeTraits() {
         public Class getValidClass(Class var1, Class var2) {
            for(int var3 = 0; var3 < IDLTypeSpecial.SPECIAL_FILES.length; ++var3) {
               if (var1.equals(IDLTypeSpecial.SPECIAL_FILES[var3])) {
                  return var1;
               }
            }

            return null;
         }

         public IDLType createType(Class var1, Class var2) {
            return new IDLTypeSpecial(var1, var2);
         }
      };
   }
}
