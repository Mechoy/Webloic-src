package weblogic.common.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import weblogic.common.WLObjectInput;
import weblogic.j2ee.ApplicationManager;
import weblogic.utils.AssertionError;
import weblogic.utils.io.DataIO;
import weblogic.utils.io.DelegatingInputStream;
import weblogic.utils.io.Replacer;

public class WLObjectInputStream extends ObjectInputStream implements WLObjectInput {
   private final DelegatingInputStream delegate;
   private Replacer replacer;

   public WLObjectInputStream(InputStream var1) throws IOException {
      this(new DelegatingInputStream(var1));
   }

   private WLObjectInputStream(DelegatingInputStream var1) throws IOException {
      super(var1);
      this.replacer = null;

      try {
         this.enableResolveObject(true);
      } catch (SecurityException var3) {
      }

      this.delegate = var1;
   }

   public final void setDelegate(InputStream var1) {
      this.delegate.setDelegate(var1);
   }

   protected final Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
      String var2 = null;

      try {
         var2 = this.readUTF();
      } catch (OptionalDataException var4) {
      }

      return ApplicationManager.loadClass(var1.getName(), var2, this.useInterAppClassLoader());
   }

   protected boolean useInterAppClassLoader() {
      return false;
   }

   protected Class resolveProxyClass(String[] var1) throws IOException, ClassNotFoundException {
      String var2 = null;

      try {
         var2 = this.readUTF();
      } catch (OptionalDataException var4) {
      }

      return ProxyClassResolver.resolveProxyClass(var1, var2, (String)null, this.useInterAppClassLoader());
   }

   public String readUTF() throws IOException {
      return DataIO.readUTF(this);
   }

   public final void setReplacer(Replacer var1) {
      this.replacer = var1;
   }

   protected Object resolveObject(Object var1) throws IOException {
      return this.replacer == null ? var1 : this.replacer.resolveObject(var1);
   }

   protected final void readStreamHeader() throws IOException {
   }

   public final void reset() throws IOException {
   }

   public final void close() throws IOException {
   }

   public Object readObjectWL() throws IOException, ClassNotFoundException {
      return this.readObject();
   }

   public final String readString() throws IOException {
      byte var1 = this.readByte();
      return var1 == 112 ? null : this.readUTF();
   }

   public final Date readDate() throws IOException {
      try {
         return (Date)this.readObject();
      } catch (ClassNotFoundException var2) {
         throw new AssertionError("Couldn't find class", var2);
      }
   }

   public final ArrayList readArrayList() throws IOException, ClassNotFoundException {
      return (ArrayList)this.readObject();
   }

   public final Properties readProperties() throws IOException {
      try {
         return (Properties)this.readObject();
      } catch (ClassNotFoundException var2) {
         throw new AssertionError("Couldn't find class", var2);
      }
   }

   public final byte[] readBytes() throws IOException {
      try {
         return (byte[])((byte[])this.readObject());
      } catch (ClassNotFoundException var2) {
         throw new AssertionError("Couldn't find class", var2);
      }
   }

   public final Object[] readArrayOfObjects() throws IOException, ClassNotFoundException {
      return (Object[])((Object[])this.readObject());
   }

   public Object readImmutable() throws IOException, ClassNotFoundException {
      return this.readObject();
   }

   public final String readAbbrevString() throws IOException {
      return this.readString();
   }
}
