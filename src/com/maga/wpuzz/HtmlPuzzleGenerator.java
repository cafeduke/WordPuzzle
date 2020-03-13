package com.maga.wpuzz;
import java.io.*;
import java.util.logging.Logger;
import java.util.*;

import org.apache.commons.io.FileUtils;

import com.maga.util.Util;

public class HtmlPuzzleGenerator
{   
   private static final File FILE_BEGIN = new File ("TemplateBegin.html");
   
   private static final File FILE_END = new File ("TemplateEnd.html");
   
   private static final String TAB = "   ";
   
   private static final int WORD_COLUMN_COUNT = 5;
   
   public static void doWritePuzzle (char data[][], List<String> listWord, File filePuzzle, String title) throws IOException
   {
      writeBeginMeta(filePuzzle);
      writeTitle(filePuzzle, title);
      writeMatrix (filePuzzle, data);
      writeWords(filePuzzle, listWord);
      writeEndMeta(filePuzzle);
   }
   
   private static void writeBeginMeta (File filePuzzle) throws IOException
   {
      FileUtils.copyFile(FILE_BEGIN, filePuzzle);
   }
   
   private static void writeEndMeta (File filePuzzle) throws IOException
   {
      FileUtils.writeLines(filePuzzle, FileUtils.readLines(FILE_END), true);
   }  
   
   private static void writeTitle (File filePuzzle, String title) throws IOException
   {
      PrintWriter out = new PrintWriter (new FileWriter (filePuzzle, true));
      out.println("<h1>" + title + "</h1>");
      out.println("<hr/>");      
      out.close();
   }
   
   private static void writeMatrix (File filePuzzle, char data[][]) throws IOException
   {
      PrintWriter out = new PrintWriter (new FileWriter (filePuzzle, true));
      String tab = "";      
      out.println("<table class='grid'>");
      for (int i = 0; i < data.length; ++i)
      {
         tab = tab + TAB;
         out.println(tab + "<tr>");
         StringBuilder builder = new StringBuilder ();
         for (char ch : data[i])
         {
            tab = tab + TAB;
            builder
               .append(tab)
               .append("<td class='cell'>").append("<img src='images/" + Character.toUpperCase(ch) + ".png'/>").append("</td>")
               .append(Util.LineSep);            
            tab = tab.substring(TAB.length());
         }
         out.print(builder.toString());
         out.println(tab + "</tr>");
         tab = tab.substring(TAB.length());
      }
      out.println("</table>");
      out.close();
   }
   
   private static void writeWords (File filePuzzle, List<String> listWord) throws IOException
   {
      PrintWriter out = new PrintWriter (new FileWriter (filePuzzle, true));
      out.println("<table class='grid'>");
      
      String tab = "";
      StringBuilder builder = new StringBuilder ();
      
      int index = -1;
      for (String word : listWord)
      {
         ++index;
         
         if (index % WORD_COLUMN_COUNT == 0)
         {
            tab = tab + TAB;
            builder.append(tab).append("<tr>").append(Util.LineSep);
         }
         
         tab = tab + TAB;
         builder
            .append(tab)
            .append("<td>").append(word).append("<input class='check' type='checkbox'/></td>")
            .append(Util.LineSep);
         
         tab = tab.substring(TAB.length());
         
         if ((index+1) % WORD_COLUMN_COUNT == 0)
         {
            builder.append(tab).append("</tr>").append(Util.LineSep);
            tab = tab.substring(TAB.length());
         }         
      }
      out.print(builder.toString());
      out.println("</table>");
      out.close();
   }
}

