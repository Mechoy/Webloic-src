package weblogic.corba.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import javax.rmi.CORBA.ClassDesc;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandler;
import org.omg.CORBA.portable.InputStream;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.SendingContext.CodeBaseHelper;
import weblogic.corba.cos.naming.NamingContextAnyHelper;
import weblogic.corba.rmic.IDLMangler;
import weblogic.iiop.IIOPService;
import weblogic.iiop.Utils;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.io.ObjectStreamClass;
import weblogic.utils.io.ObjectStreamField;

public final class RepositoryId extends MarshaledString {
   private String annotation;
   private static final String[] ILLEGAL_CHARS = new String[]{"\\U0024", "__"};
   private static final String[] LEGAL_CHARS = new String[]{"$", "$"};
   public static final RepositoryId CLASS_DESC = new RepositoryId(ClassDesc.class);
   private static final String STRING_ID = "IDL:omg.org/CORBA/WStringValue:1.0";
   public static final RepositoryId STRING = new RepositoryId("IDL:omg.org/CORBA/WStringValue:1.0");
   public static final RepositoryId NULL = new RepositoryId("IDL:omg.org/CORBA/AbstractBase:1.0");
   public static final RepositoryId EMPTY = new RepositoryId("");
   public static final RepositoryId OBJECT = new RepositoryId("IDL:omg.org/CORBA/Object:1.0");
   public static final RepositoryId NAMING = new RepositoryId(NamingContextHelper.id());
   public static final RepositoryId NAMING_EXT = new RepositoryId("IDL:omg.org/CosNaming/NamingContextExt:1.0");
   public static final RepositoryId NAMING_ANY = new RepositoryId(NamingContextAnyHelper.id());
   public static final RepositoryId CODEBASE = new RepositoryId(CodeBaseHelper.id());
   private static final boolean ASSERT = false;
   public static final RepositoryId INT_ID = new RepositoryId("RMI:int:0000000000000000");
   public static final RepositoryId BYTE_ID = new RepositoryId("RMI:byte:0000000000000000");
   public static final RepositoryId LONG_ID = new RepositoryId("RMI:long:0000000000000000");
   public static final RepositoryId FLOAT_ID = new RepositoryId("RMI:float:0000000000000000");
   public static final RepositoryId DOUBLE_ID = new RepositoryId("RMI:double:0000000000000000");
   public static final RepositoryId SHORT_ID = new RepositoryId("RMI:short:0000000000000000");
   public static final RepositoryId CHAR_ID = new RepositoryId("RMI:char:0000000000000000");
   public static final RepositoryId BOOLEAN_ID = new RepositoryId("RMI:boolean:0000000000000000");
   public static final HashMap PRIMITIVE_MAP = new HashMap(31);
   private static ConcurrentHashMap classNameMap;
   public static final RepositoryId OLD_EJB_EXCEPTION;
   public static final RepositoryId EJB_EXCEPTION;

   public RepositoryId(InputStream var1, int var2) {
      super(var1, var2);
      this.initHash();
   }

   public RepositoryId(InputStream var1) {
      super(var1);
      this.initHash();
   }

   public RepositoryId(String var1) {
      super(var1);
      this.initHash();
   }

   RepositoryId(RepositoryId var1, String var2) {
      super((MarshaledString)var1);
      this.annotation = var2;
      this.hash = var1.hash;
   }

   RepositoryId(Class var1) {
      super(createRMIRepositoryID(var1));
      this.setClassLoader(var1.getClassLoader());
      this.initHash();
   }

   void setClassLoader(ClassLoader var1) {
      if (this.annotation == null) {
         this.annotation = Utils.getAnnotation(var1);
      }

   }

   public boolean isIDLType() {
      return this.encodedString.length > 3 && this.encodedString[0] == 73 && this.encodedString[1] == 68 && this.encodedString[2] == 76;
   }

   public static final RepositoryId createFromRemote(Class var0) {
      return new RepositoryId(getIDFromRemote(var0));
   }

   public static final RepositoryId createFromValueType(Class var0) {
      return new RepositoryId(createRMIRepositoryID(var0));
   }

   private static final String getIDFromRemote(Class var0) {
      String var1 = null;

      for(Class var2 = var0; Utilities.isARemote(var2); var2 = var2.getSuperclass()) {
         Class[] var3 = var2.getInterfaces();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            Class var5 = var3[var4];
            if (Utilities.isARemote(var5)) {
               if (Utils.isIDLInterface(var5)) {
                  var1 = getIDFromIDLEntity(var5);
               } else {
                  var1 = createRMIRepositoryID(var5);
               }

               return var1;
            }
         }
      }

