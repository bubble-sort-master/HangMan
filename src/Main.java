//игра "Виселица"

import java.io.*;
import java.util.*;

public class Main {

    private static int WORD_MAX_LENGTH = 7;

    private static String GAME_STATE_PLAYER_LOST = "К сожалению, вы проиграли";
    private static String GAME_STATE_PLAYER_WON = "Вы отгадали  слово!";

    private static String WRONG_GUESS = "Буквы в слове нет";
    private static String CORRECT_GUESS = "Буква есть в слове";
    private static int MAX_MISTAKES = 7;

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        System.out.println();
        System.out.println("\t\tДобро пожаловать в игру виселица!\n");
        System.out.println("Все слова загадываются на русском языке, длина каждого слова - 7; Регистр ввода не важен\n" +
                "седьмая ошибка будет последней :)");
        ArrayList<String> dictionary = initialiseDictionary();

        do {
            System.out.println();
            System.out.println("Чтобы начать новую игру -- введите 1");
            System.out.println("Чтобы покинуть игру  -- введите 2");
            String userChoice = scanner.nextLine();
            System.out.println();

            switch (userChoice) {
                case "1":
                    System.out.println("Было загадано новое слово");
                    runNewGameLoop(dictionary);
                    break;
                case "2":
                    System.out.println("До новых встреч!");
                    return;
                default:
                    System.out.println("Некоректный ввод, попробуйте еще раз");
                    break;
            }
        }while (true);
    }

    public static void runNewGameLoop(ArrayList<String> dictionary){

        String wordToGuess = getRandomWord(dictionary).toUpperCase();

        ArrayList<String> correctGuesses= new ArrayList<>();
        ArrayList<String> wrongGuesses = new ArrayList<>();
        int mistakeCount = 0;

        HangManPicture.displayHangMan(mistakeCount);

        do{
            String userGuessStatus = processUserGuess(wordToGuess, correctGuesses, wrongGuesses);
                if (Objects.equals(userGuessStatus, WRONG_GUESS))
                    mistakeCount++;

             HangManPicture.displayHangMan(mistakeCount);
             printWordProgress(wordToGuess, correctGuesses,wrongGuesses,mistakeCount);

             if(mistakeCount == MAX_MISTAKES){
                 System.out.println(GAME_STATE_PLAYER_LOST);
                 System.out.println("Загаданное слово: "+ wordToGuess);
                 return;
             }
             else if(isWordSolved(wordToGuess, correctGuesses)){
                 System.out.println(GAME_STATE_PLAYER_WON);
                 return;
             }

        }while (true);

    }

    private static void printWordProgress(String wordToGuess, ArrayList<String> correctGuesses, ArrayList<String> wrongGuesses, int mistakeCount) {
        System.out.println("Текущее состояние слова:");

         for(int i=0;i<WORD_MAX_LENGTH;i++){
             if(correctGuesses.contains(Character.toString(wordToGuess.charAt(i)))){
                 System.out.print(wordToGuess.charAt(i));
             }
             else
                 System.out.print("_");
         }
        System.out.println();

         if(mistakeCount>0){
             System.out.println("Колличество ошибок: " + mistakeCount);
             System.out.print("Ошибки: ");
             for(String s: wrongGuesses)
                 System.out.print(s + " ");
             System.out.println("\n");
         }
    }

    public static String processUserGuess(String wordToGuess, ArrayList<String> correctGuesses, ArrayList<String> wrongGuesses){

        while (true) {
            System.out.println("Введите букву");
            String guessedLetter = scanner.nextLine().toUpperCase();

            if(!isRussianLetter(guessedLetter)){
                System.out.println("Введите одну букву русского алфавита!");
                continue;
            }

            if(isAlreadyGuessedLetter(guessedLetter,correctGuesses,wrongGuesses)){
                System.out.println("Вы уже вводили эту букву! ");
                continue;
            }

            for (int i = 0; i < WORD_MAX_LENGTH; i++) {
                if (guessedLetter.charAt(0) == wordToGuess.charAt(i)) {
                    System.out.println("Есть такая буква!");
                    correctGuesses.add(guessedLetter);
                    return CORRECT_GUESS;

                }
            }
            System.out.println("Такой буквы в слове нет!");
            wrongGuesses.add(guessedLetter);
             return WRONG_GUESS;
        }

    }

    public static ArrayList<String> initialiseDictionary(){
        ArrayList<String> dictionary = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))){
            String line;
            while ((line = reader.readLine())!=null){
                dictionary.add(line);
            }
        }catch (IOException e){
            System.out.println("Проблема при инитиализации словаря:");
            e.printStackTrace();
        }
        return dictionary;
    }

    public static String getRandomWord(ArrayList<String> dictionary){
         return dictionary.get(random.nextInt(dictionary.size()));
    }

    public static boolean isRussianLetter(String letter){
        return letter.matches("[а-яА-ЯёЁ]");
    }

    public static boolean isAlreadyGuessedLetter(String letter, ArrayList<String> correctGuesses, ArrayList<String> wrongGuesses){
        return correctGuesses.contains(letter) || wrongGuesses.contains(letter);
    }

    public static boolean isWordSolved(String guessedWord, ArrayList<String> correctGuesses){
        char[] word = guessedWord.toCharArray();
        for(char l: word){
            if(!correctGuesses.contains(Character.toString(l)))
                return false;
        }
        return true;
    }
}
