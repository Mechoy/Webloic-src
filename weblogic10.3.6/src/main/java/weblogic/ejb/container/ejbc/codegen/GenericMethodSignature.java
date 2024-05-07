package weblogic.ejb.container.ejbc.codegen;

import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.utils.PlatformConstants;

public final class GenericMethodSignature implements PlatformConstants {
   private boolean rmiSignature = false;
   private Method method;
   private List paramterizingHierarchy;

   public GenericMethodSignature(Method var1, Class var2) {
      this.setMethod(var1);
      if (var2 == null) {
         this.paramterizingHierarchy = null;
      } else {
         this.paramterizingHierarchy = this.getMethodDeclareHierarchy(var2, var1);
      }

   }

   public GenericMethodSignature(Method var1) {
      this.setMethod(var1);
      this.paramterizingHierarchy = null;
   }

   public void setRmiSignature(boolean var1) {
      this.rmiSignature = var1;
   }

   public Method getMethod() {
      return this.method;
   }

   public void setMethod(Method var1) {
      this.method = var1;
   }

   private List getMethodDeclareHierarchy(Type var1, Method var2) {
      if (!hasMethodDeclared(var1, var2)) {
         return null;
      } else {
         ArrayList var3 = new ArrayList();
         var3.add(var1);
         Class var4 = getClassFromTypeIfPossible(var1);
         Type[] var5 = null;

         do {
            if (var4 == null) {
               return var3;
            }

            if (var4.isInterface()) {
               var5 = var4.getGenericInterfaces();
            } else {
               Type[] var6 = var4.getGenericInterfaces();
               var5 = new Type[var6.length + 1];
               var5[0] = var4.getGenericSuperclass();

               for(int var7 = 1; var7 < var6.length + 1; ++var7) {
                  var5[var7] = var6[var7 - 1];
               }
            }

            if (var5 == null) {
               return var3;
            }

            for(int var8 = 0; var8 < var5.length; ++var8) {
               var4 = getClassFromTypeIfPossible(var5[var8]);
               if (hasMethodDeclared(var5[var8], var2)) {
                  if (var2.getDeclaringClass().equals(var4) && !(var5[var8] instanceof ParameterizedType)) {
                     return null;
                  }

                  if (var2.getDeclaringClass().equals(var4)) {
                     var3.add(var5[var8]);
                     return var3;
                  }

                  var3.add(var5[var8]);
                  break;
               }
            }
         } while(var5.length > 0);

         return var3;
      }
   }

   public String getRealNameForTypeVariable(Type var1, boolean var2) {
      if (!(var1 instanceof TypeVariable)) {
         return null;
      } else {
         TypeVariable var3 = (TypeVariable)var1;
         StringBuffer var4 = new StringBuffer();
         Type[] var5 = var3.getBounds();
         if (isDelcaredInTypes(var3, this.method.getTypeParameters())) {
            if (var5[0] instanceof Class && ((Class)var5[0]).equals(Object.class)) {
               return this.getSimpleClassNameForType(var3);
            } else {
               var4.append(this.getSimpleClassNameForType(var3));
               if (var2) {
                  var4.append(" extends ");

                  for(int var8 = 0; var8 < var5.length; ++var8) {
                     if (var8 > 0) {
                        var4.append(" & ");
                     }

                     var4.append(this.getParameterizedName(var5[var8]));
                  }
               }

               return var4.toString();
            }
         } else {
            Type var6 = this.getMappedTypeForTypeVariable(var3, this.paramterizingHierarchy);
            if (var6 == null) {
               byte var7 = 0;
               if (var7 < var5.length) {
                  var4.append(this.getSimpleClassNameForType(var5[var7]));
               }

               return var4.toString();
            } else {
               return this.getParameterizedName(var6);
            }
         }
      }
   }

