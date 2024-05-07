package weblogic.corba.cos.codebase;

import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.omg.CORBA.AttributeDescription;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.Initializer;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.Object;
import org.omg.CORBA.OperationDescription;
import org.omg.CORBA.Repository;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.ValueDefPackage.FullValueDescription;
import org.omg.SendingContext._CodeBaseImplBase;
import weblogic.corba.idl.TypeCodeImpl;
import weblogic.corba.utils.ClassInfo;
import weblogic.corba.utils.RepositoryId;
import weblogic.corba.utils.ValueHandlerImpl;
import weblogic.iiop.IDLUtils;
import weblogic.iiop.IOR;
import weblogic.iiop.ObjectKey;
import weblogic.iiop.Utils;
import weblogic.rmi.internal.InitialReferenceConstants;
import weblogic.rmi.utils.io.Codebase;
import weblogic.utils.collections.ConcurrentHashMap;

public final class CodeBaseImpl extends _CodeBaseImplBase implements InitialReferenceConstants {
   private static final long serialVersionUID = 7568888264800197561L;
   public static final String TYPE_ID = "IDL:omg.org/SendingContext/CodeBase:1.0";
   private static final boolean DEBUG = false;
   private static final CodeBaseImpl codebase = new CodeBaseImpl();
   private static final IOR ior = new IOR("IDL:omg.org/SendingContext/CodeBase:1.0", new ObjectKey("IDL:omg.org/SendingContext/CodeBase:1.0", 12));
   private static final short PRIVATE_ACCESS = 0;
   private static final short PUBLIC_ACCESS = 1;
   private static ConcurrentHashMap metaMap = new ConcurrentHashMap();

   private CodeBaseImpl() {
   }

   public static final CodeBaseImpl getCodeBase() {
      return codebase;
   }

   public IOR getIOR() {
      return ior;
   }

   public Repository get_ir() {
      return null;
   }

   public String implementation(String var1) {
      return Codebase.getDefaultCodebase();
   }

   public String implementationx(String var1) {
      return Codebase.getDefaultCodebase();
   }

   public String[] implementations(String[] var1) {
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.implementation(var1[var3]);
      }

