package weblogic.wsee.jaxrpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.rpc.encoding.TypeMappingRegistry;
import weblogic.xml.schema.binding.internal.TypeMappingBase;

public class TypeMappingRegistryImpl implements TypeMappingRegistry, Serializable {
   private Map typeMappings = new HashMap();
   private TypeMapping defaultTypeMapping = null;

   public void registerDefault(TypeMapping var1) {
      this.defaultTypeMapping = var1;
      this.register("http://schemas.xmlsoap.org/soap/encoding/", var1);
      this.register("http://www.w3.org/2003/05/soap-encoding", var1);
   }

   public TypeMapping register(String var1, TypeMapping var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("mapping cannot be null");
      } else {
         this.typeMappings.put(var1, var2);
         return var2;
      }
   }

   public TypeMapping getDefaultTypeMapping() {
      return this.defaultTypeMapping != null ? this.defaultTypeMapping : this.getTypeMapping("http://schemas.xmlsoap.org/soap/encoding/");
   }

   public boolean removeTypeMapping(TypeMapping var1) {
      if (var1 == null) {
         return false;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.typeMappings.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            if (var1.equals(var4.getValue())) {
               var2.add(var4.getKey());
            }
         }

         var3 = var2.iterator();

         while(var3.hasNext()) {
            this.typeMappings.remove(var3.next());
         }

         return !var2.isEmpty();
      }
   }

   public TypeMapping unregisterTypeMapping(String var1) {
      return (TypeMapping)this.typeMappings.remove(var1);
   }

   public String[] getRegisteredNamespaces() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.typeMappings.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.add(var3);
      }

      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   public void register(TypeMapping var1, String[] var2) throws JAXRPCException {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.typeMappings.put(var2[var3], var1);
      }

   }

   public void clear() {
      this.typeMappings.clear();
   }

   public TypeMapping createTypeMapping() {
      return new TypeMappingBase();
   }

   public TypeMapping removeTypeMapping(String var1) {
      return (TypeMapping)this.typeMappings.remove(var1);
   }

   public Iterator getAllTypeMappings() {
      return this.typeMappings.values().iterator();
   }

   public String[] getSupportedNamespaces() {
      return (String[])((String[])this.typeMappings.keySet().toArray(new String[0]));
   }

   public TypeMapping getTypeMapping(String var1) {
      return (TypeMapping)this.typeMappings.get(var1);
   }

   public String[] getRegisteredEncodingStyleURIs() {
      return this.getSupportedNamespaces();
   }
}
