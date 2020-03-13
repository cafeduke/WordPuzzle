# Introduction
A Word Puzzle is a Java tool to generate a HTML page having (m x n) matrix of images. Each image is an alphabet in ISL (Indian Sign Language). The matrix has words (provided as input for puzzle generation) are spread accross horizontally, vertically and diagonally. Print out the HTML, pick up a pencil and highlight the words the old fashioned way! 

# Sample Puzzle
![ISL](https://raw.githubusercontent.com/cafeduke/WordPuzzle/master/resources/ISL.jpg)

# How to genererate a puzzle

## Pre-requisite
Install ![JDK](https://www.oracle.com/java/technologies/javase-downloads.html)


## Example (Steps on Windows)
1. Download and unzip the project to say `C:\WordPuzzle`
2. Type "cmd" > Enter
3. `cd C:\WordPuzzle`
4. `Puzzle -row 15 -col 15 -word "grape,watermelon,apple,lemon,orange,mango,jackfruit,pineapple" -file MyFruit.html -title "My Fruit"`

## Usage

```bash
java -jar WordPuzzle.jar
  -row <Number of rows>
  -col <Number of columns>
  -word <Comma separated words>
  -file <HTML filename for the puzzle>
  -title <Puzzle title>
  [ -weightHorz <A weight for adding words horizontally> ]
  [ -weightVert <A weight for adding words vertically> ]
  [ -weightDia  <A weight for adding words diagonally> ]
```

## Generate Multiple Puzzles (On Windows)
1. Edit MultiplePuzzles.cmd
2. Run MultiplePuzzles
