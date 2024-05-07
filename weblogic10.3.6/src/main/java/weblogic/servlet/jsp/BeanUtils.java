package weblogic.servlet.jsp;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class BeanUtils {
   static void p(String var0) {
      System.out.println(var0);
   }

   public static boolean canBeInstantiated(Class var0) {
      int var1 = var0.getModifiers();
      if ((var1 & 1024) == 1024) {
         return false;
      } else if ((var1 & 1) != 1) {
         return false;
      } else {
         Class[] var2 = new Class[0];
         Constructor var3 = null;

         try {
            var3 = var0.getConstructor(var2);
         } catch (NoSuchMethodException var5) {
            return false;
         } catch (SecurityException var6) {
            return false;
         }

         var1 = var3.getModifiers();
         return (var1 & 1) == 1;
      }
   }

   public static String doReflection(String var0, String var1, Properties var2) throws IOException {
      try {
         return doReflection0(var0, var1, var2);
      } catch (RuntimeException var4) {
         throw var4;
      } catch (IOException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new IOException("reflection failure: " + var6.toString());
      }
   }

   public static boolean isStringConvertible(Class var0) {
      return var0 == String.class || var0 == Boolean.class || var0 == Boolean.TYPE || var0 == Byte.class || var0 == Byte.TYPE || var0 == Double.class || var0 == Double.TYPE || var0 == Integer.class || var0 == Integer.TYPE || var0 == Float.class || var0 == Float.TYPE || var0 == Long.class || var0 == Long.TYPE || var0 == Character.class || var0 == Character.TYPE || var0 == Object.class || var0 == Short.class || var0 == Short.TYPE;
   }

   public static String complexConversion(Class var0, String var1) {
      PropertyEditor var2 = PropertyEditorManager.findEditor(var0);
      if (var2 == null) {
         return null;
      } else {
         var2.setAsText(var1);
         return var2.getJavaInitializationString();
      }
   }

   public static String complexConversion0(Class var0, String var1) {
      PropertyEditor var2 = PropertyEditorManager.findEditor(var0);
      return var2 == null ? null : var2.getClass().getName();
   }

   public static void doComplex(Object var0, String var1, String var2, ServletRequest var3, String var4) throws javax.servlet.jsp.JspException {
      if (var4 == null) {
         var4 = var1;
      }

      BeanInfo var5 = null;

      try {
         var5 = Introspector.getBeanInfo(var0.getClass());
      } catch (Exception var14) {
         throw new JspException("Exception occured while getting the beanInfo for bean '" + var0.getClass() + "'");
      }

      PropertyDescriptor[] var6 = var5.getPropertyDescriptors();
      PropertyDescriptor var7 = null;

      int var8;
      for(var8 = 0; var8 < var6.length; ++var8) {
         if (var1.equals(var6[var8].getName())) {
            var7 = var6[var8];
            break;
         }
      }

      if (var7 == null) {
         for(var8 = 0; var8 < var6.length; ++var8) {
            if (var1.equalsIgnoreCase(var6[var8].getName())) {
               var7 = var6[var8];
               break;
            }
         }
      }

      if (var7 != null) {
         try {
            Class var17 = var7.getPropertyEditorClass();
            Class var9 = var7.getPropertyType();
            PropertyEditor var11;
            if (var17 == null) {
               Class var10 = var9.isArray() ? var9.getComponentType() : var9;
               var11 = PropertyEditorManager.findEditor(var10);
               if (var11 != null) {
                  var17 = var11.getClass();
               }
            }

            Method var18 = var7.getWriteMethod();
            var11 = null;
            if (var9.isArray()) {
               if (var3 == null) {
                  throw new JspException("request is needed for the type '" + var9.getName() + "'");
               }

               String[] var20 = ((HttpServletRequest)var3).getParameterValues(var4);
               if (var20 == null || var20.length == 0) {
                  return;
               }

               Object[] var19 = (Object[])((Object[])Array.newInstance(var9.getComponentType(), var20.length));

               for(int var13 = 0; var13 < var20.length; ++var13) {
                  var19[var13] = getValueFromEditor(var17, var7, var20[var13]);
               }

               if (var18 != null) {
                  var18.invoke(var0, (Object)var19);
               }
            } else {
               if (var2 == null && var3 != null) {
                  var2 = ((HttpServletRequest)var3).getParameter(var4);
               }

               if (var2 == null) {
                  return;
               }

               Object var12 = getValueFromEditor(var17, var7, var2);
               if (var18 != null) {
                  var18.invoke(var0, var12);
               }
            }

         } catch (RuntimeException var15) {
            throw new javax.servlet.jsp.JspException(var15.toString(), var15);
         } catch (Exception var16) {
            throw new javax.servlet.jsp.JspException("Exception occured while conversion :" + var16.toString());
         }
      } else {
         throw new javax.servlet.jsp.JspException("Found no PropertyDescriptor for property '" + var1 + "' of bean class '" + var0.getClass().getName() + "'");
      }
   }

   public static Object getValueFromEditor(Class var0, PropertyDescriptor var1, String var2) throws JspException {
      try {
         PropertyEditor var3 = (PropertyEditor)var0.newInstance();
         var3.setAsText(var2);
         return var3.getValue();
      } catch (Exception var4) {
         throw new JspException("unable to convert '" + var2 + "' to type '" + var1.getPropertyType().getName() + "'. " + "reason: " + var4.toString());
      }
   }

   public static String convert(Class var0, String var1) {
      if (var0 == String.class) {
         return "weblogic.utils.StringUtils.valueOf(" + var1 + ")";
      } else if (var0 == Boolean.class) {
         return "Boolean.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + "))";
      } else if (var0 == Boolean.TYPE) {
         return "Boolean.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + ")).booleanValue()";
      } else if (var0 == Byte.class) {
         return "Byte.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + "))";
      } else if (var0 == Byte.TYPE) {
         return "Byte.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + ")).byteValue()";
      } else if (var0 == Double.class) {
         return "Double.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + "))";
      } else if (var0 == Double.TYPE) {
         return "Double.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + ")).doubleValue()";
      } else if (var0 == Integer.class) {
         return "Integer.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + "))";
      } else if (var0 == Integer.TYPE) {
         return "Integer.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + ")).intValue()";
      } else if (var0 == Float.class) {
         return "Float.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + "))";
      } else if (var0 == Float.TYPE) {
         return "Float.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + ")).floatValue()";
      } else if (var0 == Long.class) {
         return "Long.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + "))";
      } else if (var0 == Long.TYPE) {
         return "Long.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + ")).longValue()";
      } else if (var0 == Character.class) {
         return "new Character(" + var1 + ".toString().length() > 0 ? " + var1 + ".toString().charAt(0): ' ')";
      } else if (var0 == Character.TYPE) {
         return var1 + ".toString().length() > 0 ? " + var1 + ".toString().charAt(0): ' '";
      } else if (var0 == Object.class) {
         return "weblogic.utils.StringUtils.valueOf(" + var1 + ")";
      } else if (var0 == Short.TYPE) {
         return "Short.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + ")).shortValue()";
      } else {
         return var0 == Short.class ? "Short.valueOf(weblogic.utils.StringUtils.valueOf(" + var1 + "))" : null;
      }
   }

   public static String convertArray(Class var0, String var1) {
      if (var0 == String.class) {
         return "String[] _propertyArray = new String[" + var1 + "];";
      } else if (var0 == Boolean.class) {
         return "Boolean[] _propertyArray = new Boolean[" + var1 + "];";
      } else if (var0 == Boolean.TYPE) {
         return "boolean[] _propertyArray = new boolean[" + var1 + "];";
      } else if (var0 == Byte.class) {
         return "Byte[] _propertyArray = new Byte[" + var1 + "];";
      } else if (var0 == Byte.TYPE) {
         return "byte[] _propertyArray = new byte[" + var1 + "];";
      } else if (var0 == Double.class) {
         return "Double[] _propertyArray = new Double[" + var1 + "];";
      } else if (var0 == Double.TYPE) {
         return "double[] _propertyArray = new double[" + var1 + "];";
      } else if (var0 == Integer.class) {
         return "Integer[] _propertyArray = new Integer[" + var1 + "];";
      } else if (var0 == Integer.TYPE) {
         return "int[] _propertyArray = new int[" + var1 + "];";
      } else if (var0 == Float.class) {
         return "Float[] _propertyArray = new Float[" + var1 + "];";
      } else if (var0 == Float.TYPE) {
         return "float[] _propertyArray = new float[" + var1 + "];";
      } else if (var0 == Long.class) {
         return "Long[] _propertyArray = new Long[" + var1 + "];";
      } else if (var0 == Long.TYPE) {
         return "long[] _propertyArray = new long[" + var1 + "];";
      } else if (var0 == Character.class) {
         return "Character[] _propertyArray = new Character[" + var1 + "];";
      } else if (var0 == Character.TYPE) {
         return "char[] _propertyArray = new char[" + var1 + "];";
      } else if (var0 == Object.class) {
         return "Object[] _propertyArray = new Object[" + var1 + "];";
      } else if (var0 == Short.TYPE) {
         return "short[] _propertyArray = new short[" + var1 + "];";
      } else {
         return var0 == Short.class ? "Short[] _propertyArray = new Short[" + var1 + "];" : null;
      }
   }

   public static String doReflection0(String var0, String var1, Properties var2) throws Exception {
      Vector var3 = new Vector();
      Class var4 = Class.forName(var1);
      var3.addElement(var4);
      recurseClasses(var4, var3);
      Hashtable var5 = new Hashtable();
      getMethods(var3, var5);
      StringBuffer var6 = new StringBuffer();
      Enumeration var7 = var2.keys();
      int var8 = 0;

      while(true) {
         String var10;
         MethodEntry var11;
         Class var12;
         do {
            do {
               String var9;
               do {
                  do {
                     if (!var7.hasMoreElements()) {
                        if (var8 == 0) {
                           return null;
                        }

                        return var6.toString();
                     }

                     var9 = (String)var7.nextElement();
                     var10 = var2.getProperty(var9);
                  } while(var10 == null);
               } while(var10.length() == 0);

               var9 = var9.toLowerCase();
               var11 = (MethodEntry)var5.get(var9);
            } while(var11 == null);

            var12 = var11.paramTypes[0];
         } while(!var12.isPrimitive() && "java.lang.String".getClass() != var12);

         String var13 = var10;

         try {
            if ("java.lang.String".getClass() == var12) {
               var13 = '"' + var10 + '"';
            } else if (var12 == Byte.TYPE) {
               Byte.decode(var10);
            } else if (var12 == Short.TYPE) {
               Short.parseShort(var10);
            } else if (var12 == Integer.TYPE) {
               Integer.parseInt(var10);
            } else if (var12 == Long.TYPE) {
               Long.parseLong(var10);
            } else if (var12 == Float.TYPE) {
               Float.valueOf(var10);
            } else if (var12 == Double.TYPE) {
               Double.valueOf(var10);
            } else if (var12 == Boolean.TYPE) {
               if (!"true".equalsIgnoreCase(var13) && !"yes".equalsIgnoreCase(var13) && !"1".equalsIgnoreCase(var13) && !"on".equalsIgnoreCase(var13)) {
                  if (!"false".equalsIgnoreCase(var13) && !"no".equalsIgnoreCase(var13) && !"0".equalsIgnoreCase(var13) && !"off".equalsIgnoreCase(var13) && !"null".equalsIgnoreCase(var13)) {
                     throw new IllegalArgumentException("cannot convert String '" + var13 + "' into a boolean");
                  }

                  var13 = "false";
               } else {
                  var13 = "true";
               }
            } else if (var12 == Character.TYPE) {
               var13 = '\'' + var10 + '\'';
            }
         } catch (NumberFormatException var15) {
            throw new IllegalArgumentException("cannot convert String '" + var10 + "' into a " + var12.getName());
         }

         var6.append(var0);
         var6.append('.');
         var6.append(var11.m.getName());
         var6.append('(');
         var6.append(var13);
         var6.append(')');
         var6.append(';');
         var6.append('\n');
         ++var8;
      }
   }

   public static boolean implementsInterface(Class var0, String var1) throws Exception {
      Vector var2 = new Vector();
      var2.addElement(var0);
      recurseClasses(var0, var2);

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         var0 = (Class)var2.elementAt(var3);
         Class[] var4 = var0.getInterfaces();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (var1.equals(var4[var5].getName())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private static void getMethods(Vector var0, Hashtable var1) throws Exception {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         Class var3 = (Class)var0.elementAt(var2);
         Method[] var4 = var3.getDeclaredMethods();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               Method var6 = var4[var5];
               String var7 = var6.getName();
               if (!Modifier.isStatic(var6.getModifiers()) && var7.startsWith("set") && var7.length() > 3) {
                  Class[] var8 = var6.getParameterTypes();
                  if (var8 != null && var8.length == 1) {
                     String var9 = var7.substring(3).toLowerCase();
                     MethodEntry var10 = new MethodEntry();
                     var10.m = var6;
                     var10.paramTypes = var8;
                     var10.paramPart = var9;
                     var1.put(var9, var10);
                  }
               }
            }
         }
      }

   }

   private static void recurseClasses(Class var0, Vector var1) throws Exception {
      Class var2 = var0.getSuperclass();
      if (!var2.getName().equals("java.lang.Object")) {
         var1.addElement(var2);
         recurseClasses(var2, var1);
      }
   }
}
