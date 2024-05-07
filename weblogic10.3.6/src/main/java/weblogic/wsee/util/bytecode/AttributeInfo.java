package weblogic.wsee.util.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class AttributeInfo {
   private static HashMap attInfos = new HashMap();
   private AttInfo attInfo;
   private short nameIndex;
   private ArrayList parameterNames;

   public AttributeInfo() {
      this((ArrayList)null);
   }

   public AttributeInfo(ArrayList var1) {
      this.parameterNames = var1;
   }

   private void addParameterName(String var1) {
      if (this.parameterNames != null) {
         this.parameterNames.add(var1);
      }

   }

   public String toString() {
      return "" + this.attInfo;
   }

   private AttInfo getAttInfoInstance(Class var1) throws IOException {
      try {
         Constructor var2 = var1.getConstructor(AttributeInfo.class);
         return (AttInfo)var2.newInstance(this);
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

   public String getName() {
      return this.attInfo.getName();
   }

   public void write(DataOutput var1) throws IOException {
      var1.writeShort(this.nameIndex);
      this.attInfo.write(var1);
   }

   public void read(DataInput var1, ConstantPool var2) throws IOException {
      this.nameIndex = var1.readShort();
      String var3 = var2.getValue(this.nameIndex);
      this.attInfo = new DefaultAttInfo();
      this.attInfo.setName(var3);
      this.attInfo.read(var1, var2);
   }

   public byte[] getAttributeBytes() {
      if (this.attInfo != null && this.attInfo instanceof DefaultAttInfo) {
         return ((DefaultAttInfo)this.attInfo).getBytes();
      } else {
         throw new RuntimeException("attInfo error: " + this.attInfo);
      }
   }

   public void setCustomAttribute(String var1, short var2, byte[] var3) {
      this.nameIndex = var2;
      this.attInfo = new DefaultAttInfo();
      this.attInfo.setName(var1);
      ((DefaultAttInfo)this.attInfo).setBytes(var3);
   }

   static {
      attInfos.put("SourceFile", SourceFile.class);
      attInfos.put("ConstantValue", ConstantValue.class);
      attInfos.put("Code", Code.class);
      attInfos.put("Exceptions", Exceptions.class);
      attInfos.put("LineNumberTable", LineNumberTable.class);
      attInfos.put("LocalVariableTable", LocalVariableTable.class);
   }

   public class LocalVariableTable extends AttInfo {
      public LocalVariableTable() {
         super();
      }

      public void read(DataInput var1, ConstantPool var2) throws IOException {
         int var3 = var1.readInt();
         short var4 = var1.readShort();

         for(int var5 = 0; var5 < var4; ++var5) {
            short var6 = var1.readShort();
            short var7 = var1.readShort();
            String var8 = var2.getValue(var1.readShort());
            if (var6 == 0) {
               AttributeInfo.this.addParameterName(var8);
            }

            var2.getValue(var1.readShort());
            short var10 = var1.readShort();
         }

      }

      public void write(DataOutput var1) throws IOException {
      }
   }

   public class LineNumberTable extends AttInfo {
      public LineNumberTable() {
         super();
      }

      public void read(DataInput var1, ConstantPool var2) throws IOException {
         int var3 = var1.readInt();
         short var4 = var1.readShort();

         for(int var5 = 0; var5 < var4; ++var5) {
            short var6 = var1.readShort();
            short var7 = var1.readShort();
         }

      }

      public void write(DataOutput var1) throws IOException {
      }
   }

   public class Exceptions extends AttInfo {
      public Exceptions() {
         super();
      }

      public void read(DataInput var1, ConstantPool var2) throws IOException {
         int var3 = var1.readInt();
         short var4 = var1.readShort();

         for(int var5 = 0; var5 < var4; ++var5) {
            var2.getValue(var1.readShort());
         }

      }

      public void write(DataOutput var1) throws IOException {
      }
   }

   public class Code extends AttInfo {
      public Code() {
         super();
      }

      public void read(DataInput var1, ConstantPool var2) throws IOException {
         int var3 = var1.readInt();
         short var4 = var1.readShort();
         short var5 = var1.readShort();
         int var6 = var1.readInt();
         byte[] var7 = new byte[var6];
         var1.readFully(var7);
         short var8 = var1.readShort();

         int var10;
         for(int var9 = 0; var9 < var8; ++var9) {
            var10 = var1.readShort();
            short var11 = var1.readShort();
            short var12 = var1.readShort();
            short var13 = var1.readShort();
         }

         short var15 = var1.readShort();

         for(var10 = 0; var10 < var15; ++var10) {
            AttributeInfo var14 = new AttributeInfo(AttributeInfo.this.parameterNames);
            var14.read(var1, var2);
         }

      }

      public void write(DataOutput var1) throws IOException {
      }
   }

   public class ConstantValue extends AttInfo {
      public ConstantValue() {
         super();
      }

      public void read(DataInput var1, ConstantPool var2) throws IOException {
         int var3 = var1.readInt();
         var2.getValue(var1.readShort());
      }

      public void write(DataOutput var1) throws IOException {
      }
   }

   public class SourceFile extends AttInfo {
      public SourceFile() {
         super();
      }

      public void read(DataInput var1, ConstantPool var2) throws IOException {
         int var3 = var1.readInt();
         var2.getValue(var1.readShort());
      }

      public void write(DataOutput var1) throws IOException {
      }
   }

   public class DefaultAttInfo extends AttInfo {
      byte[] bytes;

      public DefaultAttInfo() {
         super();
      }

      public byte[] getBytes() {
         return this.bytes;
      }

      public void setBytes(byte[] var1) {
         this.bytes = var1;
      }

      public String toString() {
         return "AttInfo[" + this.getName() + "] : " + new String(this.bytes);
      }

      public void read(DataInput var1, ConstantPool var2) throws IOException {
         int var3 = var1.readInt();
         this.bytes = new byte[var3];
         var1.readFully(this.bytes);
      }

      public void write(DataOutput var1) throws IOException {
         var1.writeInt(this.bytes.length);
         var1.write(this.bytes);
      }
   }

   public abstract class AttInfo {
      private String name;

      public void setName(String var1) {
         this.name = var1;
      }

      public String getName() {
         return this.name;
      }

      public abstract void read(DataInput var1, ConstantPool var2) throws IOException;

      public abstract void write(DataOutput var1) throws IOException;
   }
}
