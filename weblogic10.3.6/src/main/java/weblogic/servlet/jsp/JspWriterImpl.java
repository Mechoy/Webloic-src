package weblogic.servlet.jsp;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;
import weblogic.servlet.ServletResponseAttributeEvent;
import weblogic.servlet.ServletResponseAttributeListener;
import weblogic.servlet.internal.ChunkOutputWrapper;
import weblogic.servlet.internal.DelegateChunkWriter;
import weblogic.servlet.internal.ServletOutputStreamImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.utils.http.BytesToString;

public final class JspWriterImpl extends JspWriter implements ByteWriter, ServletResponseAttributeListener {
   private static final String EOL = System.getProperty("line.separator");
   private ChunkOutputWrapper co = null;
   private ServletResponse response;
   private boolean isClosed;
   private String initEncoding = null;
   private boolean isEncodingSupported = false;
   private boolean is8BitEncoding = false;
   private boolean isEncodingChanged = false;
   private boolean isServletResponseWrapper = false;

   public JspWriterImpl(ServletResponse var1, int var2, boolean var3) throws IOException {
      super(var2, var3);
      this.response = var1;
      if (this.response instanceof ServletResponseImpl) {
         ServletResponseImpl var4 = (ServletResponseImpl)this.response;
         ServletOutputStreamImpl var5 = (ServletOutputStreamImpl)var4.getOutputStreamNoCheck();
         this.co = var5.getOutput();
         this.co.setAutoFlush(super.autoFlush);
         ((ServletResponseImpl)this.response).registerAttributeListener(this);
      } else if (this.response instanceof NestedBodyResponse) {
         this.co = ((NestedBodyResponse)this.response).getBodyContentImpl().getChunkOutputWrapper();
         this.co.setAutoFlush(super.autoFlush);

         ServletResponse var6;
         for(var6 = ((NestedBodyResponse)this.response).getResponse(); var6 instanceof NestedBodyResponse; var6 = ((NestedBodyResponse)var6).getResponse()) {
         }

         if (var6 instanceof ServletResponseImpl) {
            ((ServletResponseImpl)var6).registerAttributeListener(this);
         } else {
            this.isServletResponseWrapper = true;
         }
      } else {
         this.isServletResponseWrapper = true;
         DelegateChunkWriter var7 = new DelegateChunkWriter(this.response, super.autoFlush, super.bufferSize);
         this.co = new ChunkOutputWrapper(var7);
         this.co.setAutoFlush(super.autoFlush);
      }

   }

   public void clear() throws IOException {
      if (this.response.isCommitted()) {
         throw new IOException("response already committed");
      } else {
         this.co.clearBuffer();
      }
   }

   public void clearBuffer() throws IOException {
      this.co.clearBuffer();
   }

   public void flush() throws IOException {
      this.checkIsClosed();
      this.co.flush();
   }

   public void close() throws IOException {
      if (!this.isClosed) {
         this.co.flush();
         this.isClosed = true;
      }
   }

   public int getBufferSize() {
      return this.co.getBufferSize();
   }

   public int getRemaining() {
      int var1 = this.co.getBufferSize() - this.co.getCount();
      return var1 > 0 ? var1 : 0;
   }

   public boolean isAutoFlush() {
      return this.co.isAutoFlush();
   }

   public void newLine() throws IOException {
      this.checkIsClosed();
      this.co.print(EOL);
   }

   public void print(boolean var1) throws IOException {
      this.checkIsClosed();
      this.co.print(String.valueOf(var1));
   }

   public void print(char var1) throws IOException {
      this.checkIsClosed();
      this.co.write(var1);
   }

   public void print(char[] var1) throws IOException {
      this.checkIsClosed();
      this.co.write((char[])var1, 0, var1.length);
   }

   public void print(double var1) throws IOException {
      this.checkIsClosed();
      this.co.print(String.valueOf(var1));
   }

   public void print(float var1) throws IOException {
      this.checkIsClosed();
      this.co.print(String.valueOf(var1));
   }

   public void print(int var1) throws IOException {
      this.checkIsClosed();
      this.co.print(String.valueOf(var1));
   }

   public void print(long var1) throws IOException {
      this.checkIsClosed();
      this.co.print(String.valueOf(var1));
   }

   public void print(Object var1) throws IOException {
      this.checkIsClosed();
      this.co.print(String.valueOf(var1));
   }

   public void print(String var1) throws IOException {
      this.checkIsClosed();
      if (var1 == null) {
         this.co.print("null");
      } else {
         this.co.print(var1);
      }

   }

   public void println(boolean var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(char var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(char[] var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(double var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(float var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(int var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(long var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(Object var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println(String var1) throws IOException {
      this.checkIsClosed();
      this.print(var1);
      this.newLine();
   }

   public void println() throws IOException {
      this.newLine();
   }

   public void write(char[] var1) throws IOException {
      this.checkIsClosed();
      this.co.write((char[])var1, 0, var1.length);
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      this.checkIsClosed();
      this.co.write(var1, var2, var3);
   }

   public void write(int var1) throws IOException {
      this.checkIsClosed();
      this.co.write(var1);
   }

   public void write(String var1) throws IOException {
      this.checkIsClosed();
      if (var1 == null) {
         this.co.print("null");
      } else {
         this.co.print(var1);
      }

   }

   public void write(String var1, int var2, int var3) throws IOException {
      this.checkIsClosed();
      this.co.print(var1.substring(var2, var2 + var3));
   }

   public void write(byte[] var1, String var2) throws IOException {
      this.checkIsClosed();
      if (this.isEncodingSupported && !this.hasEncodingChanged()) {
         this.co.write((byte[])var1, 0, var1.length);
      } else {
         this.print(var2);
      }

   }

   public void write(ByteBuffer var1, String var2) throws IOException {
      this.checkIsClosed();
      if (this.isEncodingSupported && !this.hasEncodingChanged()) {
         this.co.write(var1);
      } else {
         this.print(var2);
      }

   }

   private boolean hasEncodingChanged() {
      if (this.isServletResponseWrapper) {
         String var1 = this.response.getCharacterEncoding();
         if (this.is8BitEncoding) {
            return !BytesToString.is8BitUnicodeSubset(var1);
         } else {
            if (var1 != null) {
               var1 = var1.intern();
            }

            return this.initEncoding != var1;
         }
      } else {
         return this.isEncodingChanged;
      }
   }

   public void setInitCharacterEncoding(String var1, boolean var2) {
      if (this.initEncoding != var1) {
         this.initEncoding = var1;
         this.is8BitEncoding = BytesToString.is8BitUnicodeSubset(this.initEncoding);
         this.isEncodingSupported = var2;
         this.checkAndSetEncodingChange(this.response.getCharacterEncoding());
      }
   }

   private void checkIsClosed() throws IOException {
      if (this.isClosed) {
         throw new IOException("Stream already closed");
      }
   }

   public void attributeChanged(ServletResponseAttributeEvent var1) {
      if (var1.getName() == "ENCODING") {
         ServletResponse var2 = var1.getResponse();
         String var3 = var2.getCharacterEncoding();
         this.checkAndSetEncodingChange(var3);
      }

   }

   private void checkAndSetEncodingChange(String var1) {
      if (this.is8BitEncoding) {
         this.isEncodingChanged = !BytesToString.is8BitUnicodeSubset(var1);
      } else {
         this.isEncodingChanged = var1 == null ? true : this.initEncoding != var1.intern();
      }

   }
}
