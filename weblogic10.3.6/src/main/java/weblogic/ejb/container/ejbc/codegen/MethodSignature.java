package weblogic.ejb.container.ejbc.codegen;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import weblogic.utils.PlatformConstants;
import weblogic.utils.collections.AggregateKey;

public final class MethodSignature implements PlatformConstants {
   private static final boolean debug = false;
   private static final int MAX_LINE = 80;
   private boolean printThrowsClause = true;
   private GenericMethodSignature genericMethodSig;
   private String name;
   private Method method;
   private Class returnType;
   private Class[] parameterTypes;
   private Type[] typeParameters;
   private String[] parameterNames;
   private Class[] exceptionTypes;
   private int modifiers;

   MethodSignature() {
      this.parameterTypes = new Class[0];
      this.exceptionTypes = new Class[0];
   }

   public MethodSignature(Method var1) {
      this.setMethod(var1);
      this.setName(var1.getName());
      this.setReturnType(var1.getReturnType());
      this.setParameterTypes(var1.getParameterTypes());
      this.parameterNames = new String[this.parameterTypes.length];

      for(int var2 = 0; var2 < this.parameterNames.length; ++var2) {
         this.parameterNames[var2] = "arg" + var2;
      }

      this.setExceptionTypes(var1.getExceptionTypes());
      this.setModifiers(var1.getModifiers());
      GenericMethodSignature var3 = new GenericMethodSignature(var1);
      this.setGenericMethodSignature(var3);
   }

   public MethodSignature(Method var1, Class var2) {
      this.setMethod(var1);
      this.setName(var1.getName());
      this.setReturnType(var1.getReturnType());
      this.setTypeParameters(var1.getTypeParameters());
      this.setParameterTypes(var1.getParameterTypes());
      this.parameterNames = new String[this.parameterTypes.length];

      for(int var3 = 0; var3 < this.parameterNames.length; ++var3) {
         this.parameterNames[var3] = "arg" + var3;
      }

      this.setExceptionTypes(var1.getExceptionTypes());
      this.setModifiers(var1.getModifiers());
      GenericMethodSignature var4 = new GenericMethodSignature(var1, var2);
      this.setGenericMethodSignature(var4);
   }

   public MethodSignature(String var1) throws MalformedMethodSignatureException {
      this.exceptionTypes = new Class[0];
      MethodSignatureParser var2 = new MethodSignatureParser(var1);
      if (!var2.matchSignature(this)) {
         throw new MalformedMethodSignatureException(var1);
      }
   }

   public void setPrintThrowsClause(boolean var1) {
      this.printThrowsClause = var1;
   }

   public Object asNameAndParamTypesKey() {
      return new AggregateKey(this.name, new AggregateKey(this.parameterTypes));
   }

   public String toString() {
      return this.toString(true);
   }

   public String toString(boolean var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(Modifier.toString(this.modifiers & -129 & -65)).append(" ");
      String var3 = var2.toString();
      int var4 = var3.indexOf("strict ");
      if (var4 != -1) {
         var2.insert(var4 + "strict".length(), "fp");
      }

      if (this.typeParameters != null && this.typeParameters.length > 0) {
         var2.append(" <");
         var2.append(this.getTypeParametersName());
         var2.append("> ");
      }

      if (this.returnType != null) {
         var2.append(this.genericMethodSig.getParameterizedName(this.getGenericReturnType())).append(" ");
      }

      var2.append(this.name).append("(");

      int var6;
      for(var6 = 0; var6 < this.parameterTypes.length; ++var6) {
         var2.append(this.genericMethodSig.getParameterizedName(this.getGenericParameterTypes()[var6])).append(" ").append(this.parameterNames[var6]);
         if (var6 < this.parameterTypes.length - 1) {
            var2.append(", ");
         }
      }

      var2.append(")");
      if (this.printThrowsClause && this.exceptionTypes.length > 0) {
         var6 = var2.length();
         var2.append(" throws ");
         Type[] var7 = this.getGenericExceptionTypes();

         for(int var5 = 0; var5 < this.exceptionTypes.length; ++var5) {
            if (var7.length != this.exceptionTypes.length) {
               var2.append(this.toJavaCode(this.exceptionTypes[var5]));
            } else {
               var2.append(this.genericMethodSig.getParameterizedName(var7[var5]));
            }

            if (var5 < this.exceptionTypes.length - 1) {
               var2.append(", ");
            }
         }

         if (var1 && var2.length() > 80) {
            var2.insert(var6, EOL + "    ");
         }
      }

      return var2.toString();
   }

