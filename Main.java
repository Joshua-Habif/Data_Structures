import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.


public class Main {

    public static File BNA;
    public static File TB;

    public static void main(String[] args){

	

    }

    public static void doIo(){
	BNA = new File(args[0]);
	TB = new File(args[1]);

	bnaReader = new BufferedReader(new FileReader(BNA));
	tbReader = new BufferedReader(new FileReader(TB));
    }
}