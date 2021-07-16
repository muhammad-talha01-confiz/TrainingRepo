package com.company;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

public class Main implements Runnable{

    public void run() {}

    private static final HashSet<String> noDupSet = new HashSet<>();

    public static void main(String[] args) {

        String path = "src/com/company/Candidates.csv";
        List<Candidate> candidates = readCandidatesFromCSV(path);

        candidates.sort(Comparator.comparing(Candidate::getCity));

//        candidates.forEach(System.out::println);

        writeCandidatesToCSV(candidates);

//        candidates.forEach(System.out::println);

    }

    private static List<Candidate> readCandidatesFromCSV(String filePath) {
        List<Candidate> candidates= new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            br.readLine();
            String line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");
                noDupSet.add(data[4]);
                Candidate c = createCandidate(data);
                candidates.add(c);

                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.getLocalizedMessage();
        } catch (ParseException pe) {
            pe.getCause();
        }

        return candidates;
    }

    private static Candidate createCandidate(String[] dataFromCSVLine) throws ParseException {
        // CSV Format: ID, Name, Gender, Age, City, DOB
        int id = Integer.parseInt(dataFromCSVLine[0]);
        String name = dataFromCSVLine[1];
        char gender = dataFromCSVLine[2].charAt(0);
        int age = Integer.parseInt(dataFromCSVLine[3]);
        String city = dataFromCSVLine[4];
        String dateString = dataFromCSVLine[5];
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);

        return new Candidate(id, name, gender, age, city, date);
    }

    private static void writeCandidatesToCSV(List<Candidate> candidates) {

        Candidate firstCandidate = candidates.get(0);
        String cityWiseCSV = firstCandidate.getCity();

        for(int i = 0; i < noDupSet.size(); i++) {

            List<Candidate> written = new ArrayList<>();

            try {
                // CSV Format: ID, Name, Gender, Age, City, DOB
                FileWriter csvWriter = new FileWriter(cityWiseCSV + "-data.csv");
                csvWriter.append("id, Name, Gender, Age, City, DOB");
                csvWriter.append("\n");

                for (Candidate c : candidates) {

                    if(!cityWiseCSV.equals(c.getCity())) {
                        cityWiseCSV = c.getCity();
                        break;
                    }

                    String toWrite = c.getId() + ", " + c.getName() + ", " + c.getGender() +
                            ", " + c.getAge() + ", " + c.getCity() + ", " + c.getDob();
                    csvWriter.append(toWrite);
                    csvWriter.append("\n");

                    written.add(c);

                }

                csvWriter.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            candidates.removeAll(written);

        }


    }

}
