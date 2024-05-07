package weblogic.wsee.util.bytecode;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class SClass {
   private int magic = -889275714;
   private short minorVersion = 3;
   private short majorVersion = 45;
   private ConstantPool pool;
   private short accessFlag;
   private short thisClassIndex;
   private short superClassIndex;
   private short[] interfaceIndexes;
   private ArrayList fields = new ArrayList();
   private ArrayList methods = new ArrayList();
   private ArrayList attributes = new ArrayList();

   public ConstantPool getConstantPool() {
      return this.pool;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("SClass[\n");
      var1.append("magic=").append(this.magic);
      var1.append("\nminorVersion=").append(this.minorVersion);
      var1.append("\nmajorVersion=").append(this.majorVersion);
      var1.append("\nconstantPool=").append(this.pool);
      var1.append("\naccessFlag=").append(this.accessFlag);
      var1.append("\nthisClass=").append(this.pool.getValue(this.thisClassIndex));
      var1.append("\nsuperClass=").append(this.pool.getValue(this.superClassIndex));
      var1.append("\ninterfaces=");

      for(int var2 = 0; var2 < this.interfaceIndexes.length; ++var2) {
         var1.append("\n>>").append(this.pool.getValue(this.interfaceIndexes[var2]));
      }

      var1.append("\nfields=");
      Iterator var3 = this.fields.iterator();

      while(var3.hasNext()) {
         var1.append("\n>>").append(var3.next());
      }

      var1.append("\nmethods=");
      var3 = this.methods.iterator();

      while(var3.hasNext()) {
         var1.append("\n>>").append(var3.next());
      }

      var1.append("\nattributes=");
      var3 = this.attributes.iterator();

      while(var3.hasNext()) {
         var1.append("\n>>").append(var3.next());
      }

      var1.append("\n]\n");
      return var1.toString();
   }

   public void write(DataOutput var1) throws IOException {
      var1.writeInt(this.magic);
      var1.writeShort(this.minorVersion);
      var1.writeShort(this.majorVersion);
      this.pool.write(var1);
      var1.writeShort(this.accessFlag);
      var1.writeShort(this.thisClassIndex);
      var1.writeShort(this.superClassIndex);
      var1.writeShort(this.interfaceIndexes.length);

      for(int var2 = 0; var2 < this.interfaceIndexes.length; ++var2) {
         var1.writeShort(this.interfaceIndexes[var2]);
      }

      var1.writeShort(this.fields.size());
      Iterator var4 = this.fields.iterator();

      while(var4.hasNext()) {
         SField var3 = (SField)var4.next();
         var3.write(var1);
      }

      var1.writeShort(this.methods.size());
      var4 = this.methods.iterator();

      while(var4.hasNext()) {
         SMethod var5 = (SMethod)var4.next();
         var5.write(var1);
      }

      var1.writeShort(this.attributes.size());
      var4 = this.attributes.iterator();

      while(var4.hasNext()) {
         AttributeInfo var6 = (AttributeInfo)var4.next();
         var6.write(var1);
      }

   }

   public void addMethod(SMethod var1) {
      this.methods.add(var1);
   }

   public void addField(SField var1) {
      this.fields.add(var1);
   }

   public void addAttribute(AttributeInfo var1) {
      this.attributes.add(var1);
   }

   public Iterator getAttributes() {
      return this.attributes.iterator();
   }

   public AttributeInfo getAttribute(String var1) {
      Iterator var2 = this.getAttributes();

      AttributeInfo var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AttributeInfo)var2.next();
      } while(!var1.equals(var3.getName()));

      return var3;
   }

   public byte[] getAttributeBytes(String var1) {
      AttributeInfo var2 = this.getAttribute(var1);
      if (var2 == null) {
         throw new RuntimeException("Custom Attribute not found");
      } else {
         return var2.getAttributeBytes();
      }
   }

   public void addCustomAttribute(String var1, byte[] var2) {
      short var3 = this.pool.addUtf8Constant(var1);
      AttributeInfo var4 = new AttributeInfo();
      var4.setCustomAttribute(var1, var3, var2);
      this.addAttribute(var4);
   }

   public void read(DataInput var1) throws IOException {
      if (this.magic != var1.readInt()) {
         throw new IOException("wrong magic");
      } else {
         if (this.minorVersion != var1.readShort()) {
         }

         if (this.majorVersion != var1.readShort()) {
         }

         this.pool = new ConstantPool();
         this.pool.read(var1);
         this.accessFlag = var1.readShort();
         this.thisClassIndex = var1.readShort();
         this.superClassIndex = var1.readShort();
         short var2 = var1.readShort();
         this.interfaceIndexes = new short[var2];

         for(int var3 = 0; var3 < var2; ++var3) {
            this.interfaceIndexes[var3] = var1.readShort();
         }

         short var8 = var1.readShort();

         for(int var4 = 0; var4 < var8; ++var4) {
            SField var5 = new SField();
            var5.read(var1, this.pool);
            this.addField(var5);
         }

         short var9 = var1.readShort();

         for(int var10 = 0; var10 < var9; ++var10) {
            SMethod var6 = new SMethod();
            var6.read(var1, this.pool);
            this.addMethod(var6);
         }

         short var11 = var1.readShort();

         for(int var12 = 0; var12 < var11; ++var12) {
            AttributeInfo var7 = new AttributeInfo();
            var7.read(var1, this.pool);
            this.addAttribute(var7);
         }

      }
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.out.println("usage: SClass [file].class");
      } else {
         SClass var1 = new SClass();
         FileInputStream var2;
         var1.read(new DataInputStream(var2 = new FileInputStream(var0[0])));
         var2.close();
         System.out.println("clazz:" + var1);
      }
   }
}
