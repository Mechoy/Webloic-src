package weblogic.deploy.service.datatransferhandlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import weblogic.deploy.service.DataStream;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.MultiDataStream;

public class MultipartResponse {
   HttpServletResponse res;
   ServletOutputStream out;
   MultiDataStream multiStream = null;

   public MultipartResponse(HttpServletResponse var1, MultiDataStream var2) throws IOException {
      this.res = var1;
      this.out = this.res.getOutputStream();
      this.multiStream = var2;
      this.init();
   }

   private void init() throws IOException {
      this.setupMultiFileResponse();
   }

   private void setupSingleFileResponse() throws IOException {
      DataStream var1 = (DataStream)this.multiStream.getDataStreams().next();
      this.res.setContentType(var1.isZip() ? "application/zip" : "text/plain");
      this.res.setContentLength(((FileDataStream)var1).getLength());
   }

   private void setupMultiFileResponse() throws IOException {
      this.res.setContentType("multipart/mixed");
      int var1 = 0;

      for(Iterator var2 = this.multiStream.getDataStreams(); var2.hasNext(); var1 += ((FileDataStream)var2.next()).getLength()) {
      }

      this.res.setContentLength(var1);
      String var3 = MultipartHelper.constructFilesHeaderValue(this.multiStream);
      this.res.setHeader("files_header", var3);
   }

   public void write() throws IOException {
      this.res.setStatus(200);
      Iterator var1 = this.multiStream.getDataStreams();

      while(var1.hasNext()) {
         DataStream var2 = (DataStream)var1.next();
         this.writeDataStream(var2);
      }

      this.out.flush();
   }

   private void writeDataStream(DataStream var1) throws IOException {
      InputStream var2 = null;

      try {
         var2 = var1.getInputStream();
         byte[] var3 = new byte[4096];

         int var4;
         while((var4 = var2.read(var3)) != -1) {
            this.out.write(var3, 0, var4);
         }
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var11) {
            }
         }

      }

   }
}
