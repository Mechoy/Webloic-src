package weblogic.corba.rmic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import weblogic.iiop.IDLUtils;
import weblogic.utils.Debug;
import weblogic.utils.compiler.CodeGenerationException;

public abstract class IDLType {
   private static boolean debug = false;
   private static boolean verbose = false;
   protected static Hashtable m_usedTypes = new Hashtable();
   private static final boolean DEBUG = false;
   protected Class m_class;
   protected Class m_enclosingClass;
   Hashtable m_methods;
   Hashtable m_attributes;
   Hashtable m_extraLines;
   String m_directory;
   private static TypeOverride typeOverrides = new TypeOverride();

   public IDLType(Class var1, Class var2) {
      this(var1, var2, (String)null);
   }

   public IDLType(Class var1, Class var2, String var3) {
      this.m_methods = new Hashtable();
      this.m_attributes = new Hashtable();
      this.m_extraLines = new Hashtable();
      Debug.assertion(null != var1);
      this.m_class = var1;
      this.m_enclosingClass = var2;
      this.m_directory = var3;
      this.init();
   }

   public String getDirectory() {
      return this.m_directory;
   }

   public String getFileName() {
      String var1 = IDLUtils.javaTypeToPath(this.m_directory, this.m_class);
      return var1;
   }

   public static Hashtable getUsedTypes() {
      return m_usedTypes;
   }

   public static void resetUsedTypes() {
      m_usedTypes = new Hashtable();
   }

   static void getAll(Class var0, Hashtable var1, boolean var2) {
      getAll(var0, var1, var2, true);
   }

