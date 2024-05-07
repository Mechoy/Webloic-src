package weblogic.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

public final class ParamValue implements Cloneable, Externalizable {
   protected String paramName;
   protected String paramDesc;
   protected int paramType;
   protected int paramMode;
   private Object value;
   boolean verbose;
   private Vector valuevect = null;

   public void initialize() {
      this.paramName = null;
      this.paramDesc = null;
      this.paramType = 15;
      this.paramMode = 40;
      this.value = null;
      this.verbose = false;
      this.valuevect = null;
   }

   public void destroy() {
      this.paramName = null;
      this.paramDesc = null;
      this.paramType = 15;
      this.paramMode = 40;
      this.value = null;
      this.verbose = false;
      this.valuevect = null;
   }

   private Object currval(Object var1) {
      if (this.verbose) {
         System.out.println(this.paramName + " now set to " + (var1 == null ? "(null)" : var1.toString()));
      }

      this.value = var1;
      return this.currval();
   }

   private Object currval() {
      return this.value;
   }

   public ParamValue() {
      this.initialize();
   }

   public ParamValue(String var1, int var2, int var3, String var4) {
      this.initialize();
      this.paramName = var1;
      this.paramType = var2;
      this.paramMode = var3;
      this.paramDesc = var4;
   }

   private ParamValue(ParamValue var1) {
      this.paramName = var1.paramName;
      this.paramDesc = var1.paramDesc;
      this.paramType = var1.paramType;
      this.paramMode = var1.paramMode;
      this.value = var1.value;
      this.verbose = var1.verbose;
      this.valuevect = var1.valuevect;
      if (this.isVector() && this.valuevect != null) {
         this.valuevect = new Vector(var1.valuevect.size());
         Enumeration var2 = this.valuevect.elements();

         while(var2.hasMoreElements()) {
            ParamValue var3 = (ParamValue)var2.nextElement();
            this.valuevect.addElement(var3.clone());
         }
      }

   }

   public Object clone() {
      return new ParamValue(this);
   }

   private void readValue(WLObjectInput var1) throws IOException, ParamSetException {
      Object var2 = null;
      boolean var3 = var1.readBoolean();
      if (!var3) {
         label39:
         switch (this.paramType()) {
            case 1:
               var2 = new Boolean(var1.readBoolean());
               break;
            case 2:
               var2 = new Byte(var1.readByte());
               break;
            case 3:
               var2 = new Integer(var1.readInt());
               break;
            case 4:
               var2 = new Long(var1.readLong());
               break;
            case 5:
               var2 = new Double(var1.readDouble());
               break;
            case 6:
               var2 = new Float(var1.readFloat());
               break;
            case 7:
               var2 = var1.readString();
               break;
            case 8:
               var2 = var1.readString();
               break;
            case 9:
               var2 = var1.readDate();
               break;
            case 16:
               try {
                  var2 = var1.readObjectWL();
                  break;
               } catch (ClassNotFoundException var7) {
                  throw new ParamSetException("Class not found: " + var7);
               }
            case 19:
               var2 = new Short(var1.readShort());
               break;
            case 51:
               int var4 = var1.readInt();
               this.valuevect = new Vector(var4);
               int var5 = 0;

               while(true) {
                  if (var5 >= var4) {
                     break label39;
                  }

                  ParamValue var6 = new ParamValue();
                  var6.readExternal(var1);
                  this.set(var6, var5);
                  ++var5;
               }
            default:
               throw new ParamSetException("Internal Error - Unknown Type: " + this.paramType);
         }

         this.currval(var2);
         if (this.verbose) {
            System.out.println("Reading " + this.dump());
         }

      }
   }

   public void readExternal(ObjectInput var1) throws IOException {
      WLObjectInput var2 = (WLObjectInput)var1;
      this.paramName = var2.readAbbrevString();
      this.paramType = var2.readInt();

      try {
         this.readValue(var2);
      } catch (ParamSetException var4) {
         throw new IOException("" + var4);
      }
   }

