package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public final class ServiceRetract implements Externalizable {
   private static final long serialVersionUID = 1117899013903986756L;
   private int id;
   private boolean ignoreRetract;

   public ServiceRetract(int var1) {
      this.id = var1;
   }

   public ServiceRetract(int var1, boolean var2) {
      this(var1);
      this.ignoreRetract = var2;
   }

   public int id() {
      return this.id;
   }

   public boolean ignoreRetract() {
      return this.ignoreRetract;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.id);
      var1.writeBoolean(this.ignoreRetract);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.id = var1.readInt();
      this.ignoreRetract = var1.readBoolean();
   }

   public ServiceRetract() {
   }
}
