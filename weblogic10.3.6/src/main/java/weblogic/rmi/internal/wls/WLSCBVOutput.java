package weblogic.rmi.internal.wls;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.rmi.extensions.server.CBVOutputStream;
import weblogic.rmi.extensions.server.ColocatedStream;
import weblogic.rmi.internal.CBVOutput;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkOutput;
import weblogic.utils.io.Immutable;
import weblogic.utils.io.StringOutput;

public class WLSCBVOutput extends WLObjectOutputStream implements CBVOutput, ChunkOutput, StringOutput, ColocatedStream, ObjectOutput {
   private CBVOutputStream cbvos;

   public WLSCBVOutput(CBVOutputStream var1, OutputStream var2) throws IOException {
      super(var2);
      this.cbvos = var1;
   }

   public void setDelegate(CBVOutputStream var1, OutputStream var2) {
      this.cbvos = var1;
      super.setDelegate(var2);
   }

   public void writeUTF(String var1) throws IOException {
      this.cbvos.writeObjectSpecial(var1);
   }

   public void writeUTF8(String var1) throws IOException {
      this.cbvos.writeObjectSpecial(var1);
   }

   public void writeASCII(String var1) throws IOException {
      this.cbvos.writeObjectSpecial(var1);
   }

   public void writeChunks(Chunk var1) throws IOException {
      this.cbvos.writeObjectSpecial(var1);
   }

   public final void writeImmutable(Object var1) throws IOException {
      this.cbvos.writeObjectSpecial(var1);
   }

   protected Object replaceObject(Object var1) throws IOException {
      var1 = super.replaceObject(var1);
      return var1 instanceof Immutable ? new ImmutableWrapper(var1) : var1;
   }

   protected void writeClassDescriptor(ObjectStreamClass var1) throws IOException {
      this.cbvos.writeObjectSpecial(var1);
   }

   public void close() throws IOException {
      this.cbvos = null;
      super.close();
   }

   private static final class ImmutableWrapper implements Immutable, Externalizable {
      private static final long serialVersionUID = -7325914689974638362L;
      private Object obj;

      public ImmutableWrapper() {
      }

      private ImmutableWrapper(Object var1) {
         this.obj = var1;
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         ((WLObjectOutput)var1).writeImmutable(this.obj);
      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this.obj = ((WLObjectInput)var1).readImmutable();
      }

      public Object readResolve() throws ObjectStreamException {
         return this.obj;
      }

      // $FF: synthetic method
      ImmutableWrapper(Object var1, Object var2) {
         this(var1);
      }
   }
}
