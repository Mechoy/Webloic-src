package weblogic.wsee.util.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ConstantPool {
   static HashMap cpInfos = new HashMap();
   private int constantPoolCount;
   private ArrayList cpInfoInstances = new ArrayList();

   public ConstantPool() {
      this.cpInfoInstances.add(Object.class);
   }

   public String getValue(int var1) {
      return this.cpInfoInstances.get(var1).toString();
   }

   public Iterator getConstants() {
      return this.cpInfoInstances.iterator();
   }

   public CpInfo getConstant(int var1) {
      return (CpInfo)this.cpInfoInstances.get(var1);
   }

   public short addUtf8Constant(String var1) {
      ConstantUtf8Info var2 = new ConstantUtf8Info();
      var2.setTag((byte)1);
      var2.setString(var1);
      this.cpInfoInstances.add(var2);
      return (short)(this.cpInfoInstances.size() - 1);
   }

   private CpInfo getCpInfoInstance(Class var1) throws IOException {
      try {
         Constructor var2 = var1.getConstructor(ConstantPool.class);
         return (CpInfo)var2.newInstance(this);
      } catch (InstantiationException var3) {
         throw new IOException("new failed: " + var3);
      } catch (IllegalAccessException var4) {
         throw new IOException("access failed: " + var4.getMessage());
      } catch (NoSuchMethodException var5) {
         throw new IOException("constructor not found: " + var5.getMessage());
      } catch (InvocationTargetException var6) {
         throw new IOException("target not found: " + var6.getMessage());
      }
   }

   public void write(DataOutput var1) throws IOException {
      var1.writeShort(this.constantPoolCount);
      Iterator var2 = this.cpInfoInstances.iterator();
      var2.next();

      while(var2.hasNext()) {
         CpInfo var3 = (CpInfo)var2.next();
         if (var3 != null) {
            var3.write(var1);
         }
      }

   }

   public void read(DataInput var1) throws IOException {
      this.constantPoolCount = var1.readUnsignedShort();

      for(int var2 = 0; var2 < this.constantPoolCount - 1; ++var2) {
         Byte var3 = new Byte(var1.readByte());
         Class var4 = (Class)cpInfos.get(var3);
         CpInfo var5 = this.getCpInfoInstance(var4);
         var5.read(var1);
         var5.setTag(var3);
         this.cpInfoInstances.add(var5);
         if (var5 instanceof ConstantDoubleInfo || var5 instanceof ConstantLongInfo) {
            ++var2;
            this.cpInfoInstances.add((Object)null);
         }
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      int var2 = 1;

      for(Iterator var3 = this.cpInfoInstances.iterator(); var3.hasNext(); ++var2) {
         var1.append("\n: " + var2 + " ").append(var3.next());
      }

      return var1.toString();
   }

   static {
      cpInfos.put(new Byte((byte)7), ConstantClassInfo.class);
      cpInfos.put(new Byte((byte)9), ConstantRefInfo.class);
      cpInfos.put(new Byte((byte)10), ConstantRefInfo.class);
      cpInfos.put(new Byte((byte)11), ConstantRefInfo.class);
      cpInfos.put(new Byte((byte)8), ConstantStringInfo.class);
      cpInfos.put(new Byte((byte)3), ConstantIntFloatInfo.class);
      cpInfos.put(new Byte((byte)4), ConstantIntFloatInfo.class);
      cpInfos.put(new Byte((byte)6), ConstantDoubleInfo.class);
      cpInfos.put(new Byte((byte)5), ConstantLongInfo.class);
      cpInfos.put(new Byte((byte)12), ConstantNameTypeInfo.class);
      cpInfos.put(new Byte((byte)1), ConstantUtf8Info.class);
   }

   public class ConstantUtf8Info extends CpInfo {
      private String string;

      public ConstantUtf8Info() {
         super();
      }

      public void setString(String var1) {
         this.string = var1;
      }

      public String getString() {
         return this.string;
      }

      public void read(DataInput var1) throws IOException {
         this.string = var1.readUTF();
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeUTF(this.string);
      }

      public String toString() {
         return "utf-8: " + this.string;
      }
   }

   public class ConstantNameTypeInfo extends CpInfo {
      short nameIndex;
      short descriptorIndex;

      public ConstantNameTypeInfo() {
         super();
      }

      public void read(DataInput var1) throws IOException {
         this.nameIndex = var1.readShort();
         this.descriptorIndex = var1.readShort();
      }

      public short getDescriptorIndex() {
         return this.descriptorIndex;
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeShort(this.nameIndex);
         var1.writeShort(this.descriptorIndex);
      }

      public String toString() {
         return "name_type_info:" + ConstantPool.this.getValue(this.nameIndex) + "," + ConstantPool.this.getValue(this.descriptorIndex);
      }
   }

   public class ConstantDoubleInfo extends CpInfo {
      double value;

      public ConstantDoubleInfo() {
         super();
      }

      public void read(DataInput var1) throws IOException {
         this.value = var1.readDouble();
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeDouble(this.value);
      }

      public String toString() {
         return "double_info:" + this.value;
      }
   }

   public class ConstantLongInfo extends CpInfo {
      long value;

      public ConstantLongInfo() {
         super();
      }

      public void read(DataInput var1) throws IOException {
         this.value = var1.readLong();
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeLong(this.value);
      }

      public String toString() {
         return "long_info:" + this.value;
      }
   }

   public class ConstantIntFloatInfo extends CpInfo {
      int value;

      public ConstantIntFloatInfo() {
         super();
      }

      public void read(DataInput var1) throws IOException {
         this.value = var1.readInt();
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeInt(this.value);
      }

      public String toString() {
         return "float_int_info:" + this.value;
      }
   }

   public class ConstantStringInfo extends CpInfo {
      short stringIndex;

      public ConstantStringInfo() {
         super();
      }

      public void read(DataInput var1) throws IOException {
         this.stringIndex = var1.readShort();
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeShort(this.stringIndex);
      }

      public String toString() {
         return "String_info:" + ConstantPool.this.getValue(this.stringIndex);
      }
   }

   public class ConstantRefInfo extends CpInfo {
      short classIndex;
      short nameAndTypeIndex;

      public ConstantRefInfo() {
         super();
      }

      public void read(DataInput var1) throws IOException {
         this.classIndex = var1.readShort();
         this.nameAndTypeIndex = var1.readShort();
      }

      public short getNameAndTypeIndex() {
         return this.nameAndTypeIndex;
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeShort(this.classIndex);
         var1.writeShort(this.nameAndTypeIndex);
      }

      public String toString() {
         return "ref_info:" + ConstantPool.this.getValue(this.classIndex) + "," + ConstantPool.this.getValue(this.nameAndTypeIndex);
      }
   }

   public class ConstantClassInfo extends CpInfo {
      short nameIndex;

      public ConstantClassInfo() {
         super();
      }

      public short getIndex() {
         return this.nameIndex;
      }

      public void read(DataInput var1) throws IOException {
         this.nameIndex = var1.readShort();
      }

      public void write(DataOutput var1) throws IOException {
         super.write(var1);
         var1.writeShort(this.nameIndex);
      }

      public String toString() {
         return "class_info: " + ConstantPool.this.getValue(this.nameIndex);
      }
   }

   public abstract class CpInfo {
      private byte tag;

      public void setTag(byte var1) {
         this.tag = var1;
      }

      public abstract void read(DataInput var1) throws IOException;

      public void write(DataOutput var1) throws IOException {
         var1.writeByte(this.tag);
      }
   }
}
