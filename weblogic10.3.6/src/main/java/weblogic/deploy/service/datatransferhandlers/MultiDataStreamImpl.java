package weblogic.deploy.service.datatransferhandlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.deploy.service.DataStream;
import weblogic.deploy.service.MultiDataStream;

final class MultiDataStreamImpl implements MultiDataStream {
   private List dataStreams = new ArrayList();

   public int getSize() {
      return this.dataStreams.size();
   }

   public Iterator getDataStreams() {
      return this.dataStreams.iterator();
   }

   public Iterator getInputStreams() throws IOException {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getDataStreams();

      while(var2.hasNext()) {
         var1.add(((DataStream)var2.next()).getInputStream());
      }

      return var1.iterator();
   }

   public void close() {
      Iterator var1 = this.getDataStreams();

      while(var1.hasNext()) {
         DataStream var2 = (DataStream)var1.next();
         var2.close();
      }

   }

   void addDataStream(String var1, InputStream var2, boolean var3) {
      this.dataStreams.add(DataStreamFactory.createDataStream(var1, var2, var3));
   }

   void addFileDataStream(String var1, String var2, boolean var3) {
      this.addFileDataStream(var1, new File(var2), var3);
   }

   void addFileDataStream(File var1, boolean var2) {
      this.addFileDataStream((String)null, (File)var1, var2);
   }

   public void addFileDataStream(String var1, boolean var2) {
      this.addFileDataStream(new File(var1), var2);
   }

   public void addFileDataStream(String var1, File var2, boolean var3) {
      this.dataStreams.add(DataStreamFactory.createFileDataStream(var1, var2, var3));
   }

   public void addDataStream(DataStream var1) {
      this.dataStreams.add(var1);
   }

   public void removeDataStream(DataStream var1) {
      this.dataStreams.remove(var1);
   }
}