   public String getRealNameForWildcardType(Type var1) {
      if (!(var1 instanceof WildcardType)) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer();
         if (var1.toString().equals("?")) {
            var2.append("?");
         } else {
            Type[] var3 = ((WildcardType)var1).getUpperBounds();
            Type[] var4 = ((WildcardType)var1).getLowerBounds();
            byte var5;
            if (var4.length == 0) {
               var2.append("? extends ");
               var5 = 0;
               if (var5 < var3.length) {
                  var2.append(this.getParameterizedName(var3[var5]));
               }
            } else {
               var2.append("? super ");
               var5 = 0;
               if (var5 < var4.length) {
                  var2.append(this.getParameterizedName(var4[var5]));
               }
            }
         }

         return var2.toString();
      }
   }

   public String getRealNameForParameterizedType(Type var1) {
      if (!(var1 instanceof ParameterizedType)) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer();
         Type var3 = ((ParameterizedType)var1).getRawType();
         if (var3 instanceof Class) {
            var2.append(this.getSimpleClassNameForType(var3) + "<");
         }

         Type[] var4 = ((ParameterizedType)var1).getActualTypeArguments();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5] instanceof Class) {
               var2.append(this.getSimpleClassNameForType(var4[var5]));
            } else if (var4[var5] instanceof TypeVariable) {
               var2.append(this.getRealNameForTypeVariable(var4[var5], false));
            } else if (var4[var5] instanceof ParameterizedType) {
               var2.append(this.getRealNameForParameterizedType(var4[var5]));
            } else if (var4[var5] instanceof WildcardType) {
               var2.append(this.getRealNameForWildcardType(var4[var5]));
            } else if (var4[var5] instanceof GenericArrayType) {
               var2.append(this.getParameterizedName(var4[var5]));
            }

            if (var5 < var4.length - 1) {
               var2.append(",");
            }
         }

         var2.append(">");
         return var2.toString();
      }
   }

   public String getParameterizedName(Type var1) {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof Class) {
         return this.getSimpleClassNameForType(var1);
      } else if (var1 instanceof TypeVariable) {
         return this.getRealNameForTypeVariable(var1, false);
      } else if (var1 instanceof ParameterizedType) {
         return this.getRealNameForParameterizedType(var1);
      } else {
         return var1 instanceof GenericArrayType ? this.getParameterizedName(((GenericArrayType)var1).getGenericComponentType()) + "[]" : null;
      }
   }

   private Type findClassDeclarationInHierarchy(Type var1, List var2) {
      if (var1 instanceof TypeVariable && var2 != null) {
         GenericDeclaration var3 = ((TypeVariable)var1).getGenericDeclaration();
         if (!(var3 instanceof Method) && !(var3 instanceof Constructor)) {
            Type var4 = null;
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               Type var6 = (Type)var5.next();
               if (!(var3 instanceof Class)) {
                  return null;
               }

               if (this.getSimpleClassNameForType(var6).equals(this.getSimpleClassNameForType((Class)var3))) {
                  var4 = var6;
                  break;
               }
            }

            return var4;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private Map getSuperGenericMapping(Type var1) {
      if (var1 == null) {
         return null;
      } else {
         HashMap var2 = new HashMap();
         if (var1 instanceof ParameterizedType) {
            Type var3 = ((ParameterizedType)var1).getRawType();
            Class var4 = getClassFromTypeIfPossible(var3);
            TypeVariable[] var5 = var4.getTypeParameters();
            Type[] var6 = ((ParameterizedType)var1).getActualTypeArguments();
            if (var5.length != var6.length) {
               throw new MalformedParameterizedTypeException();
            }

            for(int var7 = 0; var7 < var5.length; ++var7) {
               var2.put(var5[var7], var6[var7]);
            }
         }

         return var2;
      }
   }

   private Type getMappedTypeForTypeVariable(Type var1, List var2) {
      if (!(var1 instanceof TypeVariable)) {
         return null;
      } else {
         Type var3 = this.findClassDeclarationInHierarchy(var1, var2);
         Map var4 = this.getSuperGenericMapping(var3);
         return var4 == null ? null : (Type)var4.get(var1);
      }
   }

   public String getSimpleClassNameForType(Type var1) {
      Class var2 = null;
      if (var1 == null) {
         return null;
      } else if (!(var1 instanceof Class)) {
         if (var1 instanceof ParameterizedType) {
            Type var6 = ((ParameterizedType)var1).getRawType();
            if (var6 instanceof Class) {
               return ((Class)var6).getName();
            }
         }

         return var1.toString().replaceFirst("(class |interface )", "").trim();
      } else {
         var2 = (Class)var1;

         int var3;
         for(var3 = 0; var2.isArray(); var2 = var2.getComponentType()) {
            ++var3;
         }

         StringBuffer var4 = new StringBuffer(var2.getName());
         if (!this.rmiSignature) {
            for(Class var5 = var2.getDeclaringClass(); var5 != null; var5 = var5.getDeclaringClass()) {
               var4.setCharAt(var5.getName().length(), '.');
            }
         }

         for(int var7 = 0; var7 < var3; ++var7) {
            var4.append("[]");
         }

         return var4.toString();
      }
   }

   public static Class getClassFromTypeIfPossible(Type var0) {
      if (var0 instanceof ParameterizedType) {
         Type var1 = ((ParameterizedType)var0).getRawType();
         if (var1 instanceof Class) {
            return (Class)var1;
         }
      }

      return var0 instanceof Class ? (Class)var0 : null;
   }

   public static boolean hasMethodDeclared(Type var0, Method var1) {
      Class var2 = getClassFromTypeIfPossible(var0);
      if (var2 != null) {
         Method[] var3 = var2.getMethods();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isDelcaredInTypes(Type var0, Type[] var1) {
      if (var0 != null && var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].equals(var0)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
