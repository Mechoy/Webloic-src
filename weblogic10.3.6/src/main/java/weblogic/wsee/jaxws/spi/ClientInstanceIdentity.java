package weblogic.wsee.jaxws.spi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;

public class ClientInstanceIdentity implements Serializable {
   private static final Logger LOGGER = Logger.getLogger(ClientInstanceIdentity.class.getName());
   private static final long serialVersionUID = 1L;
   private String _clientId;
   private Type _type;
   private Serializable _extraId;
   private String _id;

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.readObject();
      var1.defaultReadObject();
   }

   protected ClientInstanceIdentity(String var1, Type var2, Serializable var3) {
      this._clientId = var1;
      this._type = var2;
      this._extraId = var3;
      this.calculateId();
   }

   private void calculateId() {
      switch (this._type) {
         case SIMPLE:
            this._id = this._clientId + "::" + this._extraId;
            break;
         case POOLED:
            this._id = this._clientId + "-" + this._extraId;
            break;
         case CONVERSATIONAL:
            this._id = this._clientId + "|" + this._extraId;
      }

   }

   protected ClientInstanceIdentity(ClientInstanceIdentity var1, Serializable var2) {
      this._clientId = var1._clientId;
      this._type = var1._type;
      this._extraId = var2;
      this.calculateId();
   }

   public Serializable getExtraId() {
      return this._extraId;
   }

   public String getClientId() {
      return this._clientId;
   }

   public Type getType() {
      return this._type;
   }

   public String getId() {
      return this._id;
   }

   public int hashCode() {
      return this._id.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof ClientInstanceIdentity && ((ClientInstanceIdentity)var1)._id.equals(this._id);
   }

   public String toString() {
      return this._id;
   }

   public static enum Type {
      SIMPLE,
      POOLED,
      CONVERSATIONAL;
   }
}
