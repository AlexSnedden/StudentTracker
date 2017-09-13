package net.pinaz993.studenttracker;

import io.paperdb.Paper;

/**
 * Created by Alexander Snedden on 9/13/2017.
 */

public class PaperTest {
    private final int integer;
    private final double decimal;
    private final String word;
    private final String ID;

    PaperTest(int integer, double decimal, String word, String ID) {
        this.integer = integer;
        this.decimal = decimal;
        this.word = word;
        this.ID = ID;
    }

    PaperTest(String ID) {
        this.ID = ID;
        this.integer = Paper.book(ID).read("integer");
        this.decimal = Paper.book(ID).read("decimal");
        this.word = Paper.book(ID).read("word");
    }

    public String getWord() {
        return word;
    }

    public double getDecimal() {
        return decimal;
    }

    public int getInteger() {
        return integer;
    }

    public void saveObject() {
        Paper.book(ID).write("integer", integer);
        Paper.book(ID).write("decimal", decimal);
        Paper.book(ID).write("word", word);
    }
}
