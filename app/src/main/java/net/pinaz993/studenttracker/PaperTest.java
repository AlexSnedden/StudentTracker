package net.pinaz993.studenttracker;

import io.paperdb.Paper;

/**
 * Created by Alexander Snedden on 9/13/2017.
 */

public class PaperTest {
    private final Integer integer;
    private final Double decimal;
    private final String word;
    private final String name;
    private static final String ID = "test";

    public static PaperTest getObject(String name) {
        return Paper.book(ID).read(name);
    }

    PaperTest(Integer integer, Double decimal, String word, String name) {
        this.integer = integer;
        this.decimal = decimal;
        this.word = word;
        this.name = name;
        saveObject();
    }

    public String getWord() {
        return word;
    }

    public Double getDecimal() {return decimal;}

    public Integer getInteger() {
        return integer;
    }

    public void saveObject() {
        Paper.book(ID).write(name, this);
    }
}
