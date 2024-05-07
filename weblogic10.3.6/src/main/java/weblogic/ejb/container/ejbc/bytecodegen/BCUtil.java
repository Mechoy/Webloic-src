package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.Label;
import com.bea.objectweb.asm.MethodVisitor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.ejb.container.interfaces.Invokable;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.utils.Serializer;
import weblogic.kernel.KernelStatus;

final class BCUtil {
   public static final String WL_INVOKABLE_CLS = binName(Invokable.class);
   public static final String WL_MD_FIELD_DESCRIPTOR = fieldDesc(MethodDescriptor.class);
   public static final int PFS_CLS = 49;
   private static final int DEFAULT_CHAINING_THRESHOLD = 100;
   private static final String CHAINING_THRESHOLD_PROPERTY = "weblogic.ejb.bytecodegen.methodchainingthreshold";
   private static final int METHOD_CHAINING_THRESHOLD = KernelStatus.isApplet() ? 100 : Integer.getInteger("weblogic.ejb.bytecodegen.methodchainingthreshold", 100);
   private static final String INVOKE_METHOD_DESC = "(Ljava/lang/Object;[Ljava/lang/Object;I)Ljava/lang/Object;";
   private static final String HANDLE_EX_METHOD_DESC = "(ILjava/lang/Throwable;)V";

   private BCUtil() {
   }

   public static void boxReturn(MethodVisitor var0, Class<?> var1) {
      if (var1.isPrimitive()) {
         if (var1 == Void.TYPE) {
            var0.visitInsn(1);
         } else if (var1 == Boolean.TYPE) {
            var0.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
         } else if (var1 == Byte.TYPE) {
            var0.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
         } else if (var1 == Character.TYPE) {
            var0.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
         } else if (var1 == Double.TYPE) {
            var0.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
         } else if (var1 == Float.TYPE) {
            var0.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
         } else if (var1 == Integer.TYPE) {
            var0.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
         } else if (var1 == Long.TYPE) {
            var0.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
         } else {
            if (var1 != Short.TYPE) {
               throw new AssertionError("Unknown primitive type : " + var1);
            }

            var0.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
         }
      }

   }

   public static void unboxReturn(MethodVisitor var0, Class<?> var1) {
      if (!var1.isPrimitive()) {
         var0.visitTypeInsn(192, binName(var1));
         var0.visitInsn(176);
      } else if (var1 == Void.TYPE) {
         var0.visitInsn(87);
         var0.visitInsn(177);
      } else if (var1 == Boolean.TYPE) {
         var0.visitTypeInsn(192, "java/lang/Boolean");
         var0.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z");
         var0.visitInsn(172);
      } else if (var1 == Byte.TYPE) {
         var0.visitTypeInsn(192, "java/lang/Byte");
         var0.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B");
         var0.visitInsn(172);
      } else if (var1 == Character.TYPE) {
         var0.visitTypeInsn(192, "java/lang/Character");
         var0.visitMethodInsn(182, "java/lang/Character", "charValue", "()C");
         var0.visitInsn(172);
      } else if (var1 == Double.TYPE) {
         var0.visitTypeInsn(192, "java/lang/Double");
         var0.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D");
         var0.visitInsn(175);
      } else if (var1 == Float.TYPE) {
         var0.visitTypeInsn(192, "java/lang/Float");
         var0.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F");
         var0.visitInsn(174);
      } else if (var1 == Integer.TYPE) {
         var0.visitTypeInsn(192, "java/lang/Integer");
         var0.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I");
         var0.visitInsn(172);
      } else if (var1 == Long.TYPE) {
         var0.visitTypeInsn(192, "java/lang/Long");
         var0.visitMethodInsn(182, "java/lang/Long", "longValue", "()J");
         var0.visitInsn(173);
      } else {
         if (var1 != Short.TYPE) {
            throw new AssertionError("Unknown primitive type : " + var1);
         }

         var0.visitTypeInsn(192, "java/lang/Short");
         var0.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S");
         var0.visitInsn(172);
      }

   }

