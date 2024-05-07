package weblogic.wsee.util.bytecode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class SField {
   private short accessFlag;
   private short nameIndex;
   private short descriptorIndex;
   private ArrayList attributes = new ArrayList();
   private ConstantPool pool;

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getElementName()).append("[\n");
      var1.append("\naccessFlag=").append(this.accessFlag);
      var1.append("\nname=").append(this.pool.getValue(this.nameIndex));
      var1.append("\ndescriptor=").append(this.pool.getValue(this.descriptorIndex));
      var1.append("\nattributes=");
      Iterator var2 = this.attributes.iterator();

      while(var2.hasNext()) {
         var1.append("\n>>").append(var2.next());
      }

      var1.append("\n]\n");
      return var1.toString();
   }

   protected String getElementName() {
      return "Field";
   }

   public void read(DataInput var1, ConstantPool var2) throws IOException {
      this.pool = var2;
      this.accessFlag = var1.readShort();
      this.nameIndex = var1.readShort();
      this.descriptorIndex = var1.readShort();
      short var3 = var1.readShort();

      for(int var4 = 0; var4 < var3; ++var4) {
         AttributeInfo var5 = new AttributeInfo();
         var5.read(var1, var2);
         this.attributes.add(var5);
      }

   }

   public void write(DataOutput var1) throws IOException {
      var1.writeShort(this.accessFlag);
      var1.writeShort(this.nameIndex);
      var1.writeShort(this.descriptorIndex);
      var1.writeShort(this.attributes.size());
      Iterator var2 = this.attributes.iterator();

      while(var2.hasNext()) {
         AttributeInfo var3 = (AttributeInfo)var2.next();
         var3.write(var1);
      }

   }
}
