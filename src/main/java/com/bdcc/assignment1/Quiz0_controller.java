package com.bdcc.assignment1;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Quiz0_controller {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private quiz0_docRepo quiz0_docRepo;

    @GetMapping("/main")
    public String home( Model model){
        List<Quiz0_doc> saved = quiz0_docRepo.findAll();
        List<Quiz0_doc> empty = new ArrayList<>();
        model.addAttribute("persons",saved);
        model.addAttribute("person",new Quiz0_doc());
        model.addAttribute("edit_person",new Quiz0_doc());
//        model.addAttribute("queryResponse1",empty);
//        model.addAttribute("queryResponse2",empty);
        return "quiz0_home";
    }


    @GetMapping("/numRange")
    public String query(@ModelAttribute("person") Quiz0_doc person, Model model){
        int max_value = person.getNum()==null?0:person.getNum();
        int min_value = person.getYear()==null?0:person.getYear();
        Criteria criteria = Criteria.where("num").gte(min_value).lte(max_value);
        Query query = new Query(criteria);
        List<Quiz0_doc> result = mongoTemplate.find(query, Quiz0_doc.class);
        model.addAttribute("queryResponse2",result);
        model.addAttribute("persons",quiz0_docRepo.findAll());
        return "quiz0_home";
    }

    @GetMapping("/nameSearch")
    public String nameSearch(@ModelAttribute("person") Quiz0_doc person, Model model){
        model.addAttribute("queryResponse2", quiz0_docRepo.findByName(person.getName()));
        //model.addAttribute("persons",quiz0_docRepo.findAll());
        return "quiz0_home";
    }
    @GetMapping("/update_quiz0/{id}")
    public String updateForm(@PathVariable String id,
                             Model model){
        Quiz0_doc updateP = quiz0_docRepo.findById(id).get();
        model.addAttribute("person",updateP);
        return "Quiz0_edit";
    }
    @PostMapping("/update_quiz0/{id}")
    public String updatePerson(@PathVariable String id,
                               @ModelAttribute("person") Quiz0_doc people,
                               Model model){
        Quiz0_doc updateP = quiz0_docRepo.findById(id).get();

        updateP.setNum(people.getNum());
        updateP.setComments(people.getComments());
        quiz0_docRepo.save(updateP);
        model.addAttribute("person",updateP);
        return "redirect:/main";
    }

    @PostMapping("/upload_quiz0")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model) throws IOException, CsvException {

        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        List<Quiz0_doc> people = new ArrayList<>();

        people = csvReader.readAll().stream().map(x->{
                    Quiz0_doc p = new Quiz0_doc();
                    p.setNum(x[0].trim().equals("")?0:Integer.parseInt(x[0]));
                    p.setName(x[1]);
                    p.setYear(x[2].trim().equals("")?0:Integer.parseInt(x[2]));
                    p.setPicture(x[3]);
                    p.setComments(x[4]);
                    p.setImageUrl("https://bdassignment1.blob.core.windows.net/test1/"+x[3]);
            return p;
                }
        ).collect(Collectors.toList());
        System.out.println(people.get(0).getImageUrl());


        List<Quiz0_doc> saved = quiz0_docRepo.saveAll(people);

        model.addAttribute("savedMessage", "Successfully Inserted records into the database...");
        //model.addAttribute("persons",saved);

        return "redirect:/main";
    }


}
