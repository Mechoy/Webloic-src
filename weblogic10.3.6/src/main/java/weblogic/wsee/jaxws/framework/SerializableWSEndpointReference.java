package weblogic.wsee.jaxws.framework;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableWSEndpointReference implements Serializable {
   private static final long serialVersionUID = 1L;
   @Nullable
   private WSEndpointReference _ref;
   @NotNull
   private SerializableAddressingVersion _addrVersion;

   public SerializableWSEndpointReference(@Nullable WSEndpointReference var1, @NotNull AddressingVersion var2) {
      this._ref = var1;
      this._addrVersion = new SerializableAddressingVersion(var2);
   }

   @Nullable
   public WSEndpointReference getRef() {
      return this._ref;
   }

   @NotNull
   public AddressingVersion getAddressingVersion() {
      return this._addrVersion != null ? this._addrVersion.getAddressingVersion() : null;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this._addrVersion);
      WsUtil.serializeWSEndpointReference(this._ref, var1);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this._addrVersion = (SerializableAddressingVersion)var1.readObject();
      this._ref = WsUtil.deserializeWSEndpointReference(var1, this.getAddressingVersion());
   }
}
