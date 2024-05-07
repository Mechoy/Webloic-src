package weblogic.servlet.ejb2jsp.gui;

class MainWorker implements Runnable {
   Main m;

   static void p(String var0) {
   }

   MainWorker(Main var1) {
      this.m = var1;
   }

   public void run() {
      p("running");

      while(true) {
         while(true) {
            try {
               p("getting task");
               Runnable var1 = this.m.getTask();
               p("running task");
               var1.run();
               p("ran task");
            } catch (Exception var2) {
               p("bad task: " + var2);
               var2.printStackTrace();
            }
         }
      }
   }
}
