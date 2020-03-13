package com.maga.wpuzz;
import java.util.*;
import java.io.*;
import java.util.logging.*;

import com.maga.util.Util;

public class WordMatrix
{
   private List<String> listWord = new ArrayList<>();
   
   private char data[][];
   
   int rowSize, colSize;
   
   private Logger logger = Util.getLogger(getClass());   
   
   public enum Direction
   {
      HORIZONTAL(4), VERTICAL(4), DIAGONAL(2);
      
      int weight;
      
      private Direction (int weight)
      {
         this.weight = weight;
      }
      
      public void setWeight (int weight)
      {
         this.weight = weight;
      }
   }
   
   public WordMatrix (int size)
   {
      this (size, size);
   }
   
   public WordMatrix (int rowSize, int colSize)
   {
      data = new char[rowSize][colSize];
      this.rowSize = rowSize;
      this.colSize = colSize;
   }
   
   public static void main (String arg[])
   {
      char data[][] = new char[][] 
      {
         {'a', '.', 'x', '.', 'x', '.'},
         {'.', 'x', '.', '.', 'x', '.'},
         {'.', '.', 'x', '.', '.', 'x'},
         {'a', '.', '.', '.', 't', 'e'},
         {'.', 'x', '.', '.', 'x', '.'},
         {'.', 'a', 't', 'e', '.', '.'}
      };
      
      String word[] = new String [] {"apple", "in", "bird", "egg", "pen" };
      WordMatrix matrix = new WordMatrix(6);
      matrix.setData(data);
      matrix.setWords(Arrays.asList(word));
      System.out.println((matrix.getIndex("ate", ".a")));
   }
   
   public void setData (char data[][])
   {
      this.data = data;
      this.rowSize = data.length;
      this.colSize = data[0].length;
   }
   
   public void setWords (List<String> listWord)
   {
      this.listWord = listWord;
   }
   
   public void shuffleWords ()
   {
      for (int i = 0; i < data.length; ++i)
         Arrays.fill(data[i], '.');
      
      for (String currWord : listWord)
         tryAddWord(currWord);
   }
   
   public void fillPlaceHolders ()
   {
      for (int i = 0; i < rowSize; ++i)
         for (int j = 0; j < colSize; ++j)
         {
            if (data[i][j] == '.')
               data[i][j] = (char)('a' +  Util.rand(26));
         }
   }
   
   public void genHtml (String filename, String title) throws IOException
   {
      HtmlPuzzleGenerator.doWritePuzzle(data, listWord, new File(filename), title);
   }
   
   public void tryAddWord (String word)
   {
      final List<Direction> listDirection = new ArrayList<>(Arrays.asList(Direction.values()));
      final List<Integer>   listWeight    = new ArrayList<>();
      listDirection.forEach((d) -> listWeight.add(d.weight));

      logger.info ("---------------------------------------------------------------------------");
      while (!listDirection.isEmpty())
      {
         int chosenIndex = Util.getLotteryPickIndex(listWeight);
         Direction chosenDirection = listDirection.get(chosenIndex);
         logger.info(String.format("Try adding. word=%s, direction=%s", word, chosenDirection));
         if (tryAddWord (word, chosenDirection))
            return;
         listDirection.remove(chosenIndex);
         listWeight.remove(chosenIndex);
      }
   }
   
   public boolean tryAddWord (String word, Direction direction)
   {
      List<Integer> listIndex = new ArrayList<> ();
      int size = -1;
      if (direction == Direction.HORIZONTAL)
         size = rowSize;
      else if (direction == Direction.VERTICAL)
         size = colSize;
      else if (direction == Direction.DIAGONAL)
         size = rowSize + colSize;
      
      // Fill index array
      for (int i = 0; i < size; ++i)
         listIndex.add(i);
      
      while (listIndex.size() > 0)
      {
         int randIndex = listIndex.remove(Util.rand(listIndex.size()));
         logger.info("RandomIndex=" + randIndex);
         if (tryAddWord(word, randIndex, direction))
            return true;
      }
      return false;
   }
   
