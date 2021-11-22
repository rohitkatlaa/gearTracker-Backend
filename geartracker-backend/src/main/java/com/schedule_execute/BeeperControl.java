package com.schedule_execute;

import java.util.concurrent.*;
//import static java.util.concurrent.TimeUnit.*;
public class BeeperControl {
   private final ScheduledExecutorService scheduler =
     Executors.newScheduledThreadPool(1);

   public void beepForAnHour() {
     Runnable beeper = () -> System.out.println("beep");
     ScheduledFuture<?> beeperHandle =
       scheduler.scheduleAtFixedRate(beeper, 1, 1, TimeUnit.SECONDS);
     Runnable canceller = () -> beeperHandle.cancel(false);
     scheduler.schedule(canceller, 1, TimeUnit.HOURS);
   }

   public static void main(String[] args){
       BeeperControl beep = new BeeperControl();
       beep.beepForAnHour();
   }
 }