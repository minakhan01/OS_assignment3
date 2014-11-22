import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
	
	public static void main(String args[]){
		      File file = new File("Hello1.txt");

		      try {

		          Scanner sc = new Scanner(file);
		          MemoryManager mm=new MemoryManager();
		          while (sc.hasNextLine()) {
		        	  String nextString=sc.nextLine();
		              String[] words=nextString.split(" ");
		              int length=words.length;
		              System.out.println(nextString);
		              switch(length){
		              case 1:
		            	  mm.printMemoryState();
		            	  break;
		              case 2:
		            	  try{
		            		  
		            	  int memory_size=Integer.parseInt(words[0]);
		            	  int policy=Integer.parseInt(words[1]);
		            	  mm=new MemoryManager(memory_size,policy);}
		            	  
		            	  catch(NumberFormatException nfe)  {
		            		  if(words[0].equals("D")){
		            		  int pid=Integer.parseInt(words[1]);
		            		  mm.deallocate(pid);}
		            	  }
		            	  break;
		              case 6:
		            	  int bytes=Integer.parseInt(words[1]);
		            	  int pid=Integer.parseInt(words[2]);
		            	  int text_size=Integer.parseInt(words[3]);
		            	  int data_size=Integer.parseInt(words[4]);
		            	  int heap_size=Integer.parseInt(words[5]);
		            	  mm.allocate(bytes, pid, text_size, data_size, heap_size);
		              }
		          }
		          sc.close();
		      } 
		      catch (FileNotFoundException e) {
		          e.printStackTrace();
		      }
		   }

}
