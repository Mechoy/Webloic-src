package weblogic.corba.rmic;

import weblogic.iiop.IDLUtils;
import weblogic.iiop.Utils;
import weblogic.utils.compiler.CodeGenerationException;

public final class IDLMethod implements Cloneable {
   Class m_class = null;
   String m_mangledName = null;
   String m_name = null;
   Class m_returnType = null;
   Class[] m_parameterTypes = null;
   Class[] m_exceptionTypes = null;

   public IDLMethod(Class var1, String var2, String var3, Class var4, Class[] var5, Class[] var6) {
      this.m_class = var1;
      this.m_name = var2;
      this.m_mangledName = var3;
      this.m_returnType = var4;
      this.m_parameterTypes = var5;
      this.m_exceptionTypes = var6;
   }

   public String getName() {
      return this.m_name;
   }

   public Class getReturnType() {
      return this.m_returnType;
   }

   public Class[] getExceptionTypes() {
      return this.m_exceptionTypes;
   }

   public Class[] getParameterTypes() {
      return this.m_parameterTypes;
   }

   public String getMangledName() {
      return this.m_mangledName;
   }

   public boolean isRequired() throws CodeGenerationException {
      int var1;
      if (IDLOptions.getNoAbstract()) {
         if (Utils.isAbstractInterface(this.m_returnType)) {
            return false;
         }

         for(var1 = 0; var1 < this.m_parameterTypes.length; ++var1) {
            if (Utils.isAbstractInterface(this.m_parameterTypes[var1])) {
               return false;
            }
         }

         for(var1 = 0; var1 < this.m_exceptionTypes.length; ++var1) {
            if (Utils.isAbstractInterface(this.m_exceptionTypes[var1])) {
               return false;
            }
         }
      } else if (IDLOptions.getNoValueTypes()) {
         if (IDLUtils.isValueType(this.m_returnType)) {
            return false;
         }

         for(var1 = 0; var1 < this.m_parameterTypes.length; ++var1) {
            if (IDLUtils.isValueType(this.m_parameterTypes[var1])) {
               return false;
            }
         }

         for(var1 = 0; var1 < this.m_exceptionTypes.length; ++var1) {
            if (IDLUtils.isValueType(this.m_exceptionTypes[var1])) {
               return false;
            }
         }
      }

      return true;
   }

   public String toIDL() {
      String var1 = null;
      String var2 = this.m_mangledName;
      StringBuffer var3 = new StringBuffer();
      if (null != this.m_returnType) {
         var3.append(IDLUtils.getIDLType(this.m_returnType)).append(" ");
      }

      var3.append(var2).append("(");

      for(int var4 = 0; var4 < this.m_parameterTypes.length; ++var4) {
         if (var4 > 0) {
            var3.append(", ");
         }

         var3.append(" in ").append(IDLUtils.getIDLType(this.m_parameterTypes[var4])).append(" arg").append(var4);
      }

      var3.append(")");
      Class[] var8 = this.m_exceptionTypes;
      switch (var8.length) {
         case 0:
            break;
         case 1:
            if (IDLUtils.isACheckedException(var8[0])) {
               String var5 = IDLUtils.exceptionToEx(IDLUtils.getIDLType(var8[0]));
               var3.append(IDLUtils.RAISES).append(var5).append(")");
            }
            break;
         default:
            boolean var9 = false;

            for(int var6 = 0; var6 < var8.length; ++var6) {
               if (IDLUtils.isACheckedException(var8[var6])) {
                  if (var9) {
                     var3.append(",");
                  } else {
                     var3.append(IDLUtils.RAISES);
                  }

                  String var7 = IDLUtils.exceptionToEx(IDLUtils.getIDLType(var8[var6]));
                  var3.append(var7);
                  var9 = true;
               }
            }

            if (var9) {
               var3.append(")");
            }
      }

      var3.append(";");
      var1 = var3.toString();
      return var1;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
