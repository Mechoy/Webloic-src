package weblogic.wsee.jaxws.framework;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableAddressingVersion implements Serializable {
   private static final long serialVersionUID = 1L;
   @NotNull
   private AddressingVersion _addrVersion;

   public SerializableAddressingVersion(@NotNull AddressingVersion var1) {
      this._addrVersion = var1;
   }

   @NotNull
   public AddressingVersion getAddressingVersion() {
      return this._addrVersion;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this._addrVersion.nsUri);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      String var2 = (String)var1.readObject();
      this._addrVersion = AddressingVersion.fromNsUri(var2);
   }
}
