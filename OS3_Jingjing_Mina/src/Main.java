import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	
	public static void main(String args[]){
		      File file = new File("test_segmentation.txt");
//		      File file = new File("test_paging.txt");
		      try {
		    	  //scan each line and initialize MemoryManager
		          Scanner sc = new Scanner(file);
		          MemoryManager mm=new MemoryManager();
		          while (sc.hasNextLine()) {
		        	  String nextString=sc.nextLine();
		              String[] words=nextString.split(" ");
		              int length=words.length;
		              
		              //switch cases
		              switch(length){
		              case 1:
		            	// an instruction with 1 word is a print instruction
		            	  mm.printMemoryState();
		            	  break;
		              case 2:
		            	  try{
		            	  // if the first word in a 2 word instruction is 
		            	  // an int, then it's a create memory instruction
		            	  int memory_size=Integer.parseInt(words[0]);
		            	  int policy=Integer.parseInt(words[1]);
		            	  mm=new MemoryManager(memory_size,policy);}
		            	  
		            	  catch(NumberFormatException nfe)  {
		            		// if the first word in a 2 word instruction is 
			            	// "D", then it's a deallocate process instruction
		            		  if(words[0].equals("D")){
		            		  int pid=Integer.parseInt(words[1]);
		            		  mm.deallocate(pid);}
		            	  }
		            	  break;
		              case 6:
		            	  // an instruction with 6 words is an allocate instruction
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
