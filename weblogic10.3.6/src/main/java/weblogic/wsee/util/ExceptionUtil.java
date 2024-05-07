package weblogic.wsee.util;

import com.bea.staxb.buildtime.ArrayNameHelper;
import com.bea.staxb.buildtime.FaultMessage;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JConstructor;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPException;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.component.ComponentException;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlPart;

public final class ExceptionUtil {
   private static final String XSNS = "http://www.w3.org/2001/XMLSchema";

   public static boolean isMarshalPropertyException(List var0, String var1) {
      if (var0 != null && var0.size() > 1) {
         return false;
      } else if (var1.contains("[")) {
         return true;
      } else {
         return nonColonizedSimpleBuiltinForJavaClassName(var1) != null;
      }
   }

   public static ExceptionInfo getExceptionInfo(BuildtimeBindings var0, WsdlMessage var1) {
      Iterator var2 = var1.getParts().values().iterator();
      if (!var2.hasNext()) {
         throw new IllegalStateException("ExceptionUtil.getExceptionInfo: Fault WsdlMessage " + var1.getName() + " has no message part.");
      } else {
         WsdlPart var3 = (WsdlPart)var2.next();
         String var4 = null;
         String var5 = null;
         String var6 = null;
         List var7 = null;
         FaultMessage var8 = new FaultMessage();
         var8.setMessageName(var1.getName());
         var8.setPartName(var3.getName());
         if (var3.getType() != null) {
            var8.setComponentName(var3.getType());
            var5 = var0.getWrappedSimpleClassNameFromFaultMessageType(var8);
            if (var5 == null) {
               var4 = var0.getExceptionClassFromFaultMessageType(var8);
            } else {
               var6 = var0.getExceptionClassFromFaultMessageType(var8);
               var4 = var5;
            }

            var7 = var0.getElementNamesCtorOrderFromFaultMessageType(var8);
         } else {
            var8.setComponentName(var3.getElement());
            var5 = var0.getWrappedSimpleClassNameFromFaultMessageElement(var8);
            if (var5 == null) {
               var4 = var0.getExceptionClassFromFaultMessageElement(var8);
            } else {
               var6 = var0.getExceptionClassFromFaultMessageElement(var8);
               var4 = var5;
            }

            var7 = var0.getElementNamesCtorOrderFromFaultMessageElement(var8);
         }

         if (var4 == null) {
            throw new AssertionError("Java exception mapping for wsdl message " + var1.getName() + " is not found.");
         } else {
            ExceptionInfo var9 = new ExceptionInfo(var6, var4, var7);
            return var9;
         }
      }
   }

   public static String getMessage(String var0, Throwable var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var0);

      while(var1 != null) {
         var2.append("\n  + ");
         var2.append(var1.getClass().getName());
         var2.append(": ");
         var2.append(var1.getMessage());
         var1 = var1.getCause();
      }

