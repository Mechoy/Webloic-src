package weblogic.wsee.util;

import java.awt.Image;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.holders.BigDecimalHolder;
import javax.xml.rpc.holders.BigIntegerHolder;
import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.BooleanWrapperHolder;
import javax.xml.rpc.holders.ByteHolder;
import javax.xml.rpc.holders.ByteWrapperHolder;
import javax.xml.rpc.holders.CalendarHolder;
import javax.xml.rpc.holders.DoubleHolder;
import javax.xml.rpc.holders.DoubleWrapperHolder;
import javax.xml.rpc.holders.FloatHolder;
import javax.xml.rpc.holders.FloatWrapperHolder;
import javax.xml.rpc.holders.Holder;
import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.IntegerWrapperHolder;
import javax.xml.rpc.holders.LongHolder;
import javax.xml.rpc.holders.LongWrapperHolder;
import javax.xml.rpc.holders.ObjectHolder;
import javax.xml.rpc.holders.QNameHolder;
import javax.xml.rpc.holders.ShortHolder;
import javax.xml.rpc.holders.ShortWrapperHolder;
import javax.xml.rpc.holders.StringHolder;
import javax.xml.transform.Source;
import weblogic.wsee.holders.DataHandlerHolder;
import weblogic.wsee.holders.ImageHolder;
import weblogic.wsee.holders.MimeMultipartHolder;
import weblogic.wsee.holders.SourceHolder;

public class HolderUtil {
   private static HashMap stdHolderClass = loadMap();
   private static Set<String> primitiveTypes = primitiveTypes();
   private static Map<String, String> mimeBindingHolderMapping = mimeBinding();

   private static Set<String> primitiveTypes() {
      HashSet var0 = new HashSet();
      var0.add(Integer.TYPE.getName());
      var0.add(Float.TYPE.getName());
      var0.add(Short.TYPE.getName());
      var0.add(Double.TYPE.getName());
      var0.add(Long.TYPE.getName());
      var0.add(Byte.TYPE.getName());
      var0.add(Boolean.TYPE.getName());
      return var0;
   }

   private static Map<String, String> mimeBinding() {
      HashMap var0 = new HashMap(4);
      var0.put(Image.class.getName(), ImageHolder.class.getName());
      var0.put(MimeMultipart.class.getName(), MimeMultipartHolder.class.getName());
      var0.put(DataHandler.class.getName(), DataHandlerHolder.class.getName());
      var0.put(Source.class.getName(), SourceHolder.class.getName());
      return var0;
   }

   private static HashMap loadMap() {
      HashMap var0 = new HashMap();
      var0.put(String.class.getName(), StringHolder.class.getName());
      var0.put(Integer.TYPE.getName(), IntHolder.class.getName());
      var0.put(Float.TYPE.getName(), FloatHolder.class.getName());
      var0.put(Short.TYPE.getName(), ShortHolder.class.getName());
      var0.put(Double.TYPE.getName(), DoubleHolder.class.getName());
      var0.put(Long.TYPE.getName(), LongHolder.class.getName());
      var0.put(Byte.TYPE.getName(), ByteHolder.class.getName());
      var0.put(Boolean.TYPE.getName(), BooleanHolder.class.getName());
      var0.put(Integer.class.getName(), IntegerWrapperHolder.class.getName());
      var0.put(Float.class.getName(), FloatWrapperHolder.class.getName());
      var0.put(Short.class.getName(), ShortWrapperHolder.class.getName());
      var0.put(Double.class.getName(), DoubleWrapperHolder.class.getName());
      var0.put(Long.class.getName(), LongWrapperHolder.class.getName());
      var0.put(Byte.class.getName(), ByteWrapperHolder.class.getName());
      var0.put(Boolean.class.getName(), BooleanWrapperHolder.class.getName());
      var0.put(BigInteger.class.getName(), BigIntegerHolder.class.getName());
      var0.put(BigDecimal.class.getName(), BigDecimalHolder.class.getName());
      var0.put(QName.class.getName(), QNameHolder.class.getName());
      var0.put(Object.class.getName(), ObjectHolder.class.getName());
      var0.put(Calendar.class.getName(), CalendarHolder.class.getName());
      var0.put(MimeMultipart.class.getName(), MimeMultipartHolder.class.getName());
      var0.put(DataHandler.class.getName(), DataHandlerHolder.class.getName());
      var0.put(Image.class.getName(), ImageHolder.class.getName());
      var0.put(Source.class.getName(), SourceHolder.class.getName());
      return var0;
   }

   public static String getMimeBindingJavaHolderTypeFromMimeBindingJavaType(String var0) {
      var0 = (String)mimeBindingHolderMapping.get(var0);
      return var0 == null ? DataHandlerHolder.class.getName() : var0;
   }

   public static String getStandardHolder(String var0) {
      return (String)stdHolderClass.get(var0);
   }

   public static boolean isStandardHolderClass(String var0) {
      return var0 != null ? var0.trim().startsWith(Holder.class.getPackage().getName()) : false;
   }