      return null;
   }

   public static final RepositoryId[] getRepositoryIdList(Class var0) {
      if (var0 != null && var0.getSuperclass() != null && !var0.getSuperclass().isInterface() && Serializable.class.isAssignableFrom(var0.getSuperclass())) {
         ArrayList var1;
         for(var1 = new ArrayList(); var0 != null && !var0.isInterface() && Serializable.class.isAssignableFrom(var0); var0 = var0.getSuperclass()) {
            var1.add(new RepositoryId(var0));
         }

         return (RepositoryId[])((RepositoryId[])var1.toArray(new RepositoryId[0]));
      } else {
         return null;
      }
   }

   private static final String getIDFromIDLEntity(Class var0) {
      String var1 = var0.getName();
      String var2;
      if (var1.startsWith("org.omg.")) {
         var2 = "IDL:omg.org/" + var1.substring("org.omg.".length()).replace('.', '/') + ":1.0";
      } else {
         var2 = "IDL:" + var0.getName().replace('.', '/') + ":1.0";
      }

      return var2;
   }

   private static final String unconvertIllegalCharacters(String var0) {
      for(int var1 = 0; var1 < ILLEGAL_CHARS.length; ++var1) {
         int var2 = var0.indexOf(ILLEGAL_CHARS[var1]);
         int var3 = 0;
         if (var2 >= 0) {
            StringBuffer var4;
            for(var4 = new StringBuffer(); var2 >= 0; var2 = var0.indexOf(ILLEGAL_CHARS[var1], var3)) {
               var4.append(var0.substring(var3, var2)).append(LEGAL_CHARS[var1]);
               var3 = var2 + ILLEGAL_CHARS[var1].length();
            }

            var0 = var4.append(var0.substring(var3, var0.length() - 1)).toString();
         }
      }

      return var0;
   }

   public String getClassName() {
      String var1 = (String)classNameMap.get(this);
      if (var1 == null) {
         String var2 = this.toString();
         int var3;
         if (var2.startsWith("IDL:omg.org/")) {
            var2 = "org.omg." + var2.substring("IDL:omg.org/".length());
            var3 = var2.indexOf(58);
            if (var3 <= 0) {
               return null;
            }

            var1 = var2.substring(0, var3).replace('/', '.');
         } else {
            var2 = unconvertIllegalCharacters(var2);
            var3 = var2.indexOf(58);
            int var4 = var2.indexOf(58, var3 + 1);
            if (var3 <= 0 || var4 <= 0) {
               return null;
            }

            var1 = var2.substring(var3 + 1, var4).replace('/', '.');
         }

         if (var1 != null) {
            classNameMap.put(this, var1);
         }
      }

      return var1;
   }

   private void initHash() {
      int var1 = 0;
      int var2 = this.encodedString.length;
      if (var2 > 21 && this.encodedString[0] == 82) {
         var2 -= 17;
      }

      for(int var3 = 4; var3 < var2; ++var3) {
         var1 = 31 * var1 + this.encodedString[var3];
      }

      this.hash = var1;
   }

   public final int hashCode() {
      return this.hash;
   }

   public final boolean equals(Object var1) {
      if (!(var1 instanceof RepositoryId)) {
         return false;
      } else {
         RepositoryId var2 = (RepositoryId)var1;
         return this.compareStrings(var2) && (var2.annotation == this.annotation || this.annotation != null && var2.annotation != null && this.annotation.equals(var2.annotation));
      }
   }

   private static ValueHandler getValueHandler() {
      IIOPService.load();
      return Util.createValueHandler();
   }

   public static String createRMIRepositoryID(Class var0) {
      if (IDLMangler.isIDLEntity(var0)) {
         return Utils.createIDFromIDLEntity(var0);
      } else {
         String var1 = null;
         if (var0 == String.class) {
            var1 = "IDL:omg.org/CORBA/WStringValue:1.0";
         } else if (var0 == Class.class) {
            var1 = createRMIRepositoryID(ClassDesc.class);
         } else {
            StringBuffer var2 = new StringBuffer("RMI:");
            var2.append(convertIllegalCharacters(var0.getName()));
            var2.append(":");
            Class var3 = var0;
            if (var0.isArray()) {
               while(var3.getComponentType() != null) {
                  var3 = var3.getComponentType();
               }
            }

            ObjectStreamClass var4 = ObjectStreamClass.lookup(var3);
            if (var4 != null && !var3.isInterface() && (!var4.isArray() || !var4.forClass().getComponentType().isPrimitive())) {
               var2.append(toHexString(computeHashCode(var4, var3)));
               var2.append(":");
               var2.append(toHexString(var4.getObjectStreamClass().getSerialVersionUID()));
               var1 = var2.toString();
            } else {
               var2.append(toHexString(0L));
               var1 = var2.toString();
            }
         }

         return var1;
      }
   }

   public static String toHexString(long var0) {
      StringBuffer var2 = new StringBuffer();
      String var3 = Long.toHexString(var0).toUpperCase();
      int var4 = 16 - var3.length();

      while(var4-- > 0) {
         var2.append('0');
      }

      var2.append(var3);
      return var2.toString();
   }

   private static String convertIllegalCharacters(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         switch (var3) {
            case '$':
               var1.append("\\U0024");
               break;
            case '\\':
               if ('u' != var0.charAt(var2 + 1)) {
                  var1.append(var3);
                  break;
               }

               var1.append("U");

               for(int var4 = var2 + 2; var4 < var2 + 5; ++var4) {
                  var1.append(Character.toUpperCase(var0.charAt(var4)));
               }

               var2 += 4;
               break;
            default:
               var1.append(var3);
         }
      }

      return var1.toString();
   }

   public static long computeHashCode(ObjectStreamClass var0, Class var1) {
      try {
         if (Serializable.class.isAssignableFrom(var1) && !var1.isInterface()) {
            if (Externalizable.class.isAssignableFrom(var1)) {
               return 1L;
            } else {
               ByteArrayOutputStream var2 = new ByteArrayOutputStream(512);
               MessageDigest var3 = MessageDigest.getInstance("SHA");
               DigestOutputStream var4 = new DigestOutputStream(var2, var3);
               DataOutputStream var5 = new DataOutputStream(var4);
               Class var6 = var1.getSuperclass();
               if (var6 != null) {
                  var5.writeLong(computeHashCode(var0.getSuperclass(), var6));
               }

               if (var0.hasWriteObject()) {
                  var5.writeInt(2);
               } else {
                  var5.writeInt(1);
               }

               ObjectStreamField[] var7 = (ObjectStreamField[])((ObjectStreamField[])var0.getFields().clone());
               Arrays.sort(var7, new Comparator() {
                  public int compare(Object var1, Object var2) {
                     return ((ObjectStreamField)var1).getName().compareTo(((ObjectStreamField)var2).getName());
                  }
               });

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var5.writeUTF(var7[var8].getName());
                  var5.writeUTF(var7[var8].getSignature());
               }

               var5.flush();
               long var14 = 0L;
               byte[] var10 = var3.digest();

               for(int var11 = 0; var11 < Math.min(8, var10.length); ++var11) {
                  var14 += (long)(var10[var11] & 255) << var11 * 8;
               }

               return var14;
            }
         } else {
            return 0L;
         }
      } catch (IOException var12) {
         return -1L;
      } catch (NoSuchAlgorithmException var13) {
         return -1L;
      }
   }

   public static void main(String[] var0) throws Exception {
      Class var1 = Class.forName(var0[0]);
      System.out.println(Util.createValueHandler().getRMIRepositoryID(var1));
      System.out.println(createRMIRepositoryID(var1));
   }

   private static void addToMap(RepositoryId var0, String var1) {
      classNameMap.put(var0, var1);
   }

   private static final void initialize() {
      addToMap(NAMING, "org.omg.CosNaming.NamingContext");
      addToMap(STRING, "java.lang.String");
      addToMap(NAMING_ANY, "weblogic.corba.cos.naming.NamingContextAny");
      Class var0 = null;

      try {
         var0 = Class.forName("com.sun.org.omg.SendingContext.CodeBase");
         addToMap(CODEBASE, "com.sun.org.omg.SendingContext.CodeBase");
      } catch (ClassNotFoundException var4) {
         try {
            var0 = Class.forName("com.ibm.org.omg.SendingContext.CodeBase");
            addToMap(CODEBASE, "com.ibm.org.omg.SendingContext.CodeBase");
         } catch (ClassNotFoundException var3) {
         }
      }

   }

   public boolean isClassDesc() {
      return this.compareStrings(CLASS_DESC);
   }

   public String toPrettyString() {
      return this.toString() + "@" + this.getAnnotation();
   }

   public String getAnnotation() {
      return this.annotation;
   }

   public void setAnnotation(String var1) {
      this.annotation = var1;
   }

   static {
      PRIMITIVE_MAP.put(INT_ID, Integer.TYPE);
      PRIMITIVE_MAP.put(BYTE_ID, Byte.TYPE);
      PRIMITIVE_MAP.put(LONG_ID, Long.TYPE);
      PRIMITIVE_MAP.put(FLOAT_ID, Float.TYPE);
      PRIMITIVE_MAP.put(DOUBLE_ID, Double.TYPE);
      PRIMITIVE_MAP.put(SHORT_ID, Short.TYPE);
      PRIMITIVE_MAP.put(CHAR_ID, Character.TYPE);
      PRIMITIVE_MAP.put(BOOLEAN_ID, Boolean.TYPE);
      classNameMap = new ConcurrentHashMap();
      OLD_EJB_EXCEPTION = new RepositoryId("RMI:javax.ejb.EJBException:0E3E8C42D0E83868:800C4C7C598DF61F");
      EJB_EXCEPTION = new RepositoryId("RMI:javax.ejb.EJBException:0E3E8C42D0E83868:0B0EB2FF36CB22F6");
      initialize();
   }
}
