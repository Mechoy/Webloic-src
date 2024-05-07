package weblogic.jms.dotnet.proxy.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class PrimitiveMap implements Map<String, Object>, MarshalWritable, MarshalReadable {
   private static final int EXTVERSION = 1;
   private MarshalBitMask versionFlags;
   private static final short TYPENULL = 99;
   private static final short TYPESTRING = 102;
   private static final short TYPEBOOLEAN = 103;
   private static final short TYPEINTEGER = 104;
   private static final short TYPELONG = 105;
   private static final short TYPEBYTE = 106;
   private static final short TYPESHORT = 107;
   private static final short TYPEFLOAT = 108;
   private static final short TYPEDOUBLE = 109;
   private static final short TYPECHARACTER = 110;
   private static final short TYPEBYTEARRAY = 111;
   private static final short TYPEBIGSTRING = 112;
   private Map<String, Object> map = new HashMap();

   public void clear() {
      this.map.clear();
   }

   public boolean containsKey(Object var1) {
      return this.map.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this.map.containsValue(var1);
   }

   public Set<Map.Entry<String, Object>> entrySet() {
      return this.map.entrySet();
   }

   public Object get(Object var1) {
      return this.map.get(var1);
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public Set<String> keySet() {
      return this.map.keySet();
   }

   public Object put(String var1, Object var2) {
      this.checkValidType(var2);
      return this.map.put(var1, var2);
   }

   private void checkValidType(Object var1) {
      if (var1 != null && !(var1 instanceof byte[]) && !(var1 instanceof String) && !(var1 instanceof Boolean) && !(var1 instanceof Character) && !(var1 instanceof Byte) && !(var1 instanceof Short) && !(var1 instanceof Integer) && !(var1 instanceof Long) && !(var1 instanceof Float) && !(var1 instanceof Double)) {
         throw new IllegalArgumentException("Passed value object (Class = " + var1.getClass().getName() + ") is not allowed primitive type");
      }
   }

   public void putAll(Map<? extends String, ? extends Object> var1) {
      if (var1 == null || !var1.isEmpty()) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.put((String)var3.getKey(), var3.getValue());
         }
      }

   }

   public Object remove(Object var1) {
      return this.map.remove(var1);
   }

   public int size() {
      return this.map.size();
   }

   public Collection<Object> values() {
      return this.map.values();
   }

   public int getMarshalTypeCode() {
      return 0;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeInt(this.map.size());
      Iterator var2 = this.map.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.writeShort((short)102);
         var1.writeString((String)var3.getKey());
         if (var3.getValue() == null) {
            var1.writeShort((short)99);
         } else if (var3.getValue() instanceof String) {
            var1.writeShort((short)102);
            var1.writeString((String)var3.getValue());
         } else if (var3.getValue() instanceof Integer) {
            var1.writeShort((short)104);
            var1.writeInt((Integer)var3.getValue());
         } else if (var3.getValue() instanceof Long) {
            var1.writeShort((short)105);
            var1.writeLong((Long)var3.getValue());
         } else if (var3.getValue() instanceof byte[]) {
            var1.writeShort((short)111);
            byte[] var4 = (byte[])((byte[])var3.getValue());
            var1.writeInt(var4.length);
            var1.write(var4, 0, var4.length);
         } else if (var3.getValue() instanceof Boolean) {
            var1.writeShort((short)103);
            var1.writeBoolean((Boolean)var3.getValue());
         } else if (var3.getValue() instanceof Byte) {
            var1.writeShort((short)106);
            var1.writeByte((Byte)var3.getValue());
         } else if (var3.getValue() instanceof Short) {
            var1.writeShort((short)107);
            var1.writeShort((Short)var3.getValue());
         } else if (var3.getValue() instanceof Float) {
            var1.writeShort((short)108);
            var1.writeFloat((Float)var3.getValue());
         } else if (var3.getValue() instanceof Double) {
            var1.writeShort((short)109);
            var1.writeDouble((Double)var3.getValue());
         } else {
            if (!(var3.getValue() instanceof Character)) {
               throw new IllegalArgumentException("Invalid value type " + var3.getValue().getClass().getName() + " found");
            }

            var1.writeShort((short)110);
            var1.writeChar((Character)var3.getValue());
         }
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);

      for(int var2 = var1.readInt(); var2 != 0; --var2) {
         String var3 = (String)this.readPrimitiveType(var1);
         Object var4 = this.readPrimitiveType(var1);
         this.map.put(var3, var4);
      }

   }

   private Object readPrimitiveType(MarshalReader var1) {
      short var2 = var1.readShort();
      switch (var2) {
         case 99:
            return null;
         case 100:
         case 101:
         default:
            throw new IllegalArgumentException("Invalid value type code " + var2 + " found");
         case 102:
            return var1.readString();
         case 103:
            return var1.readBoolean();
         case 104:
            return var1.readInt();
         case 105:
            return var1.readLong();
         case 106:
            return var1.readByte();
         case 107:
            return var1.readShort();
         case 108:
            return var1.readFloat();
         case 109:
            return var1.readDouble();
         case 110:
            return var1.readChar();
         case 111:
            int var3 = var1.readInt();
            byte[] var4 = new byte[var3];
            var1.read(var4, 0, var3);
            return var4;
      }
   }
}
