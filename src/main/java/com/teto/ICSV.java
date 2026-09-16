package com.teto;


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.teto.command.Context;
import com.teto.command.keywords.AddNoisyWords;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICSV extends IString, IFields, IOptional {

    default Boolean addNoisyWords(Context ctx, List<String> words) {
        Optional<Boolean> ret = ctx.apply(new AddNoisyWords(words));
        if(ret.isPresent()) {
            return ret.get();
        }
        return false;
    }

    default boolean hasCSVKey(String fileName, String key, int index) {
        String[][] records = loadCSVFile(fileName);
        for(String[] record : records) {
            if(record[index].equals(key)) {
                return true;
            }
        }
        return false;
    }

    default String[] getCSVRecord(String fileName, String key, int index) {
        String[][] records = loadCSVFile(fileName);
        for(String[] record : records) {
            if(record[index].equals(key)) {
                return record;
            }
        }
        return null;
    }

    default String[][] loadCSVStrings(String str) {
        List<String[]> records = new ArrayList<>();
        try {
            CSVReader reader = new CSVReaderBuilder(new StringReader(str)).build();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if(line.length == 0) {
                    continue;
                }
                records.add(line);
            }
            reader.close();
            return records.toArray(new String[0][]);
        } catch(Exception e) {
            return null;
        }
    }

    default boolean addCSVRecords(String file,List<String[]> items) {
        try {
            ICSVWriter writer = new CSVWriterBuilder(new FileWriter(file)).build();

            writer.writeAll(items);
            writer.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    default Optional<CSVReader> csvReader(String file) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
            return Optional.ofNullable(reader);
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    default String[][] loadCSVFile(String file) {
        List<String[]> records = new ArrayList<>();
        int lc = 0;
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
            String[] line;
            while ((line = reader.readNext()) != null) {
                if(line.length == 0) {
                    continue;
                }
                if(line[0].startsWith("#")) {
                    continue;
                }
                lc++;
                records.add(line);
            }
            reader.close();
            return records.toArray(new String[0][]);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default void loadCSVFile(String file, CSVVisitor visitor) {
        int lc = 0;
        try {
            if(!new File(file).exists()) {
                return;
            }
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
            String[] line;
            while ((line = reader.readNext()) != null) {
                lc++;
                visitor.handle(lc, line);
            }
            reader.close();
        } catch(Exception e) {
            visitor.handle(lc, e);
        }
    }

    default String toCSVString(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for(String field : fields) {
            sb.append(quoted(field));
            sb.append(",");
        }
        return removeLast(sb.toString());
    }

    default <X> String[] toCsv(X x) {
        final List<String>  list = new ArrayList<>();
        if(x == null) {
            return list.toArray(new String[0]);
        }
        Map<String, String> map = getFieldsValues(x);
        List<String> keys = new ArrayList<>(map.keySet());
        keys.sort((o1, o2) -> o1.compareTo(o2));
        for(String key : keys) {
            list.add(map.get(key));
        }

        return list.toArray(new String[0]);
    }

    default String toCsvString(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for(String field : fields) {
            sb.append(quoted(field)).append(",");
        }
        return removeLast(sb.toString());
    }

}
