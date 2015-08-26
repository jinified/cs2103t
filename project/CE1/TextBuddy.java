import java.io.*;
import java.util.*;

/**
 * Assumptions: 
 * 1. User can enter duplicate text
 * 2. User will only enter valid command
 */

public class TextBuddy{
   

    static void printContent(Vector<String> cont,String fileName){
        if(cont.size() == 0){
            System.out.printf("%s is empty\n\n",fileName);
        } else {
            System.out.println(getContent(cont));
        }
    }

    static String getContent(Vector<String> cont){
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(String str:cont){
            String in = i++ + ". " + str + "\n";
            sb.append(in);
        }
        return sb.toString();
    }

    static void readFromFile(BufferedReader fr, Vector<String> cont)throws IOException{
        String line;
        try{
            while((line = fr.readLine()) != null){
                cont.add(line); 
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            fr.close();
        }
    }

    public static void main(String[] args) throws IOException{
        Vector<String> content = new Vector<String>();
        String fileName = args[0];
        String in = "";
        File output = new File(fileName);
        if(!output.exists()){
            output.createNewFile();
        }
        BufferedReader fr = new BufferedReader(new FileReader(output.getAbsoluteFile()));
        readFromFile(fr,content);
        BufferedWriter bw = new BufferedWriter(new FileWriter(output.getAbsoluteFile()));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //Read text from file 
        System.out.format("Welcome to TextBuddy. %s is ready for use\n\n",fileName);
        while(true){
            System.out.print("command: ");
            in = br.readLine();
            String command = in.split(" ")[0];
            System.out.print("\n");
            if(command.equals("exit")){
                StringBuilder sb = new StringBuilder();
                for(String str:content){
                    sb.append(str+"\n");
                }
                bw.write(sb.toString());
                break;
            }
            if(command.equals("display")){
                printContent(content,fileName);
            }
            if(command.equals("clear")){
                System.out.printf("all content deleted from %s\n\n",fileName);
                content.clear();
            }
            else {
                String parameter = in.substring(in.indexOf(" ")+1,in.length()).trim();
                if(command.equals("add")){
                    System.out.printf("added to %s: \"%s\"\n\n",fileName,parameter);
                    content.add(parameter);
                }
                if(command.equals("delete")){
                    int index = Integer.parseInt(parameter);
                    try{
                        System.out.printf("deleted from %s: \"%s\"\n\n",fileName,content.get(index-1));
                        content.remove(index-1);
                    } catch(IndexOutOfBoundsException e){
                        System.err.println("Wrong index\n");
                    }
                }
            }
        }
        bw.close(); //Close writer
    }

}
