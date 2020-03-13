package com.maga.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

public class Util
{
   public static final String LineSep = System.getProperty("line.separator");
   
   public static final String LOG_FORMAT_KEY = "java.util.logging.SimpleFormatter.format";
   
   public static final String LOG_FORMAT_VALUE = "[%1$ta, %1$td-%1$tb-%1$tY %1$tT %1$tZ] [%4$s] %5$s %n";
   
   public static final Level LOG_VERBOSITY = (System.getProperty("loglevel") == null) ? Level.SEVERE : Level.parse(System.getProperty("loglevel"));
   
   static
   {
      System.setProperty(LOG_FORMAT_KEY, LOG_FORMAT_VALUE);
   }
   
   public static void main (String arg[])
   {
      Logger logger = Logger.getLogger(Util.class.getName());
   }
   
   public static int getLotteryPickIndex (List<Integer> listWeight)
   {
      int weight[] = new int [listWeight.size()];
      int index = 0;
      for (Integer currWeight : listWeight)
         weight[index++] = currWeight;
      return getLotteryPickIndex(weight);
   }
   
   public static int getLotteryPickIndex (int weight[])
   {
      int totalWeight = Arrays.stream(weight).sum();
      if (totalWeight == 0)
         return -1;
      
      int randomPick = Util.rand (totalWeight) + 1;      
      for (int i = 0; i < weight.length; ++i)
      {
         randomPick -= weight[i];
         if (randomPick <= 0)
            return i;
      }
      
      return -1;
   }
   
   /*
    * ------------------------------------------------------------------
    * Argument Processing
    * ----------------------------------------------------------------- 
    */
   
   public static URL validateArgUrl(String arg[], int index) throws MalformedURLException
   {
      return new URL(Util.getSwitchValue(arg, index));
   }
   
   public static int validateArgInteger (String arg[], int index)
   {
      return Integer.parseInt(Util.getSwitchValue(arg, index));
   }   

   public static Long validateArgLong(String arg[], int index)
   {
      return Long.parseLong(Util.getSwitchValue(arg, index));
   }

   public static List<String> validateArgStrings(String arg[], int index)
   {
      return Arrays.asList(Util.getSwitchValue(arg, index).split(","));
   }

   public static File validateArgFile(String arg[], int index) throws FileNotFoundException
   {
      File file = new File(Util.getSwitchValue(arg, index));
      if (!file.exists())
        throw new FileNotFoundException ("File  " + file.getAbsolutePath() + " not found");
      return file;
   }   
   
   /**
    * {@code arg[index]} is expected to have the option switch for which value
    * needs to be retrieved. If {@code (index+1)} is NOT within the argument boundary
    * an exception is thrown, else the switch value is returned.
    * 
    * @param arg Array of arguments.
    * @param index Index of the switch
    * @return Value of the switch.
    */
   public static String getSwitchValue(String arg[], int index)
   {
      if (index + 1 >= arg.length)
         throw new IllegalArgumentException("Option " + arg[index] + " needs a value");
      return arg[index + 1];
   }   
   
   /*
    * ------------------------------------------------------------------
    * Math
    * ----------------------------------------------------------------- 
    */
   
   public static int rand (int num)
   {
      return (int)(Math.random() * num);
   }
   
   /*
    * ------------------------------------------------------------------
    * Death
    * ----------------------------------------------------------------- 
    */
   
   public static void die (String mesg)
   {
      System.out.println("Error: " + mesg);
      System.exit(1);
   }
   
   
   /*
    * ------------------------------------------------------------------
    * Logging
    * ----------------------------------------------------------------- 
    */
   
   public static Logger getLogger (Class<?> currClass)
   {
      Logger logger = Logger.getLogger(currClass.getName());
      
      // Disable use of global handlers
      logger.setUseParentHandlers(false);
      
      // Use custom console handler 
      Handler handler = new ConsoleHandler();
      
      // The final verbosity is the most restrictive verbosity among the logger and handler
      logger.setLevel(LOG_VERBOSITY);
      handler.setLevel(LOG_VERBOSITY);
      logger.addHandler(handler);
      
      return logger;
   }

   /*
    * ------------------------------------------------------------------
    * IO
    * ----------------------------------------------------------------- 
    */
   
   public static String getNormalizePath (String first, String... more)
   {
      if (first == null)
         return null;
      return Paths.get(first, more).normalize().toFile().getPath().replace("\\", "/");
   }   
   
   public static void writeToFile (File file, String content)// throws IOException
   {
      writeToFile (file, new String[] {content});
   }

   public static void writeToFile (String filePath, String content)// throws IOException
   {
      writeToFile (new File(filePath), new String[] {content});
   }

   public static void writeToFile(String filePath, String content[])// throws IOException
   {
      writeToFile (new File(filePath), content);
   }
   
   /**
    * Writes strings in <b>token</b> separated by new line in <b>file</b>.
    * 
    * @param content Array of strings
    * @param file File to write the strings into.
    */
   public static void writeToFile (File file, String content[]) //throws IOException
   {
      try
      {
          PrintWriter out = new PrintWriter(new FileWriter(file));
          for (String currToken : content)
             out.println(currToken);
          out.close();
      }
      catch (Exception e)
      {
      
      }
   }
}