   static void getAll(Class var0, Hashtable var1, boolean var2, boolean var3) {
      Class[] var4 = IDLUtils.getClasses(var0, var2, var3);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         Class var6 = var4[var5];
         if (!IDLOptions.getNoAbstract() || !IDLUtils.isAbstractInterface(var6)) {
            IDLType var7 = createType(var6, var0);
            var1.put(var7.getClassName(), var7);
         }
      }

   }

   public static IDLType createType(Class var0, Class var1) {
      Object var2 = null;
      if (null != IDLTypeSpecial.TRAITS.getValidClass(var0, var1)) {
         var2 = new IDLTypeSpecial(var0, var1);
      } else if (null != IDLTypeEntity.TRAITS.getValidClass(var0, var1)) {
         var2 = new IDLTypeEntity(var0, var1);
      } else if (null != IDLTypeRemote.TRAITS.getValidClass(var0, var1)) {
         var2 = new IDLTypeRemote(var0, var1);
      } else if (null != IDLTypeEx.TRAITS.getValidClass(var0, var1)) {
         IDLTypeValueType var3 = new IDLTypeValueType(var0, var1);
         var2 = new IDLTypeEx(var0, var1, var3);
         m_usedTypes.put(var3.getFileName(), var3);
      } else if (null != IDLTypeValueType.TRAITS.getValidClass(var0, var1)) {
         var2 = new IDLTypeValueType(var0, var1);
      } else if (null != IDLTypeSequence.TRAITS.getValidClass(var0, var1)) {
         var2 = new IDLTypeSequence(var0, var1);
      } else if (null != IDLTypeNonConformant.TRAITS.getValidClass(var0, var1)) {
         var2 = new IDLTypeNonConformant(var0, var1);
      } else if (null != IDLTypeAbstractInterface.TRAITS.getValidClass(var0, var1)) {
         var2 = new IDLTypeAbstractInterface(var0, var1);
      }

      m_usedTypes.put(((IDLType)var2).getFileName(), var2);
      return (IDLType)var2;
   }

   public static void registerOverride(String var0, String var1) {
      typeOverrides.registerMethodOverride(var0, var1);
   }

   public static void clearOverrides() {
      typeOverrides = new TypeOverride();
   }

   public boolean isRequired() throws CodeGenerationException {
      if (!IDLOptions.getNoValueTypes() && !IDLOptions.getNoAbstract() && typeOverrides.isEmpty()) {
         return true;
      } else if (IDLOptions.getNoValueTypes() && typeOverrides.isEmpty()) {
         return !IDLUtils.mustSkipClass(this.getClass(), this.getOpeningDeclaration());
      } else if (typeOverrides.isClassRequired(this.m_class)) {
         return true;
      } else {
         return !IDLUtils.mustSkipClass(this.getClass(), this.getOpeningDeclaration());
      }
   }

   public boolean isAssignableFrom(IDLType var1) throws CodeGenerationException {
      return this.getJavaClass().isAssignableFrom(var1.getJavaClass());
   }

   public void init() {
      this.initMethods();
      this.initAttributes();
   }

   public void initAttributes() {
      this.m_attributes = new Hashtable();

      try {
         if (!(this instanceof IDLTypeValueType)) {
            IDLUtils.getAttributesFromMethods(this.m_class, this.m_attributes);
         }

         IDLUtils.getAttributesFromFields(this.m_class, this.m_attributes);
      } catch (CodeGenerationException var5) {
         if (debug) {
            var5.printStackTrace();
         }
      }

      if (IDLOptions.getNoValueTypes() || IDLOptions.getNoAbstract()) {
         Enumeration var1 = this.m_attributes.elements();

         try {
            while(true) {
               IDLField var2;
               do {
                  if (!var1.hasMoreElements()) {
                     return;
                  }

                  var2 = (IDLField)var1.nextElement();
               } while(!IDLUtils.mustSkipClass(var2.getType()) && null == IDLUtils.getNonConformantType(var2.getType()));

               if (!typeOverrides.isClassRequired(var2.getType())) {
                  Object var3 = this.m_attributes.remove(var2.getMangledName());
                  Debug.assertion(null != var3);
               }
            }
         } catch (Exception var4) {
            if (debug) {
               var4.printStackTrace();
            }
         }
      }

   }

   public void initMethods() {
      this.m_methods = new Hashtable();
      Method[] var1 = this.m_class.getDeclaredMethods();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         try {
            Method var3 = var1[var2];
            boolean var4 = true;
            if (IDLOptions.getNoValueTypes() && this.methodContainsValueTypes(var3) && !typeOverrides.isMethodRequired(var3)) {
               var4 = false;
            }

            boolean var5 = IDLUtils.isValid(var3);
            if (var4 && var5) {
               boolean var6 = IDLUtils.isMethodForAnAttribute(this.m_class, var3);
               if (!var6) {
                  IDLMethod var7 = new IDLMethod(this.m_class, var3.getName(), IDLUtils.getMangledMethodName(var3, var1), var3.getReturnType(), var3.getParameterTypes(), var3.getExceptionTypes());
                  this.m_methods.put(var7.getMangledName(), var7);
               }
            }
         } catch (CodeGenerationException var8) {
            if (debug) {
               var8.printStackTrace();
            }
         }
      }

   }

   private static void p(String var0) {
      System.out.println("<IDLType>: " + var0);
   }

   boolean methodContainsValueTypes(Method var1) {
      Class var2 = var1.getReturnType();
      if (IDLUtils.mustSkipClass(var2)) {
         return true;
      } else {
         Class[] var3 = var1.getParameterTypes();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (IDLUtils.mustSkipClass(var3[var4])) {
               return true;
            }
         }

         return false;
      }
   }

   public Hashtable getMethods() {
      return this.m_methods;
   }

   public Hashtable getAttributes() {
      return this.m_attributes;
   }

   public Hashtable getExtraLines() {
      return this.m_extraLines;
   }

   public Class getJavaClass() {
      return this.m_class;
   }

   public Class getEnclosingClass() {
      return this.m_enclosingClass;
   }

   public Class[] getInheritedClasses() {
      Class[] var1 = this.getJavaClass().getInterfaces();
      Vector var2 = new Vector();
      Class var3 = this.getJavaClass().getSuperclass();
      if (var3 != null && IDLUtils.isValid(var3) && !var3.equals(Object.class)) {
         var2.addElement(var3);
      }

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (IDLUtils.isValid(var1[var4])) {
            var2.addElement(var1[var4]);
         }
      }

      Class[] var5 = new Class[var2.size()];
      var2.copyInto(var5);
      return var5;
   }

   public boolean canHaveSubtype(IDLType var1) {
      return true;
   }

   public String getPackageName() {
      return null;
   }

   public String getClassName() {
      return this.m_class.getName();
   }

   public String beforeMainDeclaration() {
      return "";
   }

   public String afterMainDeclaration() {
      return "";
   }

   public Hashtable getReferencedTypes() {
      Hashtable var1 = new Hashtable();
      new Hashtable();
      Class[] var3 = IDLUtils.getClasses(this.m_class, false, true);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Class var5 = var3[var4];
         IDLType var6 = createType(var5, this.getEnclosingClass());
         var1.put(var5, var6);
      }

      return var1;
   }

   public abstract String getIncludeDeclaration() throws CodeGenerationException;

   public abstract String getForwardDeclaration() throws CodeGenerationException;

   public abstract void getReferences(Hashtable var1);

   public String getInheritKeyword(IDLType var1) {
      return ":";
   }

   public String getOpeningDeclaration() throws CodeGenerationException {
      return "";
   }

   public final String generateMethods() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      Hashtable var2 = this.getMethods();
      Enumeration var3 = var2.elements();

      while(var3.hasMoreElements()) {
         IDLMethod var4 = (IDLMethod)var3.nextElement();
         if (var4.isRequired()) {
            if (!this.checkInheritedMethod(this.getJavaClass(), this.getJavaClass(), var4)) {
               var1.append("    " + var4.toIDL() + "\n");
            } else if (!IDLOptions.getIDLStrict()) {
               var1.append("//  clash with parent on " + var4.toIDL()).append("\n//");
            }
         }
      }

      return var1.toString();
   }

   boolean checkInheritedMethod(Class var1, Class var2, IDLMethod var3) {
      if (var2 != null && !var2.equals(Object.class)) {
         if (var2 != var1) {
            try {
               var2.getMethod(var3.getName(), var3.getParameterTypes());
               return true;
            } catch (NoSuchMethodException var7) {
            }
         }

         Class var4 = var2.getSuperclass();
         if (this.checkInheritedMethod(var1, var4, var3)) {
            return true;
         } else {
            Class[] var5 = var2.getInterfaces();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (this.checkInheritedMethod(var1, var5[var6], var3)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public final String generateAttributes() throws CodeGenerationException {
      StringBuffer var1 = new StringBuffer();
      LinkedList var2 = new LinkedList(this.getAttributes().values());
      Collections.sort(var2, new Comparator() {
         public int compare(Object var1, Object var2) {
            IDLField var3 = (IDLField)var1;
            IDLField var4 = (IDLField)var2;
            if (var3 instanceof IDLAttribute && !(var4 instanceof IDLAttribute)) {
               return 1;
            } else if (!(var3 instanceof IDLAttribute) && var4 instanceof IDLAttribute) {
               return -1;
            } else if (var3.isConst() && !var4.isConst()) {
               return -1;
            } else if (var4.isConst() && !var3.isConst()) {
               return 1;
            } else if (var3.isPrimitive() && !var4.isPrimitive()) {
               return -1;
            } else {
               return !var3.isPrimitive() && var4.isPrimitive() ? 1 : var3.getName().compareTo(var4.getName());
            }
         }
      });
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         IDLField var4 = (IDLField)var3.next();
         var1.append("    " + var4.toIDL() + "\n");
      }

      return var1.toString();
   }

   public final String generateExtraLines() {
      StringBuffer var1 = new StringBuffer();
      Hashtable var2 = this.getExtraLines();
      Enumeration var3 = var2.elements();

      while(var3.hasMoreElements()) {
         IDLExtraLine var4 = (IDLExtraLine)var3.nextElement();
         var1.append("    " + var4.toIDL() + "\n");
      }

      return var1.toString();
   }

   public static String getOpeningDecl(IDLType var0, Class var1, Class[] var2, String var3, TypeTraits var4) throws CodeGenerationException {
      StringBuffer var5 = new StringBuffer("  " + var3);
      String var6 = IDLUtils.stripPackage(IDLUtils.getIDLType(var0.getJavaClass()), "::");
      var5.append(" " + var6);
      Vector var7 = new Vector();
      Vector var8 = new Vector();

      for(int var9 = 0; var9 < var2.length; ++var9) {
         Class var10 = var2[var9];
         if (!IDLOptions.getNoAbstract() || !IDLUtils.isAbstractInterface(var10)) {
            IDLType var11 = createType(var10, var1);
            if (var0 instanceof IDLTypeValueType && var11 instanceof IDLTypeEx) {
               var11 = ((IDLTypeEx)var11).getEnclosed();
            }

            String var12 = var11.getInheritKeyword(var0);
            if (":".equals(var12)) {
               var7.addElement(var11);
            } else {
               if (!"supports".equals(var12)) {
                  throw new CodeGenerationException("Unknown inheritance keyword for " + var11.toString());
               }

               var8.addElement(var11);
            }
         }
      }

      Collections.sort(var7, new Comparator() {
         public int compare(Object var1, Object var2) {
            IDLType var3 = (IDLType)var1;
            IDLType var4 = (IDLType)var2;
            if (var3 instanceof IDLTypeValueType && !(var4 instanceof IDLTypeValueType)) {
               return -1;
            } else if (var3 instanceof IDLTypeRemote && !(var4 instanceof IDLTypeRemote)) {
               return -1;
            } else if (!(var3 instanceof IDLTypeValueType) && var4 instanceof IDLTypeValueType) {
               return 1;
            } else {
               return !(var3 instanceof IDLTypeRemote) && var4 instanceof IDLTypeRemote ? 1 : 0;
            }
         }
      });
      Vector[] var16 = new Vector[]{var7, var8};

      for(int var17 = 0; var17 < var16.length; ++var17) {
         Vector var18 = var16[var17];
         boolean var19 = true;
         Enumeration var13 = var18.elements();

         while(var13.hasMoreElements()) {
            IDLType var14 = (IDLType)var13.nextElement();
            Class var15 = var14.getJavaClass();
            if (var14.canHaveSubtype(var0)) {
               if (var19) {
                  var19 = false;
                  var5.append(" ").append(var14.getInheritKeyword(var0));
               } else {
                  var5.append(", ");
               }

               var5.append(" ").append(IDLUtils.getIDLType(var15));
            }
         }
      }

      var5.append(" ");
      return var5.toString();
   }

   public String getPragmaID() {
      return IDLUtils.getPragmaID(this.getJavaClass());
   }

   public String getOpenBrace() {
      return "{\n";
   }

   public String getCloseBrace() {
      return "\n};\n";
   }

   public String getGuardName(String var1) {
      return IDLUtils.generateGuard(this.m_class, var1);
   }

   public String toString() {
      return this.getClass().getName() + "(" + this.m_class.getName() + ")";
   }

   static class TypeOverride {
      ArrayList typeOverrides = new ArrayList();
      HashMap methodOverrides = new HashMap();
      Class remoteInterface = null;

      void registerMethodOverride(String var1, String var2) {
         try {
            this.methodOverrides.put(var1, StructureTokenizer.getParameterTypes(var2));
         } catch (ClassNotFoundException var4) {
         }

      }

      boolean isClassRequired(Class var1) {
         return true;
      }

      boolean isMethodRequired(Method var1) {
         Object var2 = this.methodOverrides.get(var1.getName());
         if (var2 != null) {
            Class[] var3 = (Class[])((Class[])var2);
            Class[] var4 = var1.getParameterTypes();
            if (var3.length == var4.length) {
               int var5 = 0;

               while(true) {
                  if (var5 >= var3.length) {
                     return true;
                  }

                  if (!var3[var5].equals(var4[var5])) {
                     break;
                  }

                  ++var5;
               }
            }
         }

         return false;
      }

      boolean isEmpty() {
         return this.methodOverrides.size() == 0;
      }
   }
}