   public static String getHolderClass(String var0) {
      String var1 = (String)stdHolderClass.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         int var2 = var0.lastIndexOf(".");
         if (var2 != -1) {
            String var3 = var0.substring(0, var2);
            if (var3.endsWith(".holders") && var0.endsWith("Holder")) {
               return var0;
            }

            if (var3.equals("oracle.sql")) {
               return var0;
            }
         }

         return getHolderPackage(var0) + "." + getShortHolderName(var0) + "Holder";
      }
   }

   public static String getHolderPackage(String var0) {
      int var1 = var0.lastIndexOf(".");
      String var2;
      if (var1 == -1) {
         if (isPrimitiveType(var0)) {
            var2 = "language_builtins.holders";
         } else {
            var2 = "holders";
         }
      } else {
         var2 = var0.substring(0, var1);
         if (var2.startsWith("java")) {
            var2 = "language_builtins.holders";
         } else {
            var2 = var2 + ".holders";
         }
      }

      return var2;
   }

   public static String getShortHolderName(String var0) {
      int var1 = var0.lastIndexOf(".");
      String var2;
      if (var1 == -1) {
         var2 = var0;
      } else {
         var2 = var0.substring(var1 + 1, var0.length());
      }

      return fixIfArrayName(var2);
   }

   public static boolean isPrimitiveType(String var0) {
      while(var0.endsWith("[]")) {
         var0 = var0.substring(0, var0.length() - 2);
      }

      return primitiveTypes.contains(var0);
   }

   public static String fixIfArrayName(String var0) {
      String var1 = var0.trim();

      int var2;
      for(var2 = 0; var1.endsWith("[]"); ++var2) {
         var1 = var1.substring(0, var1.length() - 2);
      }

      for(int var3 = var2; var3 > 0; --var3) {
         var1 = var1 + "Array";
      }

      return var1;
   }

   public static Class getRealType(Class var0) {
      if (!Holder.class.isAssignableFrom(var0)) {
         return var0;
      } else {
         try {
            Field var1 = var0.getField("value");
            return var1.getType();
         } catch (NoSuchFieldException var2) {
            throw new AssertionError("Not a holder class:" + var2);
         }
      }
   }

   public static void setHolderValue(Object var0, Object var1) {
      if (var0 == null) {
         throw new JAXRPCException("holder can not be null");
      } else {
         try {
            Field var2 = var0.getClass().getField("value");
            var2.set(var0, var1);
         } catch (NoSuchFieldException var3) {
            throw new JAXRPCException("unable to set value: " + var1 + " on the holder: " + var0, var3);
         } catch (IllegalAccessException var4) {
            throw new JAXRPCException("unable to set value: " + var1 + " on the holder: " + var0, var4);
         }
      }
   }

   public static Object getHolderValue(Object var0) {
      if (var0 == null) {
         return null;
      } else if (!(var0 instanceof Holder)) {
         return var0;
      } else {
         try {
            Field var1 = var0.getClass().getField("value");
            return var1.get(var0);
         } catch (NoSuchFieldException var2) {
            throw new JAXRPCException("unable to find field 'value' in the holder class " + var0.getClass(), var2);
         } catch (IllegalAccessException var3) {
            throw new JAXRPCException("unable to find field 'value' in the holder class " + var0.getClass(), var3);
         }
      }
   }

   public static class NameCollisionsFilter {
      private Map<String, List<String>> duplicateNameGroup = new HashMap();
      private Set<String> sorted = new HashSet();
      private static NameCollisionsFilter instance = null;

      private NameCollisionsFilter() {
      }

      public static NameCollisionsFilter getInstance() {
         if (instance == null) {
            instance = new NameCollisionsFilter();
         }

         return instance;
      }

      public void reset() {
         this.duplicateNameGroup.clear();
         this.sorted.clear();
      }

      public void use(String var1) {
         Object var2 = null;
         if (this.duplicateNameGroup.containsKey(var1.toLowerCase(Locale.ENGLISH))) {
            var2 = (List)this.duplicateNameGroup.get(var1.toLowerCase(Locale.ENGLISH));
         } else {
            var2 = new ArrayList();
            this.duplicateNameGroup.put(var1.toLowerCase(Locale.ENGLISH), var2);
         }

         if (var2 != null && !((List)var2).contains(var1)) {
            ((List)var2).add(var1);
         }

      }

      private int getSortedOrder(String var1) {
         if (this.duplicateNameGroup.containsKey(var1.toLowerCase(Locale.ENGLISH))) {
            List var2 = (List)this.duplicateNameGroup.get(var1.toLowerCase(Locale.ENGLISH));
            if (var2 != null) {
               if (var2.size() > 1) {
                  if (!this.sorted.contains(var1.toLowerCase(Locale.ENGLISH))) {
                     Collections.sort(var2);
                     this.sorted.add(var1.toLowerCase(Locale.ENGLISH));
                  }

                  return var2.indexOf(var1);
               }

               this.duplicateNameGroup.remove(var1.toLowerCase(Locale.ENGLISH));
            }
         }

         return -1;
      }

      public String filterFullName(String var1) {
         int var2 = this.getSortedOrder(var1);
         if (var2 > 0) {
            if (var1.endsWith("Holder")) {
               var1 = var1.substring(0, var1.length() - "Holder".length()) + "_" + var2 + "Holder";
            } else {
               var1 = var1 + "_" + var2;
            }
         }

         return var1;
      }

      public String filterClassName(String var1, String var2) {
         if (var1 != null && !"".equals(var1)) {
            String var3 = var1 + "." + var2;
            var3 = this.filterFullName(var3);
            return var3.substring(var1.length() + 1);
         } else {
            return this.filterFullName(var2);
         }
      }
   }
}
