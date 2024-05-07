package weblogic.wsee.jaxws.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PersistentObject implements Serializable {
   private static final long serialVersionUID = 1L;
   private PersistentContext _context;
   private Serializable _obj;

   public PersistentObject(Serializable var1, PersistentContext var2) {
      this._obj = var1;
      this._context = var2;
   }

   public PersistentContext getContext() {
      return this._context;
   }

   public Serializable getObj() {
      return this._obj;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      var1.writeObject(this._obj);
      var1.writeObject(this._context);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.readObject();
      this._obj = (Serializable)var1.readObject();
      this._context = (PersistentContext)var1.readObject();
   }
}
