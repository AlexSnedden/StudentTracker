package net.pinaz993.studenttracker;

import io.paperdb.Paper;

/**
 * Created by Alexander Snedden on 9/13/2017.
 */

public class PaperTest {
    private final int integer;
    private final double decimal;
    private final String word;
    private final String name;
    private static final String ID = "test";

    public static PaperTest getObject(String name) {
        return Paper.book(ID).read(name);
    }

    PaperTest(int integer, double decimal, String word, String name) {
        this.integer = integer;
        this.decimal = decimal;
        this.word = word;
        this.name = name;
        saveObject();
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
        Paper.book(ID).write(name, this);
    }
}