   private void writeValue(WLObjectOutput var1) throws IOException, ParamSetException {
      if (this.verbose) {
         System.out.println("PV writing:" + this.dump());
      }

      boolean var2 = this.isNull();
      var1.writeBoolean(var2);
      if (!var2) {
         switch (this.paramType()) {
            case 1:
               var1.writeBoolean(this.asBoolean());
               break;
            case 2:
               var1.writeByte(this.asByte());
               break;
            case 3:
               var1.writeInt(this.asInt());
               break;
            case 4:
               var1.writeLong(this.asLong());
               break;
            case 5:
               var1.writeDouble(this.asDouble());
               break;
            case 6:
               var1.writeFloat(this.asFloat());
               break;
            case 7:
               var1.writeString(this.asString());
               break;
            case 8:
               var1.writeString(this.asString());
               break;
            case 9:
               var1.writeDate(this.asDate());
               break;
            case 16:
               var1.writeObjectWL(this.asObject());
               break;
            case 19:
               var1.writeShort(this.asShort());
               break;
            case 51:
               int var3 = this.size();
               var1.writeInt(var3);

               for(int var4 = 0; var4 < var3; ++var4) {
                  this.elementAt(var4).writeExternal(var1);
               }

               return;
            default:
               throw new ParamSetException("Unknown Type: " + this.paramType() + " for " + this.getClass().getName());
         }

      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeAbbrevString(this.name());
      var2.writeInt(this.paramType());

      try {
         this.writeValue(var2);
      } catch (ParamSetException var4) {
         throw new IOException("" + var4);
      }
   }

   public String name() {
      return this.paramName;
   }

   public String paramDesc() {
      return this.paramDesc;
   }

   public int paramType() {
      return this.paramType;
   }

   public String paramTypeString() {
      return ParamTypes.toString(this.paramType);
   }

   public String paramModeString() {
      return ParamTypes.toString(this.paramMode);
   }

   public boolean isNull(int var1) throws ParamSetException {
      return this.elementAt(var1).isNull();
   }

   public boolean isNull() {
      if (this.isVector()) {
         return this.valuevect == null;
      } else {
         return this.currval() == null;
      }
   }

   public int paramMode() {
      return this.paramMode;
   }

   public boolean isScalar() {
      return this.paramType != 51;
   }

   public boolean isVector() {
      return this.paramType == 51;
   }

   public boolean isInt() {
      return this.paramType == 3;
   }

   public boolean isShort() {
      return this.paramType == 19;
   }

   public boolean isFloat() {
      return this.paramType == 6;
   }

   public boolean isDouble() {
      return this.paramType == 5;
   }

   public boolean isDate() {
      return this.paramType == 9;
   }

   public boolean isString() {
      return this.paramType == 8;
   }

   public boolean isChar() {
      return this.paramType == 7;
   }

   public boolean isBoolean() {
      return this.paramType == 1;
   }

   public boolean isByte() {
      return this.paramType == 2;
   }

   public boolean isLong() {
      return this.paramType == 4;
   }

   public boolean isObject() {
      return this.paramType == 16;
   }

   void paramType(int var1) {
      this.paramType = 3;
   }

   void paramType(short var1) {
      this.paramType = 19;
   }

   void paramType(float var1) {
      this.paramType = 6;
   }

   void paramType(double var1) {
      this.paramType = 5;
   }

   void paramType(Date var1) {
      this.paramType = 9;
   }

   void paramType(String var1) {
      this.paramType = 8;
   }

   void paramType(char var1) {
      this.paramType = 7;
   }

   void paramType(boolean var1) {
      this.paramType = 1;
   }

   void paramType(byte var1) {
      this.paramType = 2;
   }

   void paramType(long var1) {
      this.paramType = 4;
   }

   void paramType(Object var1) {
      this.paramType = 16;
   }

   public ParamValue elementAt(int var1) throws ParamSetException {
      if (var1 + 1 > this.size()) {
         this.valuevect.setSize(var1 + 1);
      }

      ParamValue var2 = (ParamValue)this.valuevect.elementAt(var1);
      if (var2 == null) {
         var2 = new ParamValue(this.name() + "[" + var1 + "]", 43, this.paramMode(), this.paramDesc());
         this.valuevect.setElementAt(var2, var1);
      }

      if (this.verbose) {
         System.out.println("elementAt: [" + this.dump() + "]" + "\n\tfor item #" + var1 + "is " + "\n\t[" + var2.dump() + "]");
      }

      return var2;
   }

   public void setElementAt(Object var1, int var2) throws ParamSetException {
      ParamValue var3 = this.elementAt(var2);
      var3.currval(var1);
   }

   public int size() throws ParamSetException {
      if (!this.isVector()) {
         throw new ParamSetException("Cannot get size of a " + this.paramTypeString() + "[" + this.dump() + "]");
      } else {
         if (this.valuevect == null) {
            this.valuevect = new Vector(0);
         }

         if (this.verbose) {
            System.out.println("Vectorsize of " + this.dump() + " is " + this.valuevect.size());
         }

         return this.valuevect.size();
      }
   }

   public ParamValue set(ParamValue var1, int var2) throws ParamSetException {
      if (this.verbose) {
         System.out.println("\n-----------\nVthis = (" + this.dump() + ")\nVval  = (" + var1.dump() + ")\n");
      }

      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      if (this.verbose) {
         System.out.println("Vnow = (" + this.dump() + ")\n----------");
      }

      return this;
   }

   public ParamValue set(boolean var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(short var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(int var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(long var1, int var3) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var3).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(double var1, int var3) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var3).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(float var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(char var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(String var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(Date var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(Object var1, int var2) throws ParamSetException {
      if (this.paramType() == 43) {
         this.paramType = 51;
      }

      if (this.isVector()) {
         this.elementAt(var2).set(var1);
      } else {
         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(ParamValue var1) throws ParamSetException {
      if (this.verbose) {
         System.out.println("\n-----------\nthis = (" + this.dump() + ")\nval  = (" + var1.dump() + ")\n");
      }

      if (this.paramType == 43) {
         this.paramType = var1.paramType();
         if (this.verbose) {
            System.out.println("Converting unknown   " + this.dump() + " to " + var1.dump());
         }
      }

      if (this.isVector()) {
         if (var1.isVector()) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
               this.elementAt(var2).set(var1.elementAt(var2));
            }
         } else {
            this.set((ParamValue)var1, 0);
         }
      } else {
         this.currval(this.toObject(var1));
      }

      if (this.verbose) {
         System.out.println("now = (" + this.dump() + ")\n----------");
      }

      return this;
   }

   public ParamValue set(boolean var1) throws ParamSetException {
      if (this.isVector()) {
         this.set(var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(int var1) throws ParamSetException {
      if (this.isVector()) {
         this.set((int)var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(short var1) throws ParamSetException {
      if (this.isVector()) {
         this.set((short)var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(byte var1) throws ParamSetException {
      if (this.isVector()) {
         this.set((short)((short)var1), 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(long var1) throws ParamSetException {
      if (this.isVector()) {
         this.set(var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(double var1) throws ParamSetException {
      if (this.isVector()) {
         this.set(var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(float var1) throws ParamSetException {
      if (this.isVector()) {
         this.set(var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(char var1) throws ParamSetException {
      if (this.isVector()) {
         this.set((char)var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(String var1) throws ParamSetException {
      if (this.isVector()) {
         this.set((String)var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(Date var1) throws ParamSetException {
      if (this.isVector()) {
         this.set((Date)var1, 0);
      } else {
         if (this.paramType == 43) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   public ParamValue set(Object var1) throws ParamSetException {
      if (this.isVector()) {
         this.set((Object)var1, 0);
      } else {
         if (this.paramType == 43 || this.paramType == 15) {
            this.paramType(var1);
         }

         this.currval(this.toObject(var1));
      }

      return this;
   }

   Object toObject(ParamValue var1) throws ParamSetException {
      switch (var1.paramType()) {
         case 1:
            return this.toObject(var1.asBoolean());
         case 2:
            return this.toObject(var1.asByte());
         case 3:
            return this.toObject(var1.asInt());
         case 4:
            return this.toObject(var1.asLong());
         case 5:
            return this.toObject(var1.asDouble());
         case 6:
            return this.toObject(var1.asFloat());
         case 7:
            return this.toObject(var1.asChar());
         case 8:
         default:
            return this.toObject(var1.asString());
         case 9:
            return this.toObject(var1.asDate());
         case 16:
            return this.toObject(var1.asObject());
         case 19:
            return this.toObject(var1.asShort());
         case 51:
            return null;
      }
   }

   Object toObject(boolean var1) throws ParamSetException {
      switch (this.paramType()) {
         case 1:
            return new Boolean(var1);
         case 8:
            return String.valueOf(var1);
         default:
            throw new ParamSetException("Unknown Conversion from boolean to " + this.paramTypeString() + " for " + this.dump());
      }
   }

   Object toObject(short var1) throws ParamSetException {
      switch (this.paramType()) {
         case 8:
            return String.valueOf(var1);
         case 19:
            return new Short(var1);
         default:
            throw new ParamSetException("Unknown Conversion from short to " + this.paramTypeString() + " for " + this.dump());
      }
   }

   Object toObject(int var1) throws ParamSetException {
      switch (this.paramType()) {
         case 3:
            return new Integer(var1);
         case 4:
            return new Long((long)var1);
         case 5:
            return new Double((double)var1);
         case 6:
            return new Float((float)var1);
         case 7:
         default:
            throw new ParamSetException("Unknown Conversion from int to " + this.paramTypeString() + " for " + this.dump());
         case 8:
            return String.valueOf(var1);
      }
   }

   Object toObject(byte var1) throws ParamSetException {
      switch (this.paramType()) {
         case 2:
            return new Integer(var1);
         case 3:
            return new Integer(var1);
         case 4:
            return new Long((long)var1);
         case 5:
            return new Double((double)var1);
         case 6:
            return new Float((float)var1);
         case 7:
         default:
            throw new ParamSetException("Unknown Conversion from byte to " + this.paramTypeString() + " for " + this.dump());
         case 8:
            return String.valueOf(var1);
      }
   }

   Object toObject(long var1) throws ParamSetException {
      switch (this.paramType()) {
         case 4:
            return new Long(var1);
         case 5:
            return new Double((double)var1);
         case 6:
            return new Float((float)var1);
         case 7:
         default:
            throw new ParamSetException("Unknown Conversion from long to " + this.paramTypeString() + " for " + this.dump());
         case 8:
            return String.valueOf(var1);
      }
   }

   Object toObject(double var1) throws ParamSetException {
      switch (this.paramType()) {
         case 5:
            return new Double(var1);
         case 8:
            return String.valueOf(var1);
         default:
            throw new ParamSetException("Unknown Conversion from double to " + this.paramTypeString() + " for " + this.dump());
      }
   }

   Object toObject(float var1) throws ParamSetException {
      switch (this.paramType()) {
         case 5:
            return new Double((double)var1);
         case 6:
            return new Float(var1);
         case 7:
         default:
            throw new ParamSetException("Unknown Conversion from float to " + this.paramTypeString() + " for " + this.dump());
         case 8:
            return String.valueOf(var1);
      }
   }

   Object toObject(char var1) throws ParamSetException {
      switch (this.paramType()) {
         case 7:
            return String.valueOf(var1);
         case 8:
            return String.valueOf(var1);
         default:
            throw new ParamSetException("Unknown Conversion from char to " + this.paramTypeString() + " for " + this.dump());
      }
   }

   Object toObject(String var1) throws ParamSetException {
      try {
         if (var1 == null) {
            return null;
         } else {
            switch (this.paramType()) {
               case 1:
                  return new Boolean(var1);
               case 2:
                  return new Byte(var1);
               case 3:
                  return new Integer(var1);
               case 4:
                  return new Long(var1);
               case 5:
                  return new Double(var1);
               case 6:
                  return new Float(var1);
               case 7:
                  return var1.substring(0, 1);
               case 8:
                  return var1;
               case 9:
                  return new Date(var1);
               case 10:
               case 11:
               case 12:
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 18:
               default:
                  throw new ParamSetException("Unknown Conversion from String to " + this.paramTypeString() + " for " + this.dump());
               case 19:
                  return new Short(var1);
            }
         }
      } catch (NumberFormatException var3) {
         throw new ParamSetException("[" + var3 + "] to type " + this.paramTypeString());
      }
   }

   Object toObject(Date var1) throws ParamSetException {
      if (var1 == null) {
         return null;
      } else {
         switch (this.paramType()) {
            case 8:
               return var1.toString();
            case 9:
               return var1;
            default:
               throw new ParamSetException("Unknown Conversion from Date to " + this.paramTypeString() + " for " + this.dump());
         }
      }
   }

   Object toObject(Object var1) throws ParamSetException {
      if (var1 == null) {
         return null;
      } else {
         switch (this.paramType()) {
            case 8:
               return var1.toString();
            case 16:
               return var1;
            default:
               throw new ParamSetException("Unknown Conversion from Object to " + this.paramTypeString() + " for " + this.dump());
         }
      }
   }

   ParamSetException convertFromExc(String var1) {
      return new ParamSetException("Unknown conversion from " + this.currval().getClass().getName() + " to " + var1 + " for " + this.dump());
   }

   public boolean asBoolean() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asBoolean();
      } else if (this.isNull()) {
         return false;
      } else if (this.currval() instanceof Boolean) {
         return (Boolean)this.currval();
      } else if (this.currval() instanceof String) {
         return Boolean.valueOf((String)this.currval());
      } else {
         throw this.convertFromExc("boolean");
      }
   }

   public byte asByte() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asByte();
      } else if (this.isNull()) {
         return 0;
      } else if (this.currval() instanceof Number) {
         return (byte)((Number)this.currval()).intValue();
      } else if (this.currval() instanceof String) {
         return (byte)Integer.valueOf((String)this.currval());
      } else {
         throw this.convertFromExc("byte");
      }
   }

   public int asInt() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asInt();
      } else if (this.isNull()) {
         return 0;
      } else if (this.currval() instanceof Number) {
         return ((Number)this.currval()).intValue();
      } else if (this.currval() instanceof String) {
         return Integer.valueOf((String)this.currval());
      } else {
         throw this.convertFromExc("int");
      }
   }

   public short asShort() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asShort();
      } else if (this.isNull()) {
         return 0;
      } else if (this.currval() instanceof Number) {
         return (short)((Number)this.currval()).intValue();
      } else if (this.currval() instanceof String) {
         return Short.valueOf((String)this.currval());
      } else {
         throw this.convertFromExc("short");
      }
   }

   public long asLong() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asLong();
      } else if (this.isNull()) {
         return 0L;
      } else if (this.currval() instanceof Number) {
         return ((Number)this.currval()).longValue();
      } else if (this.currval() instanceof String) {
         return Long.valueOf((String)this.currval());
      } else {
         throw this.convertFromExc("long");
      }
   }

   public double asDouble() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asDouble();
      } else if (this.isNull()) {
         return 0.0;
      } else if (this.currval() instanceof Number) {
         return (double)((Number)this.currval()).floatValue();
      } else if (this.currval() instanceof String) {
         return Double.valueOf((String)this.currval());
      } else {
         throw this.convertFromExc("double");
      }
   }

   public float asFloat() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asFloat();
      } else if (this.isNull()) {
         return 0.0F;
      } else if (this.currval() instanceof Number) {
         return ((Number)this.currval()).floatValue();
      } else if (this.currval() instanceof String) {
         return Float.valueOf((String)this.currval());
      } else {
         throw this.convertFromExc("float");
      }
   }

   public char asChar() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asChar();
      } else if (this.isNull()) {
         return ' ';
      } else if (this.currval() instanceof Character) {
         return (Character)this.currval();
      } else {
         if (this.currval() instanceof String) {
            String var1 = (String)this.currval();
            if (var1.length() == 1) {
               return var1.charAt(0);
            }

            if (var1.length() == 0) {
               return ' ';
            }
         }

         throw this.convertFromExc("char");
      }
   }

   public String asString() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asString();
      } else {
         return this.isNull() ? "" : this.currval().toString();
      }
   }

   public String toString() {
      try {
         return this.isVector() ? this.valuevect.toString() : this.currval().toString();
      } catch (Exception var2) {
         return "";
      }
   }

   public String dump() {
      return "" + this.name() + "/" + this.paramTypeString() + "/" + this.paramModeString() + "/" + this.paramDesc() + "/" + (this.currval() != null ? this.currval().toString() : (this.valuevect != null ? this.valuevect.toString() : ""));
   }

   public Date asDate() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asDate();
      } else if (this.isNull()) {
         return null;
      } else if (this.currval() instanceof Date) {
         return (Date)this.currval();
      } else if (this.currval() instanceof String) {
         return new Date((String)this.currval());
      } else {
         throw this.convertFromExc("Date");
      }
   }

   public Object asObject() throws ParamSetException {
      if (this.isVector()) {
         return this.elementAt(0).asObject();
      } else {
         return this.isNull() ? null : this.currval();
      }
   }
}
