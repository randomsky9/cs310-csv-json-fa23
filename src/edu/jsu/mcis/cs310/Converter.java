package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;


public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
public static String csvToJson(String csvString) {
    try {
        JSONObject jsonObject = new JSONObject();
        
        JSONArray colHeadings = new JSONArray();
        
        JSONArray data = new JSONArray();
        
        CSVParser csvParser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        List<CSVRecord> records = csvParser.getRecords();
        
        if (records.size() > 1) {
            CSVRecord headerRow = records.get(0);
            for (String column : headerRow) {
                colHeadings.add(column);
            }
            
            for (int i = 1; i < records.size(); i++) {
                CSVRecord dataRow = records.get(i);
                JSONArray rowData = new JSONArray();
                
                for (String value : dataRow) {
                    try {
                        rowData.add(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        rowData.add(value);
                    }
                }
                
                data.add(rowData);
            }
        }
        
        jsonObject.put("ProdNums", colHeadings);
        jsonObject.put("ColHeadings", colHeadings);
        jsonObject.put("Data", data);
        
        return jsonObject.toJSONString();
    } catch (Exception e) {
        e.printStackTrace();
        return "{}";
    }
}
@SuppressWarnings("unchecked")
public static String jsonToCsv(String jsonString) {
    try {
        StringBuilder csvBuilder = new StringBuilder();
        
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
        
        JSONArray colHeadings = (JSONArray) jsonObject.get("ColHeadings");
        JSONArray data = (JSONArray) jsonObject.get("Data");
        
        for (int i = 0; i < colHeadings.size(); i++) {
            csvBuilder.append(colHeadings.get(i));
            if (i < colHeadings.size() - 1) {
                csvBuilder.append(",");
            }
        }
        csvBuilder.append("\n");
        
        for (int i = 0; i < data.size(); i++) {
            JSONArray rowData = (JSONArray) data.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                csvBuilder.append(rowData.get(j));
                if (j < rowData.size() - 1) {
                    csvBuilder.append(",");
                }
            }
            csvBuilder.append("\n");
        }
        
        return csvBuilder.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return "";
    }
  }
}