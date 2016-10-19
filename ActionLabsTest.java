package actionlabstest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author José Ricardo Carvalho Prado de Almeida
 */
public class ActionLabsTest {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ActionLabsTest test = new ActionLabsTest();
    // Test 1 - FizzBuzz
    test.printFizzBuzz();

    Random rand = new Random();
    int amount = 10 + rand.nextInt(100);

    int[] notes = new int[amount];
    int[] doors = new int[amount];
    int[] zombies = new int[amount];

    for (int i = 0; i < amount; i++) {
      notes[i] = rand.nextInt(10);
      doors[i] = 1 + rand.nextInt(19);
      zombies[i] = 1 + rand.nextInt(19);
    }

    // Test 2 - Rare Notes
    int[] rareNotes = test.getRareNotes(notes);
    System.out.println(Arrays.toString(rareNotes));

    // Test 3 - Hold The Door
    int strength = rand.nextInt(100);
    System.out.println(test.holdTheDoor(strength, doors, zombies));

    // Test 4 - Anagrams
    System.out.println(test.areAnagrams("anagram", "nag a ram"));
    System.out.println(test.areAnagrams("test1", "test2"));

    System.out.println(test.sqlQuery());
  }

  void printFizzBuzz() {
    // Would be better if the maximum number was passed as a parameter.
    for (int index = 1; index <= 100; index++) {
      String fizzBuzz = "";

      // Starts with the gratest modulo since its the compound of others
      // That way, if it was later in the test it would be taken by those before it.
      if (index % 15 == 0) {
        fizzBuzz = "FizzBuzz";

      } else if (index % 5 == 0) {
        fizzBuzz = "Buzz";

      } else if (index % 3 == 0) {
        fizzBuzz = "Fizz";
      }

      String result;

      // There's no need to print unecessary characters if the number is not a FizzBuzz.
      if (fizzBuzz.isEmpty()) {
        result = "" + index;

      } else {
        result = String.format("%d - %s", index, fizzBuzz);
      }

      System.out.println(result);
    }
  }

  int[] getRareNotes(int[] notes) {
    // Used to count how many times a note appears
    Map<Integer, Integer> collisions = new HashMap<>();

    // Counts how many times a note appears in the song.
    // We're interested only on the ones which are unique
    for (int index = 0; index < notes.length; index++) {
      int note = notes[index];

      Integer currentValue = collisions.get(note);
      int amount = (currentValue == null) ? 1 : currentValue + 1;

      collisions.put(note, amount);
    }

    // Lists all the unique ones.
    // This is step ensures the resulting array will be of the correct size.
    List<Integer> rare = new ArrayList<>();
    for (Integer key : collisions.keySet()) {
      if (collisions.get(key) == 1) {
        rare.add(key);
      }
    }

    // If the list is blank, it means no unique notes were found and thus we should return null.
    // Otherwise, return all the unique notes found.
    int[] result = null;
    if (!rare.isEmpty()) {
      result = new int[rare.size()];

      for (int rareIndex = 0; rareIndex < rare.size(); rareIndex++) {
        result[rareIndex] = rare.get(rareIndex);
      }
    }

    return result;
  }

  int holdTheDoor(int hodorStrength, int[] doorsStrength, int[] zombiesInflux) {
    // Given that the doorsStrength and zombiesInflux array are guaranteed
    // to be the same size, we won't be checking (but we should).

    // This array keeps the calculated times of all doors for debugging purposes
    int[] time = new int[doorsStrength.length];

    // The index of the door that can hold the longest
    int bestDoor = 0;

    // The strength of the best door, used to compare
    int bestStrength = 0;

    for (int index = 0; index < doorsStrength.length; index++) {
      int totalStrength = hodorStrength + doorsStrength[index];
      int currentStrength = totalStrength / zombiesInflux[index];

      if (currentStrength > bestStrength) {
        bestDoor = index;
        bestStrength = currentStrength;
      }

      time[index] = currentStrength;
    }

    return bestDoor;
  }

  boolean areAnagrams(String word1, String word2) {
    // Its easier to compare anagrams if all the characters are listed
    // So we strip spaces and downcase them to make comparison easier.
    char[] word1Chars = word1.replace(" ", "").toLowerCase().toCharArray();
    char[] word2Chars = word2.replace(" ", "").toLowerCase().toCharArray();

    // Anagrams should have the same length.
    if (word1Chars.length != word2Chars.length) {
      return false;
    }

    // Sorts the character arrays to ensure both words have all the same characters
    // and all of them are in the same position.
    Arrays.sort(word1Chars);
    Arrays.sort(word2Chars);

    // If both sorted character arrays are equal, we have a anagram
    return Arrays.equals(word1Chars, word2Chars);
  }

  String sqlQuery() {
    return "SELECT candidate.name            AS candidate_name,\n"
            + "    sum(question_score.score) AS candidate_score\n"
            + " FROM question_score\n"
            + " JOIN candidate ON candidate.candidate_id = question_score.candidate_id\n"
            + " JOIN test      ON test.test_id           = question_score.test_id\n"
            + " WHERE test.name = \"Java backend\"\n"
            + " ORDER BY candidate_score, candidate_name\n"
            + " GROUP BY candidate_name;";
  }
}