      return var2.toString();
   }

   public static Throwable unwrapException(Throwable var0, StringBuilder var1) {
      if (var0.getMessage() != null) {
         if (var1.length() > 0) {
            var1.append(" -> ");
         }

         var1.append(var0.getMessage());
         var1.append("\n");
      }

      if (var0 instanceof InvocationTargetException) {
         InvocationTargetException var7 = (InvocationTargetException)var0;
         return var7.getTargetException() != null ? unwrapException(var7.getTargetException(), var1) : var0;
      } else if (var0 instanceof InvocationException) {
         InvocationException var6 = (InvocationException)var0;
         return var6.getCause() != null ? unwrapException(var6.getCause(), var1) : var0;
      } else if (var0 instanceof JAXRPCException) {
         JAXRPCException var5 = (JAXRPCException)var0;
         return var5.getLinkedCause() != null ? unwrapException(var5.getLinkedCause(), var1) : var0;
      } else if (var0 instanceof SOAPException) {
         SOAPException var4 = (SOAPException)var0;
         return var4.getCause() != null ? unwrapException(var4.getCause(), var1) : var0;
      } else if (var0 instanceof RemoteException) {
         RemoteException var3 = (RemoteException)var0;
         return (Throwable)(var3.detail == null ? var3 : var3.detail);
      } else if (var0 instanceof ComponentException) {
         ComponentException var2 = (ComponentException)var0;
         return (Throwable)(var2.getCause() == null ? var2 : unwrapException(var2.getCause(), var1));
      } else {
         return var0;
      }
   }

   public static Object getPropertyValue(Object var0, Class var1) {
      Method[] var2 = var0.getClass().getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().startsWith("get") && var2[var3].getReturnType().equals(var1) && var2[var3].getParameterTypes().length == 0) {
            try {
               return var2[var3].invoke(var0);
            } catch (IllegalAccessException var5) {
               return null;
            } catch (InvocationTargetException var6) {
               return null;
            }
         }
      }

      return null;
   }

   public static boolean hasSingleArgConstructorOnly(Class var0) {
      return getConstructorForSingleArgClass(var0) != null;
   }

   public static Constructor getConstructorForSingleArgClass(Class var0) {
      Constructor[] var1 = var0.getConstructors();
      Constructor var2 = null;
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         Class[] var5 = var1[var4].getParameterTypes();
         if (var5.length > 1) {
            return null;
         }

         if (var5.length == 1) {
            ++var3;
            if (var3 > 1) {
               return null;
            }

            var2 = var1[var4];
         }
      }

      return var2;
   }

   public static boolean isMarshalPropertyJavaClass(Class var0) {
      if (var0.isArray()) {
         return true;
      } else {
         return nonColonizedSimpleBuiltinForJavaClass(var0) != null;
      }
   }

   public static String nonColonizedSimpleBuiltinForJavaClass(Class var0) {
      String var1 = null;
      if (var0.isPrimitive()) {
         if (var0 == Integer.TYPE) {
            var1 = "int";
         } else if (var0 == Long.TYPE) {
            var1 = "long";
         } else if (var0 == Short.TYPE) {
            var1 = "short";
         } else if (var0 == Float.TYPE) {
            var1 = "float";
         } else if (var0 == Double.TYPE) {
            var1 = "double";
         } else if (var0 == Boolean.TYPE) {
            var1 = "boolean";
         } else if (var0 == Byte.TYPE) {
            var1 = "byte";
         }
      } else {
         var1 = var0.getName();
      }

      return nonColonizedSimpleBuiltinForJavaClassName(var1);
   }

   private static String nonColonizedSimpleBuiltinForJavaClassName(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.equals("int")) {
         return "int";
      } else if (var0.equals("long")) {
         return "long";
      } else if (var0.equals("short")) {
         return "short";
      } else if (var0.equals("float")) {
         return "float";
      } else if (var0.equals("double")) {
         return "double";
      } else if (var0.equals("boolean")) {
         return "boolean";
      } else if (var0.equals("byte")) {
         return "byte";
      } else if (var0.equals("java.lang.String")) {
         return "string";
      } else if (var0.equals("java.lang.Integer")) {
         return "int";
      } else if (var0.equals("java.lang.Long")) {
         return "long";
      } else if (var0.equals("java.lang.Short")) {
         return "short";
      } else if (var0.equals("java.lang.Float")) {
         return "float";
      } else if (var0.equals("java.lang.Double")) {
         return "double";
      } else if (var0.equals("java.lang.Boolean")) {
         return "boolean";
      } else if (var0.equals("java.lang.Byte")) {
         return "byte";
      } else if (var0.equals("java.math.BigInteger")) {
         return "integer";
      } else if (var0.equals("java.math.BigDecimal")) {
         return "decimal";
      } else if (var0.equals("java.util.Calendar")) {
         return "datetime";
      } else if (var0.equals("java.util.GregorianCalendar")) {
         return "datetime";
      } else if (var0.equals("java.util.Date")) {
         return "datetime";
      } else if (var0.equals("javax.xml.namespace.QName")) {
         return "qname";
      } else {
         return var0.equals("java.net.URI") ? "anyURI" : null;
      }
   }

   public static boolean classNameIsSchemaBuiltin(String var0) {
      return nonColonizedSimpleBuiltinForJavaClassName(var0) != null;
   }

   public static boolean isMarshalPropertyException(JClass var0) {
      if (!isUserException(var0)) {
         return false;
      } else {
         JClass var1 = getMarshalPropertyJClass(var0);
         return var1 != null;
      }
   }

   public static JClass getMarshalPropertyJClass(JClass var0) {
      JClass var1 = getMarshalPropertyType(var0);
      if (var1 == null) {
         return null;
      } else {
         return getPropertyName(var0, var1) != null ? var1 : null;
      }
   }

   private static String getPropertyName(JClass var0, JClass var1) {
      JMethod[] var2 = var0.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3].getSimpleName();
         if (var4.startsWith("get") && var4.length() > 3 && var2[var3].getReturnType().getQualifiedName().equals(var1.getQualifiedName()) && var2[var3].getParameters().length == 0) {
            return var4.substring(3);
         }
      }

      return null;
   }

   private static JClass getMarshalPropertyType(JClass var0) {
      JClass var1 = var0.getClassLoader().loadClass("java.lang.Exception");
      if (!var1.isAssignableFrom(var0)) {
         throw new IllegalArgumentException(var0.getQualifiedName() + " is not an exception.");
      } else {
         JConstructor[] var2 = var0.getConstructors();
         HashSet var3 = new HashSet();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            JParameter[] var5 = var2[var4].getParameters();
            if (var5.length != 0) {
               if (var5.length > 1) {
                  return null;
               }

               var3.add(var5[0]);
            }
         }

         Iterator var7 = var3.iterator();

         JClass var6;
         do {
            if (!var7.hasNext()) {
               return null;
            }

            JParameter var8 = (JParameter)var7.next();
            var6 = var8.getType();
            if (jClassIsBuiltinSchema(var6)) {
               return var6;
            }
         } while(!var6.isArrayType());

         return var6;
      }
   }

   public static boolean isUserException(JClass var0) {
      JClass var1 = var0.getClassLoader().loadClass("java.lang.RuntimeException");
      JClass var2 = var0.getClassLoader().loadClass("java.rmi.RemoteException");
      if (var1.isAssignableFrom(var0)) {
         return false;
      } else if (var2.isAssignableFrom(var0)) {
         return false;
      } else if ("java.lang.Exception".equals(var0.getQualifiedName())) {
         return false;
      } else {
         return !var0.getQualifiedName().startsWith("java.sql");
      }
   }

   public static boolean jClassIsBuiltinSchema(JClass var0) {
      return nonColonizedSimpleBuiltinForJClass(var0) != null;
   }

   public static String nonColonizedSimpleBuiltinForJClass(JClass var0) {
      String var1 = null;
      if (var0.isPrimitiveType()) {
         Class var2 = var0.getPrimitiveClass();
         return nonColonizedSimpleBuiltinForJavaClass(var2);
      } else {
         var1 = var0.getQualifiedName();
         return nonColonizedSimpleBuiltinForJavaClassName(var1);
      }
   }

   public static QName getDefaultExceptionElement(JClass var0) {
      return getDefaultExceptionElement(var0, (String)null);
   }

   public static QName getDefaultExceptionElement(JClass var0, String var1) {
      String var2 = var0.getQualifiedName();
      String var3 = var0.getSimpleName();
      String var4 = null;
      if (var1 != null) {
         var4 = var1;
      } else {
         String var5 = var2.substring(0, var2.lastIndexOf("."));
         var4 = "java:" + var5;
      }

      return new QName(var4, var3);
   }

   public static QName exceptionMarshalPropertyElementName(JClass var0, JClass var1) {
      JClass var2 = getMarshalPropertyType(var1);
      if (var2.isArrayType()) {
         return litArrayBindingTypeQName(var0, var2);
      } else {
         String var3 = nonColonizedSimpleBuiltinForJClass(var2);
         if (var3 == null) {
            return null;
         } else {
            String var4 = var1.getQualifiedName();
            String var5 = var4.substring(0, var4.lastIndexOf("."));
            return new QName(var5, var3);
         }
      }
   }

   private static QName litArrayBindingTypeQName(JClass var0, JClass var1) {
      if (var1.isArrayType()) {
         int var2 = var1.getArrayDimensions();
         JClass var3 = var1.getArrayComponentType();
         String var4;
         if (jClassIsBuiltinSchema(var3)) {
            var4 = nonColonizedSimpleBuiltinForJClass(var3);
            return ArrayNameHelper.getLiteralArrayTypeName(ClassUtil.getTargetNamespace(var0), var0, var1, var4);
         } else {
            var4 = var3.getSimpleName();
            return ArrayNameHelper.getLiteralArrayTypeName(ClassUtil.getTargetNamespace(var0), var0, var1, var4);
         }
      } else {
         throw new IllegalArgumentException(" ExceptionUtil.litArrayBindingTypeQName called with non-array class argument '" + var1 + "'");
      }
   }
}