   public void setParameterNames(int var1, String var2) {
      this.parameterNames[var1] = var2;
   }

   public int getParameterIndex(String var1) throws Exception {
      for(int var2 = 0; var2 < this.parameterNames.length; ++var2) {
         if (this.parameterNames[var2].equals(var1)) {
            return var2;
         }
      }

      throw new Exception("No param named " + var1);
   }

   public Class getParameterType(String var1) throws Exception {
      int var2 = this.getParameterIndex(var1);
      return this.parameterTypes[var2];
   }

   public String getParameterNames(int var1) {
      return this.parameterNames[var1];
   }

   public void setRmiSignature(boolean var1) {
      this.genericMethodSig.setRmiSignature(var1);
   }

   public String getParameterTypesAsArgs() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.parameterTypes.length; ++var2) {
         if (var2 > 0) {
            var1.append(",");
         }

         var1.append(this.genericMethodSig.getParameterizedName(this.getGenericParameterTypes()[var2]));
      }

      return var1.toString();
   }

   public String getParametersAsArgs() {
      StringBuffer var1 = new StringBuffer(4 * this.parameterNames.length);

      for(int var2 = 0; var2 < this.parameterNames.length; ++var2) {
         var1.append(this.parameterNames[var2]);
         if (var2 < this.parameterNames.length - 1) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   public GenericMethodSignature getGenericMethodSignature() {
      return this.genericMethodSig;
   }

   public void setGenericMethodSignature(GenericMethodSignature var1) {
      this.genericMethodSig = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public Method getMethod() {
      return this.method;
   }

   public void setMethod(Method var1) {
      this.method = var1;
   }

   public Class getReturnType() {
      return this.returnType;
   }

   public void setReturnType(Class var1) {
      this.returnType = var1;
   }

   public Type getGenericReturnType() {
      return this.method.getGenericReturnType();
   }

   public String getReturnTypeName() {
      return this.genericMethodSig.getParameterizedName(this.getGenericReturnType());
   }

   public Class[] getParameterTypes() {
      return this.parameterTypes;
   }

   public void setParameterTypes(Class[] var1) {
      this.parameterTypes = var1;
   }

   public Type[] getGenericParameterTypes() {
      return this.method.getGenericParameterTypes();
   }

   public Type[] getTypeParameters() {
      return this.typeParameters;
   }

   public void setTypeParameters(Type[] var1) {
      this.typeParameters = var1;
   }

   public String getTypeParametersName() {
      if (this.typeParameters != null && this.typeParameters.length != 0) {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < this.typeParameters.length; ++var2) {
            Type var3 = this.typeParameters[var2];
            if (var3 instanceof TypeVariable) {
               var1.append(this.genericMethodSig.getRealNameForTypeVariable(var3, true));
            }

            if (var2 < this.typeParameters.length - 1) {
               var1.append(", ");
            }
         }

         return var1.toString();
      } else {
         return "";
      }
   }

   public String[] getParameterNames() {
      return this.parameterNames;
   }

   public void setParameterNames(String[] var1) {
      this.parameterNames = var1;
   }

   public int getNumberOfParameters() {
      return this.parameterTypes.length;
   }

   public Class[] getExceptionTypes() {
      return this.exceptionTypes;
   }

   public void setExceptionTypes(Class[] var1) {
      this.exceptionTypes = var1;
   }

   public Type[] getGenericExceptionTypes() {
      return this.method.getGenericExceptionTypes();
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public void setModifiers(int var1) {
      this.modifiers = var1;
   }

   public boolean isPublic() {
      return Modifier.isPublic(this.modifiers);
   }

   public void setPublic(boolean var1) {
      if (var1) {
         this.modifiers |= 1;
         this.setPrivate(false);
         this.setProtected(false);
      } else {
         this.modifiers &= -2;
      }

   }

   public boolean isPrivate() {
      return Modifier.isPrivate(this.modifiers);
   }

   public void setPrivate(boolean var1) {
      if (var1) {
         this.modifiers |= 2;
         this.setPublic(false);
         this.setProtected(false);
      } else {
         this.modifiers &= -3;
      }

   }

   public boolean isProtected() {
      return Modifier.isProtected(this.modifiers);
   }

   public void setProtected(boolean var1) {
      if (var1) {
         this.modifiers |= 4;
         this.setPublic(false);
         this.setPrivate(false);
      } else {
         this.modifiers &= -5;
      }

   }

   public boolean isStatic() {
      return Modifier.isStatic(this.modifiers);
   }

   public void setStatic(boolean var1) {
      if (var1) {
         this.modifiers |= 8;
      } else {
         this.modifiers &= -9;
      }

   }

   public boolean isFinal() {
      return Modifier.isFinal(this.modifiers);
   }

   public void setFinal(boolean var1) {
      if (var1) {
         this.modifiers |= 16;
         this.setAbstract(false);
      } else {
         this.modifiers &= -17;
      }

   }

   public boolean isSynchronized() {
      return Modifier.isSynchronized(this.modifiers);
   }

   public void setSynchronized(boolean var1) {
      if (var1) {
         this.modifiers |= 32;
      } else {
         this.modifiers &= -33;
      }

   }

   public boolean isVolatile() {
      return Modifier.isVolatile(this.modifiers);
   }

   public void setVolatile(boolean var1) {
      if (var1) {
         this.modifiers |= 64;
      } else {
         this.modifiers &= -65;
      }

   }

   public boolean isTransient() {
      return Modifier.isTransient(this.modifiers);
   }

   public void setTransient(boolean var1) {
      if (var1) {
         this.modifiers |= 128;
      } else {
         this.modifiers &= -129;
      }

   }

   public boolean isNative() {
      return Modifier.isNative(this.modifiers);
   }

   public void setNative(boolean var1) {
      if (var1) {
         this.modifiers |= 256;
         this.setAbstract(false);
      } else {
         this.modifiers &= -257;
      }

   }

   public boolean isAbstract() {
      return Modifier.isAbstract(this.modifiers);
   }

   public void setAbstract(boolean var1) {
      if (var1) {
         this.modifiers |= 1024;
         this.setFinal(false);
         this.setNative(false);
      } else {
         this.modifiers &= -1025;
      }

   }

   public static boolean equalsMethodsBySig(MethodSignature var0, MethodSignature var1) {
      if (var0 != null && var1 != null) {
         if (var0.getName().equals(var1.getName())) {
            if (var0.getNumberOfParameters() != var1.getNumberOfParameters()) {
               return false;
            } else if (var0.getParameterTypesAsArgs().equals(var1.getParameterTypesAsArgs())) {
               return true;
            } else {
               for(int var2 = 0; var2 < var0.getNumberOfParameters(); ++var2) {
                  String var3 = var0.getGenericMethodSignature().getParameterizedName(var0.getGenericParameterTypes()[var2]);
                  String var4 = var1.getGenericMethodSignature().getParameterizedName(var1.getGenericParameterTypes()[var2]);
                  if (!var3.equals(var4) && !var0.equalsParameterTypeWithClass(var0.getGenericParameterTypes()[var2], var1.getGenericParameterTypes()[var2]) && !var1.equalsParameterTypeWithClass(var1.getGenericParameterTypes()[var2], var0.getGenericParameterTypes()[var2])) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean equalsParameterTypeWithClass(Type var1, Type var2) {
      if (var2 instanceof Class) {
         String var3 = this.genericMethodSig.getParameterizedName(var1);
         int var4 = var3.indexOf("<");
         if (var4 > 0 && var4 < var3.length() - 1) {
            String var5 = var3.substring(0, var4);
            if (var5.equals(((Class)var2).getName())) {
               return true;
            }
         }
      }

      return false;
   }

   public static void compare(MethodSignature var0, MethodSignature var1) {
      if (var0.getName().equals(var1.getName())) {
         System.out.println("name ok.");
      } else {
         System.out.println(var0.getName() + " != " + var1.getName());
      }

      if (var0.getReturnType().equals(var1.getReturnType())) {
         System.out.println("return type ok.");
      } else {
         System.out.println(var0.getReturnType() + " != " + var1.getReturnType());
      }

      if (var0.getModifiers() == var1.getModifiers()) {
         System.out.println("mods ok.");
      } else {
         System.out.println(var0.getModifiers() + " != " + var1.getModifiers());
      }

   }

   private String toJavaCode(Class var1) {
      int var2;
      for(var2 = 0; var1.isArray(); var1 = var1.getComponentType()) {
         ++var2;
      }

      StringBuffer var3 = new StringBuffer(var1.getName());

      for(Class var4 = var1.getDeclaringClass(); var4 != null; var4 = var4.getDeclaringClass()) {
         var3.setCharAt(var4.getName().length(), '.');
      }

      for(int var5 = 0; var5 < var2; ++var5) {
         var3.append("[]");
      }

      return var3.toString();
   }
}
