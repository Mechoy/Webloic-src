package weblogic.common.internal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import weblogic.common.WLObjectOutput;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelStream;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.io.DataIO;
import weblogic.utils.io.DelegatingOutputStream;
import weblogic.utils.io.Replacer;

public class WLObjectOutputStream extends ObjectOutputStream implements WLObjectOutput, ServerChannelStream {
   private final DelegatingOutputStream delegate;
   private Replacer replacer;
   private ServerChannel channel;

   public WLObjectOutputStream(OutputStream var1) throws IOException {
      this(new DelegatingOutputStream(var1));
   }

   private WLObjectOutputStream(DelegatingOutputStream var1) throws IOException {
      super(var1);
      this.replacer = null;
      this.channel = null;

      try {
         this.enableReplaceObject(true);
      } catch (SecurityException var3) {
      }

      this.delegate = var1;
   }

   public final void setDelegate(OutputStream var1) {
      this.delegate.setDelegate(var1);
   }

   protected final void annotateClass(Class var1) throws IOException {
      ClassLoader var2 = var1.getClassLoader();
      if (var2 instanceof GenericClassLoader) {
         GenericClassLoader var3 = (GenericClassLoader)var2;
         String var4 = var3.getAnnotation().getAnnotationString();
         if (var4 != null) {
            this.writeUTF(var4);
         } else {
            this.writeUTF("");
         }
      } else {
         this.writeUTF("");
      }

   }

   protected void annotateProxyClass(Class var1) throws IOException {
      this.annotateClass(var1);
   }

   public void writeUTF(String var1) throws IOException {
      DataIO.writeUTF(this, var1);
   }

   public final void setReplacer(Replacer var1) {
      this.replacer = var1;
   }

   public final void setServerChannel(ServerChannel var1) {
      this.channel = var1;
   }

   protected Object replaceObject(Object var1) throws IOException {
      return this.replacer == null ? var1 : this.replacer.replaceObject(var1);
   }

   protected final void writeStreamHeader() throws IOException {
   }

   public void writeObjectWL(Object var1) throws IOException {
      this.writeObject(var1);
   }

   public final void writeString(String var1) throws IOException {
      this.flush();
      if (var1 == null) {
         this.writeByte(112);
      } else {
         this.writeByte(116);
         this.writeUTF(var1);
      }

   }

   public final void writeDate(Date var1) throws IOException {
      this.writeObject(var1);
   }

   public final void writeArrayList(ArrayList var1) throws IOException {
      this.writeObject(var1);
   }

   public final void writeProperties(Properties var1) throws IOException {
      this.writeObject(var1);
   }

   public final void writeBytes(byte[] var1) throws IOException {
      this.writeObject(var1);
   }

   public final void writeBytes(byte[] var1, int var2, int var3) throws IOException {
      byte[] var4 = new byte[var3];
      System.arraycopy(var1, var2, var4, 0, var3);
      this.writeObject(var4);
   }

   public final void writeArrayOfObjects(Object[] var1) throws IOException {
      this.writeObject(var1);
   }

   public void writeImmutable(Object var1) throws IOException {
      this.writeObject(var1);
   }

   public final void writeAbbrevString(String var1) throws IOException {
      this.writeString(var1);
   }

   public ServerChannel getServerChannel() {
      return this.channel;
   }
}
