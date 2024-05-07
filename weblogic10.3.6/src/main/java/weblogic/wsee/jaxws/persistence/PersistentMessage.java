package weblogic.wsee.jaxws.persistence;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.BitSet;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class PersistentMessage implements Serializable {
   private static final long serialVersionUID = 1L;
   private transient Message _message;
   private transient PersistentContext _context;

   public PersistentMessage(@NotNull Message var1, @NotNull PersistentContext var2) {
      this._message = var1;
      this._context = var2;
   }

   @NotNull
   public Message getMessage() {
      return this._message;
   }

   @NotNull
   public PersistentContext getContext() {
      return this._context;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      if (this._message != null) {
         try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            XMLStreamWriter var3 = XMLStreamWriterFactory.create(var2, "UTF-8");
            this._message.writeTo(new Packet(this._message), var3);
            var3.flush();
            var3.close();
            var2.flush();
            XMLStreamWriterFactory.recycle(var3);
            byte[] var4 = var2.toByteArray();
            var1.writeInt(var4.length);
            var1.write(var4);
         } catch (Exception var5) {
            throw new IOException(var5.toString(), var5);
         }

         BitSet var6 = new BitSet(this._message.getHeaders().size());

         for(int var7 = 0; var7 < this._message.getHeaders().size(); ++var7) {
            if (this._message.getHeaders().isUnderstood(var7)) {
               var6.set(var7, true);
            }
         }

         var1.writeObject(var6);
      } else {
         var1.writeInt(0);
      }

      var1.writeObject(this._context);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.readObject();
      int var2 = var1.readInt();
      if (var2 != 0) {
         try {
            byte[] var3 = new byte[var2];
            var1.readFully(var3);
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
            XMLStreamReader var5 = XMLStreamReaderFactory.create((String)null, var4, "UTF-8", false);
            this._message = Messages.create(var5);
         } catch (Exception var6) {
            throw new IOException(var6.toString(), var6);
         }

         BitSet var7 = (BitSet)var1.readObject();

         for(int var8 = 0; var8 < var7.length(); ++var8) {
            if (var7.get(var8)) {
               this._message.getHeaders().understood(var8);
            }
         }
      } else {
         this._message = null;
      }

      this._context = (PersistentContext)var1.readObject();
   }
}
