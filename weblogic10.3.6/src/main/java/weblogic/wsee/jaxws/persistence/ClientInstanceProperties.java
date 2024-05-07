package weblogic.wsee.jaxws.persistence;

import com.sun.istack.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.persistence.AbstractStorable;

public class ClientInstanceProperties extends AbstractStorable {
   private static final long serialVersionUID = 1L;
   private Map<String, Serializable> _propertyMap = new HashMap();
   private transient ClientInstance<?> _clientInstance;

   /** @deprecated */
   public static ClientInstancePropertiesStore getStoreMap() {
      String var0 = WebServiceMBeanFactory.getInstance().getWebServicePersistence().getDefaultLogicalStoreName();
      return getStoreMap(var0);
   }

   public static ClientInstancePropertiesStore getStoreMap(String var0) {
      try {
         return ClientInstancePropertiesStore.getStore(var0);
      } catch (Exception var2) {
         throw new RuntimeException(var2.toString(), var2);
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
   }

   public ClientInstanceProperties(@NotNull ClientInstanceIdentity var1) {
      super(var1);
   }

   @NotNull
   public ClientInstanceIdentity getId() {
      return (ClientInstanceIdentity)this.getObjectId();
   }

   @NotNull
   public Map<String, Serializable> getPropertyMap() {
      return this._propertyMap;
   }

   public ClientInstance<?> getClientInstance() {
      return this._clientInstance;
   }

   public void setClientInstance(ClientInstance<?> var1) {
      this._clientInstance = var1;
   }

   @NotNull
   public Serializable getObjectId() {
      return this.getId();
   }
}
