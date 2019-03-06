package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    PotluckRepository potluckRepository;
    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listPotluck(Model model) {
        model.addAttribute("potluck", potluckRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String potluckForm(Model model) {
        model.addAttribute("potluck", new Potluck());
        return "potluckform";
    }

    @PostMapping("/process")
    public String processForm(@ModelAttribute Potluck potluck,
                              @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            potluck.setHeadshot(uploadResult.get("url").toString());
            potluckRepository.save(potluck);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
    }
           return"redirect:/";
}

        @RequestMapping("/update/{id}")
    public String updatePotluck(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("potluck", potluckRepository.findById(id));
        return "potluckform";
    }
        @RequestMapping("/detail/{id}")
        public String showPotluck(@PathVariable("id") long id, Model model) {
            model.addAttribute("potluck", potluckRepository.findById(id));
            return "show";
        }

    @RequestMapping("/delete/{id}")
    public String delPotluck(@PathVariable("id") long id) {
        potluckRepository.deleteById(id);
        return "redirect:/";
    }

}

