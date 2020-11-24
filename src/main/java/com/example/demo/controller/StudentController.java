package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.IllegalFirstNameException;
import com.example.demo.exception.IllegalLastNameException;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;



@Controller
public class StudentController {
	
	@Autowired
	private StudentService studentService;

	@RequestMapping(value="/students", method = RequestMethod.GET)
	// SERVLET -> handling mapping
	public ModelAndView students(){		
		List<Student> students = studentService.findAll();		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("students", students);// set the attributes to view files
		modelAndView.setViewName("student/list");
		return modelAndView;
	}
	
	@RequestMapping(value="/student", method = RequestMethod.GET)
	public String create(Model model){			
		model.addAttribute("student", new Student());
		return "student/edit";
	}
	
	@RequestMapping(value = "/student", method = RequestMethod.POST)
	public String edit(@Valid @ModelAttribute("student") Student student, 
			BindingResult result, Model model) throws IllegalFirstNameException, IllegalLastNameException  {

		if (result.hasErrors()) {
			model.addAttribute("errors", result.getAllErrors());
			return "student/edit";
		}
		String first = student.getFirstName();
		for(int i=0;i<first.length();i++) {
			if(!Character.isLetter(first.charAt(i)))
				throw new IllegalFirstNameException(); 
		}
		String last = student.getLastName();
		for(int i=0;i<last.length();i++) {
			if(!Character.isLetter(last.charAt(i)))
				throw new IllegalLastNameException(); 
		}
		student = studentService.save(student);
		return "redirect:/students";
	}	
	
	@RequestMapping(value="/student/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, Model model){	
		model.addAttribute("student", studentService.findOne(id));
		return "student/edit";
	}
	
	@RequestMapping(value="/student/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, Model model){		
		studentService.delete(id);
		return "redirect:/students";
	}	
}
