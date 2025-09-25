//игра "Виселица"

import java.io.*;
import java.util.*;

public class Main {

    private static final int WORD_LENGTH = 7;

    private static final String GAME_STATE_PLAYER_LOST = "К сожалению, вы проиграли";
    private static final String GAME_STATE_PLAYER_WON = "Вы отгадали  слово!";

    private static final String WRONG_GUESS = "Буквы в слове нет";
    private static final String CORRECT_GUESS = "Буква есть в слове";
    private static final int MAX_MISTAKES = 7;

    private static final String START = "1";
    private static final String QUIT = "2";

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println();
        System.out.println("\t\tДобро пожаловать в игру виселица!\n");
        System.out.println("Все слова загадываются на русском языке, длина каждого слова - 7; Регистр ввода не важен\n" +
                "седьмая ошибка будет последней :)");

        do {
            System.out.println();
            System.out.println("Чтобы начать новую игру -- введите " + START);
            System.out.println("Чтобы покинуть игру  -- введите "+  QUIT);
            String userChoice = scanner.nextLine();
            System.out.println();

            switch (userChoice) {
                case START:
                    try {
                        ArrayList<String> dictionary = createDictionary();
                        System.out.println("Было загадано новое слово");
                        start(dictionary);
                    }catch (IOException e){
                        return;
                    }
                    break;
                case QUIT:
                    System.out.println("До новых встреч!");
                    return;
                default:
                    System.out.println("Некоректный ввод, попробуйте еще раз");
                    break;
            }
        }while (true);
    }

    public static void start(ArrayList<String> dictionary){

        String wordToGuess = getRandomWord(dictionary).toUpperCase();

        ArrayList<String> correctGuesses= new ArrayList<>();
        ArrayList<String> wrongGuesses = new ArrayList<>();
        int mistakeCount = 0;

        HangmanPicture.printHangman(mistakeCount);

        while (true){
            String userGuessStatus = processUserGuess(wordToGuess, correctGuesses, wrongGuesses);
            if (Objects.equals(userGuessStatus, WRONG_GUESS))
                mistakeCount++;

            HangmanPicture.printHangman(mistakeCount);

            printWordState(wordToGuess,correctGuesses);
            printTotalMistakes(wrongGuesses,mistakeCount);

            if(mistakeCount == MAX_MISTAKES){
                System.out.println(GAME_STATE_PLAYER_LOST);
                System.out.println("Загаданное слово: "+ wordToGuess);
                return;
            }
            if(isWordSolved(wordToGuess, correctGuesses)){
                System.out.println(GAME_STATE_PLAYER_WON);
                return;
            }

        }

    }

    public static ArrayList<String> createDictionary() throws IOException {
        ArrayList<String> dictionary = new ArrayList<>();
        File file = new File("dictionary.txt"); // Создаем объект File

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (IOException e) {
            // Выводим абсолютный путь файла
            System.out.println("Не удалось открыть файл по пути: " + file.getAbsolutePath());
            throw new IOException();
        }
        return dictionary;
    }

    public static String getRandomWord(ArrayList<String> dictionary){
        return dictionary.get(random.nextInt(dictionary.size()));
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

            for (int i = 0; i < WORD_LENGTH; i++) {
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

    public static boolean isRussianLetter(String letter){
        return letter.matches("[а-яА-ЯёЁ]");
    }

    public static boolean isAlreadyGuessedLetter(String letter, ArrayList<String> correctGuesses, ArrayList<String> wrongGuesses){
        return correctGuesses.contains(letter) || wrongGuesses.contains(letter);
    }

    public static boolean isWordSolved(String guessedWord, ArrayList<String> correctGuesses){
        char[] word = guessedWord.toCharArray();
        for(char c: word){
            if(!correctGuesses.contains(Character.toString(c)))
                return false;
        }
        return true;
    }

    public static void printWordState(String wordToGuess, ArrayList<String> correctGuesses){
        System.out.println("Текущее состояние слова:");

        for(int i = 0; i< WORD_LENGTH; i++){
            if(correctGuesses.contains(Character.toString(wordToGuess.charAt(i)))){
                System.out.print(wordToGuess.charAt(i));
            }
            else
                System.out.print("_");
        }
        System.out.println();
    }

    public static void printTotalMistakes(ArrayList<String> wrongGuesses, int mistakeCount){
        if(mistakeCount>0){
            System.out.println("Колличество ошибок: " + mistakeCount);
            System.out.print("Ошибки: ");
            for(String s: wrongGuesses)
                System.out.print(s + " ");
            System.out.println("\n");
        }
    }
}