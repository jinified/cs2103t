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
 * Read user input and write them to file after exit of program.
 * Allow user to add, delete and displaying list of texts.
 * @author TAN SOON JIN
 *
 */
public class TextBuddy {
   
	private static final String MESSAGE_GENERIC = "\n%s\n\n";		//Formatted string for proper spacing
	private static final String MESSAGE_EMPTY_ARGUMENT = "Empty Argument";
	private static final String MESSAGE_INVALID_COMMAND = "Invalid Command";
	private static final String MESSAGE_COMMAND = "command: ";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_EXIT = "exit";

	private static boolean canRun = true;							//Termination flag 
	private static Vector<String> fileContent = new Vector<String>();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static String filePath;

	private static void setupEnvironment(String[] args)throws IOException {
		if (isEmptyArgument(args)){					
			System.out.println("Proper usage: java TextBuddy filename");
			System.exit(0);							
		} else {
			filePath = args[0];
			File userFile = new File(filePath);
			userFile.createNewFile();				//Create new file if not exists
			BufferedReader fileReader = new BufferedReader(
                        new FileReader(filePath));
			readFromFile(fileReader, fileContent);	//Read file content into Vector 
        }
	}

    private static boolean isEmptyArgument(String[] args) {
		return args.length == 0;
	}

    private static void readFromFile(BufferedReader fileReader, 
        Vector<String> fileContent)throws IOException {
            String line;

            try{
            	while ((line = fileReader.readLine()) != null){
            		fileContent.add(line); 
            	}
            } catch (IOException e) {
            	e.printStackTrace();
            } finally {
            	fileReader.close();
            }
    }

    public static void writeOutput(String arg) {
    	System.out.print(String.format(MESSAGE_GENERIC,arg));
    }

	private static void printFileContent(Vector<String> fileContent,String filePath) {
        if (fileContent.size() == 0) {
        	writeOutput(String.format("%s is empty", filePath));
        } else {
        	writeOutput(getFileContent(fileContent));
        }
    }

    private static String getFileContent(Vector<String> fileContent) {
        StringBuilder sb = new StringBuilder();
        int i = 1;

        for (String str:fileContent) {
            String in = String.format("%d. %s\n\n", i++, str);
            sb.append(in);
        }
        String linesOfText = sb.toString();
        return  linesOfText.substring(0, linesOfText.length()-2); //Remove last newlines 
    }

	private static String getCommand(String userInput) {
		return userInput.split(" ")[0];
	}

	private static String getArgument(String userInput) {
		return userInput.substring(userInput.indexOf(" ") // Extract string after first spacing 
				+ 1, userInput.length());
	}


    public static void main(String[] args) throws IOException{
    	setupEnvironment(args);
        System.out.printf("Welcome to TextBuddy. %s is ready for use\n\n",filePath);

        while (canRun){
            showToUser(MESSAGE_COMMAND);
            String userInput = br.readLine().trim();		//Sanitize user input 
            processCommand(userInput);
        }
    }

	private static void showToUser(String messageCommand) {
		System.out.print(messageCommand);
	}

	public static void processCommand(String userInput)throws IOException {
		if ("".equals(userInput)) {			//Prompt user to enter a command 
			writeOutput(MESSAGE_INVALID_COMMAND);
			return;
		}

		String command = getCommand(userInput);

		switch (command) {
        	case COMMAND_EXIT:
                exitProgram();
                break;

        	case COMMAND_CLEAR:
                clearFile(fileContent);
                break;

        	case COMMAND_ADD:
                addToFile(userInput);
                break;

        	case COMMAND_DELETE:
                deleteFromFile(userInput);
                break;

        	case COMMAND_DISPLAY:
                displayFile();
                break;

        	default:
                writeOutput(MESSAGE_INVALID_COMMAND);
                break;
		}
	}

	private static void displayFile() {
		printFileContent(fileContent, filePath);
	}

	private static void deleteFromFile(String userInput) {
		if (!isEmptyArgument(userInput)) {
			String argument = getArgument(userInput);
			int index = Integer.parseInt(argument) - 1;			//-1 because of zero-indexing

			try {
				writeOutput(String.format("deleted from %s: \"%s\"",filePath,fileContent.get(index)));
                fileContent.remove(index);
			} catch (IndexOutOfBoundsException e) {
				writeOutput("Illegal index input");
			}
		} else {
			writeOutput(MESSAGE_EMPTY_ARGUMENT);
		}
		
	}

	private static void addToFile(String userInput) {
		if (!isEmptyArgument(userInput)) {			
			String argument = getArgument(userInput);
            writeOutput(String.format("added to %s: \"%s\"",filePath,argument));
            fileContent.add(argument);
		} else {
			writeOutput(MESSAGE_EMPTY_ARGUMENT);
		}
	}

	private static boolean isEmptyArgument(String userInput) {
		return userInput.split(" ").length == 1;	//Check for string after command
	}

	private static void clearFile(Vector<String> fileContent) {
        writeOutput(String.format("all content deleted from %s",filePath));
        fileContent.clear();
	}

	private static void exitProgram()throws IOException {
		writeToFile(filePath);
		canRun = false;			//Terminate program
	}

	private static void writeToFile(String filePath)throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(
            new FileWriter(filePath));

		try {					
			String finalOutput = writeVectorToFile(); 
			fileWriter.write(finalOutput);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fileWriter.close();
		}
	}

	/**
	 * Due to different formatting compared to standard output. 
	 * @return String from list of texts from user
	 */
	private static String writeVectorToFile() {
		StringBuilder sb = new StringBuilder();
		int i = 1;

		for (String str : fileContent) {
			String in = String.format("%s\n", str);
			sb.append(in);
		}

		String linesOfText = sb.toString();
		/*Remove last newline*/
		String finalOutput = linesOfText.substring(0, linesOfText.length() - 1);
		return finalOutput;
	}

}