   public boolean tryAddWord (String word, int index, Direction direction)
   {
      String line = getLine(index, direction);  
      logger.info(String.format("Index=%d, Word=%s, Direction=%s Line=%s", index, word, direction.toString(), line));
      int chosenWordIndex = getIndex (word, line);
      if (chosenWordIndex == -1)
      {
         logger.info("Cannot add word");
         return false;
      }
      logger.info("Added word at LineIndex=" + chosenWordIndex);
      
      if (direction == Direction.HORIZONTAL)
         setWord (word, index, chosenWordIndex, direction);
      else if (direction == Direction.VERTICAL)
         setWord (word, chosenWordIndex, index, direction);
      else
      {
         int rowIndex = getDiagonalRowIndex(index) + chosenWordIndex;
         int colIndex = getDiagonalColIndex(index) + chosenWordIndex;
         setWord (word, rowIndex, colIndex, direction);
      }
      
      return true;
   }
   
   private void setWord (String word, int rowIndex, int colIndex, Direction direction)
   {
      char ch[] = word.toCharArray();
      
      if (direction == Direction.HORIZONTAL)
      {
         assert (colIndex + word.length() <= colSize);
         for (int i = 0; i < ch.length; ++i)
            data[rowIndex][colIndex+i] = ch[i];
      }
      else if (direction == Direction.VERTICAL)
      {
         assert (rowIndex + word.length() <= rowSize);
         for (int i = 0; i < ch.length; ++i)
            data[rowIndex+i][colIndex] = ch[i]; 
      }
      else      
      {
         assert (rowIndex + word.length() <= rowSize && colIndex + word.length() <= colSize);
         for (int i = 0; i < ch.length; ++i)
            data[rowIndex+i][colIndex+i] = ch[i];
      }
   }
   
   private String getLine (int index, Direction direction)
   {
      if (direction == Direction.HORIZONTAL)
         return new String (data[index]);
      
      if (direction == Direction.VERTICAL)
      {
         char ch[] = new char[rowSize];
         for (int i = 0; i < rowSize; ++i)
            ch[i] = data[i][index];
         return new String (ch);
      }
      
      int i = getDiagonalRowIndex(index), j = getDiagonalColIndex(index);
      StringBuilder builder = new StringBuilder ();
      while (i < rowSize && j < colSize)
         builder.append(data[i++][j++]);         
      return builder.toString();   
   }
   
   private int getDiagonalRowIndex (int index)
   {
      return (index < colSize) ? 0 : index - colSize + 1;
   }
   
   private int getDiagonalColIndex (int index)
   {
      return (index < colSize) ? index : 0;
   }
   
   private int getIndex (String word, String line)
   {
      if (word.length() > line.length())
         return -1;
      
      int numOfWords = line.length() - word.length() + 1;
      int weightByOverlap[] = new int [numOfWords];      
      if (line.matches("\\.+"))
         return Util.rand(numOfWords);
      
      for (int i = 0; i < numOfWords; ++i)
      { 
         String lineSubset = line.substring(i, i + word.length());
         int overlap = lineSubset.replaceAll("\\.", "").length(); 
         weightByOverlap[i] = ((word.matches(lineSubset)) ? overlap : -1) + 1;
         logger.fine(String.format("%d) LineSubset=%s Weight=%d", i, lineSubset, weightByOverlap[i]));
      }
      
      return Util.getLotteryPickIndex(weightByOverlap);
   }   
   
   @Override
   public String toString ()
   {
      StringBuilder builder = new StringBuilder ();
      for (int i = 0; i < data.length; ++i)
      {
         for (int j = 0; j < data[0].length; ++j)
            builder.append(" ").append(data[i][j]).append(" ");
         builder.append(Util.LineSep);
      }
      return builder.toString();
   }

}
