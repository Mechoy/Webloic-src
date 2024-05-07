package weblogic.corba.idl;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UnionMember;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import weblogic.corba.utils.RepositoryId;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;

public final class TypeCodeImpl extends TypeCode {
   public static final int VM_NONE = 0;
   public static final int VM_CUSTOM = 1;
   public static final int VM_ABSTRACT = 2;
   public static final int VM_TRUNCATABLE = 3;
   private static final boolean DEBUG = false;
   private TCKind type;
   private RepositoryId repid;
   private String name;
   private TypeCode content_type;
   private MemberInfo[] members;
   private int length;
   private short mod;
   private short digits;
   private short scale;
   public static final TypeCodeImpl NULL_TC = new TypeCodeImpl(0);
   public static final TypeCodeImpl NULL;
   public static final TypeCodeImpl OCTET;
   public static final TypeCodeImpl STRING;
   public static final TypeCodeImpl OBJECT;
   public static final TypeCodeImpl VALUE;
   private static final TypeCodeImpl[] simpleTypes;

   public TypeCodeImpl(int var1, RepositoryId var2, String var3, TypeCode var4) {
      this.length = 0;
      this.mod = 0;
      this.digits = -1;
      this.scale = -1;
      this.repid = var2;
      this.name = var3;
      this.type = TCKind.from_int(var1);
      this.content_type = var4;
   }

   public TypeCodeImpl(int var1, RepositoryId var2, String var3) {
      this(var1, var2, var3, (TypeCode)null);
   }

   public TypeCodeImpl(int var1) {
      this.length = 0;
      this.mod = 0;
      this.digits = -1;
      this.scale = -1;
      this.repid = null;
      this.name = null;
      this.type = TCKind.from_int(var1);
   }

   public boolean equal(TypeCode var1) {
      if (!(var1 instanceof TypeCodeImpl)) {
         return false;
      } else {
         TypeCodeImpl var2 = (TypeCodeImpl)var1;
         return var2.type.value() == this.type.value() && var2.length == this.length && var2.mod == this.mod && var2.digits == this.digits && var2.scale == this.scale && safe_equals(var2.repid, this.repid) && safe_equals(var2.name, this.name) && safe_equals(var2.content_type, this.content_type) && this.members_equal(var2);
      }
   }

   public boolean equals(Object var1) {
      return var1 instanceof TypeCode ? this.equal((TypeCode)var1) : false;
   }

   private static final boolean safe_equals(Object var0, Object var1) {
      return var0 == null && var1 == null || var0 != null && var0.equals(var1);
   }

