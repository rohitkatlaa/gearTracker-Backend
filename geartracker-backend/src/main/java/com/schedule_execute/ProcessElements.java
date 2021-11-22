package com.schedule_execute;

import java.util.concurrent.*;
import com.sendemail.SendMail;

//import static java.util.concurrent.TimeUnit.*;
public class ProcessElements {
   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

   public void parseList() {
	   int[] arr = {2,3,5,6};
	   for(int i=0; i< arr.length; i++) {
		   if(arr[i]%2 == 0) {
			   SendMail.sendmail("Hemanth.Chitti@iiitb.ac.in", "geartrackertesting486@gmail.com", "geartrackertesting684","Testing ScheduleMail",Integer.toString(arr[i]));
		   }
	   } 
   }
   
   public void scheduleParse() {
     Runnable beeper = () -> parseList();//System.out.println("beep");
     ScheduledFuture<?> beeperHandle =
       scheduler.scheduleAtFixedRate(beeper, 1, 30, TimeUnit.SECONDS);
     Runnable canceller = () -> beeperHandle.cancel(false);
     scheduler.schedule(canceller, 1, TimeUnit.HOURS);
   }

   public static void main(String[] args){
       ProcessElements beep = new ProcessElements();
       beep.scheduleParse();
   }
 }
