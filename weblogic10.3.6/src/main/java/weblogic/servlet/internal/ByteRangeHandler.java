package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.Source;
import weblogic.utils.io.Chunk;

public abstract class ByteRangeHandler {
   protected static final boolean debug = false;
   protected static final String RANGE = "Range";
   protected Source source;
   protected String contentType;

   public ByteRangeHandler(Source var1, String var2) {
      this.source = var1;
      this.contentType = var2;
   }

   public abstract void sendRangeData(HttpServletResponse var1) throws IOException;

   public static ByteRangeHandler makeInstance(Source var0, HttpServletRequest var1, String var2) {
      long var3 = var0.length();
      String var5 = var2 == null ? "text/plain" : var2;
      Enumeration var6 = var1.getHeaders("Range");
      if (var6 == null) {
         return null;
      } else {
         List var7 = parseByteRanges(var6, var3);
         if (var7 != null && var7.size() != 0) {
            if (var7.size() == 1) {
               ByteRangeInfo var10 = (ByteRangeInfo)var7.get(0);
               SingleByteRangeHandler var11 = new SingleByteRangeHandler(var0, var5, var10);
               return var11;
            } else if (var7.size() > 1) {
               String var8 = var1.getHeader("Request-Range");
               MultipleByteRangeHandler var9 = new MultipleByteRangeHandler(var0, var5, var7);
               if (var8 != null) {
                  ((MultipleByteRangeHandler)var9).setRequestRange(true);
               }

               return var9;
            } else {
               return null;
            }
         } else {
            return new UnsatisfiableRangeHandler(var0, var5);
         }
      }
   }

   public void write(InputStream var1, OutputStream var2, long var3) throws IOException {
      try {
         ((ServletOutputStreamImpl)var2).writeStream(var1, (int)var3);
      } catch (ClassCastException var6) {
         this.writeDirectly(var1, var2, var3);
      }

   }

   private void writeDirectly(InputStream var1, OutputStream var2, long var3) throws IOException {
      Chunk var5 = Chunk.getChunk();
      int var6 = (int)var3;
      boolean var7 = false;

      try {
         while(var6 > 0) {
            if (var5.end == Chunk.CHUNK_SIZE) {
               var2.write(var5.buf, 0, var5.end);
               var5.end = 0;
            }

            int var8 = Math.min(var6, Chunk.CHUNK_SIZE - var5.end);
            int var13;
            if ((var13 = var1.read(var5.buf, var5.end, var8)) == -1) {
               break;
            }

            var5.end += var13;
            var6 -= var13;
         }

         if (var5.end > 0) {
            var2.write(var5.buf, 0, var5.end);
            var6 -= var5.end;
         }

         if (var6 > 0) {
            throw new IOException("Failed to read '" + var6 + "' bytes from InputStream");
         }
      } finally {
         Chunk.releaseChunk(var5);
      }

   }

   public static List parseByteRanges(Enumeration var0, long var1) {
      ArrayList var3 = new ArrayList();

      while(var0.hasMoreElements()) {
         String var4 = (String)var0.nextElement();
         String[] var5 = StringUtils.splitCompletely(var4, "=,");

         for(int var6 = 0; var6 < var5.length; ++var6) {
            try {
               long var7 = -1L;
               long var9 = -1L;
               String var11 = var5[var6].trim();
               int var12 = var11.indexOf(45);
               if (var12 < 0) {
                  if (!"bytes".equals(var11)) {
                     break;
                  }
               } else {
                  ByteRangeInfo var13;
                  if (var12 == 0) {
                     if (var12 + 1 != var11.length()) {
                        var7 = Long.parseLong(var11.substring(var12 + 1));
                        var13 = makeRangeInfo(-1L, var7, var1);
                        if (var13 != null) {
                           var3.add(var13);
                        }
                     }
                  } else if (var12 + 1 < var11.length()) {
                     var9 = Long.parseLong(var11.substring(0, var12));
                     var7 = Long.parseLong(var11.substring(var12 + 1));
                     var13 = makeRangeInfo(var9, var7, var1);
                     if (var13 != null) {
                        var3.add(var13);
                     }
                  } else {
                     var9 = Long.parseLong(var11.substring(0, var12));
                     var7 = var1 - 1L;
                     var13 = makeRangeInfo(var9, var7, var1);
                     if (var13 != null) {
                        var3.add(var13);
                     }
                  }
               }
            } catch (NumberFormatException var14) {
            }
         }
      }

      return var3;
   }

   private static ByteRangeInfo makeRangeInfo(long var0, long var2, long var4) {
      if (var0 == -1L && var2 == -1L) {
         return null;
      } else if (var0 >= 0L && var2 >= 0L && var0 > var2) {
         return null;
      } else {
         if (var2 > var4) {
            var2 = var4 - 1L;
         }

         return var0 < var4 ? new ByteRangeInfo(var0, var2, var4) : null;
      }
   }

   static void p(String var0) {
      System.out.println("[ByteRangeHandler]" + var0);
   }

   protected void pp(String var1) {
      System.out.println("[" + this.getClass().getName() + "]" + var1);
   }

   static class ByteRangeInfo {
      long fromIndex;
      long toIndex;
      long total;

