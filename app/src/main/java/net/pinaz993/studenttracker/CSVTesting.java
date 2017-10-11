package net.pinaz993.studenttracker;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Created by Alexander Snedden on 10/11/17.
 */

public class CSVTesting {

    public static void processCSVData(String csvData) {
        CSVReader csvReader = new CSVReader(new StringReader(csvData));
        List<String[]> csvList = null;
        try {
            csvList = csvReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] csvRow;
        for(int i=0; i < csvList.size(); i++) {
            csvRow = csvList.get(i);
            for(int j=0; j < csvRow.length; j++) {
                Log.v("MyActivity", csvRow[j]);
            }
        }
    }
}