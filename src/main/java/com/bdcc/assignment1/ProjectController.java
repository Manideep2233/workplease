package com.bdcc.assignment1;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PeopleRepo pepoleRepo;


    @GetMapping("/greet")
    public String greet( @ModelAttribute("person") People people,
                         Model model){
        List<People> saved = pepoleRepo.findAll();
        model.addAttribute("greeting","Hello "+people.getName());
        return "home";
    }


    @GetMapping("/home")
    public String getAllPeople( Model model){
        List<People> saved = pepoleRepo.findAll();
        model.addAttribute("persons",saved);
        model.addAttribute("person",new People());

        return "home1";
    }

    @GetMapping("/newForm")
    public String createPerson(Model model){
        People p = new People();
        model.addAttribute("person",p);
        return "create_person";
    }

    @PostMapping("/addPerson")
    public String addPerson(@ModelAttribute("person") People person){
        System.out.println("entered");
        pepoleRepo.save(person);
        System.out.println("saved");

        return "redirect:/home";
    }

    @GetMapping("/updateForm/{id}")
    public String updateForm(@PathVariable String id,
                               Model model){
        People updateP = pepoleRepo.findById(id).get();
        model.addAttribute("person",updateP);
        return "edit_person";
    }

    @PostMapping("/updatePerson/{id}")
    public String updatePerson(@PathVariable String id,
                             @ModelAttribute("person") People people,
                               Model model){
        People updateP = pepoleRepo.findById(id).get();
        people.setPid(updateP.getPid());
        people.setImageUrl("https://bdassignment1.blob.core.windows.net/test1/"+people.getPicture());
        pepoleRepo.save(people);
        model.addAttribute("person",updateP);
        return "redirect:/home";
    }


    @GetMapping("/deletePerson/{id}")
    public String deletePerson(@PathVariable String id,
                               @ModelAttribute("person") People people,
                               Model model){
        pepoleRepo.deleteById(id);
        return "redirect:/home";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model) throws IOException, CsvException {

        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        List<People> people = new ArrayList<>();

        people = csvReader.readAll().stream().map(x->{
                    People p = new People();
                    p.setName(x[0]);
                    p.setState(x[1]);
                    p.setSalary(x[2].trim().equals("")?0:Integer.parseInt(x[2]));
                    p.setGrade(x[3].trim().equals("")?0:Integer.parseInt(x[3]));
                    p.setRoom(x[4].trim().equals("")?0:Integer.parseInt(x[4]));
                    p.setTelnum(x[5].trim().equals("")?0:Integer.parseInt(x[5]));
                    p.setPicture(x[6]);
                    p.setKeywords(x[7]);
                    p.setImageUrl("https://bdassignment1.blob.core.windows.net/test1/"+x[6]);
                    return p;
                }
        ).collect(Collectors.toList());
        System.out.println(people.get(0).getImageUrl());


        List<People> saved = pepoleRepo.saveAll(people);

        model.addAttribute("savedMessage", "Successfully Inserted records into the database...");
        //model.addAttribute("persons",saved);

        return "redirect:/home";
    }

    @GetMapping("/delete-people")
    public String delete( Model model){
        pepoleRepo.deleteAll();
        return "redirect:/home";
    }

    @GetMapping("/queryDB")
    public String query(@ModelAttribute("person") People person, Model model){

        List<People> queried = mongoTemplate.find(new BasicQuery(person.getName()), People.class);
        model.addAttribute("queryResponse",queried);
        model.addAttribute("persons",pepoleRepo.findAll());
        return "home1";
    }


//    @GetMapping("/search")
//    public String textSearch(@ModelAttribute("person") People person, Model model){
//        model.addAttribute("queryResponse",pepoleRepo.findByText(person.getKeywords()));
//        model.addAttribute("persons",pepoleRepo.findAll());
//        return "home1";
//    }

    @GetMapping("/uploadImage")
    public String getUploadForm() {
        return "upload-form";
    }


    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=bdassignment1;" +
                "AccountKey=+8Nyv7UDrAcUgBsldRWw2+mcYIwaXCFn5sO6durQJ/RCwNNE7hFJW3Nx5b9UYuwGBAy4QCGwl/Vr+AStj8kJsw==;" +
                "EndpointSuffix=core.windows.net";
        String containerName = "test1";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());
        InputStream inputStream = new BufferedInputStream(file.getInputStream());
        blobClient.upload(inputStream, file.getSize());
        List<People> saved = pepoleRepo.findAll();

        model.addAttribute("persons",saved);
        model.addAttribute("person",new People());

        return "home1";
    }

}