   public static void boxArgs(MethodVisitor var0, Method var1) {
      int var2 = Modifier.isStatic(var1.getModifiers()) ? 0 : 1;
      boxArgs(var0, var1, var2);
   }

   public static void boxArgs(MethodVisitor var0, Method var1, int var2) {
      Class[] var3 = var1.getParameterTypes();
      if (var3.length == 0) {
         var0.visitInsn(1);
      } else {
         pushInsn(var0, var3.length);
         var0.visitTypeInsn(189, "java/lang/Object");
         int var4 = var2;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var0.visitInsn(89);
            pushInsn(var0, var5);
            Class var6 = var3[var5];
            if (!var6.isPrimitive()) {
               var0.visitVarInsn(25, var4);
            } else if (var6 == Boolean.TYPE) {
               var0.visitVarInsn(21, var4);
               var0.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
            } else if (var6 == Byte.TYPE) {
               var0.visitVarInsn(21, var4);
               var0.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            } else if (var6 == Character.TYPE) {
               var0.visitVarInsn(21, var4);
               var0.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            } else if (var6 == Double.TYPE) {
               var0.visitVarInsn(24, var4);
               ++var4;
               var0.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            } else if (var6 == Float.TYPE) {
               var0.visitVarInsn(23, var4);
               var0.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            } else if (var6 == Integer.TYPE) {
               var0.visitVarInsn(21, var4);
               var0.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            } else if (var6 == Long.TYPE) {
               var0.visitVarInsn(22, var4);
               ++var4;
               var0.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
            } else {
               if (var6 != Short.TYPE) {
                  throw new AssertionError("Unknown primitive type : " + var6);
               }

               var0.visitVarInsn(21, var4);
               var0.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
            }

            ++var4;
            var0.visitInsn(83);
         }

      }
   }

   private static void unboxArgs(MethodVisitor var0, Method var1) {
      Class[] var2 = var1.getParameterTypes();
      if (var2.length != 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var0.visitVarInsn(25, 2);
            pushInsn(var0, var3);
            var0.visitInsn(50);
            if (!var2[var3].isPrimitive()) {
               var0.visitTypeInsn(192, binName(var2[var3]));
            } else {
               Class var4 = var2[var3];
               if (var4 == Boolean.TYPE) {
                  var0.visitTypeInsn(192, "java/lang/Boolean");
                  var0.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z");
               } else if (var4 == Byte.TYPE) {
                  var0.visitTypeInsn(192, "java/lang/Byte");
                  var0.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B");
               } else if (var4 == Character.TYPE) {
                  var0.visitTypeInsn(192, "java/lang/Character");
                  var0.visitMethodInsn(182, "java/lang/Character", "charValue", "()C");
               } else if (var4 == Double.TYPE) {
                  var0.visitTypeInsn(192, "java/lang/Double");
                  var0.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D");
               } else if (var4 == Float.TYPE) {
                  var0.visitTypeInsn(192, "java/lang/Float");
                  var0.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F");
               } else if (var4 == Integer.TYPE) {
                  var0.visitTypeInsn(192, "java/lang/Integer");
                  var0.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I");
               } else if (var4 == Long.TYPE) {
                  var0.visitTypeInsn(192, "java/lang/Long");
                  var0.visitMethodInsn(182, "java/lang/Long", "longValue", "()J");
               } else {
                  if (var4 != Short.TYPE) {
                     throw new AssertionError("Unknown primitive type : " + var4);
                  }

                  var0.visitTypeInsn(192, "java/lang/Short");
                  var0.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S");
               }
            }
         }
      }

   }

   public static String getBoxedClassBinName(Class<?> var0) {
      if (!var0.isPrimitive()) {
         throw new AssertionError("Invoked for a NON primitive class : " + var0);
      } else if (var0 == Void.TYPE) {
         return "java/lang/Void";
      } else if (var0 == Boolean.TYPE) {
         return "java/lang/Boolean";
      } else if (var0 == Byte.TYPE) {
         return "java/lang/Byte";
      } else if (var0 == Character.TYPE) {
         return "java/lang/Character";
      } else if (var0 == Double.TYPE) {
         return "java/lang/Double";
      } else if (var0 == Float.TYPE) {
         return "java/lang/Float";
      } else if (var0 == Integer.TYPE) {
         return "java/lang/Integer";
      } else if (var0 == Long.TYPE) {
         return "java/lang/Long";
      } else if (var0 == Short.TYPE) {
         return "java/lang/Short";
      } else {
         throw new AssertionError("Unknown primitive type : " + var0);
      }
   }

   public static void addReturnDefaultValue(MethodVisitor var0, Class<?> var1) {
      if (!var1.isPrimitive()) {
         var0.visitInsn(1);
         var0.visitInsn(176);
      } else if (var1 == Void.TYPE) {
         var0.visitInsn(177);
      } else if (var1 != Boolean.TYPE && var1 != Byte.TYPE && var1 != Character.TYPE && var1 != Integer.TYPE && var1 != Short.TYPE) {
         if (var1 == Double.TYPE) {
            var0.visitInsn(14);
            var0.visitInsn(175);
         } else if (var1 == Float.TYPE) {
            var0.visitInsn(11);
            var0.visitInsn(174);
         } else {
            if (var1 != Long.TYPE) {
               throw new AssertionError("Unknown primitive type : " + var1);
            }

            var0.visitInsn(9);
            var0.visitInsn(173);
         }
      } else {
         var0.visitInsn(3);
         var0.visitInsn(172);
      }

   }

   public static void addInvoke(ClassWriter var0, Method[] var1, String var2, String var3) {
      if (var1 != null && var1.length != 0) {
         chainedInvokeAdder(var0, var1, var2, var3, 0);
      }
   }

   private static void chainedInvokeAdder(ClassWriter var0, Method[] var1, String var2, String var3, int var4) {
      int var5 = var4 / METHOD_CHAINING_THRESHOLD;
      String var6 = "__WL_invoke" + (var5 == 0 ? "" : "_" + var5);
      int var7 = Math.min(var4 + METHOD_CHAINING_THRESHOLD, var1.length);
      MethodVisitor var8 = var0.visitMethod(var5 == 0 ? 1 : 2, var6, "(Ljava/lang/Object;[Ljava/lang/Object;I)Ljava/lang/Object;", (String)null, new String[]{"java/lang/Throwable"});
      var8.visitCode();
      Label[] var9 = new Label[var7 - var4];

      for(int var10 = 0; var10 < var9.length; ++var10) {
         var9[var10] = new Label();
      }

      Label var13 = new Label();
      var8.visitVarInsn(21, 3);
      var8.visitTableSwitchInsn(var4, var7 - 1, var13, var9);

      for(int var11 = var4; var11 < var7; ++var11) {
         Method var12 = var1[var11];
         var8.visitLabel(var9[var11 - var4]);
         var8.visitVarInsn(25, 1);
         var8.visitTypeInsn(192, var2);
         unboxArgs(var8, var12);
         var8.visitMethodInsn(185, var2, var12.getName(), methodDesc(var12));
         boxReturn(var8, var12.getReturnType());
         var8.visitInsn(176);
      }

      var8.visitLabel(var13);
      if (var7 == var1.length) {
         var8.visitTypeInsn(187, "java/lang/IllegalArgumentException");
         var8.visitInsn(89);
         var8.visitTypeInsn(187, "java/lang/StringBuilder");
         var8.visitInsn(89);
         var8.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V");
         var8.visitLdcInsn("No method found for index : ");
         var8.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
         var8.visitVarInsn(21, 3);
         var8.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
         var8.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
         var8.visitMethodInsn(183, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
         var8.visitInsn(191);
      } else {
         var8.visitVarInsn(25, 0);
         var8.visitVarInsn(25, 1);
         var8.visitVarInsn(25, 2);
         var8.visitVarInsn(21, 3);
         var8.visitMethodInsn(183, var3, "__WL_invoke_" + (var5 + 1), "(Ljava/lang/Object;[Ljava/lang/Object;I)Ljava/lang/Object;");
         var8.visitInsn(176);
      }

      var8.visitMaxs(0, 0);
      var8.visitEnd();
      if (var7 != var1.length) {
         chainedInvokeAdder(var0, var1, var2, var3, var7);
      }

   }

   public static void addHandleException(ClassWriter var0, Method[] var1, String var2, Set<Class<?>> var3) {
      if (var1 != null && var1.length != 0) {
         chainedHandleExceptionAdder(var0, var1, var2, var3, 0);
      }
   }

   private static void chainedHandleExceptionAdder(ClassWriter var0, Method[] var1, String var2, Set<Class<?>> var3, int var4) {
      int var5 = var4 / METHOD_CHAINING_THRESHOLD;
      String var6 = "__WL_handleException" + (var5 == 0 ? "" : "_" + var5);
      int var7 = Math.min(var4 + METHOD_CHAINING_THRESHOLD, var1.length);
      MethodVisitor var8 = var0.visitMethod(var5 == 0 ? 1 : 2, var6, "(ILjava/lang/Throwable;)V", (String)null, new String[]{"java/lang/Throwable"});
      var8.visitCode();
      Label var9 = new Label();
      Label[] var10 = new Label[var7 - var4];

      for(int var11 = 0; var11 < var10.length; ++var11) {
         var10[var11] = new Label();
      }

      Label var17 = new Label();
      var8.visitVarInsn(21, 1);
      var8.visitTableSwitchInsn(var4, var7 - 1, var17, var10);

      for(int var12 = var4; var12 < var7; ++var12) {
         Method var13 = var1[var12];
         var8.visitLabel(var10[var12 - var4]);
         List var14 = combineExs(var3, var13);
         if (var14.isEmpty()) {
            var8.visitJumpInsn(167, var9);
         } else {
            Label var15 = new Label();
            Iterator var16 = var14.iterator();

            while(var16.hasNext()) {
               var8.visitVarInsn(25, 2);
               var8.visitTypeInsn(193, binName((Class)var16.next()));
               if (var16.hasNext()) {
                  var8.visitJumpInsn(154, var15);
               } else {
                  var8.visitJumpInsn(153, var9);
                  var8.visitLabel(var15);
                  var8.visitVarInsn(25, 2);
                  var8.visitInsn(191);
               }
            }
         }
      }

      var8.visitLabel(var17);
      if (var7 == var1.length) {
         var8.visitTypeInsn(187, "java/lang/IllegalArgumentException");
         var8.visitInsn(89);
         var8.visitTypeInsn(187, "java/lang/StringBuilder");
         var8.visitInsn(89);
         var8.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V");
         var8.visitLdcInsn("No method found for index : ");
         var8.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
         var8.visitVarInsn(21, 1);
         var8.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
         var8.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
         var8.visitMethodInsn(183, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
         var8.visitInsn(191);
      } else {
         var8.visitVarInsn(25, 0);
         var8.visitVarInsn(21, 1);
         var8.visitVarInsn(25, 2);
         var8.visitMethodInsn(183, var2, "__WL_handleException_" + (var5 + 1), "(ILjava/lang/Throwable;)V");
      }

      var8.visitLabel(var9);
      var8.visitInsn(177);
      var8.visitMaxs(0, 0);
      if (var7 != var1.length) {
         chainedHandleExceptionAdder(var0, var1, var2, var3, var7);
      }

   }

   public static void addHandleExceptionAssertMethod(ClassWriter var0) {
      MethodVisitor var1 = var0.visitMethod(1, "__WL_handleException", "(ILjava/lang/Throwable;)V", (String)null, new String[]{"java/lang/Throwable"});
      var1.visitCode();
      var1.visitTypeInsn(187, "java/lang/AssertionError");
      var1.visitInsn(89);
      var1.visitLdcInsn("This method should NOT get called");
      var1.visitMethodInsn(183, "java/lang/AssertionError", "<init>", "(Ljava/lang/Object;)V");
      var1.visitInsn(191);
      var1.visitMaxs(3, 3);
      var1.visitEnd();
   }

   public static String binName(Class<?> var0) {
      return var0.getName().replace('.', '/');
   }

   public static String binName(String var0) {
      return var0.replace('.', '/');
   }

   public static String fieldDesc(Class<?> var0) {
      if (var0.isPrimitive()) {
         if (var0 == Byte.TYPE) {
            return "B";
         } else if (var0 == Character.TYPE) {
            return "C";
         } else if (var0 == Double.TYPE) {
            return "D";
         } else if (var0 == Float.TYPE) {
            return "F";
         } else if (var0 == Integer.TYPE) {
            return "I";
         } else if (var0 == Long.TYPE) {
            return "J";
         } else if (var0 == Short.TYPE) {
            return "S";
         } else if (var0 == Boolean.TYPE) {
            return "Z";
         } else if (var0 == Void.TYPE) {
            return "V";
         } else {
            throw new AssertionError("Unknown primitive type : " + var0);
         }
      } else {
         return var0.isArray() ? binName(var0) : "L" + binName(var0) + ";";
      }
   }

   public static String methodDesc(Method var0) {
      return methodDesc(var0.getReturnType(), var0.getParameterTypes());
   }

   public static String methodDesc(Class<?> var0, Class<?>... var1) {
      StringBuilder var2 = new StringBuilder("(");
      if (var1 != null) {
         Class[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            var2.append(fieldDesc(var6));
         }
      }

      var2.append(")");
      var2.append(fieldDesc(var0));
      return var2.toString();
   }

   public static String[] exceptionsDesc(Class<?>[] var0) {
      if (var0 == null) {
         return new String[0];
      } else {
         String[] var1 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = binName(var0[var2]);
         }

         return var1;
      }
   }

   public static int loadOpcode(Class<?> var0) {
      if (var0 == Void.TYPE) {
         throw new IllegalArgumentException("Invalid type " + var0);
      } else if (!var0.isPrimitive()) {
         return 25;
      } else if (var0 == Float.TYPE) {
         return 23;
      } else if (var0 == Double.TYPE) {
         return 24;
      } else {
         return var0 == Long.TYPE ? 22 : 21;
      }
   }

   public static int storeOpcode(Class<?> var0) {
      if (var0 == Void.TYPE) {
         throw new IllegalArgumentException("Invalid type " + var0);
      } else if (!var0.isPrimitive()) {
         return 58;
      } else if (var0 == Float.TYPE) {
         return 56;
      } else if (var0 == Double.TYPE) {
         return 57;
      } else {
         return var0 == Long.TYPE ? 55 : 54;
      }
   }

   public static int returnOpcode(Class<?> var0) {
      if (!var0.isPrimitive()) {
         return 176;
      } else if (var0 == Void.TYPE) {
         return 177;
      } else if (var0 == Double.TYPE) {
         return 175;
      } else if (var0 == Float.TYPE) {
         return 174;
      } else {
         return var0 == Long.TYPE ? 173 : 172;
      }
   }

   public static void pushInsn(MethodVisitor var0, int var1) {
      if (var1 < -1) {
         var0.visitLdcInsn(var1);
      } else if (var1 <= 5) {
         switch (var1) {
            case -1:
               var0.visitInsn(2);
               break;
            case 0:
               var0.visitInsn(3);
               break;
            case 1:
               var0.visitInsn(4);
               break;
            case 2:
               var0.visitInsn(5);
               break;
            case 3:
               var0.visitInsn(6);
               break;
            case 4:
               var0.visitInsn(7);
               break;
            case 5:
               var0.visitInsn(8);
         }
      } else if (var1 <= 127) {
         var0.visitIntInsn(16, var1);
      } else if (var1 <= 32767) {
         var0.visitIntInsn(17, var1);
      } else {
         var0.visitLdcInsn(var1);
      }

   }

   public static int sizeOf(Class<?>... var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;
         Class[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class var5 = var2[var4];
            if (var5 != Long.TYPE && var5 != Double.TYPE) {
               ++var1;
            } else {
               var1 += 2;
            }
         }

         return var1;
      }
   }

   public static void addDefInit(ClassWriter var0, String var1) {
      MethodVisitor var2 = var0.visitMethod(1, "<init>", "()V", (String)null, (String[])null);
      var2.visitCode();
      var2.visitVarInsn(25, 0);
      var2.visitMethodInsn(183, var1, "<init>", "()V");
      var2.visitInsn(177);
      var2.visitMaxs(1, 1);
      var2.visitEnd();
   }

   public static void addGetter(ClassWriter var0, String var1, String var2, String var3, Class<?> var4) {
      String var5 = fieldDesc(var4);
      MethodVisitor var6 = var0.visitMethod(1, var2, "()" + var5, (String)null, (String[])null);
      var6.visitCode();
      var6.visitVarInsn(25, 0);
      var6.visitFieldInsn(180, var1, var3, var5);
      var6.visitInsn(returnOpcode(var4));
      var6.visitMaxs(sizeOf(var4), 1);
      var6.visitEnd();
   }

   public static void addSetter(ClassWriter var0, String var1, String var2, String var3, Class<?> var4) {
      String var5 = fieldDesc(var4);
      MethodVisitor var6 = var0.visitMethod(1, var2, "(" + var5 + ")V", (String)null, (String[])null);
      var6.visitCode();
      var6.visitVarInsn(25, 0);
      var6.visitVarInsn(loadOpcode(var4), 1);
      var6.visitFieldInsn(181, var1, var3, var5);
      var6.visitInsn(177);
      int var7 = 1 + sizeOf(var4);
      var6.visitMaxs(var7, var7);
      var6.visitEnd();
   }

   public static void addSerializationAssertMethods(ClassWriter var0, String var1, String var2) {
      MethodVisitor var3 = var0.visitMethod(2, "writeObject", "(Ljava/io/ObjectOutputStream;)V", (String)null, new String[]{"java/io/IOException"});
      var3.visitCode();
      var3.visitTypeInsn(187, var1);
      var3.visitInsn(89);
      var3.visitLdcInsn(var2);
      var3.visitMethodInsn(183, var1, "<init>", "(Ljava/lang/String;)V");
      var3.visitInsn(191);
      var3.visitMaxs(3, 2);
      var3.visitEnd();
      MethodVisitor var4 = var0.visitMethod(2, "readObject", "(Ljava/io/ObjectInputStream;)V", (String)null, new String[]{"java/io/IOException", "java/lang/ClassNotFoundException"});
      var4.visitCode();
      var4.visitTypeInsn(187, var1);
      var4.visitInsn(89);
      var4.visitLdcInsn(var2);
      var4.visitMethodInsn(183, var1, "<init>", "(Ljava/lang/String;)V");
      var4.visitInsn(191);
      var4.visitMaxs(3, 2);
      var4.visitEnd();
   }

   public static List<Class<?>> combineExs(Set<Class<?>> var0, Method var1) {
      ArrayList var2 = new ArrayList(var0);
      Class[] var3 = var1.getExceptionTypes();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var3[var5];
         if (!RemoteException.class.isAssignableFrom(var6) && !var0.contains(var6)) {
            var2.add(var6);
         }
      }

      return var2;
   }

   public static void addSerializationMethods(ClassWriter var0) {
      String var1 = binName(Serializer.class);
      MethodVisitor var2 = var0.visitMethod(2, "writeObject", "(Ljava/io/ObjectOutputStream;)V", (String)null, new String[]{"java/io/IOException", "java/lang/IllegalAccessException"});
      var2.visitCode();
      var2.visitVarInsn(25, 1);
      var2.visitVarInsn(25, 0);
      var2.visitMethodInsn(184, var1, "writeObject", "(Ljava/io/ObjectOutputStream;Ljava/lang/Object;)V");
      var2.visitInsn(177);
      var2.visitMaxs(2, 2);
      var2.visitEnd();
      MethodVisitor var3 = var0.visitMethod(2, "readObject", "(Ljava/io/ObjectInputStream;)V", (String)null, new String[]{"java/io/IOException", "java/lang/ClassNotFoundException", "java/lang/IllegalAccessException"});
      var3.visitCode();
      var3.visitVarInsn(25, 1);
      var3.visitVarInsn(25, 0);
      var3.visitMethodInsn(184, var1, "readObject", "(Ljava/io/ObjectInputStream;Ljava/lang/Object;)V");
      var3.visitInsn(177);
      var3.visitMaxs(2, 2);
      var3.visitEnd();
   }

   public static void addDistinctMDFields(ClassWriter var0, String var1, Collection<String> var2, boolean var3) {
      HashSet var4 = new HashSet(var2);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         addMDField(var0, var1 + var6, var3);
      }

   }

   public static void addMDField(ClassWriter var0, String var1, boolean var2) {
      int var3 = 1;
      if (var2) {
         var3 += 8;
      }

      var0.visitField(var3, var1, WL_MD_FIELD_DESCRIPTOR, (String)null, (Object)null).visitEnd();
   }

   public static void addEOMembers(ClassWriter var0, String var1, String var2, MethInfo... var3) {
      addMembers(var0, var1, var2, true, var3);
   }

   public static void addHomeMembers(ClassWriter var0, String var1, String var2, MethInfo... var3) {
      addMembers(var0, var1, var2, false, var3);
   }

   private static void addMembers(ClassWriter var0, String var1, String var2, boolean var3, MethInfo... var4) {
      if (var4 != null) {
         MethInfo[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            MethInfo var8 = var5[var7];
            addMDField(var0, var8.getMdName(), var3);
            StringBuilder var9 = new StringBuilder();
            if (var8.getArgs() != null) {
               Class[] var10 = var8.getArgs();
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  Class var13 = var10[var12];
                  var9.append(fieldDesc(var13));
               }
            }

            String var19 = fieldDesc(var8.getRetType());
            String var20 = "(" + var9 + ")" + var19;
            String[] var21 = exceptionsDesc(var8.getExs());
            MethodVisitor var22 = var0.visitMethod(1, var8.getMethodName(), var20, (String)null, var21);
            var22.visitCode();
            var22.visitVarInsn(25, 0);
            if (var3) {
               var22.visitFieldInsn(178, var1, var8.getMdName(), WL_MD_FIELD_DESCRIPTOR);
            } else {
               var22.visitVarInsn(25, 0);
               var22.visitFieldInsn(180, var1, var8.getMdName(), WL_MD_FIELD_DESCRIPTOR);
            }

            if (var8.getArgs() != null) {
               int var14 = 1;
               Class[] var15 = var8.getArgs();
               int var16 = var15.length;

               for(int var17 = 0; var17 < var16; ++var17) {
                  Class var18 = var15[var17];
                  var22.visitVarInsn(loadOpcode(var18), var14);
                  var14 += sizeOf(var18);
               }
            }

            var22.visitMethodInsn(183, var2, var8.getMethodName(), "(" + WL_MD_FIELD_DESCRIPTOR + var9 + ")" + var19);
            var22.visitInsn(returnOpcode(var8.getRetType()));
            var22.visitMaxs(0, 0);
            var22.visitEnd();
         }

      }
   }
}