      return var2;
   }

   public FullValueDescription meta(String var1) {
      FullValueDescription var2 = null;
      if ((var2 = (FullValueDescription)metaMap.get(var1)) == null) {
         ClassInfo var3 = ClassInfo.findClassInfo(new RepositoryId(var1));
         if (var3.forClass() == null) {
            throw new BAD_PARAM("Could not find FVD class for: " + var1);
         }

         var2 = createFVD(var3);
         metaMap.put(var1, var2);
         this.addParentClassDescriptors(var3.forClass());
      }

      return var2;
   }

   private void addParentClassDescriptors(Class var1) {
      if (var1 != null && var1.getSuperclass() != null && !var1.getSuperclass().isInterface() && Serializable.class.isAssignableFrom(var1.getSuperclass())) {
         while(var1 != null && !var1.isInterface() && Serializable.class.isAssignableFrom(var1)) {
            ClassInfo.findClassInfo(var1);
            var1 = var1.getSuperclass();
         }

      }
   }

   public FullValueDescription[] metas(String[] var1) {
      FullValueDescription[] var2 = new FullValueDescription[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.meta(var1[var3]);
      }

      return var2;
   }

   public String[] bases(String var1) {
      throw new NO_IMPLEMENT();
   }

   private static final TypeCode getTypeCode(Class var0) {
      if (var0 == Byte.TYPE) {
         return TypeCodeImpl.get_primitive_tc(10);
      } else if (var0 == Character.TYPE) {
         return TypeCodeImpl.get_primitive_tc(26);
      } else if (var0 == Float.TYPE) {
         return TypeCodeImpl.get_primitive_tc(6);
      } else if (var0 == Double.TYPE) {
         return TypeCodeImpl.get_primitive_tc(7);
      } else if (var0 == Integer.TYPE) {
         return TypeCodeImpl.get_primitive_tc(3);
      } else if (var0 == Long.TYPE) {
         return TypeCodeImpl.get_primitive_tc(23);
      } else if (var0 == Short.TYPE) {
         return TypeCodeImpl.get_primitive_tc(2);
      } else if (var0 == Boolean.TYPE) {
         return TypeCodeImpl.get_primitive_tc(8);
      } else if (var0.isArray()) {
         Class var1 = var0.getComponentType();
         TypeCode var2 = getTypeCode(var1);
         return new TypeCodeImpl(30, new RepositoryId(ValueHandlerImpl.getRepositoryID(var0)), "Sequence", TypeCodeImpl.create_sequence_tc(0, var2));
      } else if (var0 == String.class) {
         return new TypeCodeImpl(30, RepositoryId.STRING, "WStringValue", TypeCodeImpl.create_wstring_tc(0));
      } else {
         return (TypeCode)(!IDLUtils.isARemote(var0) && !Object.class.isAssignableFrom(var0) ? new TypeCodeImpl(30, new RepositoryId(ValueHandlerImpl.getRepositoryID(var0)), "ValueBox", TypeCodeImpl.get_primitive_tc(29)) : TypeCodeImpl.get_primitive_tc(14));
      }
   }

   private static FullValueDescription createFVD(ClassInfo var0) {
      Class var1 = var0.forClass();
      ObjectStreamClass var2 = var0.getDescriptor().getObjectStreamClass();
      FullValueDescription var3 = new FullValueDescription();
      var3.name = var1.getName();
      int var4 = var3.name.lastIndexOf(46);
      if (var4 >= 0) {
         var3.name = var3.name.substring(var4 + 1);
      }

      var3.id = var0.getRepositoryId().toString();
      var3.is_abstract = var0.isAbstractInterface();
      var3.is_custom = var0.getDescriptor().hasWriteObject();
      if (var4 >= 0) {
         var3.defined_in = "IDL:" + var1.getName().substring(0, var4).replace('.', '/') + ":1.0";
      } else {
         var3.defined_in = "IDL::1.0";
      }

      var3.version = RepositoryId.toHexString(var2.getSerialVersionUID());
      var3.operations = new OperationDescription[0];
      var3.attributes = new AttributeDescription[0];
      var3.initializers = new Initializer[0];
      ObjectStreamField[] var5 = var2.getFields();
      var3.members = new ValueMember[var5.length];

      for(int var6 = 0; var6 < var5.length; ++var6) {
         ValueMember var7 = new ValueMember();
         ObjectStreamField var8 = var5[var6];
         Class var9 = var8.getType();
         String var10 = var9.getName();
         Field var11 = null;

         try {
            var11 = var1.getField(var8.getName());
         } catch (SecurityException var13) {
         } catch (NoSuchFieldException var14) {
         }

         var7.name = var8.getName();
         var7.id = ValueHandlerImpl.getRepositoryID(var9);
         if ((var4 = var10.lastIndexOf(46)) >= 0) {
            var7.defined_in = "IDL:" + var10.substring(0, var4).replace('.', '/') + ":1.0";
         } else {
            var7.defined_in = "IDL::1.0";
         }

         var7.version = "1.0";
         var7.type_def = null;
         if (var11 != null && Modifier.isPublic(var11.getModifiers())) {
            var7.access = 1;
         } else {
            var7.access = 0;
         }

         var7.type = getTypeCode(var9);
         var3.members[var6] = var7;
      }

      Class[] var15 = var1.getInterfaces();
      int var16 = 0;
      var3.supported_interfaces = new String[var15.length];

      int var17;
      for(var17 = 0; var17 < var15.length; ++var17) {
         var3.supported_interfaces[var17] = ValueHandlerImpl.getRepositoryID(var15[var17]);
         if (Utils.isAbstractInterface(var15[var17])) {
            ++var16;
         }
      }

      var3.abstract_base_values = new String[var16];
      var17 = var15.length;

      while(true) {
         --var17;
         if (var17 < 0) {
            Class var18 = var1.getSuperclass();
            if (IDLUtils.isValueType(var18)) {
               var3.base_value = ValueHandlerImpl.getRepositoryID(var18);
               var3.is_truncatable = true;
            } else {
               var3.base_value = "";
            }

            var3.type = TypeCodeImpl.get_primitive_tc(29);
            return var3;
         }

         if (Utils.isAbstractInterface(var15[var17])) {
            --var16;
            var3.abstract_base_values[var16] = ValueHandlerImpl.getRepositoryID(var15[var17]);
         }
      }
   }

   public static final void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.out.println("weblogic.corba.cos.codebase.CodeBaseImpl <classname>");
      }

      for(int var1 = 0; var1 < var0.length; ++var1) {
         Class var2 = Class.forName(var0[var1]);
         FullValueDescription var3 = createFVD(ClassInfo.findClassInfo(var2));
         System.out.println("FullValueDescription for " + var2.getName() + ": ");
         System.out.println(" name:\t" + var3.name);
         System.out.println(" id:\t" + var3.id);
         System.out.println(" abstract:\t" + var3.is_abstract);
         System.out.println(" custom:\t" + var3.is_custom);
         System.out.println(" defined in:\t" + var3.defined_in);
         System.out.println(" version:\t" + var3.version);
         System.out.println(" base:\t" + var3.base_value);
         System.out.println(" interfaces:\t");

         int var4;
         for(var4 = 0; var4 < var3.supported_interfaces.length; ++var4) {
            System.out.println("   " + var3.supported_interfaces[var4]);
         }

         System.out.println(" abstract bases: ");

         for(var4 = 0; var4 < var3.abstract_base_values.length; ++var4) {
            System.out.println("   " + var3.abstract_base_values[var4]);
         }

         System.out.println(" fields: ");

         for(var4 = 0; var4 < var3.members.length; ++var4) {
            System.out.println("   name:\t" + var3.members[var4].name);
            System.out.println("   id:\t" + var3.members[var4].id);
            System.out.println("   access:\t" + var3.members[var4].access);
            System.out.println("   type:\t" + var3.members[var4].type);
         }
      }

   }

   protected static void p(String var0) {
      System.err.println("<CodeBaseImpl> " + var0);
   }
}
