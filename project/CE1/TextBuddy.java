import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * 
 * @author TAN SOON JIN
 *
 */
public class TextBuddy{
   
	private static final String MESSAGE_GENERIC = "\n%s\n\n";
	private static final String MESSAGE_EMPTY_ARGUMENT = "Empty Argument";
	private static final String MESSAGE_COMMAND = "command: ";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_EXIT = "exit";
	private static boolean canRun = true;

	private static Vector<String> fileContent = new Vector<String>();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static String filePath;

	public static void setupEnvironment(String[] args) throws IOException{
		if (isEmptyArgument(args)){
			System.out.println("No input text specified.");
		} else {
			filePath = args[0];
			File userFile = new File(filePath);
			userFile.createNewFile();				//Create new file if not exists
			BufferedReader fileReader = new BufferedReader(
                        new FileReader(filePath));
			readFromFile(fileReader, fileContent);	//Read file content into Vector 
        }
	}

    public static boolean isEmptyArgument(String[] args) {
		return args.length == 0;
	}

    public static void readFromFile(BufferedReader fileReader, 
        Vector<String> fileContent)throws IOException{
            String line;
            try{
            	while((line = fileReader.readLine()) != null){
            		fileContent.add(line); 
            	}
            } catch(IOException e){
            	e.printStackTrace();
            } finally{
            	fileReader.close();
            }
    }

    public static void writeOutput(String arg){
    	System.out.print(String.format(MESSAGE_GENERIC,arg));
    }

	public static void printFileContent(Vector<String> fileContent,String filePath){
        if (fileContent.size() == 0){
        	writeOutput(String.format("%s is empty", filePath));
        } else {
        	writeOutput(getContent(fileContent));
        }
    }

    public static String getContent(Vector<String> fileContent){
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String str:fileContent){
            String in = String.format("%d. %s\n", i++, str);
            sb.append(in);
        }
        String linesOfText = sb.toString();
        return  linesOfText.substring(0, linesOfText.length()-1); //Remove last newline
    }

    public static String[] parseUserInput(BufferedReader br)throws IOException{
    	String userInput = br.readLine().trim();	//Sanitize user input 
    	String command = userInput.split(" ")[0];	//Extract first word from user input
    	String argument = getArgument(userInput);
    	return new String[] {command,argument};
    }
    
    public static String getArgument(String userInput){
    	return userInput.split(" ").length == 1 ? null : extractArgument(userInput);
    }

    public static String extractArgument(String userInput){
    	return userInput.substring(userInput.indexOf(" ")
            + 1, userInput.length());
    }

    public static void main(String[] args) throws IOException{
    	setupEnvironment(args);
        System.out.printf("Welcome to TextBuddy. %s is ready for use\n\n",filePath);
        while (canRun){
            System.out.print(MESSAGE_COMMAND);
            String[] userInput = parseUserInput(br);
            processCommand(userInput);
        }
    }

	private static void processCommand(String[] userInput) throws IOException {
		String command = userInput[0];
		String argument = userInput[1];
		switch (command){
			case COMMAND_EXIT:
				exitProgram();
				break;
				
			case COMMAND_CLEAR:
				clearFile(fileContent);
				break;
	
			case COMMAND_ADD:
				addToFile(argument);
				break;
				
			case COMMAND_DELETE:
				deleteFromFile(argument);
				break;
				
			case COMMAND_DISPLAY:
				displayFile();
				break;
				
			default:
				System.out.println("Unknown user input\n");
				break;
		}
	}

	private static void displayFile() {
		printFileContent(fileContent, filePath);
	}

	private static void deleteFromFile(String argument) {
		if (!isEmptyArgument(argument)){
			int index = Integer.parseInt(argument);
			try {
				writeOutput(String.format("deleted from %s: \"%s\"",filePath,fileContent.get(index-1)));
			} catch (IndexOutOfBoundsException e){
				writeOutput("Illegal index input");
			}
            fileContent.remove(index-1);
		} else {
			writeOutput(MESSAGE_EMPTY_ARGUMENT);
		}
		
	}

	private static void addToFile(String argument) {
		if (!isEmptyArgument(argument)){
            writeOutput(String.format("added to %s: \"%s\"",filePath,argument));
            fileContent.add(argument);
		} else {
			writeOutput(MESSAGE_EMPTY_ARGUMENT);
		}
	}

	private static boolean isEmptyArgument(String argument) {
		return argument == null;
	}

	private static void clearFile(Vector<String> fileContent) {
        writeOutput(String.format("all content deleted from %s",filePath));
        fileContent.clear();
	}

	private static void exitProgram()throws IOException{
		StringBuilder sb = new StringBuilder();
        BufferedWriter fileWriter = new BufferedWriter(
            new FileWriter(filePath));

		try {
			for (String str : fileContent) {
				sb.append(str + "\n");
			}
			fileWriter.write(sb.toString());
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			fileWriter.close();
		}

		canRun = false;			//Terminate program
	}

}
