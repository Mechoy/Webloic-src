package weblogic.corba.rmic;

import java.util.Hashtable;
import weblogic.iiop.IDLUtils;
import weblogic.utils.Debug;
import weblogic.utils.compiler.CodeGenerationException;

public class IDLTypeSequence extends IDLType {
   public static final String OPENING_DECLARATION = "valuetype";
   Class m_componentType;
   IDLType idlComponent;
   public static final TypeTraits TRAITS = new TypeTraits() {
      public Class getValidClass(Class var1, Class var2) {
         Class var3 = null;
         if (IDLUtils.isValid(var1) && null != var1.getComponentType()) {
            var3 = var1;
         }

         return var3;
      }

      public IDLType createType(Class var1, Class var2) {
         return new IDLTypeSequence(var1, var2);
      }
   };

   public IDLTypeSequence(Class var1, Class var2) {
      super(var1, var2);
      this.m_componentType = var1.getComponentType();
      Debug.assertion(null != this.m_componentType);
      if (IDLUtils.isValid(this.m_componentType)) {
         for(this.idlComponent = IDLType.createType(this.m_componentType, this.getJavaClass()); this.idlComponent instanceof IDLTypeEx; this.idlComponent = ((IDLTypeEx)this.idlComponent).getEnclosed()) {
         }
      }

   }

   public String getIncludeDeclaration() throws CodeGenerationException {
      return "";
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      Class var1 = this.getJavaClass();
      Debug.assertion(null != this.m_componentType);
      String var2 = IDLUtils.stripPackage(IDLUtils.getIDLType(var1, "."));
      return "typedef sequence<" + IDLUtils.getIDLType(this.m_componentType) + "> " + var2 + "_t ;\n" + "valuetype" + " " + var2 + " " + var2 + "_t ;\n";
   }

   public String getForwardDeclaration() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      if (this.idlComponent instanceof IDLTypeValueType) {
         var1.append(((IDLTypeValueType)this.idlComponent).getForwardDeclaration(false));
      } else if (this.m_componentType.equals(this.getEnclosingClass())) {
         var1.append(this.idlComponent.getForwardDeclaration());
      }

      var1.append(IDLUtils.generateInclude(this.getDirectory(), this.getJavaClass()));
      return var1.toString();
   }

   public String getOpenBrace() {
      return "";
   }

   public String getCloseBrace() {
      return "";
   }

   public void getReferences(Hashtable var1) {
      if (this.idlComponent != null) {
         var1.put(this.idlComponent.getClassName(), this.idlComponent);
      }

   }

   public String getPragmaID() {
      return IDLUtils.getPragmaID(this.getJavaClass());
   }

   public boolean canHaveSubtype(IDLType var1) {
      return false;
   }
}