   private final boolean members_equal(TypeCodeImpl var1) {
      if (var1.members == null && this.members == null) {
         return true;
      } else if (this.members != null && var1.members != null && this.members.length == var1.members.length) {
         for(int var2 = 0; var2 < this.members.length; ++var2) {
            if (!safe_equals(this.members[var2].name, var1.members[var2].name) || !safe_equals(this.members[var2].type, var1.members[var2].type) || !safe_equals(this.members[var2].label, var1.members[var2].label) || this.members[var2].access != var1.members[var2].access) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean equivalent(TypeCode var1) {
      return this.equal(var1);
   }

   public TypeCode get_compact_typecode() {
      return simpleTypes[this.type.value()];
   }

   public TCKind kind() {
      return this.type;
   }

   public String id() throws BadKind {
      return this.repid == null ? "" : this.repid.toString();
   }

   public final RepositoryId getRepositoryId() {
      return this.repid;
   }

   public static final RepositoryId getRepositoryId(TypeCode var0) {
      if (var0 instanceof TypeCodeImpl) {
         return ((TypeCodeImpl)var0).getRepositoryId();
      } else {
         try {
            return new RepositoryId(var0.id());
         } catch (BadKind var2) {
            return null;
         }
      }
   }

   public String name() throws BadKind {
      return this.name;
   }

   public int member_count() throws BadKind {
      return this.members == null ? 0 : this.members.length;
   }

   public String member_name(int var1) throws BadKind, Bounds {
      if (this.members == null) {
         throw new BadKind();
      } else if (var1 >= this.members.length) {
         throw new Bounds();
      } else {
         return this.members[var1].name;
      }
   }

   public TypeCode member_type(int var1) throws BadKind, Bounds {
      if (this.members == null) {
         throw new BadKind();
      } else if (var1 >= this.members.length) {
         throw new Bounds();
      } else {
         return this.members[var1].type;
      }
   }

   public Any member_label(int var1) throws BadKind, Bounds {
      if (this.members == null) {
         throw new BadKind();
      } else if (var1 >= this.members.length) {
         throw new Bounds();
      } else {
         return this.members[var1].label;
      }
   }

   public TypeCode discriminator_type() throws BadKind {
      if (this.content_type == null) {
         throw new BadKind();
      } else {
         return this.content_type;
      }
   }

   public int default_index() throws BadKind {
      if (this.type.value() != 16) {
         throw new BadKind();
      } else {
         return this.length;
      }
   }

   public int length() throws BadKind {
      if (this.length < 0) {
         throw new BadKind();
      } else {
         return this.length;
      }
   }

   public TypeCode content_type() throws BadKind {
      if (this.content_type == null) {
         throw new BadKind();
      } else {
         return this.content_type;
      }
   }

   public short fixed_digits() throws BadKind {
      if (this.digits == -1) {
         throw new BadKind();
      } else {
         return this.digits;
      }
   }

   public short fixed_scale() throws BadKind {
      if (this.scale == -1) {
         throw new BadKind();
      } else {
         return this.scale;
      }
   }

   public short member_visibility(int var1) throws BadKind, Bounds {
      if (this.members == null) {
         throw new BadKind();
      } else if (var1 >= this.members.length) {
         throw new Bounds();
      } else {
         return this.members[var1].access;
      }
   }

   public short type_modifier() throws BadKind {
      return this.mod;
   }

   public TypeCode concrete_base_type() throws BadKind {
      if (this.content_type == null) {
         throw new BadKind();
      } else {
         return this.content_type;
      }
   }

   public void read(IIOPInputStream var1) {
      long var2;
      int var4;
      int var5;
      switch (this.type.value()) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 23:
         case 24:
         case 25:
         case 26:
            break;
         case 14:
         case 32:
            var2 = var1.startEncapsulation();
            this.repid = var1.read_repository_id();
            this.name = var1.read_string();
            var1.endEncapsulation(var2);
            break;
         case 15:
         case 22:
            var2 = var1.startEncapsulation();
            this.repid = var1.read_repository_id();
            this.name = var1.read_string();
            var4 = var1.read_ulong();
            this.members = new MemberInfo[var4];

            for(var5 = 0; var5 < var4; ++var5) {
               this.members[var5] = new MemberInfo();
               this.members[var5].name = var1.read_string();
               this.members[var5].type = var1.read_TypeCode();
            }

            var1.endEncapsulation(var2);
            break;
         case 16:
            var2 = var1.startEncapsulation();
            this.repid = var1.read_repository_id();
            this.name = var1.read_string();
            this.content_type = var1.read_TypeCode();
            this.length = var1.read_long();
            var4 = var1.read_ulong();
            this.members = new MemberInfo[var4];

            for(var5 = 0; var5 < var4; ++var5) {
               this.members[var5] = new MemberInfo();
               if (var5 == this.length) {
                  this.members[var5].label = var1.read_any(OCTET);
               } else {
                  this.members[var5].label = var1.read_any((TypeCodeImpl)this.content_type);
               }

               this.members[var5].name = var1.read_string();
               this.members[var5].type = var1.read_TypeCode();
            }

            var1.endEncapsulation(var2);
            break;
         case 17:
            var2 = var1.startEncapsulation();
            this.repid = var1.read_repository_id();
            this.name = var1.read_string();
            var4 = var1.read_ulong();
            this.members = new MemberInfo[var4];

            for(var5 = 0; var5 < var4; ++var5) {
               this.members[var5] = new MemberInfo();
               this.members[var5].name = var1.read_string();
            }

            var1.endEncapsulation(var2);
            break;
         case 18:
         case 27:
            this.length = var1.read_ulong();
            break;
         case 19:
         case 20:
            var2 = var1.startEncapsulation();
            this.content_type = var1.read_TypeCode();
            this.length = var1.read_ulong();
            var1.endEncapsulation(var2);
            break;
         case 21:
            var2 = var1.startEncapsulation();
            this.repid = var1.read_repository_id();
            this.name = var1.read_string();
            this.content_type = var1.read_TypeCode();
            var1.endEncapsulation(var2);
            break;
         case 28:
            this.digits = var1.read_short();
            this.scale = var1.read_short();
            break;
         case 29:
            var2 = var1.startEncapsulation();
            this.repid = var1.read_repository_id();
            this.name = var1.read_string();
            this.mod = var1.read_short();
            this.content_type = var1.read_TypeCode();
            var4 = var1.read_ulong();
            this.members = new MemberInfo[var4];

            for(var5 = 0; var5 < var4; ++var5) {
               this.members[var5] = new MemberInfo();
               this.members[var5].name = var1.read_string();
               this.members[var5].type = var1.read_TypeCode();
               this.members[var5].access = var1.read_short();
            }

            var1.endEncapsulation(var2);
            break;
         case 30:
            var2 = var1.startEncapsulation();
            this.repid = var1.read_repository_id();
            this.name = var1.read_string();
            this.content_type = var1.read_TypeCode();
            var1.endEncapsulation(var2);
            break;
         case 31:
         default:
            throw new MARSHAL("Unknown tc: " + this.type.value());
      }

   }

   public void write(IIOPOutputStream var1) {
      write(this, var1);
   }

   public static void write(TypeCode var0, IIOPOutputStream var1) {
      var1.write_long(var0.kind().value());

      try {
         long var2;
         int var4;
         int var5;
         int var6;
         switch (var0.kind().value()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 23:
            case 24:
            case 25:
            case 26:
               break;
            case 14:
            case 32:
               var2 = var1.startEncapsulation();
               var1.write_string(var0.id());
               var1.write_string(var0.name());
               var1.endEncapsulation(var2);
               break;
            case 15:
            case 22:
               var2 = var1.startEncapsulation();
               var1.write_string(var0.id());
               var1.write_string(var0.name());
               var4 = var0.member_count();
               var1.write_ulong(var4);

               for(var6 = 0; var6 < var4; ++var6) {
                  var1.write_string(var0.member_name(var6));
                  var1.write_TypeCode(var0.member_type(var6));
               }

               var1.endEncapsulation(var2);
               break;
            case 16:
               var2 = var1.startEncapsulation();
               var1.write_string(var0.id());
               var1.write_string(var0.name());
               var1.write_TypeCode(var0.discriminator_type());
               var5 = var0.default_index();
               var1.write_long(var5);
               var4 = var0.member_count();
               var1.write_ulong(var4);

               for(var6 = 0; var6 < var4; ++var6) {
                  if (var6 == var5) {
                     var1.write_octet((byte)0);
                  } else {
                     var1.write_any(var0.member_label(var6), var0.discriminator_type());
                  }

                  var1.write_string(var0.member_name(var6));
                  var1.write_TypeCode(var0.member_type(var6));
               }

               var1.endEncapsulation(var2);
               break;
            case 17:
               var2 = var1.startEncapsulation();
               var1.write_string(var0.id());
               var1.write_string(var0.name());
               var4 = var0.member_count();
               var1.write_ulong(var4);

               for(var6 = 0; var6 < var4; ++var6) {
                  var1.write_string(var0.member_name(var6));
               }

               var1.endEncapsulation(var2);
               break;
            case 18:
            case 27:
               var1.write_ulong(0);
               break;
            case 19:
            case 20:
               var2 = var1.startEncapsulation();
               var1.write_TypeCode(var0.content_type());
               var1.write_ulong(var0.length());
               var1.endEncapsulation(var2);
               break;
            case 21:
               var2 = var1.startEncapsulation();
               var1.write_string(var0.id());
               var1.write_string(var0.name());
               var1.write_TypeCode(var0.content_type());
               var1.endEncapsulation(var2);
               break;
            case 28:
               var1.write_unsigned_short(var0.fixed_digits());
               var1.write_short(var0.fixed_scale());
               break;
            case 29:
               var2 = var1.startEncapsulation();
               var1.write_string(var0.id());
               var1.write_string(var0.name());
               var1.write_short(var0.type_modifier());
               var1.write_TypeCode(var0.concrete_base_type());
               var4 = var0.member_count();
               var1.write_ulong(var4);

               for(var5 = 0; var5 < var4; ++var5) {
                  var1.write_string(var0.member_name(var5));
                  var1.write_TypeCode(var0.member_type(var5));
                  var1.write_short(var0.member_visibility(var5));
               }

               var1.endEncapsulation(var2);
               break;
            case 30:
               var2 = var1.startEncapsulation();
               var1.write_string(var0.id());
               var1.write_string(var0.name());
               var1.write_TypeCode(var0.content_type());
               var1.endEncapsulation(var2);
               break;
            case 31:
            default:
               throw new NO_IMPLEMENT("Unsupported TypeCode: " + var0);
         }

      } catch (BadKind var7) {
         throw new MARSHAL("Invalid TypeCode: " + var0);
      } catch (Bounds var8) {
         throw new MARSHAL("Invalid TypeCode: " + var0);
      }
   }

   public static TypeCode get_primitive_tc(TCKind var0) {
      return simpleTypes[var0.value()];
   }

   public static TypeCode get_primitive_tc(int var0) {
      return simpleTypes[var0];
   }

   public static TypeCode create_struct_tc(int var0, String var1, String var2, StructMember[] var3) {
      TypeCodeImpl var4 = new TypeCodeImpl(var0, new RepositoryId(var1), var2);
      var4.members = new MemberInfo[var3.length];

      for(int var5 = 0; var5 < var4.members.length; ++var5) {
         var4.members[var5] = new MemberInfo();
         var4.members[var5].name = var3[var5].name;
         var4.members[var5].type = var3[var5].type;
      }

      return var4;
   }

   public static TypeCode create_union_tc(String var0, String var1, TypeCode var2, UnionMember[] var3) {
      TypeCodeImpl var4 = new TypeCodeImpl(16, new RepositoryId(var0), var1, var2);
      var4.members = new MemberInfo[var3.length];

      for(int var5 = 0; var5 < var4.members.length; ++var5) {
         var4.members[var5] = new MemberInfo();
         var4.members[var5].label = var3[var5].label;
         var4.members[var5].name = var3[var5].name;
         var4.members[var5].type = var3[var5].type;
      }

      return var4;
   }

   public static TypeCode create_enum_tc(String var0, String var1, String[] var2) {
      TypeCodeImpl var3 = new TypeCodeImpl(17, new RepositoryId(var0), var1);
      var3.members = new MemberInfo[var2.length];

      for(int var4 = 0; var4 < var3.members.length; ++var4) {
         var3.members[var4] = new MemberInfo();
         var3.members[var4].name = var2[var4];
      }

      return var3;
   }

   public static TypeCode create_string_tc(int var0) {
      TypeCodeImpl var1 = new TypeCodeImpl(18);
      var1.length = var0;
      return var1;
   }

   public static TypeCode create_wstring_tc(int var0) {
      TypeCodeImpl var1 = new TypeCodeImpl(27);
      var1.length = var0;
      return var1;
   }

   public static TypeCode create_sequence_tc(int var0, TypeCode var1) {
      TypeCodeImpl var2 = new TypeCodeImpl(19);
      var2.length = var0;
      var2.content_type = var1;
      return var2;
   }

   public static TypeCode create_array_tc(int var0, TypeCode var1) {
      TypeCodeImpl var2 = new TypeCodeImpl(20);
      var2.length = var0;
      var2.content_type = var1;
      return var2;
   }

   public static TypeCode create_fixed_tc(short var0, short var1) {
      TypeCodeImpl var2 = new TypeCodeImpl(28);
      var2.digits = var0;
      var2.scale = var1;
      return var2;
   }

   public static TypeCode create_value_tc(String var0, String var1, short var2, TypeCode var3, ValueMember[] var4) {
      TypeCodeImpl var5 = new TypeCodeImpl(29, new RepositoryId(var0), var1, var3);
      var5.members = new MemberInfo[var4.length];

      for(int var6 = 0; var6 < var5.members.length; ++var6) {
         var5.members[var6] = new MemberInfo();
         var5.members[var6].access = var4[var6].access;
         var5.members[var6].name = var4[var6].name;
         var5.members[var6].type = var4[var6].type;
      }

      return var5;
   }

   public String toString() {
      return toString(this.type) + (this.repid == null ? "<simple>" : this.repid.toString());
   }

   public static String toString(TCKind var0) {
      String var1;
      switch (var0.value()) {
         case 0:
            var1 = "_tk_null";
            break;
         case 1:
         case 2:
         case 4:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 12:
         case 13:
         case 17:
         case 25:
         case 26:
         case 28:
         case 31:
         default:
            var1 = "" + var0.value();
            break;
         case 3:
            var1 = "_tk_long";
            break;
         case 5:
            var1 = "_tk_ulong";
            break;
         case 11:
            var1 = "_tk_any";
            break;
         case 14:
            var1 = "_tk_objref";
            break;
         case 15:
            var1 = "_tk_struct";
            break;
         case 16:
            var1 = "_tk_union";
            break;
         case 18:
            var1 = "_tk_string";
            break;
         case 19:
            var1 = "_tk_sequence";
            break;
         case 20:
            var1 = "_tk_array";
            break;
         case 21:
            var1 = "_tk_alias";
            break;
         case 22:
            var1 = "_tk_except";
            break;
         case 23:
            var1 = "_tk_longlong";
            break;
         case 24:
            var1 = "_tk_ulonglong";
            break;
         case 27:
            var1 = "_tk_wstring";
            break;
         case 29:
            var1 = "_tk_value";
            break;
         case 30:
            var1 = "_tk_value_box";
            break;
         case 32:
            var1 = "_tk_abstract_interface";
      }

      return "TCKind<" + var1 + ">: ";
   }

   private static void p(String var0) {
      System.out.println("<TypeCodeImpl> " + var0);
   }

   static {
      NULL = new TypeCodeImpl(32, RepositoryId.NULL, "");
      OCTET = new TypeCodeImpl(10);
      STRING = new TypeCodeImpl(30, RepositoryId.STRING, "", new TypeCodeImpl(27));
      OBJECT = new TypeCodeImpl(14, RepositoryId.OBJECT, "");
      VALUE = new TypeCodeImpl(29, RepositoryId.EMPTY, "", NULL_TC);
      simpleTypes = new TypeCodeImpl[]{NULL_TC, new TypeCodeImpl(1), new TypeCodeImpl(2), new TypeCodeImpl(3), new TypeCodeImpl(4), new TypeCodeImpl(5), new TypeCodeImpl(6), new TypeCodeImpl(7), new TypeCodeImpl(8), new TypeCodeImpl(9), OCTET, new TypeCodeImpl(11), new TypeCodeImpl(12), new TypeCodeImpl(13), OBJECT, new TypeCodeImpl(15), new TypeCodeImpl(16), new TypeCodeImpl(17), new TypeCodeImpl(18), new TypeCodeImpl(19), new TypeCodeImpl(20), new TypeCodeImpl(21), new TypeCodeImpl(22), new TypeCodeImpl(23), new TypeCodeImpl(24), new TypeCodeImpl(25), new TypeCodeImpl(26), new TypeCodeImpl(27), new TypeCodeImpl(28), VALUE, new TypeCodeImpl(30), new TypeCodeImpl(31), new TypeCodeImpl(32)};
   }

   private static final class MemberInfo {
      private String name;
      private TypeCode type;
      private short access;
      private Any label;

      private MemberInfo() {
      }

      // $FF: synthetic method
      MemberInfo(Object var1) {
         this();
      }
   }
}
