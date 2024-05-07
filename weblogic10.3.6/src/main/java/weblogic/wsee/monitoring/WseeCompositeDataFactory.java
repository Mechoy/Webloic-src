package weblogic.wsee.monitoring;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import weblogic.wsee.WseeCoreLogger;

public final class WseeCompositeDataFactory {
   private WseeCompositeType _type;
   private CompositeType _compositeType;

   public WseeCompositeDataFactory(Class var1) throws IntrospectionException, OpenDataException {
      this._type = new WseeCompositeType(var1);
      this._compositeType = this._type.generateCompositeType();
   }

   public CompositeType getCompositeType() {
      return this._compositeType;
   }

   public CompositeData createCompositeData(Object var1) throws OpenDataException {
      TreeMap var2 = new TreeMap();
      Iterator var3 = this._type._propMap.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         PropertyDescriptor var5 = (PropertyDescriptor)this._type._propMap.get(var4);
         Method var6 = var5.getReadMethod();

         try {
            Object var7 = var6.invoke(var1);
            OpenType var8 = this._compositeType.getType(var4);
            if (var8 instanceof SimpleType) {
               var2.put(var4, var7);
            } else if (!var8.isArray()) {
               if (var7 instanceof WseeCompositable) {
                  var2.put(var4, ((WseeCompositable)var7).createCompositeData());
               }
            } else {
               Object[] var9 = (Object[])((Object[])var7);
               CompositeData[] var10 = new CompositeData[var9.length];
               int var11 = 0;
               Object[] var12 = var9;
               int var13 = var9.length;

               for(int var14 = 0; var14 < var13; ++var14) {
                  Object var15 = var12[var14];
                  if (var15 instanceof WseeCompositable) {
                     var10[var11++] = ((WseeCompositable)var15).createCompositeData();
                  } else {
                     var10[var11++] = null;
                  }
               }

               var2.put(var4, var10);
            }
         } catch (Exception var16) {
            WseeCoreLogger.logUnexpectedException(var16.toString(), var16);
            throw new OpenDataException(var16.toString());
         }
      }

      CompositeDataSupport var17 = new CompositeDataSupport(this._compositeType, var2);
      return var17;
   }

   public interface WseeCompositable {
      CompositeData createCompositeData() throws OpenDataException;
   }

   private static class WseeCompositeType {
      private Class _class;
      public Map<String, PropertyDescriptor> _propMap;

      public WseeCompositeType(Class var1) throws IntrospectionException {
         this(var1, (Class)null);
      }

      public WseeCompositeType(Class var1, Class var2) throws IntrospectionException {
         this._class = var1;
         this._propMap = new TreeMap();
         BeanInfo var3;
         if (var2 == null) {
            var3 = Introspector.getBeanInfo(this._class);
         } else {
            var3 = Introspector.getBeanInfo(this._class, var2);
         }

         PropertyDescriptor[] var4 = var3.getPropertyDescriptors();
         PropertyDescriptor[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PropertyDescriptor var8 = var5[var7];
            this._propMap.put(var8.getName(), var8);
         }

      }

      public CompositeType generateCompositeType() throws IntrospectionException, OpenDataException {
         String var1 = this._class.getName();
         String var2 = this._class.getName();
         String[] var3 = new String[this._propMap.size()];
         String[] var4 = new String[this._propMap.size()];
         OpenType[] var5 = new OpenType[this._propMap.size()];
         int var6 = 0;

         for(Iterator var7 = this._propMap.keySet().iterator(); var7.hasNext(); ++var6) {
            String var8 = (String)var7.next();
            PropertyDescriptor var9 = (PropertyDescriptor)this._propMap.get(var8);
            var3[var6] = var8;
            var4[var6] = var8;
            Object var10 = this.getSimpleType(var9.getPropertyType());
            if (var10 == null) {
               if (var9.getPropertyType().isArray()) {
                  Class var11 = var9.getPropertyType().getComponentType();
                  Object var12 = this.getSimpleType(var11);
                  if (var12 == null) {
                     if (CompositeData.class.isAssignableFrom(var11)) {
                        throw new IllegalArgumentException("CompositeData not supported as a field type in another CompositeType");
                     }

                     WseeCompositeType var13 = new WseeCompositeType(var11);
                     var12 = var13.generateCompositeType();
                  }

                  var10 = new ArrayType(1, (OpenType)var12);
               } else {
                  if (CompositeData.class.isAssignableFrom(var9.getPropertyType())) {
                     throw new IllegalArgumentException("CompositeData not supported as a field type in another CompositeType");
                  }

                  WseeCompositeType var14 = new WseeCompositeType(var9.getPropertyType());
                  var10 = var14.generateCompositeType();
               }
            }

            var5[var6] = (OpenType)var10;
         }

         return new CompositeType(var1, var2, var3, var4, var5);
      }

      private SimpleType<?> getSimpleType(Class var1) {
         if (var1 != Byte.TYPE && var1 != Byte.TYPE) {
            if (var1 != Short.TYPE && var1 != Short.class) {
               if (var1 != Integer.TYPE && var1 != Integer.class) {
                  if (var1 != Long.TYPE && var1 != Long.class) {
                     if (var1 != Float.TYPE && var1 != Float.class) {
                        if (var1 != Character.TYPE && var1 != Character.class) {
                           if (var1 == String.class) {
                              return SimpleType.STRING;
                           } else {
                              return var1 != Boolean.TYPE && var1 != Boolean.class ? null : SimpleType.BOOLEAN;
                           }
                        } else {
                           return SimpleType.CHARACTER;
                        }
                     } else {
                        return SimpleType.FLOAT;
                     }
                  } else {
                     return SimpleType.LONG;
                  }
               } else {
                  return SimpleType.INTEGER;
               }
            } else {
               return SimpleType.SHORT;
            }
         } else {
            return SimpleType.BYTE;
         }
      }
   }
}
