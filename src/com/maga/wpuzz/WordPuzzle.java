package com.maga.wpuzz;
import java.io.*;
import java.util.*;

import com.maga.util.Util;

public class WordPuzzle
{
   private List<String> listWord;
   
   private String filename = null, title = null;
   
   private int rowSize, colSize, weightHoriz, weightVert, weightDia;   
   
   public static void main (String arg[]) throws IOException
   {
      WordPuzzle puzzle = new WordPuzzle ();
      puzzle.parseArg(arg);
      puzzle.validateArg();
      puzzle.doGeneratePuzzle();
   }  
   
   public void doGeneratePuzzle () throws IOException
   {      
      WordMatrix matrix = new WordMatrix(rowSize, colSize);
      matrix.setWords(listWord);
      
      if (weightHoriz > 0)
         WordMatrix.Direction.HORIZONTAL.setWeight(weightHoriz);
      
      if (weightVert > 0)
         WordMatrix.Direction.VERTICAL.setWeight(weightVert);

      if (weightDia > 0)
         WordMatrix.Direction.DIAGONAL.setWeight(weightDia);
      
      matrix.shuffleWords();
      System.out.println(matrix);
      
      writeTextFile(matrix);
      
      matrix.fillPlaceHolders();
      System.out.println(matrix);
      
      matrix.genHtml(filename, title);
   }
   
   private void writeTextFile (WordMatrix matrix)
   {
      int index = filename.lastIndexOf('.');
      String textFile = ((index == -1) ? filename : filename.substring(0, filename.lastIndexOf('.')))  + ".txt";
      Util.writeToFile(textFile, matrix.toString());
   }
   
   private void parseArg (String arg[])
   {
      if (arg.length == 0)
      {
         usage ();
         System.exit (0);
      }
      
      for (int index = 0; index < arg.length; ++index)
      {
         String currArg = arg[index];
         if (currArg.equals("-row"))
            rowSize = Util.validateArgInteger(arg, index++);
         else if (currArg.equals("-col"))
            colSize = Util.validateArgInteger(arg, index++);
         else if (currArg.equals("-word"))
         {
            String csvWord= Util.getSwitchValue(arg, index++).toLowerCase();
            listWord = new ArrayList<String>(Arrays.asList(csvWord.split(",")));
         }
         else if (currArg.equals("-file"))
            filename = Util.getSwitchValue(arg, index++);
         else if (currArg.equals("-title"))
            title = Util.getSwitchValue(arg, index++);
         else if (currArg.equals("-weightHorz"))
            weightHoriz = Util.validateArgInteger(arg, index++);
         else if (currArg.equals("-weightVert"))
            weightVert = Util.validateArgInteger(arg, index++);
         else if (currArg.equals("-weightDia"))
            weightDia = Util.validateArgInteger(arg, index++);
         else
            throw new IllegalArgumentException("Unknown argument " + currArg + Util.LineSep);
      }
   }
   
   private void validateArg ()
   {
      if (rowSize == 0)
         Util.die("-row is mandatory");         
      
      if (colSize == 0)
         Util.die("-col is mandatory");
      
      if (listWord.isEmpty())
         Util.die("-word must have comma separated list of words.");
      
      if (filename == null)
         Util.die("-file is mandatory");
      
      if (title == null)
         Util.die("-title is mandatory");
   }
   
   private void usage ()
   {
      StringBuilder builder = new StringBuilder ();
      builder.append("java WordPuzzle").append(Util.LineSep)
             .append("  -row <Number of rows>").append(Util.LineSep)
             .append("  -col <Number of columns>").append(Util.LineSep)
             .append("  -word <Comma separated words>").append(Util.LineSep)
             .append("  -file <HTML filename for the puzzle>").append(Util.LineSep)
             .append("  -title <Puzzle title>").append(Util.LineSep)
             .append("  [ -weightHorz <A weight for adding words horizontally> ]").append(Util.LineSep)
             .append("  [ -weightVert <A weight for adding words vertically> ]").append(Util.LineSep)
             .append("  [ -weightDia  <A weight for adding words diagonally> ]").append(Util.LineSep);
      System.out.println(builder);
   }   
}