      public ByteRangeInfo(long var1, long var3, long var5) {
         this.fromIndex = var1;
         this.toIndex = var3;
         this.total = var5;
      }

      public String toString() {
         return "[ByteRangeInfo] fromIndex: " + this.fromIndex + " toIndex : " + this.toIndex;
      }

      public long getFromIndex() {
         if (this.fromIndex < 0L) {
            long var1 = this.total - this.toIndex;
            if (var1 < 0L) {
               var1 = 0L;
            }

            return var1;
         } else {
            return this.fromIndex;
         }
      }

      public long getToIndex() {
         if (this.fromIndex < 0L) {
            return this.total - 1L;
         } else {
            return this.toIndex >= 0L && this.toIndex < this.total ? this.toIndex : this.total - 1L;
         }
      }

      public String toHeader() {
         return "bytes " + this.getFromIndex() + '-' + this.getToIndex() + '/' + this.total;
      }
   }

   static class UnsatisfiableRangeHandler extends ByteRangeHandler {
      UnsatisfiableRangeHandler(Source var1, String var2) {
         super(var1, var2);
      }

      public void sendRangeData(HttpServletResponse var1) throws IOException {
         InputStream var2 = null;

         try {
            long var3 = this.source.length();
            var1.setStatus(416);
            if (var1 instanceof ServletResponseImpl) {
               ((ServletResponseImpl)var1).setHeaderInternal("Content-Range", "bytes */" + var3);
            } else {
               var1.setHeader("Content-Range", "bytes */" + var3);
            }

            if (var3 != -1L) {
               var1.setContentLength((int)var3);
            }

            if (this.contentType != null) {
               var1.setContentType(this.contentType);
            }

            var2 = this.source.getInputStream();
            this.write(var2, var1.getOutputStream(), (long)((int)var3));
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }

      }
   }

   static class SingleByteRangeHandler extends ByteRangeHandler {
      ByteRangeInfo info;

      SingleByteRangeHandler(Source var1, String var2, ByteRangeInfo var3) {
         super(var1, var2);
         this.info = var3;
      }

      public void sendRangeData(HttpServletResponse var1) throws IOException {
         InputStream var2 = null;

         try {
            long var3 = this.info.getToIndex() - this.info.getFromIndex() + 1L;
            var1.setStatus(206);
            if (var1 instanceof ServletResponseImpl) {
               ((ServletResponseImpl)var1).setHeaderInternal("Content-Range", this.info.toHeader());
            } else {
               var1.setHeader("Content-Range", this.info.toHeader());
            }

            var1.setContentType(this.contentType);
            ServletOutputStream var5 = var1.getOutputStream();
            var2 = this.source.getInputStream();
            long var6 = this.info.getFromIndex();
            if (var6 > 0L) {
               var2.skip(var6);
            }

            this.write(var2, var5, var3);
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }

      }
   }

   static class MultipleByteRangeHandler extends ByteRangeHandler {
      static final String SEPERATOR = "--";
      static final String CRLF = "\r\n";
      private String boundary;
      List rangeList;
      boolean requestRange;

      MultipleByteRangeHandler(Source var1, String var2, List var3) {
         super(var1, var2);
         this.rangeList = var3;
         this.boundary = System.currentTimeMillis() + "WLS";
      }

      public void setRequestRange(boolean var1) {
         this.requestRange = var1;
      }

      public boolean hasRequestRange() {
         return this.requestRange;
      }

      public void sendRangeData(HttpServletResponse var1) throws IOException {
         ServletOutputStream var2 = var1.getOutputStream();
         var1.setStatus(206);
         String var3 = null;
         if (this.hasRequestRange()) {
            var3 = "multipart/x-byteranges; boundary=";
         } else {
            var3 = "multipart/byteranges; boundary=";
         }

         var1.addHeader("Content-Type", var3 + this.boundary);
         InputStream var4 = this.source.getInputStream();
         if (var4 == null) {
            var1.sendError(404);
         } else {
            Iterator var5 = this.rangeList.iterator();
            long var6 = 0L;

            while(var5.hasNext()) {
               ByteRangeInfo var8 = (ByteRangeInfo)var5.next();
               long var9 = var8.getFromIndex();
               long var11 = var8.getToIndex() - var8.getFromIndex() + 1L;
               String var13 = this.getStartRange(var8);
               var2.write(var13.getBytes());
               if (var6 < var9) {
                  var4.skip(var9 - var6);
               } else if (var6 > var9) {
                  if (var4 != null) {
                     var4.close();
                  }

                  var4 = this.source.getInputStream();
                  var4.skip(var9);
               }

               var6 = var9 + var11;
               this.write(var4, var2, var11);
               var2.write(this.getEndRangeHeader().getBytes());
            }

            if (var4 != null) {
               var4.close();
            }

            var2.write(this.getFinalRangeHeader().getBytes());
         }
      }

      private String getStartRange(ByteRangeInfo var1) {
         return "--" + this.boundary + "\r\n" + "Content-Type: " + this.contentType + "\r\n" + "Content-Range: " + var1.toHeader() + "\r\n" + "\r\n";
      }

      private String getEndRangeHeader() {
         return "\r\n";
      }

      private String getFinalRangeHeader() {
         return "--" + this.boundary + "--" + "\r\n";
      }
   }
}
